package team.project.sos.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.domain.menu.entity.Menu;
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
import team.project.sos.domain.user.enums.UserRole;
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

    /**
     * 일반 유저가 주문을 하기 위해 사용합니다.
     */
    @Transactional
    public Long saveOrder(CreateOrderRequestDto requestDto, Long currentUserId) {
        // 로그인하지 않은 경우 예외 발생
        if (currentUserId == null) {
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
            Menu menu = menuService.findMenuIncludeDeleted(itemDto.getMenu().getId());

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .menu(menu)
                    .quantity(itemDto.getQuantity())
                    .build();

            orderItemRepository.save(item);
        }

        return savedOrder.getId();
    }

    /**
     * 일반 유저가 자신의 주문을 취소하기 위해 사용합니다.
     */
    @Transactional
    public void cancelOrder(Long orderId, Long currentUserId) {
        // 로그인하지 않은 경우 예외 발생
        if (currentUserId == null) {
            throw new OrderException(OrderError.NOT_LOGGED_IN);
        }

        // 해당하는 주문 조회
        Order order = findByIdOrElseThrow(orderId);

        // 현재 로그인한 유저의 주문이 아닐 경우 예외 발생
        if (!order.getUser().getId().equals(currentUserId)) {
            throw new OrderException(OrderError.NO_PERMISSION);
        }

        // 주문 상태를 '취소'로 변경
        order.cancel();
    }

    /**
     * 일반 유저가 자신의 주문 단건을 조회하기 위해 사용합니다.
     */
    public OrderResponseDto findOrder(Long orderId, Long currentUserId) {
        // 로그인하지 않은 경우 예외 발생
        if (currentUserId == null) {
            throw new OrderException(OrderError.NOT_LOGGED_IN);
        }

        // 해당하는 주문 조회
        Order order = findByIdOrElseThrow(orderId);

        // 현재 로그인한 유저의 주문이 아닐 경우 예외 발생
        if (!order.getUser().getId().equals(currentUserId)) {
            throw new OrderException(OrderError.NO_PERMISSION);
        }

        return OrderResponseDto.from(order);
    }

    /**
     * 관리자(UserRole.ADMIN)가 특정 유저의 주문 목록을 조회하기 위해 사용합니다.
     *
     * @param userId        주문 목록을 조회할 유저 아이디
     * @param currentUserId 현재 로그인한 유저 아이디
     * @return 특정 유저의 주문 목록
     */
    public List<OrderResponseDto> findOrders(Long userId, Long currentUserId) {
        // 현재 로그인한 유저 조회
        User currentUser = userService.findByIdOrElseThrow(currentUserId);

        // 현재 로그인한 유저가 관리자가 아닐 경우 예외 발생
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new OrderException(OrderError.NO_PERMISSION);
        }

        // 주문 목록을 조회할 유저 조회
        User user = userService.findByIdOrElseThrow(userId);

        // 유저에 해당하는 주문 목록을 DTO로 변환해서 리턴
        return orderRepository.findByUser(user)
                .stream()
                .map(OrderResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 일반 유저가 자신의 주문 목록을 조회하기 위해 사용합니다.
     */
    public List<OrderResponseDto> findMyOrders(Long currentUserId) {
        // 로그인하지 않은 경우 예외 발생
        if (currentUserId == null) {
            throw new OrderException(OrderError.NOT_LOGGED_IN);
        }

        // 해당하는 유저 조회
        User user = userService.findByIdOrElseThrow(currentUserId);

        // 유저에 해당하는 주문 목록을 DTO로 변환해서 리턴
        return orderRepository.findByUser(user)
                .stream()
                .map(OrderResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 주문 아이디를 받아서 주문을 반환합니다.
     * 해당하는 주문이 없을 시 예외를 발생시킵니다.
     */
    public Order findByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderException(OrderError.NO_SUCH_ORDER));
    }

    /**
     * 주문 아이디와 가게 아이디를 받아서 주문을 반환합니다.
     * 해당하는 주문이 없을 시 예외를 발생시킵니다.
     */
    public Order findByIdAndStoreIdOrElseThrow(Long orderId, Long storeId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getStore().getId().equals(storeId))
                .orElseThrow(() -> new OrderException(OrderError.NO_SUCH_ORDER));
    }

}