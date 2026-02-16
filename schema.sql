-- Stock Trading Simulation Database Schema
-- Compatible with H2 and MySQL

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    credits DOUBLE NOT NULL DEFAULT 100000.0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Stocks Table
CREATE TABLE IF NOT EXISTS stocks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    symbol VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    current_price DOUBLE NOT NULL,
    open_price DOUBLE NOT NULL,
    high_price DOUBLE NOT NULL,
    low_price DOUBLE NOT NULL,
    close_price DOUBLE NOT NULL,
    volume BIGINT NOT NULL,
    sma DOUBLE,
    rsi DOUBLE,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    stock_id BIGINT NOT NULL,
    transaction_type VARCHAR(10) NOT NULL CHECK (transaction_type IN ('BUY', 'SELL')),
    quantity INTEGER NOT NULL,
    price_per_share DOUBLE NOT NULL,
    total_amount DOUBLE NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

-- Portfolio Table
CREATE TABLE IF NOT EXISTS portfolio (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    stock_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    average_buy_price DOUBLE NOT NULL DEFAULT 0.0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (stock_id) REFERENCES stocks(id),
    UNIQUE KEY unique_user_stock (user_id, stock_id)
);

-- Indexes for Performance
CREATE INDEX idx_transactions_user ON transactions(user_id);
CREATE INDEX idx_transactions_stock ON transactions(stock_id);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_portfolio_user ON portfolio(user_id);
CREATE INDEX idx_stocks_symbol ON stocks(symbol);
CREATE INDEX idx_users_username ON users(username);

-- Sample Data Insertion (Optional - DataLoader.java handles this)
-- INSERT INTO users (username, password, full_name, email, role, credits) VALUES
-- ('admin1', '$2a$10$...', 'Admin User 1', 'admin1@stock.com', 'ADMIN', 100000.0),
-- ('admin2', '$2a$10$...', 'Admin User 2', 'admin2@stock.com', 'ADMIN', 100000.0);
