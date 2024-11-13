
CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1;


CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('users_id_seq'),
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT true
);

CREATE INDEX idx_users_email ON users(email);