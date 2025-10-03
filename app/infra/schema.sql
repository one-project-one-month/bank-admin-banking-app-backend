-- Drop tables if they exist to start fresh
DROP TABLE IF EXISTS "users" CASCADE;
DROP TABLE IF EXISTS "profile_detail" CASCADE;
DROP TABLE IF EXISTS "role" CASCADE;
DROP TABLE IF EXISTS "organization" CASCADE;

-- Create Organization Table
CREATE TABLE "organization" (
    "id" BIGSERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "shortcode" VARCHAR(50) UNIQUE,
    "address" VARCHAR(255) NOT NULL,
    "country" VARCHAR(255) NOT NULL,
    "created_at" DATE NOT NULL DEFAULT CURRENT_DATE,
    "updated_at" DATE,
    "created_by" BIGINT,
    "updated_by" BIGINT
);

-- Create Role Table
CREATE TABLE "role" (
    "id" BIGSERIAL PRIMARY KEY,
    "role_type" VARCHAR(50) NOT NULL, -- e.g., 'USER', 'ACC'
    "name" VARCHAR(100) NOT NULL,
    "created_at" DATE NOT NULL DEFAULT CURRENT_DATE,
    "updated_at" DATE,
    "created_by" BIGINT,
    "updated_by" BIGINT
);

-- Create Profile Detail Table
CREATE TABLE "profile_detail" (
    "id" BIGSERIAL PRIMARY KEY,
    "phone_no" VARCHAR(50),
    "address" TEXT,
    "organization_id" BIGINT,
    "nrc_state" VARCHAR(100),
    "nrc_township" VARCHAR(100),
    "nrc_type" VARCHAR(50),
    "nrc_number" VARCHAR(50),
    "passport_number" VARCHAR(100),
    "visa_id" VARCHAR(100),
    "created_at" DATE NOT NULL DEFAULT CURRENT_DATE,
    "updated_at" DATE,
    "created_by" BIGINT,
    "updated_by" BIGINT,
    FOREIGN KEY ("organization_id") REFERENCES "organization"("id")
);

-- Create User Table
CREATE TABLE "users" (
    "id" BIGSERIAL PRIMARY KEY,
    "username" VARCHAR(255) UNIQUE NOT NULL,
    "email" VARCHAR(255) UNIQUE NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "profile_id" BIGINT UNIQUE,
    "role_id" BIGINT,
    "created_at" DATE NOT NULL DEFAULT CURRENT_DATE,
    "updated_at" DATE,
    "created_by" BIGINT,
    "updated_by" BIGINT,
    FOREIGN KEY ("profile_id") REFERENCES "profile_detail"("id"),
    FOREIGN KEY ("role_id") REFERENCES "role"("id")
);

-- Insert a default role
INSERT INTO "role" ("role_type", "name") VALUES ('USER', 'Super ADMIN');

-- Insert a default organization
INSERT INTO "organization" ("name", "shortcode", "address", "country") VALUES ('OPOM Company', 'DEF', 'GitHub', 'Myanmar');

DROP TABLE IF EXISTS "account_type" CASCADE;

-- Create the table for account type options
CREATE TABLE "account_type" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
)

INSERT INTO "account_type" ("id", "name") VALUES
(1, "Savings account"),
(2, "Joined account"),
(3, "Individual account")

DROP TABLE IF EXISTS "nrc_code_value" CASCADE;
DROP TABLE IF EXISTS "nrc_code" CASCADE;

-- Create the table for NRC code categories
CREATE TABLE "nrc_code" (
    "id" BIGSERIAL PRIMARY KEY,
    "name" VARCHAR(255) UNIQUE NOT NULL,
    "created_at" DATE NOT NULL DEFAULT CURRENT_DATE,
    "updated_at" DATE,
    "created_by" BIGINT,
    "updated_by" BIGINT
);

-- Create the table for NRC code values, linked to the categories
CREATE TABLE "nrc_code_value" (
    "id" BIGSERIAL PRIMARY KEY,
    "code_id" BIGINT NOT NULL,
    "value" VARCHAR(255) NOT NULL,
    "created_at" DATE NOT NULL DEFAULT CURRENT_DATE,
    "updated_at" DATE,
    "created_by" BIGINT,
    "updated_by" BIGINT,
    FOREIGN KEY ("code_id") REFERENCES "nrc_code"("id") ON DELETE CASCADE
);

