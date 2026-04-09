# Project Structure

```
KamnywesoLiqourBackend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── backend/
│   │   │           └── kamnywesoliqourbackend/
│   │   │               ├── dto/
│   │   │               │   └── req/
│   │   │               │       └── LoginReq.java
│   │   │               ├── entity/
│   │   │               │   ├── Branch.java
│   │   │               │   ├── DispatchItem.java
│   │   │               │   ├── DispatchOrder.java
│   │   │               │   ├── Drink.java
│   │   │               │   ├── LoyaltyCustomer.java
│   │   │               │   ├── LoyaltyTransaction.java
│   │   │               │   ├── Order.java
│   │   │               │   ├── OrderItem.java
│   │   │               │   ├── Report.java
│   │   │               │   ├── Session.java
│   │   │               │   ├── Stock.java
│   │   │               │   ├── StockReturn.java
│   │   │               │   └── User.java
│   │   │               ├── enums/
│   │   │               │   ├── DispatchStatus.java
│   │   │               │   ├── LoyaltyTier.java
│   │   │               │   ├── OrderStatus.java
│   │   │               │   ├── ReportTypes.java
│   │   │               │   ├── ReturnStatus.java
│   │   │               │   ├── Role.java
│   │   │               │   ├── TransactionType.java
│   │   │               │   └── UserStatus.java
│   │   │               ├── repository/
│   │   │               │   ├── BranchRepository.java
│   │   │               │   ├── DispatchItemRepository.java
│   │   │               │   ├── DispatchOrderRepository.java
│   │   │               │   ├── DrinkRepository.java
│   │   │               │   ├── LoyaltyCustomerRepository.java
│   │   │               │   ├── LoyaltyTransactionRepository.java
│   │   │               │   ├── OrderItemRepository.java
│   │   │               │   ├── OrderRepository.java
│   │   │               │   ├── ReportRepository.java
│   │   │               │   ├── SessionRepository.java
│   │   │               │   ├── StockRepository.java
│   │   │               │   ├── StockReturnRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── service/
│   │   │               │   ├── impl/
│   │   │               │   │   ├── AuthServiceImpl.java
│   │   │               │   │   ├── BranchServiceImpl.java
│   │   │               │   │   ├── DispatchServiceImpl.java
│   │   │               │   │   ├── LoyaltyServiceImpl.java
│   │   │               │   │   ├── OrderServiceImpl.java
│   │   │               │   │   ├── ReportServiceImpl.java
│   │   │               │   │   ├── StockReturnServiceImpl.java
│   │   │               │   │   ├── StockServiceImpl.java
│   │   │               │   │   └── UserServiceImpl.java
│   │   │               │   └── interfaces/
│   │   │               │       ├── AuthService.java
│   │   │               │       ├── BranchService.java
│   │   │               │       ├── DispatchService.java
│   │   │               │       ├── LoyaltyService.java
│   │   │               │       ├── OrderService.java
│   │   │               │       ├── ReportService.java
│   │   │               │       ├── StockReturnService.java
│   │   │               │       ├── StockService.java
│   │   │               │       └── UserService.java
│   │   │               └── KamnywesoLiqourBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── backend/
│                   └── kamnywesoliqourbackend/
│                       └── KamnywesoLiqourBackendApplicationTests.java
├── DRINKSHOP_BACKEND_REFERENCE.md
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
└── roadmap.md
```

# Project Files

## File: `DRINKSHOP_BACKEND_REFERENCE.md`

```markdown
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
POST   /api/auth/login           → returns JWT + triggers OTP email
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
- **OTP** — OTPs are sent via Email using Spring Boot Mail + Resend SMTP
- **CORS** — configure Spring Boot to allow requests from the React web client origin when that is added.
- **ddl-auto=update** — Hibernate will auto-create tables from entities during development. Switch to `validate` before production.

---

*End of DRINKSHOP_BACKEND_REFERENCE.md*

```

## File: `docker-compose.yml`

```yaml
services:
  kamnywesoDb:
    container_name: kamnyweso-db
    image: docker.io/library/postgres:18-alpine
    environment:
      POSTGRES_DB: kamnyweso
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin001
      PGDATA: /data/postgres
    volumes:
      - db:/data/postgres
    ports:
      - "5332:5432"
    networks:
      - kamnyweso-network
    restart: unless-stopped
networks:
  kamnyweso-network:
    driver: bridge
volumes:
  db:
```

## File: `mvnw`

