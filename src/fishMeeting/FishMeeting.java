package fishMeeting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class FishMeeting {
	
	public static void main(String[] args) throws IOException, FileNotFoundException {
		
		Map<String, List<String>> movesPeggy = new HashMap<String, List<String>> ();
		Map<String, List<String>> movesSam = new HashMap<String, List<String>> ();
		
		Set<String> spotsSam = new HashSet<String> ();
		Set<String> spotsPeggy = new HashSet<String> ();
		
		Set<String> meetings = new TreeSet<String> ();
		Set<String> checked = new HashSet<String> ();
		
		List <String> toAvoid = new ArrayList<String> ();
		List <String> startPeggy = new ArrayList<String> ();
		List <String> startSam = new ArrayList<String> ();
			
		// open a new scanner to read from file
		Scanner scanner = new Scanner(System.in);
		String next = scanner.nextLine();
		String temp;

		
		// go through file once to understand map
		while (!next.equals("Map:")) {			// unnecessary assuming file formatted correctly
			next = scanner.nextLine();			// eliminates any possible lines before the Map starts
		}
		
		// fills map
		if (next.equals("Map:")) {				// previous while loop should guarantee that it does
			next = scanner.next();
			while (!next.equals("Avoid:")) {		
				// now next holds the first address on the line (upstream) and temp holds the second address (downstream)
				temp = scanner.next();
				
				// if the upstream address already exists as a key in Peggy's hashmap, add another value to its list
				if (movesPeggy.containsKey(next)) {
					movesPeggy.get(next).add(temp);
				}
				// if the upstream address is new to Peggy's hashmap, add it along with its possible path as a value
				else {
					List<String> directlyDownstream = new ArrayList<String> ();
					directlyDownstream.add(temp);		// assumes every key will have at least one value
					movesPeggy.put(next, directlyDownstream);
				}
				// likewise for Sam's hashmap
				if (movesSam.containsKey(temp)) {
					movesSam.get(temp).add(next);
				}
				else {
					List<String> directlyUpstream = new ArrayList<String> ();
					directlyUpstream.add(next);
					movesSam.put(temp, directlyUpstream);
				}
				
				// assumes the Map: section will always have two strings per line
				next = scanner.next();
			}
		}
		
		// fills toAvoid list
		next = scanner.next();
		while (!next.equals("Peggy:")) {
			toAvoid.add(next);
			next = scanner.next();
		}
				
		// fills startPeggy list
		next = scanner.next();
		while (!next.equals("Sam:")) {
			startPeggy.add(next);
			next = scanner.next();
		}
		
		// fills startSam list
		next = scanner.useDelimiter("\n").next();
		Scanner sam = new Scanner(next);
		while (sam.hasNext()) {
			startSam.add(sam.next());
		}
		sam.close();
		scanner.close();
	
		
				
		int i;
		String address;
		// populates an array of places peggy can possibly go
		for (i = 0; i < startPeggy.size(); i++) {
			address = startPeggy.get(i);
			addAddresses(address, movesPeggy, spotsPeggy, toAvoid, checked);
		}
		// and another one for where sam can go
		checked.clear();
		for (i = 0; i < startSam.size(); i++) {
			address = startSam.get(i);
			addAddresses(address, movesSam, spotsSam, toAvoid, checked);
		}
		
		// populate meetings with only spots where both fish can go
		meetings.addAll(spotsPeggy);
		meetings.retainAll(spotsSam);
		
		// list contents of meetings, already sorted alphabetically
		Iterator<String> itr = meetings.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	// add this address and recursively add any of the addresses it may lead to
	private static void addAddresses(String address, Map<String, List<String>> routes, Set<String> goodSpots,
			List<String> toAvoid, Set<String> checked) {
		
		// this address has already been checked, and anything you can find by proceeding, you either have already
		// found, or can find by backing up instead. either that or it's a spot 
		if (checked.contains(address) || toAvoid.contains(address)) {
			return;
		}
		// otherwise, this spot is accessible to this fish, and possibly also some of it's tributaries/distributaries
		else {
			// add this spot as valid, mark that we have checked it
			checked.add(address);
			goodSpots.add(address);
			// and if it leads anywhere, check that too
			if (routes.containsKey(address)) {
				for (int i = 0; i < routes.get(address).size(); i++) {
					addAddresses(routes.get(address).get(i), routes, goodSpots, toAvoid, checked);
				}
			}
		}
	}
}


