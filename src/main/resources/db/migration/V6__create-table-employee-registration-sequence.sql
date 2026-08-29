CREATE TABLE tb_employee_registration_sequence(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hotel_id          BIGINT NOT NULL,
    registration_year INT    NOT NULL,
    next_value        BIGINT NOT NULL,

    CONSTRAINT fk_employee_registration_sequence_hotel
        FOREIGN KEY (hotel_id)
            REFERENCES tb_hotel (id),

    CONSTRAINT uk_employee_registration_sequence_hotel_year
        UNIQUE (hotel_id, registration_year)
);