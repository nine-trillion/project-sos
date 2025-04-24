package team.project.sos.domain.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.dto.response.OrderResponseDto;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.order.exception.OrderError;
import team.project.sos.domain.order.exception.OrderException;
import team.project.sos.domain.order.repository.OrderRepository;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.exception.UserError;
import team.project.sos.domain.user.service.UserService;

import java.util.List;
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

    @Mock
    UserService userService;

    @Test
    @DisplayName("주문 생성_성공")
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
    @DisplayName("주문 생성_실패_로그인 X")
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
    @DisplayName("주문 취소_성공")
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
    @DisplayName("주문 취소_실패_로그인 X")
    void cancelOrderFailsWhenNotLoggedIn() {
        // given
        Order order = createMockOrder();

        // when
        OrderException exception = assertThrows(OrderException.class, () ->
                orderService.cancelOrder(order.getId(), null));

        // then
        assertEquals(OrderError.NOT_LOGGED_IN, exception.getErrorCode());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    @DisplayName("주문 단건 조회_성공")
    void findMyOrderSuccess() {
        // given
        Order order = createMockOrder();
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // when
        OrderResponseDto responseDto = orderService.findOrder(order.getId(), order.getUser().getId());

        // then
        assertNotNull(responseDto);
        assertEquals(order.getId(), responseDto.getId());
        assertEquals(order.getUser().getId(), responseDto.getUser().getId());
    }

    @Test
    @DisplayName("주문 단건 조회_실패_로그인 X")
    void findOrderFailsWhenNotLoggedIn() {
        // given
        Order order = createMockOrder();

        // when
        OrderException exception = assertThrows(OrderException.class, () ->
                orderService.findOrder(order.getId(), null));

        // then
        assertEquals(OrderError.NOT_LOGGED_IN, exception.getErrorCode());
    }

    @Test
    @DisplayName("주문 단건 조회_실패_다른 유저 접근")
    void findOrderFailsWhenNoPermission() {
        // given
        Order order = createMockOrder();
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // when
        OrderException exception = assertThrows(OrderException.class, () ->
                orderService.findOrder(order.getId(), 2L));

        // then
        assertEquals(OrderError.NO_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("주문 목록 조회_성공")
    void findOrdersSuccess() {
        // given
        User user = createMockUser();
        User admin = createMockAdmin();
        List<Order> orders = createMockOrders();

        when(orderRepository.findByUser(user)).thenReturn(orders);
        when(userService.findByIdOrElseThrow(user.getId())).thenReturn(user);
        when(userService.findByIdOrElseThrow(admin.getId())).thenReturn(admin);

        // when
        List<OrderResponseDto> responseDtos = orderService.findOrders(user.getId(), admin.getId());

        // then
        assertEquals(orders.size(), responseDtos.size());
        assertEquals(orders.get(0).getUser().getId(), user.getId());
    }

    @Test
    @DisplayName("내 주문 목록 조회_성공")
    void findMyOrdersSuccess() {
        // given
        User user = createMockUser();
        List<Order> orders = createMockOrders();

        when(userService.findByIdOrElseThrow(user.getId())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);

        // when
        List<OrderResponseDto> responseDtos = orderService.findMyOrders(user.getId());

        // then
        assertEquals(2, responseDtos.size());
        assertEquals(1L, responseDtos.get(0).getId());
    }

    @Test
    @DisplayName("내 주문 목록 조회_실패_로그인 X")
    void findMyOrderFailsWhenNotLoggedIn() {
        // when
        OrderException exception = assertThrows(OrderException.class, () ->
                orderService.findMyOrders(null));

        // then
        assertEquals(OrderError.NOT_LOGGED_IN, exception.getErrorCode());
    }

    @Test
    @DisplayName("내 주문 목록 조회_실패_유저 없음")
    void findMyOrderFailsWhenUserNotExist() {
        // given
        when(userService.findByIdOrElseThrow(999L)).thenThrow(new BaseException(UserError.USER_NOT_FOUND));

        // when
        BaseException exception = assertThrows(BaseException.class, () ->
                orderService.findMyOrders(999L));

        // then
        assertEquals(UserError.USER_NOT_FOUND, exception.getErrorCode());
    }

    private User createMockUser() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "role", UserRole.USER);
        ReflectionTestUtils.setField(user, "grade", Grade.BASIC);
        return user;
    }

    private User createMockAdmin() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 2L);
        ReflectionTestUtils.setField(user, "role", UserRole.ADMIN);
        return user;
    }

    private Order createMockOrder() {
        User user = createMockUser();

        return Order.builder()
                .id(1L)
                .user(user)
                .status(OrderStatus.PENDING)
                .price(10000)
                .build();
    }

    private List<Order> createMockOrders() {
        User user = createMockUser();
        Order order1 = Order.builder().id(1L).user(user).status(OrderStatus.PENDING).price(10000).build();
        Order order2 = Order.builder().id(2L).user(user).status(OrderStatus.PENDING).price(20000).build();
        return List.of(order1, order2);
    }

}