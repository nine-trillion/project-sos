package team.project.sos.domain.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.order.exception.OrderException;
import team.project.sos.domain.order.repository.OrderRepository;

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
        OrderException exception = assertThrows(OrderException.class, () -> {
            orderService.saveOrder(requestDto, userId);
        });

        // then
        assertEquals("로그인이 필요합니다.", exception.getMessage());
    }

//    @Test
//    @DisplayName("주문 취소 성공")
//    void cancelOrder() {
//        // given
//        Order order = createMockOrder();
//        orderRepository.save(order);
//
//        // when
//        orderService.cancelOrder(order.getId(), 1L);
//
//        // then
//        assertEquals(OrderStatus.CANCELLED, order.getStatus());
//    }

    private Order createMockOrder() {
        Order order = Order.builder()
                .id(1L)
                .price(10000)
                .build();
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        return order;
    }

}