// Ethan Coltrin
// CS 1450 T/R
// 11/12/2025
// Assignment 9
// This program simulates a list of Airbnbs using LinkedLists
//in which operations are performed

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ColtrinEthanAssignment9 {

	public static void main(String[] args) throws FileNotFoundException {
		
		//instances for both LinkedList
		AirbnbLinkedList singleLinkedList = new AirbnbLinkedList();
		DoubleLinkedList doubleLinkedList = new DoubleLinkedList();
		
		//open Airbnbs.txt and make reader
		File AirbnbsFileName = new File("Airbnbs.txt");
		Scanner AirbnbsFile = new Scanner(AirbnbsFileName);
		
		// Iterate through Airbnbs.txt and add each Airbnb object to 
		// singleLinkedList
		while(AirbnbsFile.hasNextLine()) {
			String line = AirbnbsFile.nextLine().trim();
			if(line.isEmpty()) continue;
			
			Scanner lineScan = new Scanner(line);
			
			int rate = lineScan.nextInt();
			int guests = lineScan.nextInt();
			int bedrooms = lineScan.nextInt();
			
			String type = lineScan.hasNext() ? lineScan.nextLine().trim() : "";
			
			System.out.println(rate + " " + guests + " " + bedrooms + " " + type);
			
			//abnb object
			Airbnb abnb = new Airbnb(rate, guests, bedrooms, type);
			//add abnb objects to singly linked list
			singleLinkedList.addByRate(abnb);
			doubleLinkedList.addToEnd(abnb);
			
			
			lineScan.close();
		}
		AirbnbsFile.close();
		
		// Display linked lists
		singleLinkedList.display();
		System.out.println(" ");
		doubleLinkedList.displayBackwards();
		
		// Open requirements file for reading
		File requirementsFile = new File("AirbnbRequirements.txt");
		Scanner requirements = new Scanner(requirementsFile);
		
		int rate = requirements.nextInt();
		int guests = requirements.nextInt();
		String type = requirements.nextLine().trim();

		singleLinkedList.findAirbnbs(rate, guests, type);
		
	}

}

class Airbnb implements Comparable<Airbnb>{
	private int nightlyRate;
	private int numGuests;
	private int numBedrooms;
	private String type;
	
	public Airbnb(int nightlyRate, int numGuests, int numBedrooms, String type) {
		this.nightlyRate = nightlyRate;
		this.numGuests = numGuests;
		this.numBedrooms = numBedrooms;
		this.type = type;
	}
	
	int getRate() {
		return nightlyRate;
	}
	
	int getGuests() {
		return numGuests;
	}
	
	int getBedrooms() {
		return numBedrooms;
	}
	
	String getType() {
		return type;
	}
	
	public String toString() {
			
		return type + nightlyRate + numGuests + numBedrooms;
	}
	
	public int compareTo(Airbnb otherAirbnb) {
		if(this.nightlyRate < otherAirbnb.nightlyRate ) {
			return -1;
		}
		
		if(this.nightlyRate > otherAirbnb.nightlyRate) {
			return 1;
		}
		else {
			return 0;
		}
	}
}



class AirbnbLinkedList{
	private Node head;
	
	public AirbnbLinkedList() {
		this.head = head;
	}
	
	public void addByRate(Airbnb airbnbToAdd) {
		Node newNode = new Node(airbnbToAdd);
		Node currentNode = head;
		
		if(head == null) {
			head = newNode;
			return;
		}
		
		//if airbnbToAdd's rate is smaller than the head's rate
		//put it before the head
		if(airbnbToAdd.compareTo(head.data) < 0) {
			newNode.next = head;
			head = newNode;
			return;
		}
		
		//start at head, move while next node data is less
		//than or equal to the new Airbnb's rate
		while(currentNode.next != null && 
					airbnbToAdd.compareTo(currentNode.next.data) >= 0) {
			currentNode = currentNode.next;	
		}
		
		newNode.next = currentNode.next;
		currentNode.next = newNode;
				
	}
	
	public PriorityQueue<Airbnb> findAirbnbs(int nightlyRate, int numGuests, String type){
		PriorityQueue<Airbnb> airbnbsQueue = new PriorityQueue<>();
		Node current = head;
		
		while(current != null) {
			Airbnb airbnb = current.data;
			
			if(airbnb.getRate() <= nightlyRate && airbnb.getGuests() >= numGuests && airbnb.getType().equals(type)) {
				airbnbsQueue.add(airbnb);
			}
			
			current = current.next;
		}
		
		return airbnbsQueue;
	}
	
	private class Node{
		private Airbnb data;
		private Node next;
		
		public Node(Airbnb data) {
			this.data = data;
			this.next = null;
		}
	}
	
	public int removeSpecificType(String typeToRemove) {
		int count = 0;
		Node current = head;
		Node prev = null;
		
		//if head node has type to remove
		while(head != null && head.data.getType().equals(typeToRemove)) {
			//move head to next node
			head = head.next;
			count ++;
		}
		
		//search for key to delete, keep track of previous node
		while(current != null) {
			if(current.data.getType().equals(typeToRemove)) {
				//skip node, effectively deleting it
				prev.next = current.next;
				count++;
			}
			
			else {
				prev = current;
			}
			
			//step through linked list
			current = current.next;
		}
		return count;
	}
	
	public void display() {
		Node current = head;
		
		System.out.println("rate\t#guests\t#bedrooms\tAirbnb type");
		
		while(current != null) {
			System.out.println(current.data.getRate() + "\t" + current.data.getGuests() + "\t" + current.data.getBedrooms()
			+ "\t\t" + current.data.getType());
			
			if(current.next == null) {
				current.next = head;
				return;
			}
			
			//step through linked list
			current = current.next;
		}
	}
}//Airbnb LinkedList


class DoubleLinkedList{
	private Node head;
	private Node tail;
	
	public void addToEnd(Airbnb airbnbToAdd) {
		Node newNode = new Node(airbnbToAdd);
		
		if(head == null) {
			head = newNode;
			tail = newNode;
			return;
		}
		
		tail.next = newNode;
		newNode.previous = tail;
		tail = newNode;
		
	}
	
	public void displayBackwards() {
		Node current = tail;
		
		if(current == null) {
			System.out.println("List empty");
			return;
		}
		
		System.out.println("rate\t#guests\t#bedrooms\tAirbnb type");
		while(current != null) {
			System.out.println(current.data.getRate() + "\t" + current.data.getGuests() + "\t" + current.data.getBedrooms()
				+ "\t\t" + current.data.getType());
			current = current.previous;	
			}	
	}
	
	
	private class Node{
		private Airbnb data;
		private Node next;
		private Node previous;
		
		public Node(Airbnb data) {
			this.data = data;
			this.next = null;
			this.previous = null;
		}
	}
}