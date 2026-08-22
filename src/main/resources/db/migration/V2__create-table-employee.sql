CREATE TABLE tb_employee (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid              UUID          NOT NULL,
    registration_code VARCHAR(20)   NOT NULL,
    hire_date         DATE          NOT NULL,
    dismissal_date    DATE,
    status            VARCHAR(20)   NOT NULL,
    job_title         VARCHAR(100)  NOT NULL,

    CONSTRAINT uk_employee_uuid UNIQUE (uuid),
    CONSTRAINT uk_employee_registration_code UNIQUE (registration_code)
);