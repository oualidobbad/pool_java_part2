package fr.s42.chat.repositories;

import java.util.Optional;
import fr.s42.chat.models.*;

public interface MessagesRepository {
	Optional<Message> findById(Long id);
}