```text
#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.3.4
#
# Optional ENV vars
# -----------------
#   JAVA_HOME - location of a JDK home dir, required when download maven via java source
#   MVNW_REPOURL - repo url base for downloading maven distribution
#   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven
#   MVNW_VERBOSE - true: enable verbose log; debug: trace the mvnw script; others: silence the output
# ----------------------------------------------------------------------------

set -euf
[ "${MVNW_VERBOSE-}" != debug ] || set -x

# OS specific support.
native_path() { printf %s\\n "$1"; }
case "$(uname)" in
CYGWIN* | MINGW*)
  [ -z "${JAVA_HOME-}" ] || JAVA_HOME="$(cygpath --unix "$JAVA_HOME")"
  native_path() { cygpath --path --windows "$1"; }
  ;;
esac

# set JAVACMD and JAVACCMD
set_java_home() {
  # For Cygwin and MinGW, ensure paths are in Unix format before anything is touched
  if [ -n "${JAVA_HOME-}" ]; then
    if [ -x "$JAVA_HOME/jre/sh/java" ]; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
      JAVACCMD="$JAVA_HOME/jre/sh/javac"
    else
      JAVACMD="$JAVA_HOME/bin/java"
      JAVACCMD="$JAVA_HOME/bin/javac"

      if [ ! -x "$JAVACMD" ] || [ ! -x "$JAVACCMD" ]; then
        echo "The JAVA_HOME environment variable is not defined correctly, so mvnw cannot run." >&2
        echo "JAVA_HOME is set to \"$JAVA_HOME\", but \"\$JAVA_HOME/bin/java\" or \"\$JAVA_HOME/bin/javac\" does not exist." >&2
        return 1
      fi
    fi
  else
    JAVACMD="$(
      'set' +e
      'unset' -f command 2>/dev/null
      'command' -v java
    )" || :
    JAVACCMD="$(
      'set' +e
      'unset' -f command 2>/dev/null
      'command' -v javac
    )" || :

    if [ ! -x "${JAVACMD-}" ] || [ ! -x "${JAVACCMD-}" ]; then
      echo "The java/javac command does not exist in PATH nor is JAVA_HOME set, so mvnw cannot run." >&2
      return 1
    fi
  fi
}

# hash string like Java String::hashCode
hash_string() {
  str="${1:-}" h=0
  while [ -n "$str" ]; do
    char="${str%"${str#?}"}"
    h=$(((h * 31 + $(LC_CTYPE=C printf %d "'$char")) % 4294967296))
    str="${str#?}"
  done
  printf %x\\n $h
}

verbose() { :; }
[ "${MVNW_VERBOSE-}" != true ] || verbose() { printf %s\\n "${1-}"; }

die() {
  printf %s\\n "$1" >&2
  exit 1
}

trim() {
  # MWRAPPER-139:
  #   Trims trailing and leading whitespace, carriage returns, tabs, and linefeeds.
  #   Needed for removing poorly interpreted newline sequences when running in more
  #   exotic environments such as mingw bash on Windows.
  printf "%s" "${1}" | tr -d '[:space:]'
}

scriptDir="$(dirname "$0")"
scriptName="$(basename "$0")"

# parse distributionUrl and optional distributionSha256Sum, requires .mvn/wrapper/maven-wrapper.properties
while IFS="=" read -r key value; do
  case "${key-}" in
  distributionUrl) distributionUrl=$(trim "${value-}") ;;
  distributionSha256Sum) distributionSha256Sum=$(trim "${value-}") ;;
  esac
done <"$scriptDir/.mvn/wrapper/maven-wrapper.properties"
[ -n "${distributionUrl-}" ] || die "cannot read distributionUrl property in $scriptDir/.mvn/wrapper/maven-wrapper.properties"

case "${distributionUrl##*/}" in
maven-mvnd-*bin.*)
  MVN_CMD=mvnd.sh _MVNW_REPO_PATTERN=/maven/mvnd/
  case "${PROCESSOR_ARCHITECTURE-}${PROCESSOR_ARCHITEW6432-}:$(uname -a)" in
  *AMD64:CYGWIN* | *AMD64:MINGW*) distributionPlatform=windows-amd64 ;;
  :Darwin*x86_64) distributionPlatform=darwin-amd64 ;;
  :Darwin*arm64) distributionPlatform=darwin-aarch64 ;;
  :Linux*x86_64*) distributionPlatform=linux-amd64 ;;
  *)
    echo "Cannot detect native platform for mvnd on $(uname)-$(uname -m), use pure java version" >&2
    distributionPlatform=linux-amd64
    ;;
  esac
  distributionUrl="${distributionUrl%-bin.*}-$distributionPlatform.zip"
  ;;
maven-mvnd-*) MVN_CMD=mvnd.sh _MVNW_REPO_PATTERN=/maven/mvnd/ ;;
*) MVN_CMD="mvn${scriptName#mvnw}" _MVNW_REPO_PATTERN=/org/apache/maven/ ;;
esac

# apply MVNW_REPOURL and calculate MAVEN_HOME
# maven home pattern: ~/.m2/wrapper/dists/{apache-maven-<version>,maven-mvnd-<version>-<platform>}/<hash>
[ -z "${MVNW_REPOURL-}" ] || distributionUrl="$MVNW_REPOURL$_MVNW_REPO_PATTERN${distributionUrl#*"$_MVNW_REPO_PATTERN"}"
distributionUrlName="${distributionUrl##*/}"
distributionUrlNameMain="${distributionUrlName%.*}"
distributionUrlNameMain="${distributionUrlNameMain%-bin}"
MAVEN_USER_HOME="${MAVEN_USER_HOME:-${HOME}/.m2}"
MAVEN_HOME="${MAVEN_USER_HOME}/wrapper/dists/${distributionUrlNameMain-}/$(hash_string "$distributionUrl")"

exec_maven() {
  unset MVNW_VERBOSE MVNW_USERNAME MVNW_PASSWORD MVNW_REPOURL || :
  exec "$MAVEN_HOME/bin/$MVN_CMD" "$@" || die "cannot exec $MAVEN_HOME/bin/$MVN_CMD"
}

if [ -d "$MAVEN_HOME" ]; then
  verbose "found existing MAVEN_HOME at $MAVEN_HOME"
  exec_maven "$@"
fi

case "${distributionUrl-}" in
*?-bin.zip | *?maven-mvnd-?*-?*.zip) ;;
*) die "distributionUrl is not valid, must match *-bin.zip or maven-mvnd-*.zip, but found '${distributionUrl-}'" ;;
esac

# prepare tmp dir
if TMP_DOWNLOAD_DIR="$(mktemp -d)" && [ -d "$TMP_DOWNLOAD_DIR" ]; then
  clean() { rm -rf -- "$TMP_DOWNLOAD_DIR"; }
  trap clean HUP INT TERM EXIT
else
  die "cannot create temp dir"
fi

mkdir -p -- "${MAVEN_HOME%/*}"

# Download and Install Apache Maven
verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
verbose "Downloading from: $distributionUrl"
verbose "Downloading to: $TMP_DOWNLOAD_DIR/$distributionUrlName"

# select .zip or .tar.gz
if ! command -v unzip >/dev/null; then
  distributionUrl="${distributionUrl%.zip}.tar.gz"
  distributionUrlName="${distributionUrl##*/}"
fi

# verbose opt
__MVNW_QUIET_WGET=--quiet __MVNW_QUIET_CURL=--silent __MVNW_QUIET_UNZIP=-q __MVNW_QUIET_TAR=''
[ "${MVNW_VERBOSE-}" != true ] || __MVNW_QUIET_WGET='' __MVNW_QUIET_CURL='' __MVNW_QUIET_UNZIP='' __MVNW_QUIET_TAR=v

# normalize http auth
case "${MVNW_PASSWORD:+has-password}" in
'') MVNW_USERNAME='' MVNW_PASSWORD='' ;;
has-password) [ -n "${MVNW_USERNAME-}" ] || MVNW_USERNAME='' MVNW_PASSWORD='' ;;
esac

if [ -z "${MVNW_USERNAME-}" ] && command -v wget >/dev/null; then
  verbose "Found wget ... using wget"
  wget ${__MVNW_QUIET_WGET:+"$__MVNW_QUIET_WGET"} "$distributionUrl" -O "$TMP_DOWNLOAD_DIR/$distributionUrlName" || die "wget: Failed to fetch $distributionUrl"
elif [ -z "${MVNW_USERNAME-}" ] && command -v curl >/dev/null; then
  verbose "Found curl ... using curl"
  curl ${__MVNW_QUIET_CURL:+"$__MVNW_QUIET_CURL"} -f -L -o "$TMP_DOWNLOAD_DIR/$distributionUrlName" "$distributionUrl" || die "curl: Failed to fetch $distributionUrl"
elif set_java_home; then
  verbose "Falling back to use Java to download"
  javaSource="$TMP_DOWNLOAD_DIR/Downloader.java"
  targetZip="$TMP_DOWNLOAD_DIR/$distributionUrlName"
  cat >"$javaSource" <<-END
	public class Downloader extends java.net.Authenticator
	{
	  protected java.net.PasswordAuthentication getPasswordAuthentication()
	  {
	    return new java.net.PasswordAuthentication( System.getenv( "MVNW_USERNAME" ), System.getenv( "MVNW_PASSWORD" ).toCharArray() );
	  }
	  public static void main( String[] args ) throws Exception
	  {
	    setDefault( new Downloader() );
	    java.nio.file.Files.copy( java.net.URI.create( args[0] ).toURL().openStream(), java.nio.file.Paths.get( args[1] ).toAbsolutePath().normalize() );
	  }
	}
	END
  # For Cygwin/MinGW, switch paths to Windows format before running javac and java
  verbose " - Compiling Downloader.java ..."
  "$(native_path "$JAVACCMD")" "$(native_path "$javaSource")" || die "Failed to compile Downloader.java"
  verbose " - Running Downloader.java ..."
  "$(native_path "$JAVACMD")" -cp "$(native_path "$TMP_DOWNLOAD_DIR")" Downloader "$distributionUrl" "$(native_path "$targetZip")"
fi

# If specified, validate the SHA-256 sum of the Maven distribution zip file
if [ -n "${distributionSha256Sum-}" ]; then
  distributionSha256Result=false
  if [ "$MVN_CMD" = mvnd.sh ]; then
    echo "Checksum validation is not supported for maven-mvnd." >&2
    echo "Please disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties." >&2
    exit 1
  elif command -v sha256sum >/dev/null; then
    if echo "$distributionSha256Sum  $TMP_DOWNLOAD_DIR/$distributionUrlName" | sha256sum -c - >/dev/null 2>&1; then
      distributionSha256Result=true
    fi
  elif command -v shasum >/dev/null; then
    if echo "$distributionSha256Sum  $TMP_DOWNLOAD_DIR/$distributionUrlName" | shasum -a 256 -c >/dev/null 2>&1; then
      distributionSha256Result=true
    fi
  else
    echo "Checksum validation was requested but neither 'sha256sum' or 'shasum' are available." >&2
    echo "Please install either command, or disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties." >&2
    exit 1
  fi
  if [ $distributionSha256Result = false ]; then
    echo "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised." >&2
    echo "If you updated your Maven version, you need to update the specified distributionSha256Sum property." >&2
    exit 1
  fi
fi

# unzip and move
if command -v unzip >/dev/null; then
  unzip ${__MVNW_QUIET_UNZIP:+"$__MVNW_QUIET_UNZIP"} "$TMP_DOWNLOAD_DIR/$distributionUrlName" -d "$TMP_DOWNLOAD_DIR" || die "failed to unzip"
else
  tar xzf${__MVNW_QUIET_TAR:+"$__MVNW_QUIET_TAR"} "$TMP_DOWNLOAD_DIR/$distributionUrlName" -C "$TMP_DOWNLOAD_DIR" || die "failed to untar"
fi

# Find the actual extracted directory name (handles snapshots where filename != directory name)
actualDistributionDir=""

# First try the expected directory name (for regular distributions)
if [ -d "$TMP_DOWNLOAD_DIR/$distributionUrlNameMain" ]; then
  if [ -f "$TMP_DOWNLOAD_DIR/$distributionUrlNameMain/bin/$MVN_CMD" ]; then
    actualDistributionDir="$distributionUrlNameMain"
  fi
fi

# If not found, search for any directory with the Maven executable (for snapshots)
if [ -z "$actualDistributionDir" ]; then
  # enable globbing to iterate over items
  set +f
  for dir in "$TMP_DOWNLOAD_DIR"/*; do
    if [ -d "$dir" ]; then
      if [ -f "$dir/bin/$MVN_CMD" ]; then
        actualDistributionDir="$(basename "$dir")"
        break
      fi
    fi
  done
  set -f
fi

if [ -z "$actualDistributionDir" ]; then
  verbose "Contents of $TMP_DOWNLOAD_DIR:"
  verbose "$(ls -la "$TMP_DOWNLOAD_DIR")"
  die "Could not find Maven distribution directory in extracted archive"
fi

verbose "Found extracted Maven distribution directory: $actualDistributionDir"
printf %s\\n "$distributionUrl" >"$TMP_DOWNLOAD_DIR/$actualDistributionDir/mvnw.url"
mv -- "$TMP_DOWNLOAD_DIR/$actualDistributionDir" "$MAVEN_HOME" || [ -d "$MAVEN_HOME" ] || die "fail to move MAVEN_HOME"

clean || :
exec_maven "$@"

```

