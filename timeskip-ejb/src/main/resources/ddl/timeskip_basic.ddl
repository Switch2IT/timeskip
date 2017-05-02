-- Tables

CREATE TABLE IF NOT EXISTS config (
  id             BIGINT       NOT NULL,
  config_path    VARCHAR(255) NOT NULL,
  default_config BOOLEAN DEFAULT NULL
);

-- Unique indexes

CREATE UNIQUE INDEX uk_config_1
  ON config (default_config);

-- Primary keys

ALTER TABLE config
  ADD PRIMARY KEY (id);