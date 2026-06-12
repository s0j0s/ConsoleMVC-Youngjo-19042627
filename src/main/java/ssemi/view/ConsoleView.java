package ssemi.view;

import ssemi.model.Order;
import ssemi.model.Sample;

import java.util.List;
import java.util.Scanner;

/**
 * 콘솔 기반 View 레이어
 * 모든 입출력을 담당 — Controller/Model은 콘솔 직접 접근 금지
 */
public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);

    // ─── 출력 메서드 ─────────────────────────────────────────

    public void showMainMenu() {
        System.out.println("\n===== S-Semi 시료 관리 시스템 =====");
        System.out.println("[1] 시료 등록          (생산 담당자)");
        System.out.println("[2] 시료 목록 조회");
        System.out.println("[3] 주문 생성          (주문 담당자)");
        System.out.println("[4] 주문 목록 조회");
        System.out.println("[5] 주문 승인/거절      (생산 담당자)");
        System.out.println("[6] 출고 처리          (생산 담당자)");
        System.out.println("[7] 모니터링 현황");
        System.out.println("[0] 종료");
        System.out.print("선택 > ");
    }

    public void showSampleList(List<Sample> samples) {
        System.out.println("\n─── 시료 목록 ───────────────────────");
        if (samples.isEmpty()) {
            System.out.println("  등록된 시료 없음");
            return;
        }
        samples.forEach(s -> System.out.println("  " + s));
    }

    public void showOrderList(List<Order> orders) {
        System.out.println("\n─── 주문 목록 ───────────────────────");
        if (orders.isEmpty()) {
            System.out.println("  주문 없음");
            return;
        }
        orders.forEach(o -> System.out.println("  " + o));
    }

    public void showMonitoring(List<Order> orders) {
        System.out.println("\n─── 모니터링 현황 (REJECTED 제외) ──");
        if (orders.isEmpty()) {
            System.out.println("  모니터링 대상 주문 없음");
            return;
        }
        orders.forEach(o -> System.out.println("  " + o));
    }

    public void showMessage(String message) {
        System.out.println("  >> " + message);
    }

    public void showError(String message) {
        System.out.println("  [오류] " + message);
    }

    // ─── 입력 메서드 ─────────────────────────────────────────

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  [오류] 숫자를 입력하세요.");
            }
        }
    }

    public String readMenuChoice() {
        return scanner.nextLine().trim();
    }

    // ─── 승인/거절 서브메뉴 ──────────────────────────────────

    public void showApproveMenu() {
        System.out.println("\n[1] 주문 승인  [2] 주문 거절");
        System.out.print("선택 > ");
    }
}
