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

INSERT INTO config VALUES (1, '/opt/wildfly/standalone/configuration/application.conf', TRUE);

INSERT INTO roles (id, auto_grant, description, name)
VALUES ('consultant', NULL, 'Automatically granted to a new user.', 'Consultant');

INSERT INTO roles (id, auto_grant, description, name) VALUES ('hr', NULL, 'HR Personnel.', 'HR');

INSERT INTO roles (id, auto_grant, description, name) VALUES ('manager', TRUE, 'Managers.', 'Manager');

-- PERMISSIONS

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 1);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 2);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 4);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 5);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 7);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 8);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 10);

INSERT INTO permissions (role_id, permissions) VALUES ('hr', 11);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 1);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 2);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 4);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 5);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 7);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 8);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 9);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 10);

INSERT INTO permissions (role_id, permissions) VALUES ('manager', 11);

INSERT INTO permissions (role_id, permissions) VALUES ('consultant', 0);

INSERT INTO permissions (role_id, permissions) VALUES ('consultant', 3);

INSERT INTO permissions (role_id, permissions) VALUES ('consultant', 6);

INSERT INTO permissions (role_id, permissions) VALUES ('consultant', 9);