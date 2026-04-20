package fr.s42.chat.app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.s42.chat.repositories.*;
import fr.s42.chat.models.*;

public class Program {
	public static void main(String[] args) 
	{
		// 1. Setup HikariCP DataSource (Adjust your credentials as needed!)
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://174.138.65.179:5432/app");
		config.setUsername("oobbad");
		config.setPassword("1234");
	
		// 2. Initialize the Repository
		try (HikariDataSource dataSource = new HikariDataSource(config)) {
			
			UsersRepositoryJdbcImpl repository = new UsersRepositoryJdbcImpl(dataSource);
			
			System.out.println("--- Fetching Page 0, Size 5 ---");
			
			// 3. Call your masterpiece!
			List<User> users = repository.findAll(0, 5);
			
			// 4. Print the results
			for (User user : users) {
				System.out.println("\n👤 User ID: " + user.getId() + " | Login: " + user.getLogin());
				
				System.out.println("   🏠 Created Rooms:");
				if (user.getCreatedRooms().isEmpty()) {
					System.out.println("      (None)");
				} else {
					for (Chatroom room : user.getCreatedRooms()) {
						System.out.println("      - [" + room.getId() + "] " + room.getName());
					}
				}
				
				System.out.println("   🌐 Social Rooms:");
				if (user.getSocializedRooms().isEmpty()) {
					System.out.println("      (None)");
				} else {
					for (Chatroom room : user.getSocializedRooms()) {
						System.out.println("      - [" + room.getId() + "] " + room.getName());
					}
				}
			}
			
			System.out.println("\n✅ Test Complete!");
			
		} catch (Exception e) {
			System.err.println("Test Failed: " + e.getMessage());
		}
	}
}

