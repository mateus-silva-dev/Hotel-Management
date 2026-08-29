CREATE TABLE tb_job_position (
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid   UUID          NOT NULL,
    name   VARCHAR(50)   NOT NULL,
    active BOOLEAN             NOT NULL,

    CONSTRAINT uk_job_position_uuid UNIQUE (uuid),
    CONSTRAINT uk_job_position_name UNIQUE (name)
);