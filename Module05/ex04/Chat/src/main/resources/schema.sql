CREATE TABLE Users(
    user_id SERIAL PRIMARY KEY,
    login VARCHAR(40) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL
);

CREATE TABLE Chatroom(
    chatroom_id SERIAL PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    owner_id INT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES Users(user_id)
);

CREATE TABLE Message(
    message_id SERIAL PRIMARY KEY,
    author_id INT NOT NULL,
    chatroom_id INT NOT NULL,
    text TEXT NOT NULL,
    date_time TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES Users(user_id),
    FOREIGN KEY (chatroom_id) REFERENCES Chatroom(chatroom_id)
);

CREATE TABLE User_Chatrooms(
	user_id INT NOT NULL,
	chatroom_id INT NOT NULL,
	PRIMARY KEY (user_id, chatroom_id),
	FOREIGN KEY (user_id) REFERENCES Users(user_id), 
	FOREIGN KEY (chatroom_id) REFERENCES Chatroom (chatroom_id)
);

-- String SQL_QUERY = 
--     "WITH PaginatedUsers AS (" +
--     "    SELECT * FROM users " +
--     "    ORDER BY user_id " +
--     "    LIMIT ? OFFSET ?" +
--     ") " +
--     "SELECT " +
--     "    u.user_id, u.login, u.password, " +
--     "    cr.chatroom_id AS cr_id, cr.name AS cr_name, " +
--     "    sr.chatroom_id AS sr_id, sr.name AS sr_name " +
--     "FROM PaginatedUsers u " +
--     "LEFT JOIN chatrooms cr ON u.user_id = cr.owner_id " +
--     "LEFT JOIN user_chatrooms uc ON u.user_id = uc.user_id " +
--     "LEFT JOIN chatrooms sr ON uc.chatroom_id = sr.chatroom_id;";

