package team.project.sos.domain.order.service;

import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.dto.response.OrderResponseDto;

public interface OrderService {

    void saveOrder(CreateOrderRequestDto requestDto);

    void cancelOrder(Long orderId, Long userId);

    OrderResponseDto findOrder(Long orderId, Long userId);

}
