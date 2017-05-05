-- Tables

CREATE TABLE IF NOT EXISTS activities (
  id          BIGINT       NOT NULL,
  name        VARCHAR(255) NOT NULL,
  description TEXT DEFAULT NULL,
  project_id  VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS activity_assignments (
  id          BIGINT       NOT NULL,
  user_id     VARCHAR(255) NOT NULL,
  activity_id BIGINT       NOT NULL
);

CREATE TABLE IF NOT EXISTS config (
  id             BIGINT       NOT NULL,
  config_path    VARCHAR(255) NOT NULL,
  default_config BOOLEAN DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS organization_memberships
(
  id              BIGINT NOT NULL,
  organization_id VARCHAR(255),
  role_id         VARCHAR(255),
  user_id         VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS organizations (
  id   VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions (
  role_id     VARCHAR(255) NOT NULL,
  permissions INT          NULL
);

CREATE TABLE IF NOT EXISTS projects (
  id          VARCHAR(255) NOT NULL,
  name        VARCHAR(255) NOT NULL,
  description VARCHAR(4096) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS project_organizations (
  project_id      VARCHAR(255) NOT NULL,
  organization_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
  id          VARCHAR(255) NOT NULL,
  auto_grant  BOOLEAN      NULL,
  description VARCHAR(512) NULL,
  name        VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS users (
  id        VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  email     VARCHAR(255) DEFAULT NULL,
  admin     BOOLEAN      DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS worklogs (
  id             BIGINT       NOT NULL,
  user_id        VARCHAR(255) NOT NULL,
  activity_id    BIGINT       NOT NULL,
  day            DATE         NOT NULL,
  logged_minutes BIGINT       NOT NULL,
  confirmed      BOOLEAN      DEFAULT FALSE
);

-- Unique indexes

ALTER TABLE config
  ADD CONSTRAINT uk_config_1
UNIQUE (default_config);

ALTER TABLE organization_memberships
  ADD CONSTRAINT uk_organization_memberships_1
UNIQUE (user_id, role_id, organization_id);

ALTER TABLE project_organizations
  ADD CONSTRAINT uk_project_organizations_1
UNIQUE (project_id, organization_id);

ALTER TABLE activities
  ADD CONSTRAINT uk_activities_1 UNIQUE (name, project_id);

-- Indexes

CREATE INDEX idx_activities_1
  ON activities (project_id);

CREATE INDEX idx_activity_assignments_1
  ON activity_assignments (user_id);

CREATE INDEX idx_activity_assignments_2
  ON activity_assignments (activity_id);

CREATE INDEX idx_permissions_1
  ON permissions (role_id);

CREATE INDEX idx_organization_memberships_1
  ON organization_memberships (user_id);

CREATE INDEX idx_users_1
  ON users (email);

CREATE INDEX idx_project_organizations_1
  ON project_organizations (project_id);

CREATE INDEX idx_project_organizations_2
  ON project_organizations (organization_id);

CREATE INDEX idx_worklogs_1
  ON worklogs (user_id);

CREATE INDEX idx_worklogs_2
  ON worklogs (activity_id);

-- Primary keys

ALTER TABLE activities
  ADD PRIMARY KEY (id);

ALTER TABLE activity_assignments
  ADD PRIMARY KEY (id);

ALTER TABLE config
  ADD PRIMARY KEY (id);

ALTER TABLE organization_memberships
  ADD PRIMARY KEY (id);

ALTER TABLE organizations
  ADD PRIMARY KEY (id);

ALTER TABLE projects
  ADD PRIMARY KEY (id);

ALTER TABLE roles
  ADD PRIMARY KEY (id);

ALTER TABLE users
  ADD PRIMARY KEY (id);

ALTER TABLE worklogs
  ADD PRIMARY KEY (id);

-- Foreign keys

ALTER TABLE activities
  ADD CONSTRAINT fk_activities_1 FOREIGN KEY (project_id) REFERENCES projects (id)
  ON UPDATE CASCADE;

ALTER TABLE activity_assignments
  ADD CONSTRAINT fk_activity_assignments_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE;

ALTER TABLE activity_assignments
  ADD CONSTRAINT fk_activity_assignments_2 FOREIGN KEY (activity_id) REFERENCES activities (id)
  ON UPDATE CASCADE;

ALTER TABLE permissions
  ADD CONSTRAINT fk_permissions_1 FOREIGN KEY (role_id) REFERENCES roles (id)
  ON UPDATE CASCADE;

ALTER TABLE project_organizations
  ADD CONSTRAINT fk_project_organizations_1 FOREIGN KEY (project_id) REFERENCES projects (id)
  ON UPDATE CASCADE;

ALTER TABLE project_organizations
  ADD CONSTRAINT fk_project_organizations_2 FOREIGN KEY (organization_id) REFERENCES organizations (id)
  ON UPDATE CASCADE;

ALTER TABLE worklogs
  ADD CONSTRAINT fk_worklogs_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE;

ALTER TABLE worklogs
  ADD CONSTRAINT fk_worklogs_2 FOREIGN KEY (activity_id) REFERENCES activities (id)
  ON UPDATE CASCADE;
