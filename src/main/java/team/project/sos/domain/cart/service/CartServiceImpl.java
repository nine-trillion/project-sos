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
    @Override
    public List<CartItemResponseDto> saveCartItems(List<CartItemRequestDto> requestDtos,
                                                   Long currentUserId) {
        throwExceptionIfNotLoggedIn(currentUserId);

        User currentUser = userService.findByIdOrElseThrow(currentUserId);

        Cart cart = findOrCreateCart(currentUser);

        List<CartItemResponseDto> results = new ArrayList<>();

        // 장바구니에 장바구니 항목 저장
        for (CartItemRequestDto dto : requestDtos) {
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
    @Override
    public List<CartItemResponseDto> findCartItems(Long currentUserId) {
        throwExceptionIfNotLoggedIn(currentUserId);

        User currentUser = userService.findByIdOrElseThrow(currentUserId);

        Cart cart = findOrCreateCart(currentUser);

        // 장바구니 항목을 DTO로 변환해서 리턴
        return cartItemRepository.findByCart(cart)
                .stream()
                .map(CartItemResponseDto::from)
                .toList();
    }

    /**
     * 장바구니 항목의 수량을 변경합니다.
     */
    @Override
    @Transactional
    public CartItemResponseDto updateCartItem(Long cartItemId,
                                              UpdateCartItemRequestDto requestDto,
                                              Long currentUserId) {
        throwExceptionIfNotLoggedIn(currentUserId);

        User currentUser = userService.findByIdOrElseThrow(currentUserId);
        CartItem cartItem = cartItemService.findByIdOrElseThrow(cartItemId);
        validateCartItemOwner(cartItem, currentUser);

        cartItem.update(requestDto.getQuantity());

        return CartItemResponseDto.from(cartItem);
    }

    /**
     * 장바구니 항목을 삭제합니다.
     */
    @Override
    public void deleteCartItem(Long cartItemId, Long currentUserId) {
        throwExceptionIfNotLoggedIn(currentUserId);

        User currentUser = userService.findByIdOrElseThrow(currentUserId);
        CartItem cartItem = cartItemService.findByIdOrElseThrow(cartItemId);
        validateCartItemOwner(cartItem, currentUser);

        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * 로그인을 하지 않은 경우 예외를 발생시킵니다.
     */
    private void throwExceptionIfNotLoggedIn(Long currentUserId) {
        if (currentUserId == null) {
            throw new CartException(CartError.NOT_LOGGED_IN);
        }
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

    /**
     * 해당 장바구니 항목이 현재 로그인한 사용자 소유가 맞는지 확인합니다.
     */
    private void validateCartItemOwner(CartItem cartItem, User currentUser) {
        if (!cartItem.getCart().getUser().getId().equals(currentUser.getId())) {
            throw new CartException(CartItemError.NO_PERMISSION);
        }
    }

}