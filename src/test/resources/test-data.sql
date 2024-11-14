TRUNCATE TABLE book, borrowing_record, patron, users CASCADE;

ALTER SEQUENCE book_id_seq RESTART WITH 1;
ALTER SEQUENCE patron_id_seq RESTART WITH 1;
ALTER SEQUENCE borrowing_record_id_seq RESTART WITH 1;

INSERT INTO book (title, author, publication_year, isbn, genre, copies_available)
VALUES 
    ('The Great Gatsby', 'F. Scott Fitzgerald', 1925, '9780743273515', 'Fiction', 0),
    ('To Kill a Mockingbird', 'Harper Lee', 1960, '9780060935267', 'Fiction', 3),
    ('1984', 'George Orwell', 1949, '9780452124935', 'Dystopian', 4),
    ('Pride and Prejudice', 'Jane Austen', 1813, '9780141040349', 'Romance', 2);

INSERT INTO patron (name, mobile, email, address, membership_date, status)
VALUES 
    ('Ahmed Basuny', '01276063525', 'ahmedbasuny13@gmail.com', 'Alex', '2024-01-15', 'ACTIVE'),
    ('Ali Ebrahim', '0987654321', 'ali.ebrahim@example.com', 'Cairo', '2024-02-10', 'ACTIVE'),
    ('Sara Ibrahim', '0123456789', 'sara.ibrahim@example.com', 'Giza', '2024-03-01', 'INACTIVE');



INSERT INTO borrowing_record (book_id, patron_id, borrow_date, due_date, return_date)
VALUES (3, 2, '2024-08-01', '2024-08-15', null);
