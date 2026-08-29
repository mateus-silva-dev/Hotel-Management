CREATE TABLE tb_hotel (
    id                        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                      UUID                NOT NULL,
    name                      VARCHAR(100)        NOT NULL,
    description               TEXT                NOT NULL,
    type                      VARCHAR(30)         NOT NULL,
    rating                    VARCHAR(10)         NOT NULL,
    default_board_basis       VARCHAR(20)         NOT NULL,
    document                  VARCHAR(14)         NOT NULL,
    email                     VARCHAR(255)        NOT NULL,
    phone_number              VARCHAR(10),
    mobile_number             VARCHAR(11),
    active                    BOOLEAN             NOT NULL,


    CONSTRAINT uk_hotel_uuid UNIQUE (uuid),
    CONSTRAINT uk_hotel_document UNIQUE (document),
    CONSTRAINT uk_hotel_email UNIQUE (email)
);