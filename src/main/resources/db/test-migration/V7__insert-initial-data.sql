INSERT INTO tb_person (uuid, first_name, surname, document, birth_date, email, phone_number, mobile_number)
VALUES
    ('1ace1347-2f4a-4db9-88e7-36d2bac76a8c', 'Maria',  'Ferreira Pereira', '42957390060', '1962-07-19', 'maria@email.com', '6821879908', '11913075437'),
    ('a92f715a-2f1a-4019-95d2-138a09aa0421', 'Otávio',  'César Ian Martins', '18257746843', '1992-07-23', 'otavio@email.com', '1142010307', '11967423789'),
    ('ca444c5a-0c76-4994-b384-e5cad5908501', 'Heitor',  'Lorenzo Silva', '42440019992', '2005-05-11', 'heitor_ferreira@email.com', '9126497310', '91992043955');

INSERT INTO tb_job_position (uuid, name, active)
VALUES
    ('4b146b88-badf-48c4-9d16-fdf44d0fc585', 'RECEPCIONISTA', 'true'),
    ('40f48c94-ee6b-4369-ad7d-165581343abf', 'PORTEIRO', 'true');

INSERT INTO tb_hotel (uuid, name, description, type, rating, default_board_basis, document, email, phone_number, mobile_number, active)
VALUES
    ('c181a544-4143-49b3-9f45-e475aa789937', 'Paradise Beach Resort', 'Resort de luxo localizado na beira da praia com lazer completo para toda a família.', 'RESORT', 'FIVE_STARS', 'ALL_INCLUSIVE', '97638211000196', 'contato@paradisebeach.com', '1133004400', '11999998888', true),
    ('de55a99e-f2ea-4599-8eaf-f516f025b059', 'Metropolitan Business Hotel', 'Hotel moderno no centro financeiro, ideal para viagens corporativas e eventos.', 'HOTEL', 'FOUR_STARS', 'BED_AND_BREAKFAST', '14138244000140', 'reservas@metropolitan.com', '1133005500', '11988887777', true),
    ('f19a6371-544d-4f00-8667-d39f62d29e82', 'Vale Verde Eco Farm', 'Hotel-fazenda aconchegante com atividades de ecoturismo, cavalgadas e culinária típica.', 'FARM_HOTEL', 'FOUR_STARS', 'FULL_BOARD', '96929809000171', 'info@valeverdeeco.com', '1133006600', '11977776666', true);

INSERT INTO tb_employee (uuid, registration_code, hire_date, status, job_position_id, person_id, hotel_id)
VALUES
    ('0dc96f81-954d-4ee7-9426-af74dca1b72c', '01-2026-0001', '2026-05-20', 'ACTIVE', 1, 1, 1),
    ('2830c6e2-1c8b-4930-819a-f270ac989028', '03-2026-0001', '2026-05-21', 'ACTIVE', 2, 2, 3);

INSERT INTO tb_hotel_amenities (hotel_id, amenity)
VALUES
    (1, 'WIFI'),
    (1, 'SWIMMING_POOL'),
    (1, 'SPA'),
    (1, 'PET_FRIENDLY'),
    (1, 'ROOM_SERVICE_24H');

INSERT INTO tb_hotel_amenities (hotel_id, amenity)
VALUES
    (2, 'WIFI'),
    (2, 'GYM'),
    (2, 'PARKING'),
    (2, 'CONCIERGE');

INSERT INTO tb_hotel_amenities (hotel_id, amenity)
VALUES
    (3, 'WIFI'),
    (3, 'SWIMMING_POOL'),
    (3, 'PET_FRIENDLY');

INSERT INTO tb_employee_registration_sequence (hotel_id, registration_year, next_value)
VALUES
    (1, 2026, 2),
    (3, 2026, 2);