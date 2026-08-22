CREATE TABLE tb_person (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid          UUID         NOT NULL,
    first_name    VARCHAR(80)  NOT NULL,
    surname       VARCHAR(255) NOT NULL,
    document      VARCHAR(11)  NOT NULL,
    birth_date    DATE         NOT NULL,
    email         VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(10),
    mobile_number VARCHAR(11),

    CONSTRAINT uk_person_uuid UNIQUE (uuid),
    CONSTRAINT uk_person_document UNIQUE (document),
    CONSTRAINT uk_person_email UNIQUE (email)
);