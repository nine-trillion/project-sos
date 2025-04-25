package team.project.sos.domain.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.cart.dto.request.CreateCartItemsRequestDto;
import team.project.sos.domain.cart.dto.request.UpdateCartItemRequestDto;
import team.project.sos.domain.cart.dto.response.CartItemResponseDto;
import team.project.sos.domain.cart.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<List<CartItemResponseDto>> saveCartItems(
            CreateCartItemsRequestDto requestDto,
            @AuthenticationPrincipal String currentUserIdStr) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);

        List<CartItemResponseDto> results = cartService.saveCartItems(requestDto, currentUserId);

        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItemResponseDto>> findCartItems(
            @AuthenticationPrincipal String currentUserIdStr) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);

        List<CartItemResponseDto> results = cartService.findCartItems(currentUserId);

        return ResponseEntity.ok(results);
    }

    @PutMapping("/cart/{cartItemId}")
    public ResponseEntity<CartItemResponseDto> updateCartItem(@PathVariable Long cartItemId,
                                                              @RequestBody UpdateCartItemRequestDto requestDto,
                                                              @AuthenticationPrincipal String currentUserIdStr) {
        // 토큰에서 로그인된 사용자 정보 꺼내기
        Long currentUserId = Long.parseLong(currentUserIdStr);

        CartItemResponseDto responseDto = cartService.updateCartItem(cartItemId, requestDto, currentUserId);

        return ResponseEntity.ok().body(responseDto);
    }

}