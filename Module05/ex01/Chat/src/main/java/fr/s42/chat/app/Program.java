package fr.s42.chat.app;

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
			HikariDataSource dataSource = new HikariDataSource(config);
			MessagesRepository mRepository = new MessagesRepositoryJdbcImpl(dataSource);
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter a message ID:");
			System.out.print("-> ");
			Long id = sc.nextLong();
	
			Optional<Message> msg = mRepository.findById(id);
			if (msg.isPresent())
				System.out.println(msg.get());
			else
				System.out.println("not found");
			dataSource.close();

		}catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

}

