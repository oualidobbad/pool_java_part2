package rabat.s1337.classes;

public class User
{
	private String firstName;
	private String lastName;
	private int height;
	private int age;

	public User(){}

	public User(String firstName, String lastName, int height, int age){
		this.firstName = firstName;
		this.lastName = lastName;
		this.height = height;
		this.age = age;
	}

	public int grow(int value) {
		this.height += value;
		return height;
	}

	@Override
	public String toString() {
		return "User{\n   first name: "+firstName+"\n   last name: "+lastName+"\n    height: "+height+"\n    age: "+age+"\n    }";
	}
}