-- Insert NRC code categories (States/Regions and NRC Type)
INSERT INTO "nrc_code" ("id", "name") VALUES
(1, 'KACHIN_STATE_TOWNSHIP_CODES'),
(2, 'KAYAH_STATE_TOWNSHIP_CODES'),
(3, 'KAYIN_STATE_TOWNSHIP_CODES'),
(4, 'CHIN_STATE_TOWNSHIP_CODES'),
(5, 'SAGAING_REGION_TOWNSHIP_CODES'),
(6, 'TANINTHARYI_REGION_TOWNSHIP_CODES'),
(7, 'BAGO_REGION_TOWNSHIP_CODES'),
(8, 'MAGWAY_REGION_TOWNSHIP_CODES'),
(9, 'MANDALAY_REGION_TOWNSHIP_CODES'),
(10, 'MON_STATE_TOWNSHIP_CODES'),
(11, 'RAKHINE_STATE_TOWNSHIP_CODES'),
(12, 'YANGON_REGION_TOWNSHIP_CODES'),
(13, 'SHAN_STATE_TOWNSHIP_CODES'),
(14, 'AYEYARWADY_REGION_TOWNSHIP_CODES'),
(15, 'NRC_TYPE'),
(16, 'NRC_STATE_CODE');

-- Insert NRC code values
-- KACHIN_STATE_TOWNSHIP_CODES (Code ID: 1)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(1, 'AhGaYa'), (1, 'BaMaNa'), (1, 'KhaPhaNa'), (1, 'DaPhaYa'), (1, 'HaPaNa'), (1, 'KaMaNa'), (1, 'KhaLaPha'), (1, 'LaGaNa'), (1, 'MaKhaBa'), (1, 'MaSaNa'), (1, 'MaNyaNa'), (1, 'MaKaTa'), (1, 'MaMaNa'), (1, 'MaKaNa'), (1, 'NaMaNa'), (1, 'PhaKaNa'), (1, 'PaTaAh'), (1, 'YaKaNa'), (1, 'SaBaNa'), (1, 'SaLaNa'), (1, 'SaPaBa'), (1, 'TaNaNa'), (1, 'WaMaNa');

-- KAYAH_STATE_TOWNSHIP_CODES (Code ID: 2)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(2, 'BaLaKha'), (2, 'DaMaSa'), (2, 'LaKaNa'), (2, 'MaSaNa'), (2, 'PhaSaNa'), (2, 'YaTaNa'), (2, 'PhaYaSa');

-- KAYIN_STATE_TOWNSHIP_CODES (Code ID: 3)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(3, 'BaAhNa'), (3, 'DaNyaKha'), (3, 'HaPaNa'), (3, 'KaKaYa'), (3, 'KaSaKa'), (3, 'LaThaNa'), (3, 'MaWaTa'), (3, 'PhaPaNa'), (3, 'ThaTaNa'), (3, 'KaMaMa'), (3, 'PaKaNa'), (3, 'YaWaNa');

-- CHIN_STATE_TOWNSHIP_CODES (Code ID: 4)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(4, 'HaKhaNa'), (4, 'HtaTaLa'), (4, 'KaPaLa'), (4, 'MaTaPa'), (4, 'MaTaNa'), (4, 'PaLaWa'), (4, 'PhaLaNa'), (4, 'SaMaNa'), (4, 'TaTaNa'), (4, 'TaZaNa');

-- SAGAING_REGION_TOWNSHIP_CODES (Code ID: 5)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(5, 'AhYaTa'), (5, 'BaMaNa'), (5, 'BaTaLa'), (5, 'DaPaYa'), (5, 'HaKhaNa'), (5, 'HaMaLa'), (5, 'HtaPaKha'), (5, 'AhTaNa'), (5, 'KaNaNa'), (5, 'KaThaNa'), (5, 'KaLaHta'), (5, 'KaLaWa'), (5, 'KaBaLa'), (5, 'KhaOuNa'), (5, 'KhaTaNa'), (5, 'KaLaTa'), (5, 'LaHaNa'), (5, 'LaYaNa'), (5, 'MaLaNa'), (5, 'MaKaNa'), (5, 'MaYaNa'), (5, 'NaYaNa'), (5, 'PaLaNa'), (5, 'PhaPaNa'), (5, 'PaLanNa'), (5, 'SaKaNa'), (5, 'SaLaKa'), (5, 'TaMaNa'), (5, 'TaSaNa'), (5, 'WaLaNa'), (5, 'YaBaNa'), (5, 'OuOuMa'), (5, 'YaOuNa');

