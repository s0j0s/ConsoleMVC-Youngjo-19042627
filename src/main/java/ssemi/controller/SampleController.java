package ssemi.controller;

import ssemi.model.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 시료(Sample) 관련 비즈니스 로직 처리
 * 생산 담당자 기능: 시료 등록, 조회, 재고 관리
 */
public class SampleController {

    private final List<Sample> samples = new ArrayList<>();
    private int idSequence = 1;

    public Sample registerSample(String name, String spec, int initialStock) {
        String id = "S" + String.format("%03d", idSequence++);
        Sample sample = new Sample(id, name, spec, initialStock);
        samples.add(sample);
        return sample;
    }

    public List<Sample> getAllSamples() {
        return List.copyOf(samples);
    }

    public Optional<Sample> findById(String sampleId) {
        return samples.stream()
                .filter(s -> s.getSampleId().equals(sampleId))
                .findFirst();
    }

    /** 생산 완료 시 재고 증가 */
    public boolean addStock(String sampleId, int quantity) {
        return findById(sampleId).map(s -> {
            s.addStock(quantity);
            return true;
        }).orElse(false);
    }

    /** 출고 시 재고 차감 */
    public boolean reduceStock(String sampleId, int quantity) {
        return findById(sampleId).map(s -> {
            if (s.getStock() < quantity) return false;
            s.reduceStock(quantity);
            return true;
        }).orElse(false);
    }
}
