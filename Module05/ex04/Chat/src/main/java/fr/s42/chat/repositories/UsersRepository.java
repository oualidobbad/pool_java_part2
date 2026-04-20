package fr.s42.chat.repositories;
import fr.s42.chat.models.User;
import java.util.List;

public interface UsersRepository {
	List<User> findAll(int page, int size);
	
}
