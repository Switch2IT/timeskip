-- Tables

CREATE TABLE IF NOT EXISTS config (
  id             BIGINT       NOT NULL,
  config_path    VARCHAR(255) NOT NULL,
  default_config BOOLEAN DEFAULT NULL
);

CREATE TABLE memberships
(
  id BIGINT NOT NULL,
  organization_id VARCHAR(255),
  role_id VARCHAR(255),
  user_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS organizations (
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions (
  role_id VARCHAR(255) NOT NULL,
  permissions INT NULL
);

CREATE TABLE IF NOT EXISTS roles (
  id VARCHAR(255) NOT NULL,
  auto_grant BOOLEAN NULL,
  description VARCHAR(512) NULL,
  name VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) DEFAULT NULL,
  admin BOOLEAN DEFAULT FALSE
);

-- Unique indexes

ALTER TABLE config ADD CONSTRAINT uk_config_1
  UNIQUE (default_config);

ALTER TABLE memberships ADD CONSTRAINT uk_memberships_1
  UNIQUE (user_id, role_id, organization_id);

-- Indexes

CREATE INDEX idx_permissions_1
  ON permissions(role_id);

CREATE INDEX idx_memberships_1
  ON memberships (user_id);

CREATE INDEX idx_users_1
  ON users (email);

-- Primary keys

ALTER TABLE config
  ADD PRIMARY KEY (id);

ALTER TABLE memberships
  ADD PRIMARY KEY (id);

ALTER TABLE organizations
  ADD PRIMARY KEY (id);

ALTER TABLE roles
  ADD PRIMARY KEY (id);

ALTER TABLE users
  ADD PRIMARY KEY (id);

-- Foreign keys

ALTER TABLE permissions ADD CONSTRAINT fk_permissions_1 FOREIGN KEY (role_id) REFERENCES roles (id);
