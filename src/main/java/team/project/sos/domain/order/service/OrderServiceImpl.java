package team.project.sos.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.domain.menu.service.MenuService;
import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.dto.request.OrderItemRequestDto;
import team.project.sos.domain.order.dto.response.OrderResponseDto;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.order.entity.OrderItem;
import team.project.sos.domain.order.exception.OrderError;
import team.project.sos.domain.order.exception.OrderException;
import team.project.sos.domain.order.repository.OrderItemRepository;
import team.project.sos.domain.order.repository.OrderRepository;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final MenuService menuService;

    @Transactional
    public Long saveOrder(CreateOrderRequestDto requestDto, Long userId) {
        // 로그인하지 않은 경우 예외 발생
        if (userId == null) {
            throw new OrderException(OrderError.NOT_LOGGED_IN);
        }

        // 주문 엔티티 생성
        Order order = Order.builder()
                .user(requestDto.getUser())
                .store(requestDto.getStore())
                .price(requestDto.getPrice())
                .requestedAt(requestDto.getRequestedAt())
                .build();

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 주문 항목 저장
        for (OrderItemRequestDto itemDto : requestDto.getItems()) {
//            Menu menu = menuService.findMenuIncludeDeleted();

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .menu(itemDto.getMenu())
                    .quantity(itemDto.getQuantity())
                    .build();

            orderItemRepository.save(item);
        }

        return savedOrder.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        // 로그인하지 않은 경우 예외 발생
        if (userId == null) {
            throw new OrderException(OrderError.NOT_LOGGED_IN);
        }

        // 해당하는 주문 조회
        Order order = findByIdOrElseThrow(orderId);

        // 현재 로그인한 유저의 주문이 아닐 경우 예외 발생
        if (!order.getUser().getId().equals(userId)) {
            throw new OrderException(OrderError.NO_PERMISSION);
        }

        // 주문 상태를 '취소'로 변경
        order.cancel();
    }

    public OrderResponseDto findOrder(Long orderId, Long userId) {
        // 로그인하지 않은 경우 예외 발생
        if (userId == null) {
            throw new OrderException(OrderError.NOT_LOGGED_IN);
        }

        // 해당하는 주문 조회
        Order order = findByIdOrElseThrow(orderId);

        // 현재 로그인한 유저의 주문이 아닐 경우 예외 발생
        if (!order.getUser().getId().equals(userId)) {
            throw new OrderException(OrderError.NO_PERMISSION);
        }

        return OrderResponseDto.from(order);
    }

    public List<OrderResponseDto> findOrders(Long userId) {
        // 해당하는 유저 조회
        User user = userService.findByIdOrElseThrow(userId);

        // 유저에 해당하는 주문 목록을 DTO로 변환해서 리턴
        return orderRepository.findByUser(user)
                .stream()
                .map(OrderResponseDto::from)
                .collect(Collectors.toList());
    }

    public Order findByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderException(OrderError.NO_SUCH_ORDER));
    }

}