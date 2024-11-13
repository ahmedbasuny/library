
INSERT INTO book (title, author, publication_year, isbn, genre, copies_available)
VALUES 
    ('The Great Gatsby', 'F. Scott Fitzgerald', 1925, '9780743273565', 'Fiction', 5),
    ('To Kill a Mockingbird', 'Harper Lee', 1960, '9780060935467', 'Fiction', 3),
    ('1984', 'George Orwell', 1949, '9780451524935', 'Dystopian', 4),
    ('Pride and Prejudice', 'Jane Austen', 1813, '9780141040349', 'Romance', 2),
    ('The Catcher in the Rye', 'J.D. Salinger', 1951, '9780316769488', 'Fiction', 4),
    ('Moby-Dick', 'Herman Melville', 1851, '9781503280786', 'Adventure', 1),
    ('War and Peace', 'Leo Tolstoy', 1869, '9780199232765', 'Historical', 3),
    ('The Odyssey', 'Homer', 1993, '9780140268867', 'Epic', 6),
    ('Frankenstein', 'Mary Shelley', 1818, '9780141439471', 'Horror', 3),
    ('The Iliad', 'Homer', 1989, '9780140275360', 'Epic', 2);

INSERT INTO patron (name, mobile, email, address, membership_date, status)
VALUES 
    ('Ahmed Basuny', '01276063525', 'ahmedbasuny13@gmail.com', 'Alex', '2024-01-15', 'ACTIVE'),
    ('Ali Ebrahim', '0987654321', 'ali.ebrahim@example.com', 'Cairo', '2024-02-10', 'ACTIVE'),
    ('Mohamed Mostafa', '1122334455', 'moahmed.mostafa@example.com', 'Alexandria', '2024-03-05', 'ACTIVE'),
    ('Ismail Ahmed', '5566778899', 'ismail.ahmed@example.com', 'Ismailia', '2024-04-20', 'ACTIVE'),
    ('Mona Tawfeek', '6677889900', 'mona.tawfeek@example.com', 'New Cairo', '2024-05-15', 'INACTIVE');

INSERT INTO borrowing_record (book_id, patron_id, borrow_date, due_date)
VALUES 
    (1, 1, '2024-08-01', '2024-08-15'),
    (2, 1, '2024-08-03', '2024-08-17'),
    (3, 2, '2024-08-05', '2024-08-19'),
    (4, 3, '2024-08-07', '2024-08-21'),
    (5, 4, '2024-08-09', '2024-08-23'),
    (6, 5, '2024-08-11', '2024-08-25'),
    (7, 2, '2024-08-13', '2024-08-27'),
    (8, 3, '2024-08-15', '2024-08-29'),
    (9, 4, '2024-08-17', '2024-08-31'),
    (10, 5, '2024-08-19', '2024-09-02');
