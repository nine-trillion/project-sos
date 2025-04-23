package team.project.sos.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.dto.response.OrderResponseDto;
import team.project.sos.domain.order.service.OrderServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping("/stores/{storeId}/order")
    public ResponseEntity<Void> saveOrder(@RequestBody CreateOrderRequestDto requestDto) {
        orderService.saveOrder(requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId
//                                            , @AuthenticationPrincipal UserPrincipal user
    ) {
        // TODO: 토큰에서 로그인된 사용자 정보 꺼내기
        Long userId = 1L;

        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> findOrder(@PathVariable Long orderId
//                                                      , @AuthenticationPrincipal UserPrincipal user
    ) {
        // TODO: 토큰에서 로그인된 사용자 정보 꺼내기
        Long userId = 1L;

        OrderResponseDto responseDto = orderService.findOrder(orderId, userId);
        return ResponseEntity.ok(responseDto);
    }

//    @GetMapping("/orders")
//    public ResponseEntity<List<OrderResponseDto>> findOrders(
////            @AuthenticationPrincipal UserPrincipal user
//    ) {
//        // TODO: 토큰에서 로그인된 사용자 정보 꺼내기
//        Long userId = 1L;
//
//        List<OrderResponseDto> responseDtos = orderService.findOrders(userId);
//        return ResponseEntity.ok(responseDtos);
//    }

}