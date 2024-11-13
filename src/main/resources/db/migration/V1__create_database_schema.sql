
CREATE SEQUENCE book_id_seq START 1 increment by 1;
CREATE SEQUENCE patron_id_seq START 1 increment by 1;
CREATE SEQUENCE borrowing_record_id_seq START 1 increment by 1;

CREATE TABLE book (
    id BIGINT PRIMARY KEY DEFAULT nextval('book_id_seq'),
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_year INT,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    genre VARCHAR(100),
    copies_available INT CHECK (copies_available >= 0)
);

CREATE TABLE patron (
    id BIGINT PRIMARY KEY DEFAULT nextval('patron_id_seq'),
    name VARCHAR(255) NOT NULL,
    mobile VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255),
    membership_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

CREATE TABLE borrowing_record (
    id BIGINT PRIMARY KEY DEFAULT nextval('borrowing_record_id_seq'),
    book_id BIGINT NOT NULL,
    patron_id BIGINT NOT NULL,
    borrow_date DATE NOT NULL,
    return_date DATE,
    due_date DATE
);

ALTER TABLE borrowing_record
ADD CONSTRAINT fk_borrowing_book
FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE;

ALTER TABLE borrowing_record
ADD CONSTRAINT fk_borrowing_patron
FOREIGN KEY (patron_id) REFERENCES patron(id) ON DELETE CASCADE;