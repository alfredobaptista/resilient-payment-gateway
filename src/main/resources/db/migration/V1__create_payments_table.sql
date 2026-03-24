-- V1__create_payments_table.sql
CREATE TABLE tb_payments (
    payment_id              UUID            PRIMARY KEY,
    idempotency_key         VARCHAR(100)    NOT NULL UNIQUE,
    amount                  NUMERIC(19, 2)  NOT NULL,
    currency                VARCHAR(3)         NOT NULL,
    customer_id             VARCHAR(100)    NOT NULL,
    description             VARCHAR(255),
    status                  VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    gateway_transaction_id  VARCHAR(100),
    failure_reason          TEXT,
    created_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- Índices para consultas frequentes
CREATE INDEX idx_payments_idempotency_key ON tb_payments(idempotency_key);
CREATE INDEX idx_payments_customer_id     ON tb_payments(customer_id);
CREATE INDEX idx_payments_status          ON tb_payments(status);
CREATE INDEX idx_payments_created_at      ON tb_payments(created_at DESC);

-- Comentários
COMMENT ON TABLE  tb_payments                        IS 'Registo de todos os pagamentos processados';
COMMENT ON COLUMN tb_payments.payment_id             IS 'Identificador único do pagamento (UUID gerado pela aplicação)';
COMMENT ON COLUMN tb_payments.idempotency_key        IS 'Chave única por pedido — evita processamento duplicado';
COMMENT ON COLUMN tb_payments.amount                 IS 'Valor do pagamento com precisão de 2 casas decimais';
COMMENT ON COLUMN tb_payments.currency               IS 'Moeda no formato ISO 4217 (ex: USD, EUR, AOA)';
COMMENT ON COLUMN tb_payments.status                 IS 'Estado do pagamento: PENDING, APPROVED, FAILED';
COMMENT ON COLUMN tb_payments.gateway_transaction_id IS 'ID da transação devolvido pelo gateway externo (ex: Stripe)';
COMMENT ON COLUMN tb_payments.failure_reason         IS 'Motivo da falha quando status = FAILED';
COMMENT ON COLUMN tb_payments.created_at             IS 'Data de criação — nunca alterada após inserção';
COMMENT ON COLUMN tb_payments.updated_at             IS 'Data da última actualização — gerida pela aplicação';