-- TANINTHARYI_REGION_TOWNSHIP_CODES (Code ID: 6)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(6, 'BaPaNa'), (6, 'DaWaNa'), (6, 'KaThaNa'), (6, 'KaSaNa'), (6, 'LaLaNa'), (6, 'MaMaNa'), (6, 'PaLaNa'), (6, 'PaLaTa'), (6, 'TaThaYa'), (6, 'YaPhaNa');

-- BAGO_REGION_TOWNSHIP_CODES (Code ID: 7)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(7, 'BaKaLa'), (7, 'DaOuNa'), (7, 'HaTaTa'), (7, 'KaPaKa'), (7, 'KaTaKha'), (7, 'KaWaNa'), (7, 'LaPaDa'), (7, 'MaLaNa'), (7, 'NaTaLa'), (7, 'NyaLaPa'), (7, 'AhTaNa'), (7, 'PaKhaNa'), (7, 'PaTaNa'), (7, 'PaMaNa'), (7, 'PhaMaNa'), (7, 'PaTaSa'), (7, 'YaKaNa'), (7, 'YaTaYa'), (7, 'TaKaNa'), (7, 'ThaKaLa'), (7, 'ThaNaPa'), (7, 'WaMaNa'), (7, 'YaTaNa'), (7, 'ZaKaNa');

-- MAGWAY_REGION_TOWNSHIP_CODES (Code ID: 8)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(8, 'AhLaNa'), (8, 'BaMaNa'), (8, 'KhaMaNa'), (8, 'GaGaNa'), (8, 'HtaLaNa'), (8, 'KaMaNa'), (8, 'MaBaNa'), (8, 'MaKaNa'), (8, 'MaTaNa'), (8, 'MaLaNa'), (8, 'NaMaNa'), (8, 'NgaPhaNa'), (8, 'PaKhaKa'), (8, 'PaMaNa'), (8, 'PhaMaNa'), (8, 'SaLaNa'), (8, 'SaMaNa'), (8, 'SaPhaNa'), (8, 'SaTaYa'), (8, 'SaKaNa'), (8, 'TaTaKa'), (8, 'ThaYaNa'), (8, 'YaNaKha'), (8, 'YaSaKa'), (8, 'SaPaWa');

-- MANDALAY_REGION_TOWNSHIP_CODES (Code ID: 9)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(9, 'AhMaYa'), (9, 'AhMaZa'), (9, 'KhaAhZa'), (9, 'KhaMaZa'), (9, 'KaPaTa'), (9, 'KaLaNa'), (9, 'MaHaMa'), (9, 'MaHlaNa'), (9, 'MaKaNa'), (9, 'MaKhaNa'), (9, 'MaLaNa'), (9, 'MaMaNa'), (9, 'MaTaYa'), (9, 'MaYaMa'), (9, 'MaHtaLa'), (9, 'NaHtaKa'), (9, 'NgaZaNa'), (9, 'NyaOuNa'), (9, 'PaBaNa'), (9, 'PaKhaKa'), (9, 'PaLaNa'), (9, 'PaThaKa'), (9, 'PaMaNa'), (9, 'SaKaNa'), (9, 'SaTaKa'), (9, 'TaKaNa'), (9, 'TaThaNa'), (9, 'TaOuNa'), (9, 'ThaPaKa'), (9, 'ThaZaNa'), (9, 'WaTaNa'), (9, 'YaMaTha');

-- MON_STATE_TOWNSHIP_CODES (Code ID: 10)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(10, 'BaLaMa'), (10, 'KhaSaNa'), (10, 'KaMaYa'), (10, 'LaMaNa'), (10, 'MaDaNa'), (10, 'MaLaMa'), (10, 'PaMaNa'), (10, 'ThaPhaYa'), (10, 'ThaHtaNa'), (10, 'YaMaNa');

-- RAKHINE_STATE_TOWNSHIP_CODES (Code ID: 11)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(11, 'AhNaNa'), (11, 'BaThaTa'), (11, 'GaMaNa'), (11, 'KaPhaTa'), (11, 'KaKaNa'), (11, 'MaAhNa'), (11, 'MaOuNa'), (11, 'MaPaNa'), (11, 'PaNaKa'), (11, 'PaKhaTa'), (11, 'PaTaNa'), (11, 'RaMaNa'), (11, 'SaTaNa'), (11, 'TaKaPha'), (11, 'TaNaHta'), (11, 'ThaTaNa'), (11, 'YaBaNa');

