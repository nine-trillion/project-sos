-- User 테이블
CREATE TABLE user
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    nickname     VARCHAR(50)  NOT NULL,
    phone_number VARCHAR(20)  NOT NULL UNIQUE,
    role         VARCHAR(20)  NOT NULL,
    grade        VARCHAR(20),
    is_deleted   BOOLEAN  DEFAULT FALSE,
    deleted_at   DATETIME,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Store 테이블
CREATE TABLE store
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id        BIGINT      NOT NULL,
    name            VARCHAR(30) NOT NULL,
    open_time       TIME        NOT NULL,
    close_time      TIME        NOT NULL,
    min_order_price INT         NOT NULL,
    status          VARCHAR(20) DEFAULT 'OPERATING',
    notice          VARCHAR(1000),
    is_operating    BOOLEAN     DEFAULT TRUE,
    created_at      DATETIME    DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES user (id)
);

-- Menu 테이블
CREATE TABLE menu
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id   BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    price      INT          NOT NULL,
    category   VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store (id)
);

-- Cart 테이블
CREATE TABLE cart
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

-- CartItem 테이블
CREATE TABLE cart_item
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id    BIGINT NOT NULL,
    menu_id    BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart (id),
    FOREIGN KEY (menu_id) REFERENCES menu (id)
);

-- Order 테이블
CREATE TABLE `order`
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    store_id     BIGINT NOT NULL,
    status       VARCHAR(20) DEFAULT 'PENDING',
    price        INT    NOT NULL,
    requested_at DATETIME    DEFAULT CURRENT_TIMESTAMP,
    created_at   DATETIME    DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (store_id) REFERENCES store (id)
);

-- OrderItem 테이블
CREATE TABLE order_item
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT NOT NULL,
    menu_id    BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES `order` (id),
    FOREIGN KEY (menu_id) REFERENCES menu (id)
);

-- Review 테이블
CREATE TABLE review
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id   BIGINT NOT NULL,
    order_id   BIGINT NOT NULL,
    content    TEXT   NOT NULL,
    rating     INT    NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store (id),
    FOREIGN KEY (order_id) REFERENCES `order` (id)
);