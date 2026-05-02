package app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

// import rabat.s1337.classes.User;

public class Program {

	private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

	public static String nextLineHandled(Scanner sc) {
		if (!sc.hasNextLine()) {
			throw new RuntimeException("No more input");
		}
		return sc.nextLine();
	}



	public static void printingFieldsAndMethods(Class<?> clazz)
	{
		System.out.println("fields:");
		for (Field field : clazz.getDeclaredFields())
		{
			System.out.println("    "
				+field.getType().getSimpleName() + " "
				+field.getName()
			);
		}
		System.out.println("methods:");
		for (Method method : clazz.getDeclaredMethods())
		{
			if (method.getName().equals("toString"))
				continue;
			StringBuilder paramsStr = new StringBuilder("(");
			int i = 0;
			for (Class<?> params : method.getParameterTypes()) {
				if (i > 0)	
					paramsStr.append(", ");
				paramsStr.append(params.getSimpleName());
				i++;
			}
			paramsStr.append(")");

			System.out.println("    "
				+method.getReturnType().getSimpleName() + " "
				+method.getName() + " "
				+paramsStr.toString()+ ""
			);
		}
		System.out.println("---------------------");
	}

	public static Object creatObject(Scanner sc, Class<?> clazz) throws Exception{
		System.out.println("Let's create an object.");
		Object obj = clazz.getDeclaredConstructor().newInstance();
		for (Field field : clazz.getDeclaredFields())
		{
			System.out.println(field.getName());
			System.out.print("-> ");
			String input = nextLineHandled(sc);
			field.setAccessible(true);
			Class<?> type = field.getType();

			if (type == String.class)
				field.set(obj, input);
			else if (type == int.class)
				field.set(obj, Integer.parseInt(input));
			else if (type == double.class)
				field.set(obj, Double.parseDouble(input));
		}
		System.out.println("Object created: "+ obj);
		System.out.println("---------------------");
		return obj;
	}

	public static void updateObject(Scanner sc, Object obj, Class<?> clazz) throws Exception
	{
		System.out.println("Enter name of the field for changing:");
		System.out.print("-> ");
		String input =  nextLineHandled(sc);
		Field field = clazz.getDeclaredField(input);
		field.setAccessible(true);
		Class<?> type = field.getType();
		System.out.println("Enter "+type.getSimpleName()+ " value:");
		System.out.print("-> ");
		input =  nextLineHandled(sc);

		if (type == String.class)
			field.set(obj, input);
		else if (type == int.class)
			field.set(obj, Integer.parseInt(input));
		else if (type == double.class)
			field.set(obj, Double.parseDouble(input));
		System.out.println("Object updated: "+ obj);
		System.out.println("---------------------");
	}

	public static void callMethod(Scanner sc, Class<?> clazz, Object obj) throws Exception{
		System.out.println("Enter name of the method for call:");
		System.out.print("-> ");
		String input = nextLineHandled(sc);
		Class<?>[] paramTypes = new Class[0];
		int len = input.indexOf('(') == -1 ? input.length() : input.indexOf('(');
		String name = input.substring(0, len).trim();

		if (input.contains("(") && input.contains(")"))
		{
			String paramsStr = input.substring(input.indexOf('(')+1, input.indexOf(')'));
			String[] params = paramsStr.split(",");
			
			if (!params[0].isEmpty())
				paramTypes = new Class<?>[params.length];

			for (int i = 0; i < params.length; i++) 
			{
				if (params[i].trim().equals("String"))
					paramTypes[i] = String.class;
				else if (params[i].trim().equals("int"))	
					paramTypes[i] = int.class;
				else if (params[i].trim().equals("double"))
					paramTypes[i] = double.class;
			}
		}else if (input.contains("(") && !input.contains(")")) {
			throw new RuntimeException("must contain '(' and ')'");
		}
		else if (!input.contains("(") && input.contains(")")) {
			throw new RuntimeException("must contain '(' and ')'");
		}

		Method method = clazz.getDeclaredMethod(name, paramTypes);
		method.setAccessible(true);
		Object[] args = new Object[paramTypes.length];
		int i = 0;
		for (Class<?> param : paramTypes)
		{
			System.out.println("Enter "+param+" value:");
			System.out.print("-> ");
			input = nextLineHandled(sc);
			
			if (param == int.class || param == Integer.class) {
				args[i] = Integer.parseInt(input);
			} else if (param == double.class || param == Double.class) {
				args[i] = Double.parseDouble(input);
			} else if (param == String.class) {
				args[i] = input;
			} else {
				throw new IllegalArgumentException("Unsupported type: " + param);
			}
			i++;
		}
		Object result = method.invoke(obj, args);
		if (result != null)
		{
			System.out.println("Method returned:");
			System.out.println(result);
		}
		System.out.println("---------------------");
	}
	
	public static void main(String[] args) {
		
		try (Scanner sc = new Scanner(System.in)) {
			String input;
			while (true) {
				System.out.println("Classes:");
				System.out.println("  User");
				System.out.println("  Car");
				System.out.println("---------------------");
				System.out.println("Enter class name:");
				System.out.print("-> ");
				input = nextLineHandled(sc);
				System.out.println("---------------------");
				try 
				{
					Class<?> clazz = Class.forName("classes."+input);
					printingFieldsAndMethods(clazz);
					Object obj = creatObject(sc, clazz);
					updateObject(sc, obj, clazz);
					callMethod(sc, clazz, obj);


				} catch (ClassNotFoundException e) 
				{
					System.err.println(RED + e.getMessage() + ": class Not Found" + RESET);
					System.out.println("---------------------");
				}
			}
		} catch (Exception e) {
			
			System.out.println("\n"+e.getMessage());
		}
		
	}
}