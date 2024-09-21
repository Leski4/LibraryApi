CREATE TABLE books
(
    isbn VARCHAR(30) PRIMARY KEY NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    author VARCHAR(150) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE track_of_books
(
    book_isbn VARCHAR(30) PRIMARY KEY NOT NULL UNIQUE,
    start_date TIMESTAMP,
    end_date TIMESTAMP
);

CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);