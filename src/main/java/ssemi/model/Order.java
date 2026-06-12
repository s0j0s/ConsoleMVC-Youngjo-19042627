package ssemi.model;

/**
 * 주문(Order) 도메인 모델
 * 주문 담당자가 생성하며, 상태 흐름에 따라 관리됨
 * 상태: RESERVED → (REJECTED | PRODUCING | CONFIRMED) → RELEASED
 */
public class Order {

    private final String orderId;       // 주문 고유 ID
    private final String sampleId;      // 요청 시료 ID
    private final String customerId;    // 고객 ID
    private final int quantity;         // 요청 수량
    private OrderStatus status;         // 현재 주문 상태

    public Order(String orderId, String sampleId, String customerId, int quantity) {
        this.orderId = orderId;
        this.sampleId = sampleId;
        this.customerId = customerId;
        this.quantity = quantity;
        this.status = OrderStatus.RESERVED;
    }

    public String getOrderId() { return orderId; }
    public String getSampleId() { return sampleId; }
    public String getCustomerId() { return customerId; }
    public int getQuantity() { return quantity; }
    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%s] 시료:%s 고객:%s 수량:%d 상태:%s",
                orderId, sampleId, customerId, quantity, status);
    }
}
