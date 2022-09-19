DROP TABLE IF EXISTS MEMBER CASCADE;
CREATE TABLE MEMBER
(
    id LONG NOT NULL,
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS BOOKING CASCADE;
CREATE TABLE BOOKING
(
    id LONG NOT NULL,
    creator int NOT NULL,
    day_duration int NOT NULL,
    date date NOT NULL,
    status VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (creator) REFERENCES MEMBER (id)
);