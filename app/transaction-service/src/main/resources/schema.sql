CREATE TABLE Transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    creditAccountId BIGINT NOT NULL,
    debitAccountId BIGINT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    transactionType VARCHAR(50) NOT NULL,
    transactionGroupId BIGINT NOT NULL
);

CREATE TABLE TransactionGroup (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    creditorOrg BIGINT NOT NULL,
    totalAmount DECIMAL(19,4) NOT NULL,
    transactionType VARCHAR(50) NOT NULL,
    fileUrl VARCHAR(255),
    fileStatus VARCHAR(50),
    ruleSetId BIGINT,
    groupId BIGINT,
    fileName VARCHAR(255),
    makerId BIGINT NOT NULL
);