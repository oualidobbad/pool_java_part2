INSERT INTO users (login, password)
VALUES 
('alice', 'alice123'),
('bob', 'bob123'),
('charlie', 'charlie123'),
('diana', 'diana123'),
('eric', 'eric123');

INSERT INTO chatroom (name, owner_id)
VALUES
('Java Beginners', 1),
('PostgreSQL Help', 2),
('42 Network', 3),
('Movie Club', 4),
('Daily Standup', 5);

INSERT INTO message (author_id, chatroom_id, text, date_time)
VALUES
(1, 1, 'Hi everyone, welcome to Java room!', '2026-04-12 10:00:00'),
(2, 2, 'Does anyone know JDBC setup?', '2026-04-12 10:05:00'),
(3, 3, 'Pool progress check?', '2026-04-12 10:10:00'),
(4, 4, 'Tonight movie suggestion: Inception', '2026-04-12 10:15:00'),
(5, 5, 'Standup in 10 minutes', '2026-04-12 10:20:00');

INSERT INTO user_chatrooms (user_id, chatroom_id)
VALUES
(1, 1),
(2, 1),
(3, 1),
(2, 2),
(1, 2),
(4, 4),
(5, 5),
(3, 5);