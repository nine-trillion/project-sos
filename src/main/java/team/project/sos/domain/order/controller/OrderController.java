package team.project.sos.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.dto.response.OrderResponseDto;
import team.project.sos.domain.order.service.OrderService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/stores/{storeId}/order")
    public ResponseEntity<Void> saveOrder(@RequestBody CreateOrderRequestDto requestDto,
                                          HttpRequest request) {
        // 유저 아이디 추출
        Long userId = (Long) request.getAttributes().get("userId");

        // 주문 생성
        Long orderId = orderService.saveOrder(requestDto, userId);

        // 반환할 URI 생성
        URI uri = URI.create("/api/orders/" + orderId);

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, HttpRequest request) {
        // 유저 아이디 추출
//        Long userId = (Long) request.getAttributes().get("userId");

        // TODO: 토큰에서 로그인된 사용자 정보 꺼내기
        Long userId = 1L;

        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> findOrder(@PathVariable Long orderId) {
        // TODO: 토큰에서 로그인된 사용자 정보 꺼내기
        Long userId = 1L;

        OrderResponseDto responseDto = orderService.findOrder(orderId, userId);
        return ResponseEntity.ok(responseDto);
    }

//    @GetMapping("/orders")
//    public ResponseEntity<List<OrderResponseDto>> findOrders(HttpRequest request) {
//        // 유저 아이디 추출
////        Long userId = (Long) request.getAttributes().get("userId");
//
//        // TODO: 토큰에서 로그인된 사용자 정보 꺼내기
//        Long userId = 1L;
//
//        List<OrderResponseDto> responseDtos = orderService.findOrders(userId);
//        return ResponseEntity.ok(responseDtos);
//    }

}