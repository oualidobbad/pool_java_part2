package fr.s42.chat.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.s42.chat.repositories.*;
import fr.s42.chat.models.*;

public class Program {
	public static void main(String[] args) {
		try{
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl("jdbc:postgresql://174.138.65.179:5432/app");
			config.setUsername("oobbad");
			config.setPassword("1234");
			try (HikariDataSource dataSource = new HikariDataSource(config);)
			{
				MessagesRepository mRepository = new MessagesRepositoryJdbcImpl(dataSource);
				User creator = new User(2L, "bob", "bob123", new ArrayList(), new ArrayList());
				User author = creator;
				Chatroom room = new Chatroom(2L, "PostgreSQL Help", creator, new ArrayList());
				Message message = new Message(null, author, room, "Hello!", LocalDateTime.now());
				mRepository.save(message);
				System.out.println(message.getId()); // ex. id == 11
			}
		}catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

}

