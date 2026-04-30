package rabat.s1337.classes;

public class Car
{
	private String brand;
	private String color;
	private double price;

	public Car(){}
	
	public Car(String brand, String color, double price){
		this.brand = brand;
		this.color = color;
		this.price = price;
	}

	public String changeColor(String color){
		this.color = color;
		return color;
	}
	
	@Override
	public String toString() {
		return "Car {\n   brand: "+brand+"\n   color: "+color+ "\n    price: "+price+"\n    }";
	}
}
