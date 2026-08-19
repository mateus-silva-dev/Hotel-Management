CREATE TABLE tb_person (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           uuid VARCHAR(36) UNIQUE NOT NULL,
                           first_name VARCHAR(80) NOT NULL,
                           surname VARCHAR(255) NOT NULL,
                           document VARCHAR(11) UNIQUE NOT NULL,
                           birth_date DATE NOT NULL,
                           email VARCHAR(255) UNIQUE NOT NULL,
                           phone_number VARCHAR(10),
                           mobile_number VARCHAR(11)
);