## File: `mvnw.cmd`

```batch
<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.4
@REM
@REM Optional ENV vars
@REM   MVNW_REPOURL - repo url base for downloading maven distribution
@REM   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven
@REM   MVNW_VERBOSE - true: enable verbose log; others: silence the output
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0'; $script='%__MVNW_ARG0_NAME__%'; icm -ScriptBlock ([Scriptblock]::Create((Get-Content -Raw '%~f0'))) -NoNewScope}"`) DO @(
  IF "%%A"=="MVN_CMD" (set __MVNW_CMD__=%%B) ELSE IF "%%B"=="" (echo %%A) ELSE (echo %%A=%%B)
)
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%
@SET __MVNW_PSMODULEP_SAVE=
@SET __MVNW_ARG0_NAME__=
@SET MVNW_USERNAME=
@SET MVNW_PASSWORD=
@IF NOT "%__MVNW_CMD__%"=="" ("%__MVNW_CMD__%" %*)
@echo Cannot start maven from wrapper >&2 && exit /b 1
@GOTO :EOF
: end batch / begin powershell #>

$ErrorActionPreference = "Stop"
if ($env:MVNW_VERBOSE -eq "true") {
  $VerbosePreference = "Continue"
}

# calculate distributionUrl, requires .mvn/wrapper/maven-wrapper.properties
$distributionUrl = (Get-Content -Raw "$scriptDir/.mvn/wrapper/maven-wrapper.properties" | ConvertFrom-StringData).distributionUrl
if (!$distributionUrl) {
  Write-Error "cannot read distributionUrl property in $scriptDir/.mvn/wrapper/maven-wrapper.properties"
}

