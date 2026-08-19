CREATE TABLE tb_employee (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           uuid VARCHAR(36) UNIQUE NOT NULL,
                           registration_code VARCHAR(20) UNIQUE NOT NULL,
                           hire_date DATE NOT NULL,
                           dismissal_date DATE,
                           status VARCHAR(20) NOT NULL
);