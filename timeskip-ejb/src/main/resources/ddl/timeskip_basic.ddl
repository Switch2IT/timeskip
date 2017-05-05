-- Tables
CREATE TABLE IF NOT EXISTS activities (
  id          BIGINT       NOT NULL,
  name        VARCHAR(255) NOT NULL,
  description TEXT DEFAULT NULL,
  project_id  BIGINT       NOT NULL,
  billable    BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS config (
  id             BIGINT       NOT NULL,
  config_path    VARCHAR(255) NOT NULL,
  default_config BOOLEAN DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS memberships
(
  id              BIGINT       NOT NULL,
  organization_id VARCHAR(255) NOT NULL,
  role_id         VARCHAR(255) NOT NULL,
  user_id         VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS organizations (
  id          VARCHAR(255) NOT NULL,
  name        VARCHAR(255) NOT NULL,
  description TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS paygrades (
  id          BIGINT       NOT NULL,
  name        VARCHAR(255) NOT NULL,
  description TEXT DEFAULT NULL,
  hourly_rate DOUBLE       NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions (
  role_id     VARCHAR(255) NOT NULL,
  permissions INT          NULL
);

CREATE TABLE IF NOT EXISTS project_assignments (
  user_id    VARCHAR(255) NOT NULL,
  project_id BIGINT       NOT NULL
);

CREATE TABLE IF NOT EXISTS projects (
  id              BIGINT       NOT NULL,
  name            VARCHAR(255) NOT NULL,
  description     TEXT    DEFAULT NULL,
  allow_overtime  BOOLEAN DEFAULT TRUE,
  bill_overtime   BOOLEAN DEFAULT TRUE,
  organization_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
  id          VARCHAR(255) NOT NULL,
  auto_grant  BOOLEAN      NULL,
  description TEXT         NULL,
  name        VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS users (
  id                    VARCHAR(255) NOT NULL,
  first_name            VARCHAR(255) NOT NULL,
  last_name             VARCHAR(255) NOT NULL,
  email                 VARCHAR(255) NOT NULL,
  admin                 BOOLEAN DEFAULT FALSE,
  paygrade_id           BIGINT  DEFAULT NULL,
  default_hours_per_day DOUBLE  DEFAULT 8,
  default_activity_id   BIGINT  DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS user_workdays (
  user_id  VARCHAR(255) NOT NULL,
  week_day VARCHAR(9)   NOT NULL
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

ALTER TABLE memberships
  ADD CONSTRAINT uk_organization_memberships_1
UNIQUE (user_id, role_id, organization_id);

ALTER TABLE activities
  ADD CONSTRAINT uk_activities_1 UNIQUE (project_id, name);

ALTER TABLE projects
  ADD CONSTRAINT uk_projects_1 UNIQUE (organization_id, name);

-- Indexes

CREATE INDEX idx_permissions_1
  ON permissions (role_id);

CREATE INDEX idx_users_1
  ON users (email);

-- Primary keys

ALTER TABLE activities
  ADD PRIMARY KEY (id);

ALTER TABLE config
  ADD PRIMARY KEY (id);

ALTER TABLE memberships
  ADD PRIMARY KEY (id);

ALTER TABLE organizations
  ADD PRIMARY KEY (id);

ALTER TABLE paygrades
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

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE;

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_2 FOREIGN KEY (role_id) REFERENCES roles (id)
  ON UPDATE CASCADE;

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_3 FOREIGN KEY (organization_id) REFERENCES organizations (id)
  ON UPDATE CASCADE;

ALTER TABLE permissions
  ADD CONSTRAINT fk_permissions_1 FOREIGN KEY (role_id) REFERENCES roles (id)
  ON UPDATE CASCADE;

ALTER TABLE project_assignments
  ADD CONSTRAINT fk_project_assignments_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE;

ALTER TABLE project_assignments
  ADD CONSTRAINT fk_project_assignments_2 FOREIGN KEY (project_id) REFERENCES projects (id)
  ON UPDATE CASCADE;

ALTER TABLE projects
  ADD CONSTRAINT fk_projects_1 FOREIGN KEY (organization_id) REFERENCES organizations (id)
  ON UPDATE CASCADE;

ALTER TABLE users
  ADD CONSTRAINT fk_users_1 FOREIGN KEY (paygrade_id) REFERENCES paygrades (id)
  ON UPDATE CASCADE;

ALTER TABLE users
  ADD CONSTRAINT fk_users_2 FOREIGN KEY (default_activity_id) REFERENCES activities (id)
  ON UPDATE CASCADE;

ALTER TABLE user_workdays
  ADD CONSTRAINT fk_user_workdays_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE;

ALTER TABLE worklogs
  ADD CONSTRAINT fk_worklogs_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE;

ALTER TABLE worklogs
  ADD CONSTRAINT fk_worklogs_2 FOREIGN KEY (activity_id) REFERENCES activities (id)
  ON UPDATE CASCADE;