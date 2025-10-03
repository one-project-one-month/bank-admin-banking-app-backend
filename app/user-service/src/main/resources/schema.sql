CREATE TABLE IF NOT EXISTS `USER` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    gender_id INT,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
    );
-- CREATE TABLE IF NOT EXISTS "USER" (
--                                       id BIGSERIAL PRIMARY KEY,
--                                       full_name VARCHAR(255) NOT NULL,
--     date_of_birth DATE,
--     gender_id INT,
--     email VARCHAR(255) NOT NULL,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     created_by BIGINT,
--     updated_by BIGINT
--     );