switch -wildcard -casesensitive ( $($distributionUrl -replace '^.*/','') ) {
  "maven-mvnd-*" {
    $USE_MVND = $true
    $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$',"-windows-amd64.zip"
    $MVN_CMD = "mvnd.cmd"
    break
  }
  default {
    $USE_MVND = $false
    $MVN_CMD = $script -replace '^mvnw','mvn'
    break
  }
}

# apply MVNW_REPOURL and calculate MAVEN_HOME
# maven home pattern: ~/.m2/wrapper/dists/{apache-maven-<version>,maven-mvnd-<version>-<platform>}/<hash>
if ($env:MVNW_REPOURL) {
  $MVNW_REPO_PATTERN = if ($USE_MVND -eq $False) { "/org/apache/maven/" } else { "/maven/mvnd/" }
  $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$($distributionUrl -replace "^.*$MVNW_REPO_PATTERN",'')"
}
$distributionUrlName = $distributionUrl -replace '^.*/',''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]*$','' -replace '-bin$',''

$MAVEN_M2_PATH = "$HOME/.m2"
if ($env:MAVEN_USER_HOME) {
  $MAVEN_M2_PATH = "$env:MAVEN_USER_HOME"
}

if (-not (Test-Path -Path $MAVEN_M2_PATH)) {
    New-Item -Path $MAVEN_M2_PATH -ItemType Directory | Out-Null
}

$MAVEN_WRAPPER_DISTS = $null
if ((Get-Item $MAVEN_M2_PATH).Target[0] -eq $null) {
  $MAVEN_WRAPPER_DISTS = "$MAVEN_M2_PATH/wrapper/dists"
} else {
  $MAVEN_WRAPPER_DISTS = (Get-Item $MAVEN_M2_PATH).Target[0] + "/wrapper/dists"
}

$MAVEN_HOME_PARENT = "$MAVEN_WRAPPER_DISTS/$distributionUrlNameMain"
$MAVEN_HOME_NAME = ([System.Security.Cryptography.SHA256]::Create().ComputeHash([byte[]][char[]]$distributionUrl) | ForEach-Object {$_.ToString("x2")}) -join ''
$MAVEN_HOME = "$MAVEN_HOME_PARENT/$MAVEN_HOME_NAME"

if (Test-Path -Path "$MAVEN_HOME" -PathType Container) {
  Write-Verbose "found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
  exit $?
}

if (! $distributionUrlNameMain -or ($distributionUrlName -eq $distributionUrlNameMain)) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
}

# prepare tmp dir
$TMP_DOWNLOAD_DIR_HOLDER = New-TemporaryFile
$TMP_DOWNLOAD_DIR = New-Item -Itemtype Directory -Path "$TMP_DOWNLOAD_DIR_HOLDER.dir"
$TMP_DOWNLOAD_DIR_HOLDER.Delete() | Out-Null
trap {
  if ($TMP_DOWNLOAD_DIR.Exists) {
    try { Remove-Item $TMP_DOWNLOAD_DIR -Recurse -Force | Out-Null }
    catch { Write-Warning "Cannot remove $TMP_DOWNLOAD_DIR" }
  }
}

New-Item -Itemtype Directory -Path "$MAVEN_HOME_PARENT" -Force | Out-Null

# Download and Install Apache Maven
Write-Verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
Write-Verbose "Downloading from: $distributionUrl"
Write-Verbose "Downloading to: $TMP_DOWNLOAD_DIR/$distributionUrlName"

$webclient = New-Object System.Net.WebClient
if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
  $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
}
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
$webclient.DownloadFile($distributionUrl, "$TMP_DOWNLOAD_DIR/$distributionUrlName") | Out-Null

# If specified, validate the SHA-256 sum of the Maven distribution zip file
$distributionSha256Sum = (Get-Content -Raw "$scriptDir/.mvn/wrapper/maven-wrapper.properties" | ConvertFrom-StringData).distributionSha256Sum
if ($distributionSha256Sum) {
  if ($USE_MVND) {
    Write-Error "Checksum validation is not supported for maven-mvnd. `nPlease disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
  }
  Import-Module $PSHOME\Modules\Microsoft.PowerShell.Utility -Function Get-FileHash
  if ((Get-FileHash "$TMP_DOWNLOAD_DIR/$distributionUrlName" -Algorithm SHA256).Hash.ToLower() -ne $distributionSha256Sum) {
    Write-Error "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised. If you updated your Maven version, you need to update the specified distributionSha256Sum property."
  }
}

# unzip and move
Expand-Archive "$TMP_DOWNLOAD_DIR/$distributionUrlName" -DestinationPath "$TMP_DOWNLOAD_DIR" | Out-Null

# Find the actual extracted directory name (handles snapshots where filename != directory name)
$actualDistributionDir = ""

# First try the expected directory name (for regular distributions)
$expectedPath = Join-Path "$TMP_DOWNLOAD_DIR" "$distributionUrlNameMain"
$expectedMvnPath = Join-Path "$expectedPath" "bin/$MVN_CMD"
if ((Test-Path -Path $expectedPath -PathType Container) -and (Test-Path -Path $expectedMvnPath -PathType Leaf)) {
  $actualDistributionDir = $distributionUrlNameMain
}

# If not found, search for any directory with the Maven executable (for snapshots)
if (!$actualDistributionDir) {
  Get-ChildItem -Path "$TMP_DOWNLOAD_DIR" -Directory | ForEach-Object {
    $testPath = Join-Path $_.FullName "bin/$MVN_CMD"
    if (Test-Path -Path $testPath -PathType Leaf) {
      $actualDistributionDir = $_.Name
    }
  }
}

if (!$actualDistributionDir) {
  Write-Error "Could not find Maven distribution directory in extracted archive"
}

Write-Verbose "Found extracted Maven distribution directory: $actualDistributionDir"
Rename-Item -Path "$TMP_DOWNLOAD_DIR/$actualDistributionDir" -NewName $MAVEN_HOME_NAME | Out-Null
try {
  Move-Item -Path "$TMP_DOWNLOAD_DIR/$MAVEN_HOME_NAME" -Destination $MAVEN_HOME_PARENT | Out-Null
} catch {
  if (! (Test-Path -Path "$MAVEN_HOME" -PathType Container)) {
    Write-Error "fail to move MAVEN_HOME"
  }
} finally {
  try { Remove-Item $TMP_DOWNLOAD_DIR -Recurse -Force | Out-Null }
  catch { Write-Warning "Cannot remove $TMP_DOWNLOAD_DIR" }
}

Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"

