CREATE TABLE User
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(100) NOT NULL,
    nickname     VARCHAR(50)  NOT NULL,
    phone_number VARCHAR(20)  NOT NULL,
    role         VARCHAR(20)  NOT NULL,
    grade        VARCHAR(20),
    is_deleted   BOOLEAN DEFAULT FALSE,
    created_at   DATETIME,
    updated_at   DATETIME
);

CREATE TABLE Store
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id        BIGINT       NOT NULL,
    name            VARCHAR(255) NOT NULL,
    open_time       TIME,
    close_time      TIME,
    min_order_price INT,
    status          VARCHAR(50),
    notice          TEXT,
    created_at      DATETIME,
    updated_at      DATETIME,
    FOREIGN KEY (owner_id) REFERENCES User (id)
);

CREATE TABLE Menu
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id    BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    price       INT          NOT NULL,
    status      VARCHAR(50),
    category    VARCHAR(50),
    created_at  DATETIME,
    updated_at  DATETIME,
    FOREIGN KEY (store_id) REFERENCES Store (id)
);

CREATE TABLE MenuOption
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_id BIGINT NOT NULL,
    name    VARCHAR(255),
    price   INT,
    FOREIGN KEY (menu_id) REFERENCES Menu (id)
);

CREATE TABLE `Order`
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    store_id     BIGINT NOT NULL,
    menu_id      BIGINT,
    status       VARCHAR(50),
    price        INT,
    requested_at DATETIME,
    created_at   DATETIME,
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (store_id) REFERENCES Store (id),
    FOREIGN KEY (menu_id) REFERENCES Menu (id)
);

CREATE TABLE CartItem
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id   BIGINT,
    store_id  BIGINT,
    menu_id   BIGINT,
    quantity  INT,
    option_id INT,
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (store_id) REFERENCES Store (id),
    FOREIGN KEY (menu_id) REFERENCES Menu (id),
    FOREIGN KEY (option_id) REFERENCES MenuOption (id)
);

CREATE TABLE Review
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    order_id   BIGINT NOT NULL,
    store_id   BIGINT NOT NULL,
    rating     INT    NOT NULL,
    content    TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (order_id) REFERENCES `Order` (id),
    FOREIGN KEY (store_id) REFERENCES Store (id)
);

CREATE TABLE ReviewReply
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id  BIGINT NOT NULL,
    owner_id   BIGINT NOT NULL,
    content    TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (review_id) REFERENCES Review (id),
    FOREIGN KEY (owner_id) REFERENCES User (id)
);

CREATE TABLE Point
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT,
    amount     INT,
    type       VARCHAR(50),
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES User (id)
);

CREATE TABLE Coupon
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT,
    code            VARCHAR(255),
    discount_amount INT,
    is_used         BOOLEAN,
    created_at      DATETIME,
    FOREIGN KEY (user_id) REFERENCES User (id)
);

