package fr.s42.chat.repositories;
import fr.s42.chat.models.User;

import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

public class UsersRepositoryJdbcImpl implements UsersRepository {
	private final DataSource dataSource;
	
	public UsersRepositoryJdbcImpl(DataSource dataSource){
		this.dataSource = dataSource;
	}

	@Override
	public List<User> findAll(int page, int size){

		
		return new LinkedList<>();
	}
}