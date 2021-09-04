DROP TABLE IF EXISTS currency_exchange_rate;

CREATE TABLE currency_exchange_rate (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              currency_in VARCHAR(50) NOT NULL,
                              currency_out VARCHAR(50) NOT NULL,
                              rate NUMBER NOT NULL,
                              date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO currency_exchange_rate (currency_in, currency_out, rate) VALUES
                                                             ('rub', 'usd', 0.014),
                                                             ('rub', 'eur', 0.012);