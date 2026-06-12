package ssemi.model;

/**
 * 반도체 시료(Sample) 도메인 모델
 * 생산 담당자가 등록하며, 주문 가능한 시료 목록을 구성함
 */
public class Sample {

    private final String sampleId;   // 시료 고유 ID
    private final String name;       // 시료명
    private final String spec;       // 규격/사양
    private int stock;               // 현재 재고 수량

    public Sample(String sampleId, String name, String spec, int stock) {
        this.sampleId = sampleId;
        this.name = name;
        this.spec = spec;
        this.stock = stock;
    }

    public String getSampleId() { return sampleId; }
    public String getName() { return name; }
    public String getSpec() { return spec; }
    public int getStock() { return stock; }

    public void addStock(int quantity) { this.stock += quantity; }
    public void reduceStock(int quantity) { this.stock -= quantity; }

    @Override
    public String toString() {
        return String.format("[%s] %s (규격: %s, 재고: %d)", sampleId, name, spec, stock);
    }
}
