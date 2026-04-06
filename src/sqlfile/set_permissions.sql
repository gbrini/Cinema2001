INSERT INTO role (role_id, role_name)
VALUES (1, 'ADMIN'), (2, 'EMPLOYEE'), (3, 'USER');


INSERT INTO permission (permission_id, permission_name, permission_description)
VALUES (1, 'movie:add', ''), (2, 'movie:edit', ''), (3, 'movie:delete', ''), (4, 'movie:view', ''),
(5, 'screen:add', ''), (6, 'screen:edit', ''), (7, 'screen:delete', ''), (8, 'screen:view', ''),
(9, 'screening:add', ''), (10, 'screening:edit', ''), (11, 'screening:delete', ''), (12, 'screening:view', ''),
(13, 'seat:add', ''), (14, 'seat:edit', ''), (15, 'seat:delete', ''), (16, 'seat:view', ''),
(17, 'ticket:view'), (18, 'ticket:buy'), (19, 'ticket:cancel');

INSERT INTO role_permission(role_id, permission_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17);

INSERT INTO role_permission(role_id, permission_id)
VALUES (2, 1), (2, 2), (2, 3), (2, 4), (2, 6), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14), (2, 15), (2, 16);

INSERT INTO role_permission(role_id, permission_id)
VALUES (3, 4), (3, 12), (3, 16),
       (3, 17), (3, 18), (3, 19);

INSERT INTO users (
    user_id,
    first_name,
    last_name,
    email,
	password,
	birth_date,
	role_id
)
VALUES (1, 'Admin', 'cognome', 'admin@me.com', '', '1999-10-11', 1),
(2, 'Operatore', 'cognome', 'employee@me.com', '', '1999-10-11', 2),
(3, 'Utente', 'cognome', 'user@me.com', '', '1999-10-11', 3);