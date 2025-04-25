package team.project.sos.domain.dashboard.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.project.sos.domain.dashboard.dto.response.DashboardResponse;
import team.project.sos.domain.order.entity.QOrder;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.store.enums.StoreStatus;
import team.project.sos.domain.store.service.StoreService;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DashboardRepositoryImpl implements DashboardRepository {

    private final JPAQueryFactory queryFactory;
    private final StoreService storeService;

    @Override
    public DashboardResponse getStoreDashboard(Long storeId, LocalDateTime start, LocalDateTime end, String basis) {
        QOrder order = QOrder.order;

        Tuple result = queryFactory
                .select(
                        order.user.countDistinct(),
                        order.count(),
                        order.price.sum()
                )
                .from(order)
                .where(
                        order.store.id.eq(storeId),
                        order.store.status.eq(StoreStatus.OPERATING),
                        order.status.eq(OrderStatus.COMPLETED),
                        order.requestedAt.between(start, end)
                )
                .fetchOne();

        long customerCount = Optional.ofNullable(result.get(0, Long.class)).orElse(0L);
        long orderCount = Optional.ofNullable(result.get(1, Long.class)).orElse(0L);
        long totalSales = Optional.ofNullable(result.get(2, Integer.class)).orElse(0).longValue();

        return DashboardResponse.builder()
                .store(storeService.findStore(storeId))
                .customerCount(customerCount)
                .orderCount(orderCount)
                .totalSales(totalSales)
                .basis(basis)
                .build();
    }

    @Override
    public DashboardResponse getOwnerDashboard(Long ownerId, LocalDateTime start, LocalDateTime end, String basis) {
        QOrder order = QOrder.order;

        Tuple result = queryFactory
                .select(
                        order.user.countDistinct(),
                        order.count(),
                        order.price.sum()
                )
                .from(order)
                .where(
                        order.store.owner.id.eq(ownerId),
                        order.store.status.eq(StoreStatus.OPERATING),
                        order.status.eq(OrderStatus.COMPLETED),
                        order.requestedAt.between(start, end)
                )
                .fetchOne();

        long customerCount = Optional.ofNullable(result.get(0, Long.class)).orElse(0L);
        long orderCount = Optional.ofNullable(result.get(1, Long.class)).orElse(0L);
        long totalSales = Optional.ofNullable(result.get(2, Integer.class)).orElse(0).longValue();

        return DashboardResponse.builder()
                .store(null)
                .customerCount(customerCount)
                .orderCount(orderCount)
                .totalSales(totalSales)
                .basis(basis)
                .build();
    }
}
