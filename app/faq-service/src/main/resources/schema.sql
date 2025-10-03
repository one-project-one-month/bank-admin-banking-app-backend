CREATE TABLE FAQ(
    id SERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL default now(),
    updated_at TIMESTAMP
);
