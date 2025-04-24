package team.project.sos.domain.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.order.exception.OrderException;
import team.project.sos.domain.order.repository.OrderRepository;
import team.project.sos.domain.user.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    OrderRepository orderRepository;

    @Test
    void saveOrderSuccess() {
        // given
        Order order = createMockOrder();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // when
        Order savedOrder = orderRepository.save(order);

        // then
        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getId());
    }

    @Test
    @DisplayName("로그인이 안 된 경우 주문 생성 실패")
    void saveOrderFailsWhenNotLoggedIn() {
        // given
        Long userId = null;
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();

        // when
        OrderException exception = assertThrows(OrderException.class, () ->
            orderService.saveOrder(requestDto, userId)
        );

        // then
        assertEquals("로그인이 필요합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 취소 성공")
    void cancelOrder() {
        // given
        Order order = createMockOrder();
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // when
        orderService.cancelOrder(order.getId(), 1L);

        // then
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    @DisplayName("로그인이 안 된 경우 주문 취소 실패")
    void cancelOrderFailsWhenNotLoggedIn() {
        // given
        Order order = createMockOrder();

        // when
        OrderException exception = assertThrows(OrderException.class, () ->
                orderService.cancelOrder(order.getId(), null));

        // then
        assertEquals("로그인이 필요합니다.", exception.getMessage());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    private Order createMockOrder() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        return Order.builder()
                .id(1L)
                .user(user)
                .status(OrderStatus.PENDING)
                .price(10000)
                .build();
    }

}