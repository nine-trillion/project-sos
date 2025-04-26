# 기능 명세서 - 가게 / 대시보드

## 1. 가게 등록

### 1.1 가게 등록 (OWNER만 가능)

- **설명**: 사장(OWNER) 권한을 가진 사용자가 새 가게를 등록할 수 있다.
- **요청 데이터**:
    - 가게명
    - 영업 시작 시간
    - 영업 마감 시간
    - 최소 주문 금액
- **처리 로직**:
    1. 로그인 여부 확인
    2. 사용자 권한이 OWNER인지 검증
    3. 해당 사용자가 이미 소유한 가게가 3개 이상인 경우 등록 거부
    4. 새 가게 엔티티를 생성 및 저장
    5. 저장된 가게 정보를 응답
- **예외 처리**:
    - OWNER 권한이 아닌 경우: `StoreException(StoreError.UNAUTHORIZED_STORE_OWNER)`
    - 운영 중인 가게가 3개 이상인 경우: `StoreException(StoreError.EXCEEDED_STORE_LIMIT)`

## 2. 가게 조회

### 2.1 가게 목록 조회 (이름 검색)

- **설명**: 가게 이름에 특정 문자열이 포함된 가게 목록을 조회할 수 있다.
- **요청 데이터**:
    - 검색어 (name)
- **처리 로직**:
    1. 이름에 검색어가 포함된 가게 목록 조회
    2. 운영 중인 가게만 필터링하여 반환
- **예외 처리**:
    - 없음 (검색 결과가 없으면 빈 리스트 반환)

### 2.2 가게 단건 조회 (메뉴 포함)

- **설명**: 특정 가게 ID로 가게 상세 정보와 가게의 메뉴 리스트를 함께 조회할 수 있다.
- **요청 데이터**:
    - 가게 ID
- **처리 로직**:
    1. 가게 ID로 가게 조회
    2. 해당 가게에 소속된 메뉴 목록 함께 조회
    3. 가게 정보 + 메뉴 목록을 응답
- **예외 처리**:
    - 가게를 찾을 수 없는 경우: `StoreException(StoreError.NOT_FOUND_STORE)`

## 3. 가게 수정

### 3.1 가게 정보 수정

- **설명**: 가게 소유자가 본인의 가게 정보를 수정할 수 있다.
- **요청 데이터**:
    - 가게명
    - 영업 시작 시간
    - 영업 마감 시간
    - 최소 주문 금액
    - 공지사항
- **처리 로직**:
    1. 로그인 여부 확인
    2. 가게 ID로 가게 조회
    3. 로그인한 사용자가 가게 소유자인지 검증
    4. 영업 시작 시간이 마감 시간 이후이면 수정 거부
    5. 가게 정보를 업데이트
    6. 수정된 가게 정보를 응답
- **예외 처리**:
    - OWNER가 아닌 사용자가 수정 시도할 경우: `StoreException(StoreError.UNAUTHORIZED_STORE_OWNER)`
    - 영업 시작 시간이 마감 시간 이후일 경우: `StoreException(StoreError.INVALID_OPEN_TIME_AFTER_CLOSE)`

## 4. 가게 삭제

### 4.1 가게 삭제 (Soft Delete)

- **설명**: 가게 소유자가 본인의 가게를 삭제(폐업)할 수 있다.
- **요청 데이터**:
    - 가게 ID
- **처리 로직**:
    1. 로그인 여부 확인
    2. 가게 ID로 가게 조회
    3. 로그인한 사용자가 가게 소유자인지 검증
    4. 가게를 삭제 처리 (Soft Delete → 실제로는 status를 'CLOSED'로 변경)
- **예외 처리**:
    - OWNER가 아닌 사용자가 삭제 시도할 경우: `StoreException(StoreError.UNAUTHORIZED_STORE_OWNER)`
    - 가게를 찾을 수 없는 경우: `StoreException(StoreError.NOT_FOUND_STORE)`

---

## 5. 대시보드 조회

### 5.1 가게별 대시보드 조회

- **설명**: 사장이 본인의 특정 가게에 대한 일간/월간 매출 통계를 조회할 수 있다.
- **요청 데이터**:
    - 가게 ID
    - 날짜 (`date`, 형식: yyyy-MM-dd) 또는
    - 월 (`month`, 형식: yyyy-MM)
- **처리 로직**:
    1. 로그인 여부 확인
    2. 가게 ID로 가게 조회
    3. 로그인한 사용자가 해당 가게의 소유자인지 검증
    4. 조회 기준(basis)을 일(`DATE`) 또는 월(`MONTH`)로 구분
    5. 기간(start~end) 설정
    6. 해당 기간 동안의 주문 데이터를 기준으로
        - 방문한 고객 수 (고유 사용자 수)
        - 주문 건수
        - 총 매출액 합계
    7. 가게 정보와 함께 응답
- **예외 처리**:
    - 로그인하지 않은 경우
    - OWNER가 아닌 사용자가 접근 시도한 경우: `StoreException(StoreError.UNAUTHORIZED_STORE_OWNER)`
    - 날짜(`date`)와 월(`month`) 둘 다 입력하지 않은 경우: `DashboardException(DashboardError.DATE_REQUIRED)`
    - 날짜(`date`)와 월(`month`) 둘 다 입력한 경우: `DashboardException(DashboardError.DATE_PARAM_CONFLICT)`

### 5.2 사장별 대시보드 조회

- **설명**: 사장이 자신이 운영하는 모든 가게에 대한 일간/월간 통합 매출 통계를 조회할 수 있다.
- **요청 데이터**:
    - 날짜 (`date`, 형식: yyyy-MM-dd) 또는
    - 월 (`month`, 형식: yyyy-MM)
- **처리 로직**:
    1. 로그인 여부 확인
    2. 로그인한 사용자가 OWNER 권한을 가지고 있는지 검증
    3. 조회 기준(basis)을 일(`DATE`) 또는 월(`MONTH`)로 구분
    4. 기간(start~end) 설정
    5. OWNER가 소유한 모든 운영 중인 가게들의 주문 데이터 합산
        - 방문한 고객 수 (고유 사용자 수)
        - 주문 건수
        - 총 매출액 합계
    6. 응답에는 가게 정보 없이 매출 데이터만 반환
- **예외 처리**:
    - 로그인하지 않은 경우
    - OWNER가 아닌 사용자가 접근 시도한 경우: `StoreException(StoreError.UNAUTHORIZED_STORE_OWNER)`
    - 날짜(`date`)와 월(`month`) 둘 다 입력하지 않은 경우: `DashboardException(DashboardError.DATE_REQUIRED)`
    - 날짜(`date`)와 월(`month`) 둘 다 입력한 경우: `DashboardException(DashboardError.DATE_PARAM_CONFLICT)`