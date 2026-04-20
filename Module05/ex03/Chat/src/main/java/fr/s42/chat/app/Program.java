package fr.s42.chat.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
				MessagesRepository mr = new MessagesRepositoryJdbcImpl(dataSource);
				
				Optional<Message> messageOptional = mr.findById(3L);
				if (messageOptional.isPresent()) {
					Message message = messageOptional.get();
					message.setText("Bye");
					message.setDateTime(null);
					mr.update(message);
				}
				else
					System.err.println("invalid: Id Of Message Not Found");
			}
		}catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}

