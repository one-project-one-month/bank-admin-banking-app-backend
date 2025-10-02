CREATE TABLE IF NOT EXISTS Transaction (
    id BIGSERIAL PRIMARY KEY,
    creditAccountId BIGINT NOT NULL,
    debitAccountId BIGINT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    transactionType VARCHAR(50) NOT NULL,
    transactionGroupId BIGINT
);
