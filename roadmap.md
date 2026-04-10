# 🗺️ Kamnyweso Liquor - Backend Roadmap

## ✅ PHASE 1: Data Layer (COMPLETED)
- [x] Define Database Schema & Architecture
- [x] Create JPA Entities mapping to tables
- [x] Configure relationships (One-to-Many, Many-to-One)
- [x] Set up Spring Data JPA Repositories
- [x] Configure PostgreSQL connection via `docker-compose`

## ✅ PHASE 2: Business Logic / Service Layer (COMPLETED)
- [x] `BranchService`: HQ and branch lookups
- [x] `UserService`: Registration, approval, suspension
- [x] `OrderService`: Order creation and status management
- [x] `StockService`: Stock lookups and dynamic restocking
- [x] `DispatchService`: HQ dispatch creation and branch confirmation
- [x] `StockReturnService`: Damaged goods tracking
- [x] `LoyaltyService`: Point accumulation and redemption math
- [x] `ReportService`: Sales aggregations by date/branch
- [x] `DrinkService`: Product catalogue management

## ✅ PHASE 3: API & Web Layer (COMPLETED)
- [x] Define DTO (Data Transfer Object) architecture (Records)
- [x] Create `BranchController` (Full CRUD)
- [x] Create `UserController` (Staff Management)
- [x] Create `DrinkController` (Product Management)
- [x] Create `OrderController` (Checkout & Stock Deduction)
- [x] Create `StockController` & `DispatchController` (Inventory & Internal Transfers)
- [x] Create `LoyaltyController` (Customer Rewards)
- [x] Create `StockReturnController` (Damaged Goods)
- [x] Create `ReportController` (Sales & Profit Analysis)
- [ ] Build a Global Exception Handler (`@ControllerAdvice`) for clean JSON error messages

## 🚧 PHASE 4: Security & Authentication (NEXT UP)
- [ ] Add `spring-boot-starter-mail` & configure **Resend SMTP**
- [ ] Implement OTP Generation & Email dispatch
- [ ] Set up Spring Security Filter Chain
- [ ] Implement JWT Token Generation & Validation
- [ ] Create Role-Based Access Control (`@PreAuthorize("hasRole('ADMIN')")`)
- [ ] Build `/api/auth/` controllers (Login, Verify, Register)

## ⏳ PHASE 5: Production Readiness (PENDING)
- [ ] Configure CORS for Frontend integration
- [ ] Add pagination for large lists (Orders, Transactions)
- [ ] (Optional) Add Swagger/OpenAPI documentation
- [ ] Switch `ddl-auto` from `update` to `validate`