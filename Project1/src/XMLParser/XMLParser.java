package XMLParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import ProblemData.Connection;
import ProblemData.DeliveryInfo;
import ProblemData.Location;
import ProblemData.Package;
import UserInterface.GraphDisplay;

public class XMLParser {
	public static void read(String docName, DeliveryInfo info) {
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
				info.addLocation(id, location);
			}
			
			// Get connections
			nList = doc.getElementsByTagName("connection");
			for (int i = 0; i < nList.getLength(); i++) {
				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nList.item(i);
				int location1 = Integer.parseInt(eElement.getAttribute("location1"));
				int location2 = Integer.parseInt(eElement.getAttribute("location2"));
				int fuel = Integer.parseInt(eElement.getAttribute("fuel"));
				
				Location l1 = info.getLocation(location1);
				Location l2 = info.getLocation(location2);
				
				Connection c = new Connection(l1, l2, fuel);
				
				l1.addConnection(c);
				l2.addConnection(c);
			}
			
			// Get truck info
			nList = doc.getElementsByTagName("truck");
			for (int i = 0; i < nList.getLength(); i++) {
				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nList.item(i);
				int fuel = Integer.parseInt(eElement.getAttribute("fuel"));
				int load = Integer.parseInt(eElement.getAttribute("load"));
				int startlocation = Integer.parseInt(eElement.getAttribute("startlocation"));
				
				Location l1 = info.getLocation(startlocation);
				info.setTruck(fuel, load, l1);
			}
			
