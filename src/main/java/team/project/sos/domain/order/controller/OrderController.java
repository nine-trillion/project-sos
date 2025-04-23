package team.project.sos.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.dto.response.OrderResponseDto;
import team.project.sos.domain.order.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/stores/{storeId}/order")
    public ResponseEntity<Void> saveOrder(@RequestBody CreateOrderRequestDto requestDto) {
        orderService.saveOrder(requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId,
                                            @AuthenticationPrincipal UserPrincipal user) {
        orderService.cancelOrder(orderId, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> findOrders(@AuthenticationPrincipal UserPrincipal user) {
        Long userId = user.getId();
        List<OrderResponseDto> orders = orderService.findOrders(userId);
        return ResponseEntity.ok(orders);
    }

}