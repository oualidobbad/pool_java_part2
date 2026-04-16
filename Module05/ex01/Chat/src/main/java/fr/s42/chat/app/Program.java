package fr.s42.chat.app;

import java.util.Optional;
import java.util.Scanner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.s42.chat.repositories.*;
import fr.s42.chat.models.*;

public class Program {
	public static void main(String[] args) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/chatdb");
		config.setUsername("oobbad");
		config.setPassword("1234");
		HikariDataSource dataSource = new HikariDataSource(config);
		MessagesRepository mRepository = new MessagesRepositoryJdbcImpl(dataSource);
		Scanner sc = new Scanner(System.in);
		Long id = sc.nextLong();

		Optional<Message> msg = mRepository.findById(id);
		if (msg.isPresent())
			System.out.println(msg.get());
		else
			System.out.println("not found");
		dataSource.close();
	}
}

