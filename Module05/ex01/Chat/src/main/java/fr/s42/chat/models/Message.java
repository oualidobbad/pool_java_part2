package fr.s42.chat.models;
// import java.time.String;
import java.util.Objects;

public class Message {
    private Long id;
	private User author;
	private Chatroom room;
	private String text;
	private String dateTime;
	// getters
	public Long getId() {
		return id;
	}
	public User getAuthor() {
		return author;
	}
	public Chatroom getRoom() {
		return room;
	}
	public String getText() {
		return text;
	}
	public String getDateTime() {
		return dateTime;
	}

	//setters
	public void setId(Long id) {
		this.id = id;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public void setRoom(Chatroom room) {
		this.room = room;
	}
	public void setText(String text) {
		this.text = text;
	}public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public String toString() {
		return "message : {\n      id= "+id+",\n      author= "+author+",\n      room= "+room+",\n      text= "+text+",\n      date= " +dateTime+"\n     }";
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || obj.getClass() != getClass())
			return false;
		Message message = (Message)obj;
		return Objects.equals(message.id, this.id);
	}
}
