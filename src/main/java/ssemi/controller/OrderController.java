package ssemi.controller;

import ssemi.model.Order;
import ssemi.model.OrderStatus;
import ssemi.model.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 주문(Order) 관련 비즈니스 로직 처리
 *
 * 주문 담당자 기능: 주문 생성
 * 생산 담당자 기능: 주문 승인/거절, 출고 처리
 *
 * 승인 시 재고 확인:
 *   - 재고 충분 → CONFIRMED (출고 대기)
 *   - 재고 부족 → PRODUCING (생산 중)
 */
public class OrderController {

    private final List<Order> orders = new ArrayList<>();
    private final SampleController sampleController;
    private int idSequence = 1;

    public OrderController(SampleController sampleController) {
        this.sampleController = sampleController;
    }

    public Order createOrder(String sampleId, String customerId, int quantity) {
        String id = "O" + String.format("%03d", idSequence++);
        Order order = new Order(id, sampleId, customerId, quantity);
        orders.add(order);
        return order;
    }

    /**
     * 생산 담당자가 주문 승인
     * 재고 충분 → CONFIRMED, 부족 → PRODUCING
     */
    public boolean approveOrder(String orderId) {
        Optional<Order> optOrder = findById(orderId);
        if (optOrder.isEmpty()) return false;

        Order order = optOrder.get();
        if (order.getStatus() != OrderStatus.RESERVED) return false;

        Optional<Sample> optSample = sampleController.findById(order.getSampleId());
        if (optSample.isEmpty()) return false;

        Sample sample = optSample.get();
        if (sample.getStock() >= order.getQuantity()) {
            sampleController.reduceStock(sample.getSampleId(), order.getQuantity());
            order.setStatus(OrderStatus.CONFIRMED);
        } else {
            order.setStatus(OrderStatus.PRODUCING);
        }
        return true;
    }

    /** 생산 담당자가 주문 거절 */
    public boolean rejectOrder(String orderId) {
        return findById(orderId).map(order -> {
            if (order.getStatus() != OrderStatus.RESERVED) return false;
            order.setStatus(OrderStatus.REJECTED);
            return true;
        }).orElse(false);
    }

    /**
     * 생산 완료 후 출고 처리 (CONFIRMED → RELEASED)
     * PRODUCING 상태 주문은 생산 완료 후 재고 보충 → approveOrder 재호출 권장
     */
    public boolean releaseOrder(String orderId) {
        return findById(orderId).map(order -> {
            if (order.getStatus() != OrderStatus.CONFIRMED) return false;
            order.setStatus(OrderStatus.RELEASED);
            return true;
        }).orElse(false);
    }

    public List<Order> getAllOrders() {
        return List.copyOf(orders);
    }

    /** 모니터링 대상: REJECTED 제외 */
    public List<Order> getMonitoringOrders() {
        return orders.stream()
                .filter(o -> o.getStatus() != OrderStatus.REJECTED)
                .toList();
    }

    public Optional<Order> findById(String orderId) {
        return orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst();
    }
}
