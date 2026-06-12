package ssemi;

import ssemi.controller.OrderController;
import ssemi.controller.SampleController;
import ssemi.model.Order;
import ssemi.model.Sample;
import ssemi.view.ConsoleView;

/**
 * 진입점 — MVC 인스턴스 생성 및 메인 루프 실행
 * Controller끼리 의존성은 생성자 주입으로 처리
 */
public class Main {

    public static void main(String[] args) {
        SampleController sampleController = new SampleController();
        OrderController orderController = new OrderController(sampleController);
        ConsoleView view = new ConsoleView();

        while (true) {
            view.showMainMenu();
            String choice = view.readMenuChoice();

            switch (choice) {
                case "1" -> handleRegisterSample(sampleController, view);
                case "2" -> view.showSampleList(sampleController.getAllSamples());
                case "3" -> handleCreateOrder(sampleController, orderController, view);
                case "4" -> view.showOrderList(orderController.getAllOrders());
                case "5" -> handleApproveOrReject(orderController, view);
                case "6" -> handleRelease(orderController, view);
                case "7" -> view.showMonitoring(orderController.getMonitoringOrders());
                case "0" -> {
                    view.showMessage("시스템 종료");
                    return;
                }
                default -> view.showError("유효하지 않은 메뉴입니다.");
            }
        }
    }

    private static void handleRegisterSample(SampleController ctrl, ConsoleView view) {
        String name  = view.readLine("시료명 > ");
        String spec  = view.readLine("규격   > ");
        int stock    = view.readInt("초기 재고 > ");
        Sample s = ctrl.registerSample(name, spec, stock);
        view.showMessage("시료 등록 완료: " + s);
    }

    private static void handleCreateOrder(SampleController sampleCtrl,
                                          OrderController orderCtrl,
                                          ConsoleView view) {
        view.showSampleList(sampleCtrl.getAllSamples());
        String sampleId    = view.readLine("시료 ID > ");
        String customerId  = view.readLine("고객 ID > ");
        int quantity       = view.readInt("수량   > ");

        if (sampleCtrl.findById(sampleId).isEmpty()) {
            view.showError("존재하지 않는 시료 ID: " + sampleId);
            return;
        }
        Order o = orderCtrl.createOrder(sampleId, customerId, quantity);
        view.showMessage("주문 접수 완료: " + o);
    }

    private static void handleApproveOrReject(OrderController ctrl, ConsoleView view) {
        view.showOrderList(ctrl.getAllOrders());
        String orderId = view.readLine("주문 ID > ");

        view.showApproveMenu();
        String sub = view.readMenuChoice();

        boolean result = switch (sub) {
            case "1" -> ctrl.approveOrder(orderId);
            case "2" -> ctrl.rejectOrder(orderId);
            default  -> { view.showError("잘못된 선택"); yield false; }
        };

        if (result) {
            ctrl.findById(orderId).ifPresent(o -> view.showMessage("처리 완료: " + o));
        } else {
            view.showError("처리 실패 — 주문 ID 또는 상태 확인 필요");
        }
    }

    private static void handleRelease(OrderController ctrl, ConsoleView view) {
        view.showOrderList(ctrl.getAllOrders());
        String orderId = view.readLine("출고할 주문 ID > ");

        if (ctrl.releaseOrder(orderId)) {
            ctrl.findById(orderId).ifPresent(o -> view.showMessage("출고 완료: " + o));
        } else {
            view.showError("출고 실패 — CONFIRMED 상태 주문만 출고 가능");
        }
    }
}
