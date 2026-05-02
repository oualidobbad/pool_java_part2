package classes;

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
	public void sold(){
		price -= 50.0;
		System.out.println(price);
	}
	@Override
	public String toString() {
		return "Car[brand='"+brand+"', color='"+color.toUpperCase()+ ", 'price="+price+"]";
	}
}
