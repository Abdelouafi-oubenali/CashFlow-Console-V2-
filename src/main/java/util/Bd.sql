CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(15) CHECK (role IN ('ADMIN','AUDITOR','TELLER','MANAGER')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    cin VARCHAR(20) UNIQUE NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(50),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(10) CHECK (type IN ('COURANT','EPARGNE','CREDIT')) NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(10) CHECK (currency IN ('MAD','EUR','USD')) NOT NULL,
    client_id UUID NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_client_account FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_user_account FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE transaction (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount DECIMAL(15,2) NOT NULL,
    account_from_id UUID NOT NULL,
    account_to_id UUID NOT NULL,
    fee_rule_id UUID,
    status VARCHAR(30) CHECK (status IN ('PENDING','SETTLED','FAILED')) NOT NULL,
    type VARCHAR(30) CHECK (type IN ('TRANSFER_EXTERNAL','WITHDRAW_FOREIGN_CURRENCY','DEPOSIT','WITHDRAW')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_from FOREIGN KEY (account_from_id) REFERENCES account(id),
    CONSTRAINT fk_account_to FOREIGN KEY (account_to_id) REFERENCES account(id),
    CONSTRAINT fk_fee_rule FOREIGN KEY (fee_rule_id) REFERENCES FeeRule(id),
    CONSTRAINT chk_diff_accounts CHECK (account_from_id <> account_to_id)
);

CREATE TABLE credit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    montant DECIMAL(15,2) NOT NULL,
    duree DATE,
    taux DECIMAL(15,2),
    interest_type VARCHAR(15) CHECK (interest_type IN ('SIMPLE','COMPOUND')) NOT NULL,
    status VARCHAR(15) CHECK (status IN ('PENDING','ACTIVE','LATE','CLOSED')) NOT NULL DEFAULT 'PENDING',
    account_id UUID NOT NULL,
    revenu_mensuel DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_credit_account FOREIGN KEY (account_id) REFERENCES account(id),
    CONSTRAINT fk_credit_fee_rule FOREIGN KEY (fee_rule_id) REFERENCES FeeRule(id)
);

CREATE TABLE bank (
    id VARCHAR(50) PRIMARY KEY,
    capital DECIMAL(15,2) DEFAULT 0,
    total_fees DECIMAL(15,2) DEFAULT 0,
    total_gains DECIMAL(15,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO bank (id, capital, total_fees, total_gains)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 900000, 10000000, 0);


-- crount joub 