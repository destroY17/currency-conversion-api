DROP TABLE IF EXISTS exchange_rates;
DROP TABLE IF EXISTS currencies;

CREATE TABLE IF NOT EXISTS currencies (
    id serial primary key,
    code varchar UNIQUE NOT NULL,
    full_name varchar,
    sign varchar
);

CREATE TABLE IF NOT EXISTS exchange_rates(
    id serial primary key,
    base_currency_id INTEGER,
    target_currency_id INTEGER,
    rate DECIMAL(6),
    UNIQUE (base_currency_id, target_currency_id),
    FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
    FOREIGN KEY (target_currency_id) REFERENCES currencies(id)
);
