package team.project.sos.domain.cart.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import team.project.sos.domain.cart.dto.request.CartItemRequestDto;
import team.project.sos.domain.cart.dto.request.CreateCartItemsRequestDto;
import team.project.sos.domain.cart.dto.response.CartItemResponseDto;
import team.project.sos.domain.cart.entity.Cart;
import team.project.sos.domain.cart.entity.CartItem;
import team.project.sos.domain.cart.repository.CartItemRepository;
import team.project.sos.domain.cart.repository.CartRepository;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.menu.service.MenuService;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @InjectMocks
    CartServiceImpl cartService;

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    UserService userService;

    @Mock
    MenuService menuService;

    @Test
    @DisplayName("장바구니가 없으면 생성하고 항목 저장")
    void saveCartItemsSuccess() {
        // given
        User user = createMockUser();
        Menu menu = createMockMenu();
        Cart cart = new Cart(1L, user);
        List<CartItemRequestDto> requestDtos = createMockCartItemDtos();

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .menu(menu)
                .quantity(2)
                .build();

        when(userService.findByIdOrElseThrow(user.getId())).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty()); // 아직 장바구니가 존재하지 않는 경우
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(menuService.findByIdOrElseThrow(menu.getId())).thenReturn(menu);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        // when
        List<CartItemResponseDto> results = cartService.saveCartItems(requestDtos, user.getId());

        // then
        assertThat(results).hasSize(2);
        assertEquals(2, results.get(0).getQuantity());
        assertEquals(menu.getId(), results.get(0).getMenuId());
    }

    private User createMockUser() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "role", UserRole.USER);
        ReflectionTestUtils.setField(user, "grade", Grade.BASIC);
        return user;
    }

    private Menu createMockMenu() {
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", 1L);
        return menu;
    }

    private Cart createMockCart() {
        User user = createMockUser();
        return Cart.builder().id(1L).user(user).build();
    }

    private List<CartItemRequestDto> createMockCartItemDtos() {
        Cart cart = createMockCart();
        Menu menu = createMockMenu();
        CartItem cartItem1 = CartItem.builder().id(1L).cart(cart).menu(menu).quantity(1).build();
        CartItem cartItem2 = CartItem.builder().id(2L).cart(cart).menu(menu).quantity(2).build();
        return Stream.of(cartItem1, cartItem2).map(CartItemRequestDto::from).toList();
    }

}