-- YANGON_REGION_TOWNSHIP_CODES (Code ID: 12)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(12, 'AhLaNa'), (12, 'BaHaNa'), (12, 'BaTaHta'), (12, 'KaKaKa'), (12, 'DaGaNa'), (12, 'DaGaYa'), (12, 'DaGaMa'), (12, 'DaGaSa'), (12, 'DaLaNa'), (12, 'DaPaNa'), (12, 'KaMaYa'), (12, 'HtaTaPa'), (12, 'AhSaNa'), (12, 'KaTaTa'), (12, 'KaMaNa'), (12, 'KhaYaNa'), (12, 'KaTaNa'), (12, 'LaMaNa'), (12, 'LaThaYa'), (12, 'LaKaNa'), (12, 'MaBaNa'), (12, 'MaGaTa'), (12, 'MaYaKa'), (12, 'OuKaMa'), (12, 'PaBaTa'), (12, 'PaZaTa'), (12, 'SaKhaNa'), (12, 'SaKaKha'), (12, 'SaMaNa'), (12, 'YaPaTha'), (12, 'TaKaNa'), (12, 'TaMaNa'), (12, 'ThaKaTa'), (12, 'ThaLaNa'), (12, 'ThaGaKa'), (12, 'TaTaNa'), (12, 'OuKaTa'), (12, 'YaKaNa'), (12, 'LaMaTa'), (12, 'HlaGaNa'), (12, 'SaKhaKa');

-- SHAN_STATE_TOWNSHIP_CODES (Code ID: 13)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(13, 'KaLaNa'), (13, 'KaKaNa'), (13, 'KaThaNa'), (13, 'KaMaNa'), (13, 'LaKaNa'), (13, 'LaLaNa'), (13, 'LaYaNa'), (13, 'MaKhaNa'), (13, 'MaMaNa'), (13, 'MaPaNa'), (13, 'MaSaNa'), (13, 'MaTaNa'), (13, 'NaSaNa'), (13, 'NyaYaNa'), (13, 'PhaKhaNa'), (13, 'PaTaYa'), (13, 'RaThaNa'), (13, 'TaYaNa'), (13, 'TaMaNya'), (13, 'TaKhaLa'), (13, 'TaLaNa'), (13, 'TaKaNa'), (13, 'ThaNaNa'), (13, 'ThaPaNa'), (13, 'YaNgaNa'), (13, 'YaSaNa'), (13, 'AhPaNa'), (13, 'AhTaNa'), (13, 'AhTaYa'), (13, 'HaHaNa'), (13, 'HaMaNa'), (13, 'KaLaHta'), (13, 'KhaLaNa'), (13, 'MaHtaNa'), (13, 'MaKhaTa'), (13, 'MaNgaNa'), (13, 'MaPhaHta'), (13, 'NaTaYa'), (13, 'PaPaKa'), (13, 'PaWaNa');

-- AYEYARWADY_REGION_TOWNSHIP_CODES (Code ID: 14)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(14, 'AhGaPa'), (14, 'BaKaLa'), (14, 'DaDaYa'), (14, 'HaKaKa'), (14, 'HaThaTa'), (14, 'AhMaNa'), (14, 'KaKaNa'), (14, 'KaLaNa'), (14, 'KaKhaNa'), (14, 'KaPaNa'), (14, 'LaPaTa'), (14, 'MaAhPa'), (14, 'MaMaKa'), (14, 'MaAhaNa'), (14, 'MaMaNa'), (14, 'NgaPaTa'), (14, 'NyaTaNa'), (14, 'PaTaNa'), (14, 'PhaPaNa'), (14, 'ThaPaNa'), (14, 'WaKhaMa'), (14, 'YaKaNa'), (14, 'ZaLaNa');

-- NRC_TYPE (Code ID: 15)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(15, 'N'), (15, 'E'), (15, 'P'), (15, 'THI'), (15, 'THA');

-- NRC_STATE_CODE (Code ID: 16)
INSERT INTO "nrc_code_value" ("code_id", "value") VALUES
(16, '1'),(16, '2'),(16, '3'),(16, '4'),(16, '5'),(16, '6'),(16, '7'),
(16, '8'),(16, '9'),(16, '10'),(16, '11'),(16, '12'),(16, '13'),(16, '14');
