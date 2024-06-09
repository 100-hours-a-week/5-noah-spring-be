CREATE TABLE IF NOT EXISTS Member
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    imageUrl VARCHAR(128) NOT NULL,
    email    VARCHAR(64)  NOT NULL,
    password VARCHAR(64)  NOT NULL,
    nickname VARCHAR(10)  NOT NULL,
    UNIQUE (email),
    UNIQUE (nickname)
);