package fr.s42.chat.models;
import java.util.List;
import java.util.Objects;

public class User {
    private Long id;
    private String login;
    private String password;
    private List<Chatroom> createdRooms;
    private List<Chatroom> socializedRooms;
    
	public User(){}
	public User(Long id, String login, String password, List<Chatroom> createdRooms, List<Chatroom> socializedRooms){
		this.id = id;
		this.login = login;
		this.password = password;
		this.createdRooms = createdRooms;
		this.socializedRooms = socializedRooms;
	}

    // getters 
    public Long getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public List<Chatroom> getCreatedRooms() {
        return createdRooms;
    }
    public List<Chatroom> getSocializedRooms() {
        return socializedRooms;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setCreatedRooms(List<Chatroom> createdRooms) {
        this.createdRooms = createdRooms;
    }
    public void setSocializedRooms(List<Chatroom> socializedRooms) {
        this.socializedRooms = socializedRooms;
    }
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;
        
        User user = (User) obj;
        return Objects.equals(this.id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id = "+this.id+", login = "+this.login+", password = "+this.password;
    }
}
