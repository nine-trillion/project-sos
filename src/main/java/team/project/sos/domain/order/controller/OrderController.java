package team.project.sos.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.order.dto.request.CreateOrderRequestDto;
import team.project.sos.domain.order.dto.request.UpdateStatusRequestDto;
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
                                          @AuthenticationPrincipal String currentUserIdStr) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);

        // 주문 생성
        Long orderId = orderService.saveOrder(requestDto, currentUserId);

        // 반환할 URI 생성
        URI uri = URI.create("/api/orders/" + orderId);

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/stores/{storeId}/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId,
                                                              @PathVariable Long storeId,
                                                              @AuthenticationPrincipal String currentUserIdStr,
                                                              @RequestBody UpdateStatusRequestDto requestDto) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);
        OrderResponseDto response = orderService.updateOrderStatus(storeId, orderId, currentUserId, requestDto.getStatus());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId,
                                            @AuthenticationPrincipal String currentUserIdStr) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);
        orderService.cancelOrder(orderId, currentUserId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> findOrder(@PathVariable Long orderId,
                                                      @AuthenticationPrincipal String currentUserIdStr) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);

        OrderResponseDto responseDto = orderService.findOrder(orderId, currentUserId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 관리자가 사용자의 주문을 조회합니다.
     */
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<OrderResponseDto>> findOrders(@PathVariable Long userId,
                                                             @AuthenticationPrincipal String currentUserIdStr) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);

        // 해당 유저의 주문 목록 조회
        List<OrderResponseDto> responseDtos = orderService.findOrders(userId, currentUserId);

        return ResponseEntity.ok().body(responseDtos);
    }

    /**
     * 일반 사용자가 자신의 주문 목록을 조회합니다.
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> findMyOrders(@AuthenticationPrincipal String currentUserIdStr) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);

        List<OrderResponseDto> responseDtos = orderService.findMyOrders(currentUserId);
        return ResponseEntity.ok(responseDtos);
    }

}