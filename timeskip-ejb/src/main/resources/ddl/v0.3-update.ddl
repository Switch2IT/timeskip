ALTER TABLE permissions
  CHANGE permissions permission INTEGER;

INSERT INTO permissions (role_id, permission) VALUES ('hr', 12);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 13);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 14);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 15);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 12);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 13);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 14);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 15);

DELETE FROM permissions
WHERE role_id = 'consultant' AND permission = 6;

DELETE FROM permissions
WHERE role_id = 'consultant' AND permission = 9;

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 12);

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 14);

ALTER TABLE memberships
  MODIFY COLUMN id BIGINT AUTO_INCREMENT;
ALTER TABLE users
  DROP FOREIGN KEY fk_users_2;
ALTER TABLE worklogs
  DROP FOREIGN KEY fk_worklogs_2;
ALTER TABLE activities
  MODIFY COLUMN id BIGINT AUTO_INCREMENT;
ALTER TABLE config
  MODIFY COLUMN id BIGINT AUTO_INCREMENT;
ALTER TABLE users
  DROP FOREIGN KEY fk_users_1;
ALTER TABLE paygrades
  MODIFY COLUMN id BIGINT AUTO_INCREMENT;
ALTER TABLE activities
  DROP FOREIGN KEY fk_activities_1;
ALTER TABLE project_assignments
  DROP FOREIGN KEY fk_project_assignments_2;
ALTER TABLE projects
  MODIFY COLUMN id BIGINT AUTO_INCREMENT;
ALTER TABLE worklogs
  MODIFY COLUMN id BIGINT AUTO_INCREMENT;

ALTER TABLE users
  ADD CONSTRAINT fk_users_2 FOREIGN KEY (default_activity_id) REFERENCES activities (id)
  ON UPDATE CASCADE;
ALTER TABLE worklogs
  ADD CONSTRAINT fk_worklogs_2 FOREIGN KEY (activity_id) REFERENCES activities (id)
  ON UPDATE CASCADE;
ALTER TABLE users
  ADD CONSTRAINT fk_users_1 FOREIGN KEY (paygrade_id) REFERENCES paygrades (id)
  ON UPDATE CASCADE;
ALTER TABLE activities
  ADD CONSTRAINT fk_activities_1 FOREIGN KEY (project_id) REFERENCES projects (id)
  ON UPDATE CASCADE;
ALTER TABLE project_assignments
  ADD CONSTRAINT fk_project_assignments_2 FOREIGN KEY (project_id) REFERENCES projects (id)
  ON UPDATE CASCADE;

ALTER TABLE config
  ADD COLUMN day_of_monthly_reminder_email INTEGER DEFAULT 1;