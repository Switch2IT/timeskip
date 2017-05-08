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

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 7);

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 10);

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

ALTER TABLE config
  ADD COLUMN last_day_of_month BOOLEAN DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS mail_templates (
  topic   VARCHAR(255) NOT NULL,
  subject TEXT         NOT NULL,
  content TEXT         NOT NULL
);

ALTER TABLE mail_templates
  ADD PRIMARY KEY (topic);

INSERT INTO mail_templates (topic, subject, content) VALUES ('STARTUP', 'Timeskip API Server restart',
                                                             '<!doctype html><html><head> <meta name="viewport" content="width=device-width"> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> <title>Timeskip</title> <style>* { font-family: "Helvetica Neue", "Helvetica", Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6em; margin: 0; padding: 0 } img { max-width: 600px; width: 100% } body { -webkit-font-smoothing: antialiased; height: 100%; -webkit-text-size-adjust: none; width: 100% !important } a { color: #348eda } .btn-primary { Margin-bottom: 10px; width: auto !important } .btn-primary td { background-color: #348eda; border-radius: 25px; font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; font-size: 14px; text-align: center; vertical-align: top } .btn-primary td a { background-color: #348eda; border-radius: 25px; border-width: 10px 20px; border-style: solid; border-color: #348eda; display: inline-block; color: #fff; cursor: pointer; font-weight: bold; line-height: 2; text-decoration: none } .last { margin-bottom: 0 } .first { margin-top: 0 } .padding { padding: 10px 0 } table.body-wrap { padding: 20px; width: 100% } table.body-wrap .container { border: 1px solid #f0f0f0 } table.footer-wrap { clear: both !important; width: 100% } .footer-wrap .container p { color: #666; font-size: 12px } table.footer-wrap a { color: #999 } h1, h2, h3 { color: #111; font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; font-weight: 200; line-height: 1.2em; margin: 40px 0 10px } h1 { font-size: 36px } h2 { font-size: 28px } h3 { font-size: 22px } p, ul, ol { font-size: 14px; font-weight: normal; margin-bottom: 10px } ul li, ol li { margin-left: 5px; list-style-position: inside } .container { clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important } .body-wrap .container { padding: 20px } .content { display: block; margin: 0 auto; max-width: 600px } .content table { width: 100% }</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"> <tr> <td></td> <td class="container" bgcolor="#FFFFFF"> <div class="content"> <table> <tr> <td><h2>Hi,</h2> <p>This is a message to inform you that the Timeskip API server was restarted. No further action is necessary.</p> <table class="btn-primary" cellpadding="0" cellspacing="0" border="0"> <tr></tr> </table> </td> </tr> </table> </div> </td> <td></td> </tr></table></body></html>');

INSERT INTO mail_templates (topic, subject, content) VALUES
  ('CONFIRMATION_REMINDER', 'Timeskip - Worklog confirmation required',
   '<!doctype html><html><head> <meta name="viewport" content="width=device-width"> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> <title>Timeskip</title> <style>* { font-family: "Helvetica Neue", "Helvetica", Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6em; margin: 0; padding: 0 } img { max-width: 600px; width: 100% } body { -webkit-font-smoothing: antialiased; height: 100%; -webkit-text-size-adjust: none; width: 100% !important } a { color: #348eda } .btn-primary { Margin-bottom: 10px; width: auto !important } .btn-primary td { background-color: #348eda; border-radius: 25px; font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; font-size: 14px; text-align: center; vertical-align: top } .btn-primary td a { background-color: #348eda; border-radius: 25px; border-width: 10px 20px; border-style: solid; border-color: #348eda; display: inline-block; color: #fff; cursor: pointer; font-weight: bold; line-height: 2; text-decoration: none } .last { margin-bottom: 0 } .first { margin-top: 0 } .padding { padding: 10px 0 } table.body-wrap { padding: 20px; width: 100% } table.body-wrap .container { border: 1px solid #f0f0f0 } table.footer-wrap { clear: both !important; width: 100% } .footer-wrap .container p { color: #666; font-size: 12px } table.footer-wrap a { color: #999 } h1, h2, h3 { color: #111; font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; font-weight: 200; line-height: 1.2em; margin: 40px 0 10px } h1 { font-size: 36px } h2 { font-size: 28px } h3 { font-size: 22px } p, ul, ol { font-size: 14px; font-weight: normal; margin-bottom: 10px } ul li, ol li { margin-left: 5px; list-style-position: inside } .container { clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important } .body-wrap .container { padding: 20px } .content { display: block; margin: 0 auto; max-width: 600px } .content table { width: 100% }</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"> <tr> <td class="container" bgcolor="#FFFFFF"> <div class="content"><h2>Hi, {userName}</h2> <p>This is a reminder that you still have unconfirmed work logs. Please confirm or correct the following work logs:</p> <br> <p>{requiredWorklogConfirmations}</p> </div> </td> </tr> <tr> <td class="container" bgcolor="#FFFFFF"> <div class="content"><p>Greetings from the Timeskip team.</p></div> </td> </tr></table></body></html>');

ALTER TABLE paygrades
  ADD CONSTRAINT uk_paygrades_1 UNIQUE (name);