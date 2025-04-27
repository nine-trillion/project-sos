package team.project.sos.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.menu.repository.MenuRepository;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.order.entity.OrderItem;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.order.repository.OrderItemRepository;
import team.project.sos.domain.order.repository.OrderRepository;
import team.project.sos.domain.review.entity.Review;
import team.project.sos.domain.review.repository.ReviewRepository;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.store.repository.StoreRepository;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.repository.UserRepository;
import team.project.sos.domain.user.security.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.update("DELETE FROM review");
        jdbcTemplate.update("DELETE FROM order_item");
        jdbcTemplate.update("DELETE FROM orders");
        jdbcTemplate.update("DELETE FROM menu");
        jdbcTemplate.update("DELETE FROM store");
        jdbcTemplate.update("DELETE FROM user");

        List<User> users = new ArrayList<>();
        List<Store> stores = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        // 사용자 10명 생성 (3명은 OWNER)
        for (int i = 1; i <= 10; i++) {
            String email = "user" + i + "@test.com";
            String password = passwordEncoder.encode("Password1@");
            String nickname = "User" + i;
            String phone = "010-0000-00" + i;
            UserRole role = (i <= 3) ? UserRole.OWNER : UserRole.USER;
            Grade grade = Grade.BASIC;

            User user = new User(email, password, nickname, phone, role, grade);
            users.add(userRepository.save(user));
        }

        // 사장당 가게 2개 생성
        for (User owner : users.stream().filter(u -> u.getRole() == UserRole.OWNER).toList()) {
            for (int i = 1; i <= 2; i++) {
                Store store = new Store(
                        owner.getNickname() + " 가게" + i,
                        LocalTime.of(10, 0),
                        LocalTime.of(22, 0),
                        10000,
                        owner
                );
                stores.add(storeRepository.save(store));
            }
        }

        // 가게당 메뉴 3개씩
        for (Store store : stores) {
            for (int i = 1; i <= 3; i++) {
                Menu menu = new Menu(store.getId(), "메뉴" + i + "@" + store.getName(), 5000 * i, "식사류");
                menus.add(menuRepository.save(menu));
            }
        }

        Random random = new Random();

        // 주문 30건 생성 (user 4~10이 랜덤하게 주문)
        for (int i = 0; i < 30; i++) {
            User customer = users.get(random.nextInt(7) + 3); // user[3] ~ user[9]
            Store store = stores.get(random.nextInt(stores.size()));

            List<Menu> storeMenus = menus.stream()
                    .filter(m -> m.getStoreId().equals(store.getId()))
                    .toList();

            int itemCount = random.nextInt(2) + 2;
            int total = 0;
            List<Menu> selectedMenus = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();

            for (int j = 0; j < itemCount; j++) {
                Menu menu = storeMenus.get(j % storeMenus.size());
                int quantity = random.nextInt(3) + 1;
                total += menu.getPrice() * quantity;

                selectedMenus.add(menu);
                quantities.add(quantity);
            }

            // 주문 저장
            Order order = Order.builder()
                    .user(customer)
                    .store(store)
                    .status(OrderStatus.COMPLETED)
                    .price(total)
                    .requestedAt(LocalDateTime.now().minusDays(random.nextInt(10)))
                    .build();
            order = orderRepository.save(order);
            orders.add(order);

            // 주문 아이템 저장
            for (int j = 0; j < itemCount; j++) {
                OrderItem item = OrderItem.builder()
                        .order(order)
                        .menu(selectedMenus.get(j))
                        .quantity(quantities.get(j))
                        .build();
                orderItemRepository.save(item);
            }
        }

        // 리뷰 10건 생성
        int reviewTargetCount = Math.min(10, orders.size());
        for (int i = 0; i < reviewTargetCount; i++) {
            Order order = orders.get(i);
            Review review = Review.builder()
                    .user(order.getUser())
                    .store(order.getStore())
                    .order(order)
                    .rating(random.nextInt(5) + 1)
                    .content("리뷰 내용 " + (i + 1))
                    .build();
            reviewRepository.save(review);
        }

        log.info("더미 데이터 초기화 완료");
    }
}