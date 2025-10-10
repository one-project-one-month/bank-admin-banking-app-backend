DROP TABLE IF EXISTS faq;
DROP TABLE IF EXISTS faq_category;

CREATE TABLE IF NOT EXISTS faq_category(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL default now(),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS faq(
    id SERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    faq_category_id INT REFERENCES faq_category(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL default now(),
    updated_at TIMESTAMP
);
