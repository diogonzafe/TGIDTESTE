CREATE TABLE transacoes (
        id TEXT PRIMARY KEY UNIQUE NOT NULL,
        deposito DECIMAL NOT NULL,
        saque DECIMAL NOT NULL,
        cpf TEXT REFERENCES cliente (cpf),
        cnpj TEXT REFERENCES empresa (cnpj)
);

