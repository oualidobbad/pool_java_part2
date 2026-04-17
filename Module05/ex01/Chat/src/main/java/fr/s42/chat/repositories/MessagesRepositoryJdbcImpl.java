package fr.s42.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import fr.s42.chat.models.*;
import fr.s42.chat.exceptions.*;

import javax.sql.DataSource;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
	private final DataSource dataSource;

	public MessagesRepositoryJdbcImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Optional<Message> findById(Long id) {

		String SQL_QUERY = "SELECT * FROM message JOIN users ON message.author_id = users.user_id INNER JOIN chatroom ON message.chatroom_id = chatroom.chatroom_id WHERE message.message_id = ?;";

		try (Connection con = dataSource.getConnection();
				PreparedStatement ps = con.prepareStatement(SQL_QUERY)) {
			ps.setLong(1, id);
			try (ResultSet res = ps.executeQuery();) {
				if (res.next()) {
					// 1. Build the User (Author)
					User user = new User();
					user.setId(res.getLong("user_id"));
					user.setLogin(res.getString("login"));
					user.setPassword(res.getString("password"));

					// 2. Build the Chatroom
					Chatroom room = new Chatroom();
					room.setId(res.getLong("chatroom_id"));
					room.setName(res.getString("name"));

					// 3. Build the Message
					Message message = new Message();
					message.setId(res.getLong("message_id"));
					message.setAuthor(user);
					message.setRoom(room); // Fixed: We attached the room!

					// Check your DB schema: is the column named "text" or "message"?
					message.setText(res.getString("text"));
					// Fixed: Get the Timestamp and convert it to Java's LocalDateTime

					DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

					LocalDateTime dt = LocalDateTime.parse(res.getString("date_time"), dbFormat);
					message.setDateTime(dt.format(outputFormat));

					// 4. Return the finished product!
					return Optional.of(message);
				} else
					System.out.println("the ID doesn't exist");

			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return Optional.empty();
	}
}
