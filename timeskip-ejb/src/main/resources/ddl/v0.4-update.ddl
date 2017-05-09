UPDATE mail_templates
SET
  content = '<!doctype html><html><head> <meta name="viewport" content="width=device-width"> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> <title>Timeskip</title> <style>* { font-family: "Helvetica Neue", "Helvetica", Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6em; margin: 0; padding: 0 } img { max-width: 600px; width: 100% } body { -webkit-font-smoothing: antialiased; height: 100%; -webkit-text-size-adjust: none; width: 100% !important } a { color: #348eda } .btn-primary { Margin-bottom: 10px; width: auto !important } .btn-primary td { background-color: #348eda; border-radius: 25px; font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; font-size: 14px; text-align: center; vertical-align: top } .btn-primary td a { background-color: #348eda; border-radius: 25px; border-width: 10px 20px; border-style: solid; border-color: #348eda; display: inline-block; color: #fff; cursor: pointer; font-weight: bold; line-height: 2; text-decoration: none } .last { margin-bottom: 0 } .first { margin-top: 0 } .padding { padding: 10px 0 } table.body-wrap { padding: 20px; width: 100% } table.body-wrap .container { border: 1px solid #f0f0f0 } table.footer-wrap { clear: both !important; width: 100% } .footer-wrap .container p { color: #666; font-size: 12px } table.footer-wrap a { color: #999 } h1, h2, h3 { color: #111; font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; font-weight: 200; line-height: 1.2em; margin: 40px 0 10px } h1 { font-size: 36px } h2 { font-size: 28px } h3 { font-size: 22px } p, ul, ol { font-size: 14px; font-weight: normal; margin-bottom: 10px } ul li, ol li { margin-left: 5px; list-style-position: inside } .container { clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important } .body-wrap .container { padding: 20px } .content { display: block; margin: 0 auto; max-width: 600px } .content table { width: 100% }</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"> <tr> <td></td> <td class="container" bgcolor="#FFFFFF"> <div class="content"> <table> <tr> <td><h2>Hi,</h2> <p>This is a message to inform you that the Timeskip API server was restarted. No further action is necessary.</p> <table class="btn-primary" cellpadding="0" cellspacing="0" border="0"> <tr></tr> </table> </td> </tr> </table> </div> </td> <td></td> </tr></table></body></html>'
WHERE topic = 'STARTUP';

UPDATE mail_templates
SET
  content = '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>Timeskip</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi, {userName}</h2><p>This is a reminder that you still have unconfirmed work logs. Please confirm or correct the following work logs:</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>{requiredWorklogConfirmations}</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the Timeskip team.</p></div></td></tr></table></body></html>'
WHERE topic = 'CONFIRMATION_REMINDER';

ALTER TABLE activities
  DROP FOREIGN KEY fk_activities_1;

ALTER TABLE activities
  ADD CONSTRAINT fk_activities_1 FOREIGN KEY (project_id) REFERENCES projects (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE memberships
  DROP FOREIGN KEY fk_memberships_1;

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE memberships
  DROP FOREIGN KEY fk_memberships_2;

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_2 FOREIGN KEY (role_id) REFERENCES roles (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE memberships
  DROP FOREIGN KEY fk_memberships_3;

ALTER TABLE memberships
  ADD CONSTRAINT fk_memberships_3 FOREIGN KEY (organization_id) REFERENCES organizations (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE permissions
  DROP FOREIGN KEY fk_permissions_1;

ALTER TABLE permissions
  ADD CONSTRAINT fk_permissions_1 FOREIGN KEY (role_id) REFERENCES roles (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE project_assignments
  DROP FOREIGN KEY fk_project_assignments_1;

ALTER TABLE project_assignments
  ADD CONSTRAINT fk_project_assignments_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE project_assignments
  DROP FOREIGN KEY fk_project_assignments_2;

ALTER TABLE project_assignments
  ADD CONSTRAINT fk_project_assignments_2 FOREIGN KEY (project_id) REFERENCES projects (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE projects
  DROP FOREIGN KEY fk_projects_1;

ALTER TABLE projects
  ADD CONSTRAINT fk_projects_1 FOREIGN KEY (organization_id) REFERENCES organizations (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE users
  DROP FOREIGN KEY fk_users_1;

ALTER TABLE users
  ADD CONSTRAINT fk_users_1 FOREIGN KEY (paygrade_id) REFERENCES paygrades (id)
  ON UPDATE CASCADE
  ON DELETE SET NULL;

ALTER TABLE users
  DROP FOREIGN KEY fk_users_2;

ALTER TABLE users
  ADD CONSTRAINT fk_users_2 FOREIGN KEY (default_activity_id) REFERENCES activities (id)
  ON UPDATE CASCADE
  ON DELETE SET NULL;

ALTER TABLE user_workdays
  DROP FOREIGN KEY fk_user_workdays_1;

ALTER TABLE user_workdays
  ADD CONSTRAINT fk_user_workdays_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE worklogs
  DROP FOREIGN KEY fk_worklogs_1;

ALTER TABLE worklogs
  ADD CONSTRAINT fk_worklogs_1 FOREIGN KEY (user_id) REFERENCES users (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE worklogs
  DROP FOREIGN KEY fk_worklogs_2;

ALTER TABLE worklogs
  ADD CONSTRAINT fk_worklogs_2 FOREIGN KEY (activity_id) REFERENCES activities (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE user_workdays
  ADD CONSTRAINT uk_user_workdays_1 UNIQUE (user_id, week_day);

ALTER TABLE permissions
  ADD CONSTRAINT uk_permissions_1 UNIQUE (role_id, permission);