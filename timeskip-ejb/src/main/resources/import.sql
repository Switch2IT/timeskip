--
-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- You can use this file to load seed data into the database using SQL statements
-- insert into Registrant(id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212')

INSERT INTO config (id, config_path, default_config, day_of_monthly_reminder_email)
VALUES (1, '/opt/wildfly/standalone/configuration/application.conf', TRUE, 1);

INSERT INTO roles (id, auto_grant, description, name)
VALUES ('consultant', NULL, 'Automatically granted to a new user.', 'Consultant');

INSERT INTO roles (id, auto_grant, description, name) VALUES ('hr', NULL, 'HR Personnel.', 'HR');

INSERT INTO roles (id, auto_grant, description, name) VALUES ('manager', TRUE, 'Managers.', 'Manager');

-- PERMISSIONS

INSERT INTO permissions (role_id, permission) VALUES ('hr', 0);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 1);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 3);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 4);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 5);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 6);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 7);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 8);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 9);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 10);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 11);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 12);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 13);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 14);

INSERT INTO permissions (role_id, permission) VALUES ('hr', 15);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 0);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 1);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 2);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 3);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 4);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 5);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 6);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 7);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 8);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 9);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 10);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 11);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 12);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 13);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 14);

INSERT INTO permissions (role_id, permission) VALUES ('manager', 15);

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 0);

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 3);

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 7);

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 10);

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 12);

INSERT INTO permissions (role_id, permission) VALUES ('consultant', 14);

INSERT INTO mail_templates (topic, subject, content) VALUES
  ('STARTUP', 'Timeskip API Server restart',
   '<!doctype html><html><head> <meta name="viewport" content="width=device-width"> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> <title>Timeskip</title> <style>* { font-family: "Helvetica Neue", "Helvetica", Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6em; margin: 0; padding: 0 } img { max-width: 600px; width: 100% } body { -webkit-font-smoothing: antialiased; height: 100%; -webkit-text-size-adjust: none; width: 100% !important } a { color: #348eda } .btn-primary { Margin-bottom: 10px; width: auto !important } .btn-primary td { background-color: #348eda; border-radius: 25px; font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; font-size: 14px; text-align: center; vertical-align: top } .btn-primary td a { background-color: #348eda; border-radius: 25px; border-width: 10px 20px; border-style: solid; border-color: #348eda; display: inline-block; color: #fff; cursor: pointer; font-weight: bold; line-height: 2; text-decoration: none } .last { margin-bottom: 0 } .first { margin-top: 0 } .padding { padding: 10px 0 } table.body-wrap { padding: 20px; width: 100% } table.body-wrap .container { border: 1px solid #f0f0f0 } table.footer-wrap { clear: both !important; width: 100% } .footer-wrap .container p { color: #666; font-size: 12px } table.footer-wrap a { color: #999 } h1, h2, h3 { color: #111; font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; font-weight: 200; line-height: 1.2em; margin: 40px 0 10px } h1 { font-size: 36px } h2 { font-size: 28px } h3 { font-size: 22px } p, ul, ol { font-size: 14px; font-weight: normal; margin-bottom: 10px } ul li, ol li { margin-left: 5px; list-style-position: inside } .container { clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important } .body-wrap .container { padding: 20px } .content { display: block; margin: 0 auto; max-width: 600px } .content table { width: 100% }</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"> <tr> <td></td> <td class="container" bgcolor="#FFFFFF"> <div class="content"> <table> <tr> <td><h2>Hi,</h2> <p>This is a message to inform you that the Timeskip API server was restarted. No further action is necessary.</p> <table class="btn-primary" cellpadding="0" cellspacing="0" border="0"> <tr></tr> </table> </td> </tr> </table> </div> </td> <td></td> </tr></table></body></html>');

INSERT INTO mail_templates (topic, subject, content) VALUES
  ('CONFIRMATION_REMINDER', 'Timeskip - Worklog confirmation required',
   '<!doctype html><html><head><meta name="viewport" content="width=device-width"><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>Timeskip</title><style>*{font-family:"Helvetica Neue","Helvetica",Helvetica,Arial,sans-serif;font-size:100%;line-height:1.6em;margin:0;padding:0}img{max-width:600px;width:100%}body{-webkit-font-smoothing:antialiased;height:100%;-webkit-text-size-adjust:none;width:100% !important}a{color:#348eda}.btn-primary{Margin-bottom:10px;width:auto !important}.btn-primary td{background-color:#348eda;border-radius:25px;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-size:14px;text-align:center;vertical-align:top}.btn-primary td a{background-color:#348eda;border-radius:25px;border-width:10px 20px;border-style:solid;border-color:#348eda;display:inline-block;color:#fff;cursor:pointer;font-weight:bold;line-height:2;text-decoration:none}.last{margin-bottom:0}.first{margin-top:0}.padding{padding:10px 0}table.body-wrap{padding:20px;width:100%}table.body-wrap .container{border:1px solid #f0f0f0}table.footer-wrap{clear:both !important;width:100%}.footer-wrap .container p{color:#666;font-size:12px}table.footer-wrap a{color:#999}h1,h2,h3{color:#111;font-family:"Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;font-weight:200;line-height:1.2em;margin:40px 0 10px}h1{font-size:36px}h2{font-size:28px}h3{font-size:22px}p,ul,ol{font-size:14px;font-weight:normal;margin-bottom:10px}ul li, ol li{margin-left:5px;list-style-position:inside}.container{clear:both !important;display:block !important;Margin:0 auto !important;max-width:600px !important}.body-wrap .container{padding:20px}.content{display:block;margin:0 auto;max-width:600px}.content table{width:100%}</style></head><body bgcolor="#f6f6f6"><table class="body-wrap" bgcolor="#f6f6f6"><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><h2>Hi, {userName}</h2><p>This is a reminder that you still have unconfirmed work logs. Please confirm or correct the following work logs:</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>{requiredWorklogConfirmations}</p></div></td></tr><tr><td class="container" bgcolor="#FFFFFF"><div class="content"><p>Greetings from the Timeskip team.</p></div></td></tr></table></body></html>');