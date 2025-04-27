# 기능 명세서 - 리뷰

## 1. 리뷰 등록

### 1.1 리뷰 작성

- **설명**: 사용자가 본인의 완료된 주문에 대해 리뷰를 작성할 수 있다.
- **요청 데이터**:
    - 리뷰 내용
    - 평점 (1~5점)
- **처리 로직**:
    1. 주문 ID로 주문 조회
    2. 로그인한 사용자가 주문 소유자인지 검증
    3. 주문 상태가 'COMPLETED'인지 확인
    4. 배송 완료 후 5일 이내인지 검증
    5. 리뷰 엔티티 생성 및 저장
- **예외 처리**:
    - 로그인한 사용자와 주문 소유자가 다를 경우: `ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS)`
    - 주문이 완료되지 않았거나 리뷰 작성 기한(5일) 초과 시: `ReviewException(ReviewError.ORDER_NOT_COMPLETED)`

## 2. 리뷰 조회

### 2.1 가게별 평점 기반 리뷰 조회

- **설명**: 특정 가게에 대해 지정한 평점으로 작성된 리뷰 목록을 조회할 수 있다.
- **요청 데이터**:
    - 가게 ID
    - 평점 (1~5점)
- **처리 로직**:
    1. 가게 ID로 가게 존재 여부 검증
    2. 해당 가게의 지정 평점에 해당하는 리뷰 리스트 조회
    3. 작성 시간 기준으로 내림차순 정렬
- **예외 처리**:
    - 가게를 찾을 수 없는 경우: `StoreException(StoreError.NOT_FOUND_STORE)`

## 3. 리뷰 수정

### 3.1 리뷰 수정

- **설명**: 사용자가 본인이 작성한 리뷰를 수정할 수 있다.
- **요청 데이터**:
    - 수정할 리뷰 내용
    - 수정할 평점
- **처리 로직**:
    1. 주문 ID로 주문 조회
    2. 로그인한 사용자가 주문 소유자인지 검증
    3. 주문 ID로 리뷰 조회
    4. 리뷰 엔티티에 수정 요청 반영
- **예외 처리**:
    - 로그인한 사용자와 주문 소유자가 다를 경우: `ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS)`
    - 리뷰를 찾을 수 없는 경우: `ReviewException(ReviewError.REVIEW_NOT_FOUND)`

## 4. 리뷰 삭제

### 4.1 리뷰 삭제

- **설명**: 사용자가 본인이 작성한 리뷰를 삭제할 수 있다.
- **요청 데이터**:
    - 주문 ID
- **처리 로직**:
    1. 주문 ID로 주문 조회
    2. 로그인한 사용자가 주문 소유자인지 검증
    3. 주문 ID로 리뷰 조회
    4. 리뷰 삭제
- **예외 처리**:
    - 로그인한 사용자와 주문 소유자가 다를 경우: `ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS)`
    - 리뷰를 찾을 수 없는 경우: `ReviewException(ReviewError.REVIEW_NOT_FOUND)`