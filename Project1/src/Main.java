import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		System.out.println("1 - Create file");
		System.out.println("2 - Read file");
		int decision = reader.nextInt();
		
		if (decision == 1) {
			DeliveryInfo deliveryInfo = new DeliveryInfo();
			XMLParser parser = new XMLParser(deliveryInfo);
			parser.write();
		}
		else if (decision == 2){
			System.out.print("Document name: ");
			String file = reader.nextLine(); file = reader.nextLine();
			DeliveryInfo deliveryInfo = new DeliveryInfo(file);
			GUI gui = new GUI(deliveryInfo);
			gui.display();
		}
		
		reader.close();
	}
}
