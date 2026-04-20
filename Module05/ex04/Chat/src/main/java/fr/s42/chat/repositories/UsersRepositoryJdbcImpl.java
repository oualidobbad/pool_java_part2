package fr.s42.chat.repositories;
import fr.s42.chat.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class UsersRepositoryJdbcImpl implements UsersRepository {
	private final DataSource dataSource;
	
	public UsersRepositoryJdbcImpl(DataSource dataSource){
		this.dataSource = dataSource;
	}

	@Override
	public List<User> findAll(int page, int size){

		String SQL_QUERY = "WITH PaginatedUsers AS "+
		"(SELECT * FROM Users "+
		"ORDER BY user_id "+
		"LIMIT ? OFFSET ?) "+
		"SELECT "+
		"PaginatedUsers.user_id, PaginatedUsers.login, PaginatedUsers.password, "+
		"created_room.chatroom_id AS c_id, created_room.name AS c_name, social_room.chatroom_id AS s_id, social_room.name AS s_name "+
		"FROM PaginatedUsers "+
		"LEFT JOIN Chatroom AS created_room ON PaginatedUsers.user_id = created_room.owner_id "+
		"LEFT JOIN User_Chatrooms ON PaginatedUsers.user_id = User_Chatrooms.user_id "+
		"LEFT JOIN Chatroom AS social_room ON User_Chatrooms.chatroom_id = social_room.chatroom_id;";

		
		try (Connection con = dataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_QUERY))
		{
			ps.setLong(1, size);
			ps.setLong(2, size * page);

			ResultSet rs = ps.executeQuery();
			Map <Long, User> mapUsers = new LinkedHashMap<>();

			while (rs.next())
			{
				Long id = rs.getLong("user_id");
				if (!mapUsers.containsKey(id))
				{
					mapUsers.put(id, new User(id, rs.getString("login"), rs.getString("password"), new ArrayList<>(), new ArrayList<>()));
				}
				User targetUser = mapUsers.get(id);
				long c_id = rs.getLong("c_id");
				long s_id = rs.getLong("s_id");
				if (c_id != 0)
				{
					boolean isIn = targetUser.getCreatedRooms().stream().anyMatch(r -> r.getId() == c_id);
					if (!isIn)
						targetUser.getCreatedRooms().add(new Chatroom(c_id, rs.getString("c_name"), targetUser, new ArrayList<>()));
				}
				if (s_id != 0)
				{
					boolean isIn = targetUser.getSocializedRooms().stream().anyMatch(r -> r.getId() == s_id);
					if (!isIn)
						targetUser.getSocializedRooms().add(new Chatroom(s_id, rs.getString("s_name"), targetUser, new ArrayList<>()));
				}
			}
			List<User> listUser =  new ArrayList<>(mapUsers.values());
			return listUser;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}


// app=> WITH PaginatedUsers AS (
//     SELECT * FROM users
//     ORDER BY user_id
//     LIMIT 3 OFFSET 2
// )
// SELECT
//     PaginatedUsers.user_id,
//     PaginatedUsers.login,
//     PaginatedUsers.password,
//     created_rooms.chatroom_id  AS c_room_id,
//     created_rooms.name         AS c_room_name,
//     social_rooms.chatroom_id   AS s_room_id,
//     social_rooms.name          AS s_room_name
// FROM PaginatedUsers
// LEFT JOIN chatroom AS created_rooms
//     ON PaginatedUsers.user_id = created_rooms.owner_id
// LEFT JOIN User_Chatrooms
//     ON PaginatedUsers.user_id = User_Chatrooms.user_id
// LEFT JOIN chatroom AS social_rooms
//     ON User_Chatrooms.chatroom_id = social_rooms.chatroom_id
// app-> ;
//  user_id |  login  |  password  | c_room_id |  c_room_name  | s_room_id |  s_room_name   
// ---------+---------+------------+-----------+---------------+-----------+----------------
//        3 | charlie | charlie123 |         3 | 42 Network    |         1 | Java Beginners
//        3 | charlie | charlie123 |         3 | 42 Network    |         5 | Daily Standup
//        4 | diana   | diana123   |         4 | Movie Club    |         4 | Movie Club
//        5 | eric    | eric123    |         5 | Daily Standup |         5 | Daily Standup
// (4 rows)
