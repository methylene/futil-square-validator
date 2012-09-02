package some.group;

public class SquareMethods {
	
	public static double area(int num, UnitOfLength unit) {
		double sideLength = unit.getMeters() * num;
		return Math.pow(sideLength, 2);
	}

}
