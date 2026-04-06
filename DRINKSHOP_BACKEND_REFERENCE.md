# DRINKSHOP — BACKEND REFERENCE
> Tables & REST API Endpoints by Portal
> Last updated: April 2026

---

## 1. DATABASE TABLES

### Shared Tables (Used by Both Admin & Branch)
These tables are central — both portals read/write to them.

| Table | Purpose |
|---|---|
| `branches` | Stores HQ + all branch locations |
| `users` | All staff accounts (Admin, Manager, Cashier) |
| `sessions` | JWT session tracking per user |
| `drinks` | Product catalogue (all drinks) |
| `stock` | Stock levels per drink per branch |

```sql
branches (id, name, location, manager_id, created_at)

users (id, name, email, phone, password_hash, role, branch_id,
       status, last_login, created_at)
-- roles: ADMIN, MANAGER, CASHIER

sessions (id, user_id, jwt_token, login_at, last_activity,
          ip_address, is_active)

drinks (id, name, brand, price, cost_price, category, image_emoji)

stock (id, drink_id, branch_id, quantity, min_threshold,
       last_restocked_at)
```

---

### Admin Portal Tables (HQ Only)
Admin reads from all branches — full visibility.

| Table | Purpose |
|---|---|
| `orders` | All customer orders across all branches |
| `order_items` | Line items per order |
| `dispatch_orders` | HQ → Branch stock dispatches |
| `dispatch_items` | Line items per dispatch |
| `stock_returns` | Damaged stock return requests from branches |
| `reports` | Generated report metadata |

```sql
orders (id, branch_id, customer_name, customer_phone,
        loyalty_card_id, staff_id, total_amount, status, created_at)
-- status: PENDING, PROCESSING, COMPLETED, CANCELLED

order_items (id, order_id, drink_id, quantity, unit_price,
             points_earned)

dispatch_orders (id, branch_id, created_by, status,
                 dispatched_at, confirmed_at, received_at,
                 driver_name, vehicle_plate, notes)
-- status: PENDING, APPROVED, DISPATCHED, IN_TRANSIT,
--         CONFIRMED, RECEIVED, REJECTED

dispatch_items (id, dispatch_order_id, drink_id, quantity)

stock_returns (id, branch_id, drink_id, quantity, reason,
               status, hq_response, raised_by, created_at)
-- status: PENDING_REVIEW, APPROVED, REJECTED, REPLACEMENT_SENT

reports (id, type, branch_id, date_from, date_to,
         generated_by, file_path, created_at)
```

---

### Branch Portal Tables (Branch Scoped)
Branch reads only its own data — no cross-branch visibility.

| Table | Purpose |
|---|---|
| `orders` | This branch's customer orders only |
| `order_items` | Line items for this branch's orders |
| `dispatch_orders` | Dispatches addressed to this branch only |
| `stock` | This branch's stock levels only |
| `stock_returns` | This branch's return requests only |
| `loyalty_customers` | Loyalty customers registered at this branch |
| `loyalty_transactions` | Points history per loyalty customer |

```sql
loyalty_customers (id, name, phone, card_id, tier, points_balance,
                   branch_id, registered_at)
-- tiers: BRONZE, SILVER, GOLD, PLATINUM

loyalty_transactions (id, customer_id, order_id, points_earned,
                      points_redeemed, transaction_type, created_at)
```

---

## 2. REST API ENDPOINTS

### Auth (Both Portals)
```
POST   /api/auth/login           → returns JWT + triggers OTP SMS
POST   /api/auth/verify-otp      → validates OTP, activates session
POST   /api/auth/register        → creates pending user (requires approval)
POST   /api/auth/logout          → invalidates JWT session
POST   /api/auth/refresh         → refreshes JWT token
```

---

### Admin Portal Endpoints

