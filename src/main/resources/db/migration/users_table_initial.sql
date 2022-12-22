CREATE TABLE IF NOT EXISTS tg_user (
    telegram_id INTEGER PRIMARY KEY,
    request_count INTEGER DEFAULT 0
);