```

## File: `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.5</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.backend</groupId>
    <artifactId>KamnywesoLiqourBackend</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>KamnywesoLiqourBackend</name>
    <description>KamnywesoLiqourBackend</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>17</java.version>
    <kotlin.version>2.3.10</kotlin.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib-jdk8</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-test</artifactId>
            <version>${kotlin.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>kotlin-maven-plugin</artifactId>
                <version>${kotlin.version}</version>
                <executions>
                    <execution>
                        <id>compile</id>
                        <phase>compile</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                        <configuration/>
                    </execution>
                    <execution>
                        <id>test-compile</id>
                        <phase>test-compile</phase>
                        <goals>
                            <goal>test-compile</goal>
                        </goals>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-noarg</artifactId>
                        <version>${kotlin.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-allopen</artifactId>
                        <version>${kotlin.version}</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <jvmTarget>1.8</jvmTarget>
                    <compilerPlugins>
                        <plugin>jpa</plugin>
                        <plugin>spring</plugin>
                    </compilerPlugins>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <executions>
                    <execution>
                        <id>default-compile</id>
                        <phase>compile</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                        <configuration>
                            <annotationProcessorPaths>
                                <path>
                                    <groupId>org.projectlombok</groupId>
                                    <artifactId>lombok</artifactId>
                                </path>
                            </annotationProcessorPaths>
                        </configuration>
                    </execution>
                    <execution>
                        <id>default-testCompile</id>
                        <phase>test-compile</phase>
                        <goals>
                            <goal>testCompile</goal>
                        </goals>
                        <configuration>
                            <annotationProcessorPaths>
                                <path>
                                    <groupId>org.projectlombok</groupId>
                                    <artifactId>lombok</artifactId>
                                </path>
                            </annotationProcessorPaths>
                        </configuration>
                    </execution>
                    <execution>
                        <id>compile</id>
                        <phase>compile</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>testCompile</id>
                        <phase>test-compile</phase>
                        <goals>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>

```

## File: `roadmap.md`

```markdown

```markdown
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

## 🚧 PHASE 3: API & Web Layer (IN PROGRESS)
- [x] Define DTO (Data Transfer Object) architecture (Records)
- [ ] Create `BranchController` (In Progress)
- [ ] Create `OrderController`
- [ ] Create `StockController` & `DispatchController`
- [ ] Create `LoyaltyController`
- [ ] Create `ReportController`
- [ ] Build a Global Exception Handler (`@ControllerAdvice`) for clean JSON error messages

## ⏳ PHASE 4: Security & Authentication (PENDING)
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
```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/KamnywesoLiqourBackendApplication.java`

```java
package com.backend.kamnywesoliqourbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KamnywesoLiqourBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(KamnywesoLiqourBackendApplication.class, args);
    }

}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/dto/req/LoginReq.java`

```java
package com.backend.kamnywesoliqourbackend.dto.req;

public record LoginReq() {
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/Branch.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String location;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
    private boolean isHq;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/DispatchItem.java`

```java
package com.backend.kamnywesoliqourbackend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dispatch_items")
public class DispatchItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "dispatch_order_id")
    private DispatchOrder dispatchOrder;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
    private Integer quantity;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/DispatchOrder.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.DispatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dispatch_orders")
public class DispatchOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    private String driverName;
    private String vehiclePlate;
    private String notes;
    @Enumerated(EnumType.STRING)
    private DispatchStatus status;
    private LocalDateTime dispatchedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime receivedAt;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/Drink.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drinks")
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String brand;
    private BigDecimal price;
    private BigDecimal costPrice;
    private String category;
    private String image;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/LoyaltyCustomer.java`

```java
package com.backend.kamnywesoliqourbackend.entity;


import com.backend.kamnywesoliqourbackend.enums.LoyaltyTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loyalty_customers")
public class LoyaltyCustomer {
//    loyalty_customers (id, name, phone, card_id, tier, points_balance,
//                       branch_id, registered_at)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String phone;
    private String cardId;
    @Enumerated(EnumType.STRING)
    private LoyaltyTier tier;
    private Integer pointsBalance;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @CreationTimestamp
    private LocalDateTime registeredAt;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/LoyaltyTransaction.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loyalty_transactions")
public class LoyaltyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private LoyaltyCustomer customer;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Integer pointsEarned;
    private Integer pointsRedeemed;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/Order.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private String customerName;
    private String customerPhone;
    @ManyToOne
    @JoinColumn(name = "loyalty_card_id")
    private LoyaltyCustomer loyaltyCustomer;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;

}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/OrderItem.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer pointsEarned;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/Report.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.ReportTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private ReportTypes type;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    @ManyToOne
    @JoinColumn(name = "generated_by")
    private User generatedBy;
    private String filePath;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/Session.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String jwtToken;
    private String ipAddress;
    private boolean isActive;
    private LocalDateTime loginAt;
    private LocalDateTime lastActivity;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/Stock.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private Integer quantity;
    private Integer minThreshold;
    private LocalDateTime lastRestockedAt;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/StockReturn.java`

```java
package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.ReturnStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_returns")
public class StockReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
    private Integer quantity;
    private String reason;
    @Enumerated(EnumType.STRING)
    private ReturnStatus status;
    private String hqResponse;
    @ManyToOne
    @JoinColumn(name = "raised_by")
    private User raisedBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/entity/User.java`

```java
package com.backend.kamnywesoliqourbackend.entity;


import com.backend.kamnywesoliqourbackend.enums.Role;
import com.backend.kamnywesoliqourbackend.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private LocalDateTime lastLogin;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/enums/DispatchStatus.java`

```java
package com.backend.kamnywesoliqourbackend.enums;

