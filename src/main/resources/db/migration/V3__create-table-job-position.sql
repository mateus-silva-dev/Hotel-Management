CREATE TABLE tb_job_position (
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid   UUID         UNIQUE NOT NULL,
    name   VARCHAR(50)  UNIQUE NOT NULL,
    active BOOLEAN             NOT NULL
);