#### Orders & Dispatch
```
GET    /api/admin/orders                   → all orders (filterable by branch, date, status)
GET    /api/admin/orders/{id}              → single order detail
PATCH  /api/admin/orders/{id}/status       → update order status

GET    /api/admin/dispatch                 → all dispatch orders
POST   /api/admin/dispatch                 → create new dispatch order
PATCH  /api/admin/dispatch/{id}/approve    → approve pending dispatch
PATCH  /api/admin/dispatch/{id}/status     → update dispatch status
```

#### Stock & Returns
```
GET    /api/admin/stock                    → stock levels across all branches
POST   /api/admin/stock/restock            → restock a branch (creates dispatch)
GET    /api/admin/stock/returns            → all return requests from branches
PATCH  /api/admin/stock/returns/{id}       → approve / reject a return request
```

#### Reports
```
GET    /api/admin/reports/sales            → sales report (per branch, date range)
GET    /api/admin/reports/profit-loss      → profit & loss report
GET    /api/admin/reports/orders           → orders report
GET    /api/admin/reports/stock            → stock levels report
GET    /api/admin/reports/loyalty          → loyalty programme report
```

#### User & Session Management
```
GET    /api/admin/users                    → all staff accounts
GET    /api/admin/users/pending            → pending registration approvals
PATCH  /api/admin/users/{id}/approve       → approve a registration
PATCH  /api/admin/users/{id}/suspend       → suspend a staff account
GET    /api/admin/sessions                 → all active sessions
DELETE /api/admin/sessions/{id}            → terminate a specific session
DELETE /api/admin/sessions/all             → emergency lock — kill all sessions
```

---

### Branch Portal Endpoints
> All branch endpoints are scoped to `{branchId}` — branches cannot see each other's data.

#### Orders
```
GET    /api/branch/{branchId}/drinks               → available drinks + stock levels
POST   /api/branch/{branchId}/orders               → place a new customer order
GET    /api/branch/{branchId}/orders               → this branch's order history
```

#### Stock & Dispatch
```
GET    /api/branch/{branchId}/stock                → this branch's stock levels
GET    /api/branch/{branchId}/dispatch             → dispatches addressed to this branch
PATCH  /api/branch/{branchId}/dispatch/{id}/confirm → confirm receipt of dispatch
```

#### Returns
```
POST   /api/branch/{branchId}/returns              → raise a damaged stock return
GET    /api/branch/{branchId}/returns              → this branch's return requests
```

---

### Loyalty Endpoints (Branch Portal)
```
GET    /api/loyalty/search?q=              → search customer by name / phone / card ID
POST   /api/loyalty/register               → register a new loyalty customer
GET    /api/loyalty/{cardId}               → customer profile + points balance
GET    /api/loyalty/{cardId}/history       → purchase & points history
POST   /api/loyalty/{cardId}/earn          → add points after an order
```

---

## 3. ENTITY BUILD ORDER
> Build JPA entities in this order to respect foreign key dependencies.

```
1. Branch
2. User
3. Session
4. Drink
5. Stock
6. Order
7. OrderItem
8. DispatchOrder
9. DispatchItem
10. LoyaltyCustomer
11. LoyaltyTransaction
12. StockReturn
13. Report
```

---

## 4. KEY NOTES FOR IMPLEMENTATION

- **Branch scoping** — all branch queries must filter by `branch_id`. Never expose cross-branch data to branch clients.
- **JWT** — every request (except `/api/auth/*`) must carry a valid JWT in the `Authorization: Bearer <token>` header.
- **Roles enforced server-side** — `ADMIN` endpoints reject `MANAGER` and `CASHIER` tokens. Don't rely on the UI to enforce this.
- **OTP** — sent via Africa's Talking SMS API. Use sandbox mode for demo.
- **CORS** — configure Spring Boot to allow requests from the React web client origin when that is added.
- **ddl-auto=update** — Hibernate will auto-create tables from entities during development. Switch to `validate` before production.

---

*End of DRINKSHOP_BACKEND_REFERENCE.md*