public enum DispatchStatus {
    PENDING, APPROVED, DISPATCHED, IN_TRANSIT, CONFIRMED, RECEIVED, REJECTED
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/enums/LoyaltyTier.java`

```java
package com.backend.kamnywesoliqourbackend.enums;

public enum LoyaltyTier {
    BRONZE, SILVER, GOLD, PLATINUM
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/enums/OrderStatus.java`

```java
package com.backend.kamnywesoliqourbackend.enums;

public enum OrderStatus {
    PENDING, PROCESSING, COMPLETED, CANCELLED
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/enums/ReportTypes.java`

```java
package com.backend.kamnywesoliqourbackend.enums;

public enum ReportTypes {
    SALES,
    PROFIT_LOSS,
    ORDERS,
    STOCK,
    LOYALTY
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/enums/ReturnStatus.java`

```java
package com.backend.kamnywesoliqourbackend.enums;

public enum ReturnStatus {
    PENDING_REVIEW, APPROVED, REJECTED, REPLACEMENT_SENT
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/enums/Role.java`

```java
package com.backend.kamnywesoliqourbackend.enums;

public enum Role {
    ADMIN,
    MANAGER,
    CASHIER
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/enums/TransactionType.java`

```java
package com.backend.kamnywesoliqourbackend.enums;

public enum TransactionType {
    EARNED,REDEEMED,ADJUSTMENT
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/enums/UserStatus.java`

```java
package com.backend.kamnywesoliqourbackend.enums;

public enum UserStatus {
    ACTIVE, INACTIVE, SUSPENDED, PENDING
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/BranchRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
    Branch getBranchesByIsHq(boolean isHq);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/DispatchItemRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.DispatchItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DispatchItemRepository extends JpaRepository<DispatchItem, UUID> {
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/DispatchOrderRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.DispatchOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DispatchOrderRepository extends JpaRepository<DispatchOrder, UUID> {

    Optional<DispatchOrder> findByIdAndBranch_Id(UUID id, UUID branchId);

    List<DispatchOrder> findByBranch_Id(UUID branchId);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/DrinkRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.Drink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DrinkRepository extends JpaRepository<Drink, UUID> {
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/LoyaltyCustomerRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.LoyaltyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoyaltyCustomerRepository extends JpaRepository<LoyaltyCustomer, UUID> {
    List<LoyaltyCustomer> findByNameContainingOrPhoneContainingOrCardIdContaining(String query, String query1, String query2);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/LoyaltyTransactionRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.LoyaltyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, UUID> {
    List<LoyaltyTransaction> findByCustomer_Id(UUID customerId);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/OrderItemRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/OrderRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByBranch_Id(UUID branchId);
    List<Order> findByBranch_IdAndCreatedAtBetween(UUID branchId, LocalDateTime start, LocalDateTime end);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/ReportRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/SessionRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/StockRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    List<Stock> findByBranch_Id(UUID branchId);
    Optional<Stock> findByDrink_IdAndBranch_Id(UUID drinkId, UUID branchId);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/StockReturnRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.StockReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockReturnRepository extends JpaRepository<StockReturn, UUID> {
    List<StockReturn> findByBranch_Id(UUID branchId);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/repository/UserRepository.java`

```java
package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.User;
import com.backend.kamnywesoliqourbackend.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAllByStatus(UserStatus status);
    Optional<User> findByEmail(String email);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/AuthServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.User;
import com.backend.kamnywesoliqourbackend.enums.UserStatus;
import com.backend.kamnywesoliqourbackend.repository.UserRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User register(String name, String email, String password, String phone) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("User with email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setPhone(phone);
        user.setStatus(UserStatus.PENDING);
        userRepository.save(user);
        return user;
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User with email does not exist"));
        if(!user.getPasswordHash().equals(password)){
            throw new RuntimeException("Invalid Credentials");
        }
        return "token";
    }

    @Override
    public void logout(String token) {
//        logs out the user
    }

    @Override
    public Boolean verifyOtp(String email, String otp) {
        return null;
    }

    @Override
    public String refreshToken(String token) {
        return "";
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/BranchServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Branch;
import com.backend.kamnywesoliqourbackend.repository.BranchRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.BranchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository){
        this.branchRepository = branchRepository;
    }

    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public Branch getBranchById(UUID id) {
        return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    @Override
    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public Branch updateBranch(UUID id, Branch branch) {
        branch.setId(id);
        return branchRepository.save(branch);
    }

    @Override
    public void deleteBranch(UUID id) {
        branchRepository.deleteById(id);
    }

    @Override
    public Branch getHqBranch() {
        return branchRepository.getBranchesByIsHq(true);
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/DispatchServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.DispatchOrder;
import com.backend.kamnywesoliqourbackend.enums.DispatchStatus;
import com.backend.kamnywesoliqourbackend.repository.DispatchOrderRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.DispatchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DispatchServiceImpl implements DispatchService {
    private final DispatchOrderRepository dispatchOrderRepository;

    public DispatchServiceImpl(DispatchOrderRepository dispatchOrderRepository){
        this.dispatchOrderRepository = dispatchOrderRepository;
    }

    @Override
    public List<DispatchOrder> getAllDispatches() {
        return dispatchOrderRepository.findAll();
    }

    @Override
    public DispatchOrder createDispatch(DispatchOrder dispatchOrder) {
        dispatchOrderRepository.save(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    public DispatchOrder approveDispatch(UUID id) {
        DispatchOrder dispatchOrder = dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
        dispatchOrder.setStatus(DispatchStatus.APPROVED);
        dispatchOrderRepository.save(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    public DispatchOrder getDispatch(UUID id) {
        if(id == null) {
            throw new RuntimeException("Dispatch id is required");
        }
        return dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
    }

    @Override
    public DispatchOrder updateDispatchStatus(UUID id, DispatchStatus status) {
        if(id == null || status == null) {
            throw new RuntimeException("Dispatch id and status are required");
        }
        DispatchOrder dispatchOrder = dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
        dispatchOrder.setStatus(status);
        dispatchOrderRepository.save(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    public List<DispatchOrder> getBranchDispatch(UUID branchId) {
        if(branchId == null) {
            throw new RuntimeException("Branch id is required");
        }
        return dispatchOrderRepository.findByBranch_Id(branchId);
    }

    @Override
    public DispatchOrder confirmBranchDispatch(UUID id, UUID branchId) {
        if(id == null || branchId == null) {
            throw new RuntimeException("Dispatch id and branch id are required");
        }
        DispatchOrder dispatchOrder = dispatchOrderRepository.findByIdAndBranch_Id(id, branchId).orElseThrow(()-> new RuntimeException("Dispatch not found"));
        dispatchOrder.setStatus(DispatchStatus.CONFIRMED);
        return dispatchOrderRepository.save(dispatchOrder);
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/LoyaltyServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.LoyaltyCustomer;
import com.backend.kamnywesoliqourbackend.entity.LoyaltyTransaction;
import com.backend.kamnywesoliqourbackend.enums.TransactionType;
import com.backend.kamnywesoliqourbackend.repository.LoyaltyCustomerRepository;
import com.backend.kamnywesoliqourbackend.repository.LoyaltyTransactionRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.LoyaltyService;
import com.backend.kamnywesoliqourbackend.service.interfaces.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {
    private final LoyaltyCustomerRepository loyaltyCustomerRepository;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;
    private final OrderService orderService;

    public LoyaltyServiceImpl(LoyaltyCustomerRepository loyaltyCustomerRepository , LoyaltyTransactionRepository loyaltyTransactionRepository, OrderService orderService){
        this.loyaltyCustomerRepository = loyaltyCustomerRepository;
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
        this.orderService = orderService;
    }

    @Override
    public List<LoyaltyCustomer> getAllLoyaltyCustomers() {
        return loyaltyCustomerRepository.findAll();
    }

    @Override
    public List<LoyaltyCustomer> getLoyaltyCustomer(String query) {
         return loyaltyCustomerRepository.findByNameContainingOrPhoneContainingOrCardIdContaining(query, query, query);
    }

    @Override
    public LoyaltyCustomer getLoyaltyCustomerById(UUID customerId) {
        if(customerId == null) {
            throw new RuntimeException("Customer id is required");
        }
        return loyaltyCustomerRepository.findById(customerId).orElseThrow(()-> new RuntimeException("Customer not found"));
    }

    @Override
    public LoyaltyCustomer registerCustomer(LoyaltyCustomer customer) {
        if(customer == null) {
            throw new RuntimeException("Customer is required");
        }
        return loyaltyCustomerRepository.save(customer);
    }

    @Override
    public List<LoyaltyTransaction> getLoyaltyTransactions(UUID customerId) {
        if(customerId == null) {
            throw new RuntimeException("Customer id is required");
        }
        return loyaltyTransactionRepository.findByCustomer_Id(customerId);
    }

    @Override
    public LoyaltyTransaction createLoyaltyTransaction(UUID customerId, UUID orderId, TransactionType type, Integer points) {
        if(customerId == null || orderId == null || type == null || points == null) {
            throw new RuntimeException("All fields are required");
        }

        LoyaltyCustomer customer = loyaltyCustomerRepository.findById(customerId).orElseThrow(()-> new RuntimeException("Customer not found"));

        LoyaltyTransaction transaction = new LoyaltyTransaction();
        transaction.setCustomer(customer);
        transaction.setOrder(orderService.getOrderById(orderId));
        transaction.setTransactionType(type);

        transaction.setPointsEarned(type == TransactionType.EARNED ?  points : 0);
        transaction.setPointsEarned(type == TransactionType.REDEEMED ? points : 0);

        int currentPointBal = customer.getPointsBalance() == null ? 0 : customer.getPointsBalance();

        if (type == TransactionType.EARNED) {
            customer.setPointsBalance(currentPointBal + points);
        } else if (type == TransactionType.REDEEMED) {
            if (currentPointBal < points) {
                throw new RuntimeException("Insufficient points to redeem");
            }
            customer.setPointsBalance(currentPointBal - points);
        }
        loyaltyCustomerRepository.save(customer);
        loyaltyTransactionRepository.save(transaction);

        return transaction;
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/OrderServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.enums.OrderStatus;
import com.backend.kamnywesoliqourbackend.repository.OrderRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getBranchOrders(UUID branchId) {
        return orderRepository.findByBranch_Id(branchId);
    }

    @Override
    public Order createOrder(UUID branchId, Order order) {
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderStatus(UUID id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return order;
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/ReportServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.Report;
import com.backend.kamnywesoliqourbackend.repository.OrderRepository;
import com.backend.kamnywesoliqourbackend.repository.ReportRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final OrderRepository orderRepository;

    public ReportServiceImpl(ReportRepository reportRepository, OrderRepository orderRepository){
        this.reportRepository = reportRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getSalesReport(UUID branchId, LocalDate dateFrom, LocalDate dateTo) {
        return orderRepository.findByBranch_IdAndCreatedAtBetween(branchId, dateFrom.atStartOfDay(), dateTo.plusDays(1).atStartOfDay());
    }

    @Override
    public BigDecimal getProfitLoss(UUID branchId, LocalDate dateFrom, LocalDate dateTo) {
        return null;
    }

    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/StockReturnServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.StockReturn;
import com.backend.kamnywesoliqourbackend.enums.ReturnStatus;
import com.backend.kamnywesoliqourbackend.repository.StockReturnRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.StockReturnService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockReturnServiceImpl implements StockReturnService {
    private final StockReturnRepository stockReturnRepository;

    public StockReturnServiceImpl(StockReturnRepository stockReturnRepository){
        this.stockReturnRepository = stockReturnRepository;
    }

    @Override
    public List<StockReturn> getAllStockReturns() {
        return stockReturnRepository.findAll();
    }
    @Override
    public StockReturn processStockReturn(UUID branchId, StockReturn stockReturn) {
        stockReturn.setStatus(ReturnStatus.PENDING_REVIEW);
        return stockReturnRepository.save(stockReturn);
    }

    @Override
    public List<StockReturn> getStockReturnsByBranch(UUID branchId) {
        if(branchId == null) {
            throw new RuntimeException("Branch id is required");
        }
        return stockReturnRepository.findByBranch_Id(branchId);
    }


    @Override
    public StockReturn updateStockReturn(UUID stockReturnId, StockReturn stockReturn) {
        if(stockReturnId == null || stockReturn == null) {
            throw new RuntimeException("Stock return id and stock return are required");
        }
        StockReturn existingStockReturn = stockReturnRepository.findById(stockReturnId).orElseThrow(()-> new RuntimeException("Stock return not found"));
        existingStockReturn.setStatus(stockReturn.getStatus());
        return stockReturnRepository.save(existingStockReturn);
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/StockServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Branch;
import com.backend.kamnywesoliqourbackend.entity.Drink;
import com.backend.kamnywesoliqourbackend.entity.Stock;
import com.backend.kamnywesoliqourbackend.repository.BranchRepository;
import com.backend.kamnywesoliqourbackend.repository.DrinkRepository;
import com.backend.kamnywesoliqourbackend.repository.StockRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final DrinkRepository drinkRepository;
    private final BranchRepository branchRepository;


    public StockServiceImpl(StockRepository stockRepository, DrinkRepository drinkRepository, BranchRepository branchRepository){
        this.stockRepository = stockRepository;
        this.drinkRepository = drinkRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> getStockByBranch(UUID branchId) {
        if(branchId == null) {
            throw new RuntimeException("Branch id is required");
        }

        return stockRepository.findByBranch_Id(branchId);
    }

    @Override
    public Stock restockBranch(UUID branchId, Integer quantity, UUID drinkId) {
        if(branchId == null || quantity == null || drinkId == null) {
            throw new RuntimeException("All fields are required");
        }

        if(quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Drink drink = drinkRepository.findById(drinkId).orElseThrow(() -> new RuntimeException("Drink not found"));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch not found"));

        Optional<Stock> existingStock = stockRepository.findByDrink_IdAndBranch_Id(drinkId, branchId);
        if(existingStock.isPresent()) {
            Stock stock = existingStock.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            return stockRepository.save(stock);
        } else{
            Stock newStock = new Stock();
            newStock.setDrink(drink);
            newStock.setBranch(branch);
            newStock.setQuantity(quantity);
            newStock.setMinThreshold(10);
            return stockRepository.save(newStock);
        }
    }

    @Override
    public Stock getStockByDrinkAndBranch(UUID drinkId, UUID branchId) {
        drinkRepository.findById(drinkId).orElseThrow(() -> new RuntimeException("Drink not found"));
        return stockRepository.findByDrink_IdAndBranch_Id(drinkId, branchId).orElseThrow(() -> new RuntimeException("Stock not found"));
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/impl/UserServiceImpl.java`

```java
package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Session;
import com.backend.kamnywesoliqourbackend.entity.User;
import com.backend.kamnywesoliqourbackend.enums.UserStatus;
import com.backend.kamnywesoliqourbackend.repository.SessionRepository;
import com.backend.kamnywesoliqourbackend.repository.UserRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public UserServiceImpl(UserRepository userRepository,SessionRepository sessionRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public List<User> getPendingUsers() {
        return userRepository.findAllByStatus(UserStatus.PENDING);
    }

    @Override
    public void approveUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void suspendUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public void deleteSessionById(UUID id) {
        sessionRepository.deleteById(id);
    }

    @Override
    public void deleteAllSessions() {
        sessionRepository.deleteAll();
    }
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/AuthService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.User;

public interface AuthService {
    String login(String email, String password);
    User register(String name, String email, String password, String phone);
    void logout(String token);
    Boolean verifyOtp(String email, String otp);
    String refreshToken(String token);
}


```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/BranchService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Branch;

import java.util.List;
import java.util.UUID;

public interface BranchService {
    List<Branch> getAllBranches();
    Branch getBranchById(UUID id);
    Branch createBranch(Branch branch);
    Branch updateBranch(UUID id , Branch branch);
    void deleteBranch(UUID id);
    Branch getHqBranch();
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/DispatchService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.DispatchOrder;
import com.backend.kamnywesoliqourbackend.enums.DispatchStatus;

import java.util.List;
import java.util.UUID;

public interface DispatchService {
    List<DispatchOrder> getAllDispatches();
    DispatchOrder createDispatch(DispatchOrder dispatchOrder);
    DispatchOrder approveDispatch(UUID id);
    DispatchOrder getDispatch(UUID id);
    DispatchOrder updateDispatchStatus(UUID id, DispatchStatus status);
    List<DispatchOrder> getBranchDispatch(UUID branchId);
    DispatchOrder confirmBranchDispatch(UUID id,UUID branchId);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/LoyaltyService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.LoyaltyCustomer;
import com.backend.kamnywesoliqourbackend.entity.LoyaltyTransaction;
import com.backend.kamnywesoliqourbackend.enums.TransactionType;

import java.util.List;
import java.util.UUID;

public interface LoyaltyService {
    List<LoyaltyCustomer> getAllLoyaltyCustomers();
    List<LoyaltyCustomer> getLoyaltyCustomer(String query);
    LoyaltyCustomer getLoyaltyCustomerById(UUID customerId);
    LoyaltyCustomer registerCustomer(LoyaltyCustomer customer);
    List<LoyaltyTransaction> getLoyaltyTransactions(UUID customerId);
    LoyaltyTransaction createLoyaltyTransaction(UUID customerId, UUID orderId, TransactionType type , Integer points);

}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/OrderService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(UUID id);
    List<Order> getBranchOrders(UUID branchId);
    Order createOrder(UUID branchId,Order order);
    Order updateOrderStatus(UUID id , OrderStatus status);
//    void deleteOrder(UUID id);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/ReportService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.Report;
import com.backend.kamnywesoliqourbackend.entity.Stock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReportService {
    List<Order> getSalesReport(UUID branchId, LocalDate dateFrom, LocalDate dateTo);
    BigDecimal getProfitLoss(UUID branchId, LocalDate dateFrom, LocalDate dateTo);
    Report saveReport(Report report);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/StockReturnService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.StockReturn;

import java.util.List;
import java.util.UUID;

public interface StockReturnService {
    StockReturn processStockReturn(UUID branchId,StockReturn stockReturn);
    List<StockReturn> getStockReturnsByBranch(UUID branchId);
    List<StockReturn> getAllStockReturns();
    StockReturn updateStockReturn(UUID stockReturnId,StockReturn stockReturn);

}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/StockService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Stock;

import java.util.List;
import java.util.UUID;

public interface StockService {
    List<Stock> getAllStocks();
    Stock createStock(Stock stock);
    List<Stock> getStockByBranch(UUID branchId);
    Stock restockBranch(UUID branchId, Integer quantity, UUID drinkId);
    Stock getStockByDrinkAndBranch(UUID drinkId, UUID branchId);

//    void deleteStock(Long id);
}

```

## File: `src/main/java/com/backend/kamnywesoliqourbackend/service/interfaces/UserService.java`

```java
package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Session;
import com.backend.kamnywesoliqourbackend.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(UUID id);
    List<User> getPendingUsers();
    void approveUser(UUID id);
//    void rejectUser(UUID id);
    void suspendUser(UUID id);
    void deleteUser(UUID id);
    List<Session> getAllSessions();
    void deleteSessionById(UUID id);
    void deleteAllSessions();
}

```

## File: `src/main/resources/application.properties`

```properties
spring.application.name=KamnywesoLiqourBackend
spring.datasource.url=jdbc:postgresql://localhost:5332/kamnyweso
spring.datasource.username=admin
spring.datasource.password=admin001
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.jpa.open-in-view=false

```

## File: `src/test/java/com/backend/kamnywesoliqourbackend/KamnywesoLiqourBackendApplicationTests.java`

```java
package com.backend.kamnywesoliqourbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KamnywesoLiqourBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}

```
