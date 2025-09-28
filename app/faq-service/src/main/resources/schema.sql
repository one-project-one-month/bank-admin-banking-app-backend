CREATE TABLE FAQ(
    id INT PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL default now(),
    updated_at TIMESTAMP,
)