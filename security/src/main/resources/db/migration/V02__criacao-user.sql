CREATE TABLE users
(
    id       INT AUTO_INCREMENT UNIQUE NOT NULL,
    login    VARCHAR(20) UNIQUE        NOT NULL,
    password VARCHAR                   NOT NULL,
    role     VARCHAR(20)               NOT NULL,

    PRIMARY KEY (id)
);