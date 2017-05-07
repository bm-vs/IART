package ProblemData;
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

public class XMLParser {	
	public XMLParser() {}
	
	public void read(String docName, DeliveryInfo info) {
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

	public void write(int nLocations, int maxX, int maxY, int nFuel, int nConnections, int maxFuel, int fuel, int load, int start, int nPackages, int maxVolume, int maxValue) {
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
			// Locations
			org.w3c.dom.Element locations = doc.createElement("locations");
			rootElement.appendChild(locations);
			ArrayList<Location> nodes = new ArrayList<Location>();
			
			//Create Locations
			for (int i = 0; i < nLocations; i++) {
				Integer id = i;
				Integer x = (Integer) random.nextInt(maxX);
				Integer y = (Integer) random.nextInt(maxY);
				Location node = new Location(id,x,y,false);
				
				nodes.add(node);
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
			
			
			
			//---------------------------------------------------------------------------
			// Connections
			org.w3c.dom.Element connections = doc.createElement("connections");
			rootElement.appendChild(connections);
			
			for (int i = 0; i < nConnections; i++) {
				org.w3c.dom.Element connection = doc.createElement("connection");
				connections.appendChild(connection);
				connection.setAttribute("location1", ((Integer) random.nextInt(nLocations)).toString());
				connection.setAttribute("location2", ((Integer) random.nextInt(nLocations)).toString());
				connection.setAttribute("fuel", ((Integer) (random.nextInt(maxFuel)+1)).toString());
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
}
