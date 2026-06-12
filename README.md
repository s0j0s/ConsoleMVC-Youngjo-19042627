# ConsoleMVC-Youngjo — S-Semi 반도체 시료 관리 시스템 POC

가상의 반도체 회사 **S-Semi**의 시료 생산주문관리 시스템을 콘솔 기반 MVC 패턴으로 구현한 POC 프로젝트입니다.

---

## POC 구성

| # | 항목 | 상태 |
|---|------|------|
| 1 | MVC 스켈레톤 코드 | ✅ 완료 |
| 2 | 데이터 영속성 처리 (H2 DB) | 🔲 예정 |
| 3 | 데이터 모니터링 Tool | 🔲 예정 |
| 4 | Dummy 데이터 생성 Tool | 🔲 예정 |

---

## POC 1 — MVC 스켈레톤

### 패키지 구조

```
src/main/java/ssemi/
├── Main.java                       # 진입점, MVC 조립 및 메인 루프
├── model/
│   ├── OrderStatus.java            # 주문 상태 Enum
│   ├── Sample.java                 # 시료 도메인 객체
│   └── Order.java                  # 주문 도메인 객체
├── controller/
│   ├── SampleController.java       # 시료 등록/조회/재고 관리
│   └── OrderController.java        # 주문 생성/승인/거절/출고
└── view/
    └── ConsoleView.java            # 콘솔 I/O 전담
```

### 역할 분리 원칙

- **Model** — 순수 데이터 객체. 콘솔 접근 없음
- **Controller** — 비즈니스 로직 처리. `System.out` 없음
- **View** — 모든 입출력 전담. 로직 없음

### 주문 상태 흐름

```
RESERVED
  ├─ 거절 → REJECTED       (모니터링 제외)
  ├─ 재고 충분 → CONFIRMED → RELEASED
  └─ 재고 부족 → PRODUCING → (재고 보충 후) → CONFIRMED → RELEASED
```

### 주요 비즈니스 로직

**주문 승인 (`OrderController.approveOrder`)**
- 재고 ≥ 요청 수량 → `CONFIRMED` + 재고 차감
- 재고 < 요청 수량 → `PRODUCING` (생산 대기)

---

## 기술 스택

- Java 17
- Gradle 8.7
- H2 DB (POC 2부터 사용)

---

## 실행 방법

```bash
# 콘솔 한글 출력을 위해 UTF-8 설정 (Windows)
chcp 65001

# 빌드
./gradlew build

# 실행
./gradlew run
```

---

## 역할별 흐름

| 역할 | 담당 기능 |
|------|-----------|
| 고객 | 시료 요청 (이메일 → 주문 담당자) |
| 주문 담당자 | 주문서 생성 (`메뉴 3`) |
| 생산 담당자 | 시료 등록(`메뉴 1`), 주문 승인/거절(`메뉴 5`), 출고 처리(`메뉴 6`) |
