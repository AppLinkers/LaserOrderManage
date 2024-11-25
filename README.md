[![thumbnail](https://github.com/user-attachments/assets/72c6d094-d1d7-4dfb-af1e-38e7f037a671)](https://www.kumoh.org/)

# 금오 거래 센터 🏭
### 기존 전화와 서류로 진행되는 레이저 가공 거래에 디지털 트랜스포메이션을 수행한 플랫폼 🌐
- 거래 생성, 견적서 및 발주서 작성, 인수자 서명을 통한 거래 완료 등의 기능을 제공합니다.
- 레이저 가공 업체 관리자를 위한 거래 및 자재 재고 데이터 관리와 분석 그래프를 제공합니다.

---
## ERD 🗄️ → [링크](https://www.erdcloud.com/p/AuF5n5TJjaPiiLiyX)
### 고객 사용자 및 거래 ERD
![order_erd](https://github.com/user-attachments/assets/cfd294d5-bd45-47b5-bbaf-8f57aaeff4ad)
### 공장 사용자 및 자재 ERD
![image](https://github.com/user-attachments/assets/19eea7f4-b23e-4022-b31f-d8ccd75ae91d)

## 프로젝트 아키텍처 ⚙️
![architecture](https://github.com/user-attachments/assets/3fa98f8e-a560-4efd-9ec0-adb5f5ae01e9)

---
## 주요 비즈니스 기능
|기능|도메인|API 문서|API(컨트롤러)|서비스 or 레포지토리|
|-----|---|---|---|---|
|공장 사용자 거래 조회|[Order.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Order.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/factory-order-f6d3fa06212047579af30e9a6da52b22)|[FactoryOrderHistoryAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/api/FactoryOrderHistoryAPI.java#L83)|[OrderRpositoryCustomImpl.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/repository/OrderRepositoryCustomImpl.java#L186)|
|고객 사용자 거래 조회|[Order.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Order.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/customer-order-6dd94e4596e94a438aa343cf4eaa095b)|[CustomerOrderHistoryAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/customer/api/CustomerOrderHistoryAPI.java#L39)|[OrderRpositoryCustomImpl.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/repository/OrderRepositoryCustomImpl.java#L44)|
|거래 상세 보기|[Order.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Order.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/order-order-id-detail-470c46c0bd864ec18db1623ed116a43c)|[OrderAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/api/OrderAPI.java#L30)|[OrderRpositoryCustomImpl.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/repository/OrderRepositoryCustomImpl.java#L323)|
|도면 파일 업로드|[Drawing.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Drawing.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/drawing-440a4747c37f49fa95a85e8d64eacdea)|[DrawingAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/api/DrawingAPI.java#L28)|[DrawingService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/service/DrawingService.java#L62)|
|거래 생성|[Order.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Order.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/customer-order-937ea33127634883be5a28c41c35efbe)|[CustomerOrderAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/customer/api/CustomerOrderAPI.java#L41)|[CustomerOrderService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/customer/service/CustomerOrderService.java#L45)|
|견적서 작성 및 수정|[Quotation](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Quotation.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/factory-order-order-id-quotation-649e8458f83b47d290635019c63ac9d1)|[FactoryOrderAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/api/FactoryOrderAPI.java#L66)|[FactoryOrderService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/service/FactoryOrderService.java#L51)|
|견적서 승인|[Quotation](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Quotation.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/customer-order-order-id-quotation-8a9849d498fa4d85aadf4176bdfb53c0)|[CustomerOrderAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/customer/api/CustomerOrderAPI.java#L167)|[CustomerOrderService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/customer/service/CustomerOrderService.java#L174)|
|발주서 작성 및 수정|[PurchaseOrder](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/PurchaseOrder.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/customer-order-order-id-purchase-order-a677dc9a4c1d4b03a8fff1e999e63710)|[CustomerOrderAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/customer/api/CustomerOrderAPI.java#L189)|[CustomerOrderService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/customer/service/CustomerOrderService.java#L189)|
|발주서 승인|[PurchaseOrder](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/PurchaseOrder.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/factory-order-order-id-purchase-order-7161e2e67f4e49b2b9835b2527158c64)|[FactoryOrderAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/api/FactoryOrderAPI.java#L99)|[FactoryOrderService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/service/FactoryOrderService.java#L100)|
|제작 완료|[Order.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Order.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/factory-order-order-id-stage-production-completed-f103cafd214d4de698ae0c8b6d1e646b)|[FactoryOrderAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/api/FactoryOrderAPI.java#L117)|[FactoryOrderService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/service/FactoryOrderService.java#L115)|
|거래 완료<br>(인수자 정보 및 서명 등록)|[Order.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/order/domain/Order.java)|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/factory-order-order-id-stage-completed-e7af1331dba749ca9e34cdf1d68a9be3)|[FactoryOrderAPI.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/api/FactoryOrderAPI.java#L159)|[FactoryOrderService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/factory/service/FactoryOrderService.java#L143)|


---
## 트러블 이슈 및 성능 개선
|이슈|사용 기술|해결 방법|해결 과정|주요 코드|
|-----|---|---|---|---|
|메일 발송 로직의<br>응답 지연 문제|Java CompletableFuture|CompletableFuture를 활용한 비동기 로직 구현 및 예외 처리|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/1255a0fb769580bc8033e4333e03e452)|[EmailService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/common/email/EmailService.java)|
|자재 재고 관리에 대한 동시 요청 시,<br>Race Condition 발생|Spring @Lock|비관적 Lock 방식을 활용한 동시성 문제 해결|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/1255a0fb769580578c63f445dc930f5d)|[Ingredientrepository.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/ingredient/repository/IngredientRepository.java)|
|거래 단계 변경 및 자재 재고 관리의<br>자동화를 위한 스케줄링 필요|Spring Quartz|Spring Quartz를 활용한 스케줄러 구현|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/Quartz-27fd3a606be645268aaba11231045301)|[ScheduleService.java](https://github.com/AppLinkers/LaserOrderManage-server/blob/main/src/main/java/com/laser/ordermanage/common/scheduler/service/ScheduleService.java)|
|인증 및 인가 기능|Spring Security, JWT|JWT 기반 인증 및 Role, Authority 기반 인가 기능 개발|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/JWT-Role-Authority-af00936eb7fd49789cf76572ad5ce73c)|[/security](https://github.com/AppLinkers/LaserOrderManage-server/tree/main/src/main/java/com/laser/ordermanage/common/security)|
|통합 및 단위 테스트 코드 작성|Spring Test, JUnit|1,195개 테스트 코드 작성 및 98% 라인 커버리지|[<img src="https://github.com/user-attachments/assets/d8f94d14-0b8e-4bf9-82d8-f3485d887a68" width="36" height="36">](https://yuseogi0218.notion.site/ba1f4147d2c6451ba97f99de92103e84)|[/src/test/](https://github.com/AppLinkers/LaserOrderManage-server/tree/main/src/test)|

