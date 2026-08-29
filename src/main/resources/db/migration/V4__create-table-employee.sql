CREATE TABLE tb_employee (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid              UUID          NOT NULL,
    registration_code VARCHAR(20)   NOT NULL,
    hire_date         DATE          NOT NULL,
    dismissal_date    DATE,
    status            VARCHAR(20)   NOT NULL,
    job_position_id   BIGINT        NOT NULL,
    person_id         BIGINT        NOT NULL,
    hotel_id          BIGINT        NOT NULL,

    CONSTRAINT uk_employee_uuid UNIQUE (uuid),
    CONSTRAINT uk_employee_registration_code UNIQUE (registration_code),
    CONSTRAINT uk_employee_person UNIQUE (person_id),
    CONSTRAINT fk_employee_job_position
        FOREIGN KEY (job_position_id)
                            REFERENCES tb_job_position(id),
    CONSTRAINT fk_employee_person
        FOREIGN KEY (person_id)
                         REFERENCES tb_person(id),
    CONSTRAINT fk_employee_hotel
        FOREIGN KEY (hotel_id)
                         REFERENCES tb_hotel(id)
);