			// Get packages
			nList = doc.getElementsByTagName("package");
			for (int i = 0; i < nList.getLength(); i++) {
				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nList.item(i);
				int locationID = Integer.parseInt(eElement.getAttribute("location"));
				int volume = Integer.parseInt(eElement.getAttribute("volume"));
				int value = Integer.parseInt(eElement.getAttribute("value"));
				
				Location location = info.getLocation(locationID);
				
				Package delivery = new Package(location, volume, value);
				info.addPackage(delivery);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(int nLocations, int maxX, int maxY, int connectionLevel, int nFuel, int maxFuel, int fuel, int load, int start, int nPackages, int maxVolume, int maxValue) {
		try {
			GraphDisplay graphDisplay = new GraphDisplay(true);
			graphDisplay.display();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			//---------------------------------------------------------------------------
			// root
			Document doc = docBuilder.newDocument();
			org.w3c.dom.Element rootElement = doc.createElement("deliveryInfo");
			doc.appendChild(rootElement);
			Scanner reader = new Scanner(System.in);
			Random random = new Random(1);
			
			//---------------------------------------------------------------------------
			// Locations and connections
			org.w3c.dom.Element locations = doc.createElement("locations");
			org.w3c.dom.Element connections = doc.createElement("connections");
			rootElement.appendChild(locations);
			rootElement.appendChild(connections);
			
			//Create Locations and connect to graph
			ArrayList<Location> nodes = new ArrayList<Location>();
			for (int i = 0; i < nLocations; i++) {
				boolean valid;
				int x, y;
				
				// Create node
				do {
					valid = true;
					x = (Integer) random.nextInt(maxX);
					y = (Integer) random.nextInt(maxY);
					
					for (int j = 0; j < nodes.size(); j++) {
						if (x == nodes.get(j).getX() && y == nodes.get(j).getY()) {
							valid = false;
							break;
						}
					}
				} while(!valid);
				
				Location node = new Location(i,x,y,false);
				graphDisplay.addNode(node);				
				nodes.add(node);
			}
			
			// Create connection of node to graph
			ArrayList<Connection> edges = new ArrayList<Connection>();
			ArrayList<Location> connected = new ArrayList<Location>();
			ArrayList<Location> notConnected = new ArrayList<Location>();
			
			do {
				connectNodes(nodes, edges, connected, notConnected, random, graphDisplay);
			} while(notConnected.size() != 0);
			
			for (int i = 0; i < connectionLevel*connected.size(); i++) {
				boolean valid = true;
				Location l1 = connected.get(random.nextInt(connected.size()));
				Location l2 = connected.get(random.nextInt(connected.size()));
				
				if (l1.equals(l2)) {
					i--;
					continue;
				}
				
				Connection edge = new Connection(l1, l2);
				
				// Check if connection intersects existing ones
				for (int j = 0; j < edges.size(); j++) {
					if (edge.intersects(edges.get(j))) {							
						valid = false;
						break;
					}
				}
				
				// Check if connection contains other points
				for (int j = 0; j < nodes.size(); j++) {
					if (!nodes.get(j).equals(l1) && !nodes.get(j).equals(l2) && edge.contains(nodes.get(j))) {
						valid = false;
						break;
					}
				}
				
				if (valid && edges.contains(edge)) {
					valid = false;
				}
				
				if (valid) {
					edges.add(edge);
					graphDisplay.addEdge(edge);
				}
				else {
					i--;
				}
			}
			
			//Generate fuel locations correctly
			int nrFuelNodes = 0;
			while(nrFuelNodes < nFuel*nodes.size()/100){
				int nodeindex = random.nextInt(nodes.size());
				if(!nodes.get(nodeindex).getFuel()){
					nodes.get(nodeindex).setFuel(true);
					nrFuelNodes++;
				}
			}
			
			//Insert locations in document
			for(Location node : nodes){
				org.w3c.dom.Element location = doc.createElement("location");
				locations.appendChild(location);
				location.setAttribute("id", ((Integer)node.getID()).toString());
				location.setAttribute("x", ((Integer)node.getX()).toString());
				location.setAttribute("y", ((Integer)node.getY()).toString());
				location.setAttribute("fuel", ((Boolean)node.getFuel()).toString());
			}
			
			// Insert connections in document
			for (Connection edge : edges) {
				org.w3c.dom.Element connection = doc.createElement("connection");
				locations.appendChild(connection);
				connection.setAttribute("location1", ((Integer) edge.getLocation1().getID()).toString());
				connection.setAttribute("location2", ((Integer) edge.getLocation2().getID()).toString());
				connection.setAttribute("fuel", ((Integer) random.nextInt(maxFuel)).toString());
			}
			
			//---------------------------------------------------------------------------
			// Truck
			org.w3c.dom.Element truck = doc.createElement("truck");
			rootElement.appendChild(truck);
			
			truck.setAttribute("fuel", Integer.toString(fuel));
			truck.setAttribute("load", Integer.toString(load));
			truck.setAttribute("startlocation", Integer.toString(start));
			
			//---------------------------------------------------------------------------
			// Packages
			org.w3c.dom.Element packages = doc.createElement("packages");
			rootElement.appendChild(packages);
			
			for (int i = 0; i < nPackages; i++) {
				org.w3c.dom.Element dPackage = doc.createElement("package");
				packages.appendChild(dPackage);
				dPackage.setAttribute("location", ((Integer) random.nextInt(nLocations)).toString());
				dPackage.setAttribute("volume", ((Integer) (random.nextInt(maxVolume)+1)).toString());
				dPackage.setAttribute("value", ((Integer) (random.nextInt(maxValue)+1)).toString());
			}
			
			//---------------------------------------------------------------------------
			// Write to xml file
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
	
	private static void connectNodes(ArrayList<Location> nodes, ArrayList<Connection> edges, ArrayList<Location> connected, ArrayList<Location> notConnected, Random random, GraphDisplay graphDisplay) {
		for (Location node : nodes) {
			if (connected.isEmpty()) {
				connected.add(node);
				continue;
			}
			
			if (connected.contains(node)) {
				continue;
			}
			
			boolean valid, notFound = false;
			Connection edge = new Connection();
			int count = 0;
			do {
				valid = true;
				Location l = connected.get(random.nextInt(connected.size()));
				
				if (l.equals(node)) {
					valid = false;
				}
				
				edge = new Connection(node, l);
				
				// Check if connection intersects existing ones
				for (int j = 0; j < edges.size(); j++) {
					if (edge.intersects(edges.get(j))) {							
						valid = false;
						break;
					}
				}
				
				// Check if connection contains other points
				for (int j = 0; j < nodes.size(); j++) {
					if (!nodes.get(j).equals(l) && !nodes.get(j).equals(node) && edge.contains(nodes.get(j))) {
						valid = false;
						break;
					}
				}
				
				if (valid && edges.contains(edge)) {
					valid = false;
				}
				
				if (valid) {
					connected.add(node);
				}
				else {
					count++;
				}
				
				if (count == connected.size()) {
					count = 0;
					notConnected.add(node);
					valid = true;
					notFound = true;
				}
			} while (!valid);
		
			if (!notFound) {
				edges.add(edge);
				graphDisplay.addEdge(edge);
				if (notConnected.contains(node)) {
					notConnected.remove(node);
				}
				
			}
		}
	}	
}
