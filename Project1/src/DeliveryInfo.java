import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

// XML parser
import org.w3c.dom.*;

// Graph display
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class DeliveryInfo {
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		System.out.println("1 - Create file");
		System.out.println("2 - Read file");
		int decision = reader.nextInt();
		
		if (decision == 1) {
			DeliveryInfo deliveryInfo = new DeliveryInfo();
			deliveryInfo.createDocument();
		}
		else if (decision == 2){
			System.out.print("Document name: ");
			String file = reader.nextLine(); file = reader.nextLine();
			DeliveryInfo deliveryInfo = new DeliveryInfo(file);
			deliveryInfo.displayGraph();
		}
		
		reader.close();
	}
	
	/*=======================================================================================*/
	/* Paramenters and constructors */
	
	private Truck truck;
	private HashMap<Integer, Location> locations;
	private ArrayList<Package> packages;
	
	public DeliveryInfo() {
		locations = new HashMap<Integer,Location>();
		packages = new ArrayList<Package>();
		parseDocument("data1");
	}
	
	public DeliveryInfo(String docName) {
		locations = new HashMap<Integer,Location>();
		packages = new ArrayList<Package>();
		parseDocument(docName);
	}
	
	/*=======================================================================================*/
	/* Gets */
	
	public Truck getTruck() {
		return truck;
	}
	
	public HashMap<Integer, Location> getLocations() {
		return locations;
	}
	
	public ArrayList<Package> getDeliveries() {
		return packages;
	}
	
	/*=======================================================================================*/
	/* Print data */
	
	public void printTruck() {
		System.out.println("----Truck----");
		truck.print();
	}
	
	public void printLocations() {
		System.out.println("----Locations----");
		for (int key: locations.keySet()) {
			locations.get(key).print();
		}
	}
	
	public void printPackages() {
		System.out.println("----Packages----");
		for (int i = 0; i < packages.size(); i++) {
			packages.get(i).print();
		}
	}
	
	/*=======================================================================================*/
	/* XML data documents */
	
	public void parseDocument(String docName) {
		try {
			File inputFile = new File("data/" + docName + ".xml");
			
			// Create a DocumentBuilder
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			// Create a Document from a file or stream
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			// Get locations
			NodeList nList = doc.getElementsByTagName("location");
			for (int i = 0; i < nList.getLength(); i++) {
				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nList.item(i);
				int id = Integer.parseInt(eElement.getAttribute("id"));
				int x = Integer.parseInt(eElement.getAttribute("x"));
				int y = Integer.parseInt(eElement.getAttribute("y"));
				boolean fuel = Boolean.parseBoolean(eElement.getAttribute("fuel"));
				
				Location location = new Location(id, x, y, fuel);
				locations.put(id, location);
			}
			
			// Get connections
			nList = doc.getElementsByTagName("connection");
			for (int i = 0; i < nList.getLength(); i++) {
				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nList.item(i);
				int location1 = Integer.parseInt(eElement.getAttribute("location1"));
				int location2 = Integer.parseInt(eElement.getAttribute("location2"));
				int fuel = Integer.parseInt(eElement.getAttribute("fuel"));
				
				Location l1 = locations.get(location1);
				Location l2 = locations.get(location2);
				
				l1.addConnection(l2, fuel);
				l2.addConnection(l1, fuel);
			}
			
			// Get truck info
			nList = doc.getElementsByTagName("truck");
			for (int i = 0; i < nList.getLength(); i++) {
				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nList.item(i);
				int fuel = Integer.parseInt(eElement.getAttribute("fuel"));
				int load = Integer.parseInt(eElement.getAttribute("load"));
				int startlocation = Integer.parseInt(eElement.getAttribute("startlocation"));
				
				Location l1 = locations.get(startlocation);
				
				truck = new Truck(fuel, load, l1);
			}
			
			// Get packages
			nList = doc.getElementsByTagName("package");
			for (int i = 0; i < nList.getLength(); i++) {
				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nList.item(i);
				int locationID = Integer.parseInt(eElement.getAttribute("location"));
				int volume = Integer.parseInt(eElement.getAttribute("volume"));
				int value = Integer.parseInt(eElement.getAttribute("value"));
				
				Location location = locations.get(locationID);
				
				Package delivery = new Package(location, volume, value);
				packages.add(delivery);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createDocument() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			//---------------------------------------------------------------------------
			// root
			Document doc = docBuilder.newDocument();
			org.w3c.dom.Element rootElement = doc.createElement("deliveryInfo");
			doc.appendChild(rootElement);
			Scanner reader = new Scanner(System.in);
			Random random = new Random();
			
			//---------------------------------------------------------------------------
			// locations
			org.w3c.dom.Element locations = doc.createElement("locations");
			rootElement.appendChild(locations);
			
			System.out.println("----Locations----");
			System.out.print("Number of locations: ");
			int nLocations = reader.nextInt();
			System.out.print("Max X: ");
			int maxX = reader.nextInt();
			System.out.print("Max Y: ");
			int maxY = reader.nextInt();
			System.out.print("Aprox. nr. of fuel nodes (%): ");
			int nFuel = reader.nextInt();
			
			for (int i = 0; i < nLocations; i++) {
				org.w3c.dom.Element location = doc.createElement("location");
				locations.appendChild(location);
				location.setAttribute("id", ((Integer) i).toString());
				location.setAttribute("x", ((Integer) random.nextInt(maxX)).toString());
				location.setAttribute("y", ((Integer) random.nextInt(maxY)).toString());
				
				int isFuel = random.nextInt(99);
				if (isFuel <= nFuel) {
					location.setAttribute("fuel", "true");
				}
				else {
					location.setAttribute("fuel", "false");	
				}
			}
			
			//---------------------------------------------------------------------------
			// connections
			org.w3c.dom.Element connections = doc.createElement("connections");
			rootElement.appendChild(connections);
			
			System.out.println("----Connections----");
			System.out.print("Number of connections: ");
			int nConnections = reader.nextInt();
			System.out.print("Max. fuel per km: ");
			int maxFuel = reader.nextInt();
			
			for (int i = 0; i < nConnections; i++) {
				org.w3c.dom.Element connection = doc.createElement("connection");
				connections.appendChild(connection);
				connection.setAttribute("location1", ((Integer) random.nextInt(nLocations)).toString());
				connection.setAttribute("location2", ((Integer) random.nextInt(nLocations)).toString());
				connection.setAttribute("fuel", ((Integer) (random.nextInt(maxFuel)+1)).toString());
			}
			
			//---------------------------------------------------------------------------
			// truck
			org.w3c.dom.Element truck = doc.createElement("truck");
			rootElement.appendChild(truck);
			
			System.out.println("----Truck----");
			System.out.print("Fuel: ");
			int fuel = reader.nextInt();
			System.out.print("Load: ");
			int load = reader.nextInt();
			System.out.print("Start node id: ");
			int start = reader.nextInt();
			
			truck.setAttribute("fuel", Integer.toString(fuel));
			truck.setAttribute("load", Integer.toString(load));
			truck.setAttribute("startlocation", Integer.toString(start));
			
			//---------------------------------------------------------------------------
			// packages
			org.w3c.dom.Element packages = doc.createElement("packages");
			rootElement.appendChild(packages);
			
			System.out.println("----Packages----");
			System.out.print("Number of packages: ");
			int nPackages = reader.nextInt();
			System.out.print("Max. volume: ");
			int maxVolume = reader.nextInt();
			System.out.print("Max. value: ");
			int maxValue = reader.nextInt();
			
			for (int i = 0; i < nPackages; i++) {
				org.w3c.dom.Element dPackage = doc.createElement("package");
				packages.appendChild(dPackage);
				dPackage.setAttribute("location", ((Integer) random.nextInt(nLocations)).toString());
				dPackage.setAttribute("volume", ((Integer) (random.nextInt(maxVolume)+1)).toString());
				dPackage.setAttribute("value", ((Integer) (random.nextInt(maxValue)+1)).toString());
			}
			
			
			//---------------------------------------------------------------------------
			// write to xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("data/" + System.currentTimeMillis() + ".xml"));
			
			transformer.transform(source, result);
			reader.close();
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	/*=======================================================================================*/
	/* Graph display */

	public void displayGraph() {
		Graph graph = new SingleGraph("Delivery Info");
		
		// Create nodes
		for (Integer key: locations.keySet()) {
			graph.addNode(key.toString());
			org.graphstream.graph.Node n = graph.getNode(key.toString());
			n.setAttribute("x", locations.get(key).getX());
			n.setAttribute("y", locations.get(key).getY());
			n.addAttribute("ui.label", key.toString());
			n.addAttribute("layout.frozen");
		}
		
		// Create edges
		for (Integer key: locations.keySet()) {
			for (Location location: locations.get(key).getConnections().keySet()) {
				String node1 = ((Integer) locations.get(key).getID()).toString();
				String node2 = ((Integer) location.getID()).toString();
				
				Edge e1 = graph.getEdge(node1+node2);
				Edge e2 = graph.getEdge(node2+node1);
				if (e1 == null && e2 == null) {
					graph.addEdge(node1+node2, node1, node2);
				}
			}
		}
		
		graph.display();
	}
}
