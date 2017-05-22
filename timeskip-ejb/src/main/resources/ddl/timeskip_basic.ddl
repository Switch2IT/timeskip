CREATE TABLE activities
(
  id          BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  name        VARCHAR(255)           NOT NULL,
  description TEXT                   NULL,
  project_id  BIGINT                 NOT NULL,
  billable    TINYINT(1) DEFAULT '1' NULL,
  CONSTRAINT uk_activities_1
  UNIQUE (project_id, name)
);

CREATE TABLE config
(
  id                            BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  config_path                   VARCHAR(255)           NOT NULL,
  default_config                TINYINT(1)             NULL,
  day_of_monthly_reminder_email INT                    NULL,
  last_day_of_month             TINYINT(1) DEFAULT '0' NULL,
  CONSTRAINT uk_config_1
  UNIQUE (default_config)
);

CREATE TABLE mail_templates
(
  topic   VARCHAR(255) NOT NULL
    PRIMARY KEY,
  subject TEXT         NOT NULL,
  content TEXT         NOT NULL
);

CREATE TABLE memberships
(
  id              BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  organization_id VARCHAR(255) NOT NULL,
  role_id         VARCHAR(255) NOT NULL,
  user_id         VARCHAR(255) NOT NULL,
  CONSTRAINT uk_organization_memberships_1
  UNIQUE (user_id, role_id, organization_id)
);

CREATE INDEX fk_memberships_2
  ON memberships (role_id);

CREATE INDEX fk_memberships_3
  ON memberships (organization_id);

CREATE TABLE organizations
(
  id          VARCHAR(255) NOT NULL
    PRIMARY KEY,
  name        VARCHAR(255) NOT NULL,
  description TEXT         NULL,
  CONSTRAINT uk_organizations_1
  UNIQUE (name)
);

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_3
FOREIGN KEY (organization_id) REFERENCES timeskip.organizations (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

CREATE TABLE paygrades
(
  id          BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  name        VARCHAR(255) NOT NULL,
  description TEXT         NULL,
  hourly_rate DOUBLE       NOT NULL,
  CONSTRAINT uk_paygrades_1
  UNIQUE (name)
);

CREATE TABLE permissions
(
  role_id    VARCHAR(255) NOT NULL,
  permission INT          NULL,
  CONSTRAINT uk_permissions_1
  UNIQUE (role_id, permission)
);

CREATE INDEX idx_permissions_1
  ON permissions (role_id);

CREATE TABLE project_assignments
(
  user_id    VARCHAR(255) NOT NULL,
  project_id BIGINT       NOT NULL
);

CREATE INDEX fk_project_assignments_1
  ON project_assignments (user_id);

CREATE INDEX fk_project_assignments_2
  ON project_assignments (project_id);

CREATE TABLE projects
(
  id              BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  name            VARCHAR(255)           NOT NULL,
  description     TEXT                   NULL,
  allow_overtime  TINYINT(1) DEFAULT '1' NULL,
  bill_overtime   TINYINT(1) DEFAULT '1' NULL,
  organization_id VARCHAR(255)           NOT NULL,
  CONSTRAINT uk_projects_1
  UNIQUE (organization_id, name),
  CONSTRAINT fk_projects_1
  FOREIGN KEY (organization_id) REFERENCES timeskip.organizations (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

ALTER TABLE activities
  ADD CONSTRAINT fk_activities_1
FOREIGN KEY (project_id) REFERENCES timeskip.projects (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE project_assignments
  ADD CONSTRAINT fk_project_assignments_2
FOREIGN KEY (project_id) REFERENCES timeskip.projects (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

CREATE TABLE roles
(
  id          VARCHAR(255) NOT NULL
    PRIMARY KEY,
  auto_grant  TINYINT(1)   NULL,
  description TEXT         NULL,
  name        VARCHAR(255) NULL
);

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_2
FOREIGN KEY (role_id) REFERENCES timeskip.roles (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE permissions
  ADD CONSTRAINT fk_permissions_1
FOREIGN KEY (role_id) REFERENCES timeskip.roles (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

CREATE TABLE user_workdays
(
  user_id  VARCHAR(255) NOT NULL,
  week_day VARCHAR(9)   NOT NULL,
  CONSTRAINT uk_user_workdays_1
  UNIQUE (user_id, week_day)
);

CREATE TABLE users
(
  id                    VARCHAR(255)           NOT NULL
    PRIMARY KEY,
  first_name            VARCHAR(255)           NOT NULL,
  last_name             VARCHAR(255)           NOT NULL,
  email                 VARCHAR(255)           NOT NULL,
  admin                 TINYINT(1) DEFAULT '0' NULL,
  paygrade_id           BIGINT                 NULL,
  default_hours_per_day DOUBLE DEFAULT '8'     NULL,
  default_activity_id   BIGINT                 NULL,
  CONSTRAINT fk_users_1
  FOREIGN KEY (paygrade_id) REFERENCES timeskip.paygrades (id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);

CREATE INDEX fk_users_1
  ON users (paygrade_id);

CREATE INDEX fk_users_2
  ON users (default_activity_id);

CREATE INDEX idx_users_1
  ON users (email);

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_1
FOREIGN KEY (user_id) REFERENCES timeskip.users (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE project_assignments
  ADD CONSTRAINT fk_project_assignments_1
FOREIGN KEY (user_id) REFERENCES timeskip.users (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE user_workdays
  ADD CONSTRAINT fk_user_workdays_1
FOREIGN KEY (user_id) REFERENCES timeskip.users (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

CREATE TABLE worklogs
(
  id             BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  user_id        VARCHAR(255)           NOT NULL,
  activity_id    BIGINT                 NOT NULL,
  day            DATE                   NOT NULL,
  logged_minutes BIGINT                 NOT NULL,
  confirmed      TINYINT(1) DEFAULT '0' NULL,
  CONSTRAINT fk_worklogs_1
  FOREIGN KEY (user_id) REFERENCES timeskip.users (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  CONSTRAINT fk_worklogs_2
  FOREIGN KEY (activity_id) REFERENCES timeskip.activities (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE INDEX fk_worklogs_1
  ON worklogs (user_id);

CREATE INDEX fk_worklogs_2
  ON worklogs (activity_id);

