package team.project.sos.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/stores/{storeId}/orders")
    public ResponseEntity<Void> saveOrder(@RequestBody CreateOrderRequestDto requestDto,
                                          @AuthenticationPrincipal String currentUserId) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long userId = Long.parseLong(currentUserId);

        // 주문 생성
        Long orderId = orderService.saveOrder(requestDto, userId);

        // 반환할 URI 생성
        URI uri = URI.create("/api/orders/" + orderId);

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId,
                                            @AuthenticationPrincipal String currentUserId) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long userId = Long.parseLong(currentUserId);

        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> findOrder(@PathVariable Long orderId,
                                                      @AuthenticationPrincipal String currentUserId) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long userId = Long.parseLong(currentUserId);

        OrderResponseDto responseDto = orderService.findOrder(orderId, userId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 관리자가 사용자의 주문 목록을 조회하기 위해 사용합니다.
     * 일반 사용자는 접근 권한이 없습니다.
     */
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<OrderResponseDto>> findOrders(@PathVariable Long userId,
                                                             @AuthenticationPrincipal String currentUserId) {

    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> findMyOrders(@AuthenticationPrincipal String currentUserId) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long userId = Long.parseLong(currentUserId);

        List<OrderResponseDto> responseDtos = orderService.findMyOrders(userId);
        return ResponseEntity.ok(responseDtos);
    }

}