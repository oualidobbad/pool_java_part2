package fr.s42.chat.repositories;

import java.util.Optional;
import fr.s42.chat.models.Message;

public interface MessagesRepository {
	Optional<Message> findById(Long id);
	void save(Message message);
	void update(Message message);
}
// String SQL_QUERY = "UPDTAE message SET author_id = ?, chatroom_id = ?, text = ?,  date_time = ? WHERE message_id = ?";