package team.project.sos.domain.order.service;

import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {

    Long saveOrder(CreateOrderRequestDto requestDto, Long userId);

    void cancelOrder(Long orderId, Long userId);

    OrderResponseDto findOrder(Long orderId, Long userId);

    List<OrderResponseDto> findOrders(Long userId, Long currentUserId);

    List<OrderResponseDto> findMyOrders(Long userId);

    Order findByIdOrElseThrow(Long orderId);

}