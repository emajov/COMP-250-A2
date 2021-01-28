package assignment2;

import java.util.Arrays;
import java.util.Random;

public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
		/*
		 * Constructor for TrainStation input: stationList - An array of TrainStation
		 * containing the stations to be placed in the line name - Name of the line
		 * goingRight - boolean indicating the direction of travel
		 */
	{
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}

		this.lineMap = this.getLineArray();
	}

	public TrainLine(String[] stationNames, String name,
			boolean goingRight) {
		/*
		* Constructor for TrainStation. input: stationNames - An array of String
		* containing the name of the stations to be placed in the line name - Name of
		* the line goingRight - boolean indicating the direction of travel
		*/
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();

	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	// to get number of stations on train line 
	public int getSize() {
		TrainStation current = leftTerminus;
		int size = 1;
		while (current != rightTerminus) {
			current = current.getRight();
			size++;
		}
		return size;
	}

	public void reverseDirection() {
		this.goingRight = !this.goingRight;
	}

	// to get passenger to travel on station
	public TrainStation travelOneStation(TrainStation current, TrainStation previous) throws StationNotFoundException{
		
		findStation(current.getName());
			
		if (current.getTransferStation() != null) {
			if (!current.getTransferStation().equals(previous)) {
				return current.getTransferStation();
			}
		}
		return getNext(current);		
	}

	// to find next station on train line
	public TrainStation getNext(TrainStation station) {
		
		findStation(station.getName());
		
		if (goingRight) {
			TrainStation next = station.getRight();
			if (station.equals(rightTerminus)) {
				reverseDirection();
				next = station.getLeft();
			}
			return next;
		}
		TrainStation next = station.getLeft();
		if (station.equals(leftTerminus)) {
			reverseDirection();
			next = station.getRight();
		}
		return next;
	}

	// to find certain train station on train line
	public TrainStation findStation(String name) {
		int i;
		for (i = 0; i < getSize(); i++) {
			if (lineMap[i].getName().equals(name)) {
				return lineMap[i];
			}
		}
		throw new StationNotFoundException("Cannot find" + name);
	}
	
	// to sort line in alphabetical order
	public void sortLine() {
		int n = getSize();
		TrainStation [] array = getLineArray();
		// bubble sort to sort array
		for (int i = 0; i < n-1; i++) {
			for (int j = 0; j < n-i-1; j++) {
				if (array[j].getName().compareTo(array[j+1].getName())>0) {
					TrainStation temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
		}
		// assigning to trainline
		lineMap = array;
		
		lineMap[0].setNonTerminal();
		lineMap[0].setLeftTerminal();
		leftTerminus = lineMap[0];
		leftTerminus.setLeft(null);
		leftTerminus.setRight(lineMap[1]);
		
		int i;
		for (i = 1; i < lineMap.length-1; i++) {
			lineMap[i].setLeft(lineMap[i-1]);
			lineMap[i].setNonTerminal();
			lineMap[i].setRight(lineMap[i+1]);
		}
		lineMap[lineMap.length-1].setNonTerminal();
		lineMap[lineMap.length-1].setRightTerminal();
		rightTerminus = lineMap[lineMap.length-1];
		rightTerminus.setRight(null);
		rightTerminus.setLeft(lineMap[lineMap.length-2]); 
	
	}
	
	// gives array of train stations on line
	public TrainStation[] getLineArray() {

		TrainStation[] trainArray = new TrainStation[getSize()];
		trainArray[0] = leftTerminus;
		int i = 0;
		while (trainArray[i] != rightTerminus) {
			trainArray[i+1] = trainArray[i].getRight();
			i++;
			
		}
		trainArray[getSize()-1] = rightTerminus;
		return trainArray; 
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();
		rand.setSeed(11);
		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		this.lineMap = array;
		return array;
	}

	// shuffles the stations on the line
	public void shuffleLine() {

		// shuffled array of trainStations 
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);

	
		// reorder line station to match line map
		shuffledArray[0].setNonTerminal();
		shuffledArray[0].setLeftTerminal();
		leftTerminus = shuffledArray[0];
		leftTerminus.setLeft(null);
		leftTerminus.setRight(shuffledArray[1]);
		
		int i;
		for (i = 1; i < shuffledArray.length-1; i++) {
			shuffledArray[i].setLeft(shuffledArray[i-1]);
			shuffledArray[i].setNonTerminal();
			shuffledArray[i].setRight(shuffledArray[i+1]);
		}
		shuffledArray[shuffledArray.length-1].setNonTerminal();
		shuffledArray[shuffledArray.length-1].setRightTerminal();
		rightTerminus = shuffledArray[shuffledArray.length-1];
		rightTerminus.setRight(null);
		rightTerminus.setLeft(shuffledArray[shuffledArray.length-2]);
	}

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}
		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}
}

//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}
