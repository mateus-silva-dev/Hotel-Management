INSERT INTO tb_person (uuid, first_name, surname, document, birth_date, email, phone_number, mobile_number) VALUES ('1ace1347-2f4a-4db9-88e7-36d2bac76a8c', 'Maria',  'Ferreira Pereira', '42957390060', '1962-07-19', 'maria@email.com', '6821879908', '11913075437');
INSERT INTO tb_person (uuid, first_name, surname, document, birth_date, email, phone_number, mobile_number) VALUES ('a92f715a-2f1a-4019-95d2-138a09aa0421', 'Otávio',  'César Ian Martins', '18257746843', '1992-07-23', 'otavio@email.com', '1142010307', '11967423789');

INSERT INTO tb_employee (uuid, registration_code, hire_date, status, job_title) VALUES ('0dc96f81-954d-4ee7-9426-af74dca1b72c', '01-2026-0001', '2026-05-20', 'ACTIVE', 'RECEPCIONISTA');
INSERT INTO tb_employee (uuid, registration_code, hire_date, status, job_title) VALUES ('2830c6e2-1c8b-4930-819a-f270ac989028', '01-2026-0002', '2026-05-21', 'ACTIVE', 'PORTEIRO');

INSERT INTO tb_job_position (uuid, name, active) VALUES ('4b146b88-badf-48c4-9d16-fdf44d0fc585', 'RECEPCIONISTA', 'true');
INSERT INTO tb_job_position (uuid, name, active) VALUES ('40f48c94-ee6b-4369-ad7d-165581343abf', 'PORTEIRO', 'true');