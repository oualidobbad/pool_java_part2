package fr.s42.chat.repositories;
import fr.s42.chat.models.User;

public interface UsersRepository {
	List<User> findAll(int page, int size);
	
}
