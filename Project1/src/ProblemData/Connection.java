package ProblemData;

public class Connection implements Comparable<Connection> {
	private double m;
	private double b;
	private Location location1;
	private Location location2;
	private double fuel;
	
	public Connection() {}
	
	public Connection(Location location1, Location location2) {
		this.location1 = location1;
		this.location2 = location2;
		if (location1.getX() == location2.getX()) {
			this.m = Integer.MAX_VALUE;
		}
		else {
			this.m = (location2.getY()-location1.getY())/(double)(location2.getX()-location1.getX());
		}
		this.b = location1.getY()-m*location1.getX();
	}
	
	public Connection(Location location1, Location location2, double fuel) {
		this(location1, location2);
		this.fuel = fuel;
	}
	
	public boolean intersects(Connection f) {		
		if (f.getM() == m) {
			return false;
		}

		double x, y;
		if (f.getM() == Integer.MAX_VALUE) {
			x = f.getLocation1().getX();
			y = m*x+b;
		}
		else if (m == Integer.MAX_VALUE) {
			x = location1.getX();
			y = f.getM()*x+f.getB();
		}
		else {
			x = (b-f.getB())/(f.getM()-m);
			y = m*x+b;
		}
		
		// Make sure the intersection point is between the two segments bounds
		if (betweenTwo(x, location1.getX(), location2.getX()) && betweenTwo(x, f.getLocation1().getX(), f.getLocation2().getX())) {
			if (betweenTwo(y, location1.getY(), location2.getY()) && betweenTwo(y, f.getLocation1().getY(), f.getLocation2().getY())) {
				// Make sure the intersection point isn't an anchor point in either
				if (!(compareDouble(x, location1.getX()) && compareDouble(y, location1.getY()))) {
					if (!(compareDouble(x, location2.getX()) && compareDouble(y, location2.getY()))) {
						if (!(compareDouble(x, f.getLocation1().getX()) && compareDouble(y, f.getLocation1().getY()))) {
							if (!(compareDouble(x, f.getLocation2().getX()) && compareDouble(y, f.getLocation2().getY()))) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}
	
	public boolean contains(Location l) {		
		if (m == Integer.MAX_VALUE && l.getX() == location1.getX() && betweenTwo(l.getY(), location1.getY(), location2.getY())) { //((l.getY() > location1.getY() && l.getY() < location2.getY()) || (l.getY() < location2.getY() && l.getY() > location2.getY()))) {
			return true;
		}
		
		if (compareDouble(l.getY(), m*l.getX()+b) && betweenTwo(l.getX(), location1.getX(), location2.getX())) {
			return true;
		}
		
		return false;
	}
	
	public double getM() {
		return m;
	}
	
	public double getB() {
		return b;
	}
	
	public Location getLocation1() {
		return location1;
	}
	
	public Location getLocation2() {
		return location2;
	}
	
	public double getFuel() {
		return fuel;
	}
	
	public String toString() {
		return location1.toString() + "->" + location2.toString();
	}
	
	public boolean betweenTwo(double test, double num1, double num2) {
		if (Math.abs(test-num1) < 0.001 || Math.abs(test-num2) < 0.001) {
			return true;
		}
		
		if ((num2 > test && test > num1) || (num1 > test && test > num2)) {
			return true;
		}
		
		return false;
	}
	
	public boolean compareDouble(double num1, double num2) {
		if (Math.abs(num1-num2) < 0.001) {
			return true;
		}
		
		return false;
	}
	
	public boolean equals(Connection connection) {
		return ((location1 == connection.getLocation1() && location2 == connection.getLocation2()) ||
				(location2 == connection.getLocation1() && location1 == connection.getLocation2()));
	}
	
	@Override
	public boolean equals(Object o){
		Connection connection = (Connection) o;
		return equals(connection);
	}
	
	@Override
	public int compareTo(Connection c) {
		if (this.equals(c)) {
			return 0;
		}
		else if (c.getFuel() > fuel) {
			return 1;
		}
		else {
			return -1;
		}
	}
	
}
