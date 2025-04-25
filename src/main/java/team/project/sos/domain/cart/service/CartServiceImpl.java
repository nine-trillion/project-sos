package team.project.sos.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.domain.cart.dto.request.CartItemRequestDto;
import team.project.sos.domain.cart.dto.request.CreateCartItemsRequestDto;
import team.project.sos.domain.cart.dto.request.UpdateCartItemRequestDto;
import team.project.sos.domain.cart.dto.response.CartItemResponseDto;
import team.project.sos.domain.cart.entity.Cart;
import team.project.sos.domain.cart.entity.CartItem;
import team.project.sos.domain.cart.exception.CartError;
import team.project.sos.domain.cart.exception.CartException;
import team.project.sos.domain.cart.exception.CartItemError;
import team.project.sos.domain.cart.repository.CartItemRepository;
import team.project.sos.domain.cart.repository.CartRepository;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.menu.service.MenuService;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final MenuService menuService;

    /**
     * 장바구니에 장바구니 항목을 담습니다.
     * 장바구니가 아직 없는 사용자의 경우에는 장바구니를 생성해서 담습니다.
     */
    public List<CartItemResponseDto> saveCartItems(CreateCartItemsRequestDto requestDto,
                                                   Long currentUserId) {
        // 로그인하지 않은 경우 예외 발생
        if (currentUserId == null) {
            throw new CartException(CartError.NOT_LOGGED_IN);
        }

        // 현재 로그인한 유저 조회
        User currentUser = userService.findByIdOrElseThrow(currentUserId);

        // 장바구니 조회(없으면 생성)
        Cart cart = findOrCreateCart(currentUser);

        // 결과를 담을 리스트 선언
        List<CartItemResponseDto> results = new ArrayList<>();

        // 장바구니에 장바구니 항목 저장
        for (CartItemRequestDto dto : requestDto.getItems()) {
            Menu menu = menuService.findByIdOrElseThrow(dto.getMenuId());

            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .menu(menu)
                    .quantity(dto.getQuantity())
                    .build();

            // 장바구니 항목을 저장하고 응답 DTO를 생성해서 결과 리스트에 담기
            results.add(CartItemResponseDto.from(cartItemRepository.save(cartItem)));
        }

        return results;
    }

    /**
     * 현재 로그인한 사용자의 장바구니 항목을 조회합니다.
     * 아직 장바구니가 없는 경우 빈 리스트를 반환합니다.
     */
    public List<CartItemResponseDto> findCartItems(Long currentUserId) {
        // 로그인하지 않은 경우 예외 발생
        if (currentUserId == null) {
            throw new CartException(CartError.NOT_LOGGED_IN);
        }

        // 현재 로그인한 유저 조회
        User currentUser = userService.findByIdOrElseThrow(currentUserId);

        // 장바구니 조회(없으면 생성)
        Cart cart = findOrCreateCart(currentUser);

        // 장바구니 항목을 DTO로 변환해서 리턴
        return cartItemRepository.findByCart(cart)
                .stream()
                .map(CartItemResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 장바구니 항목의 수량을 변경합니다.
     */
    @Transactional
    public CartItemResponseDto updateCartItem(Long cartItemId,
                                              UpdateCartItemRequestDto requestDto,
                                              Long currentUserId) {
        // 로그인하지 않은 경우 예외 발생
        if (currentUserId == null) {
            throw new CartException(CartError.NOT_LOGGED_IN);
        }

        // 현재 로그인한 유저 조회
        User currentUser = userService.findByIdOrElseThrow(currentUserId);

        // 장바구니 항목 조회
        CartItem cartItem = cartItemService.findByIdOrElseThrow(cartItemId);

        // 현재 로그인한 유저의 장바구니 항목이 아닐 경우 예외 발생
        if (!cartItem.getCart().getUser().getId().equals(currentUserId)) {
            throw new CartException(CartItemError.NO_PERMISSION);
        }

        cartItem.update(requestDto.getQuantity());

        return CartItemResponseDto.from(cartItem);
    }

    /**
     * 사용자의 장바구니를 반환하고, 아직 장바구니가 존재하지 않으면 생성하여 반환합니다.
     */
    private Cart findOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

}