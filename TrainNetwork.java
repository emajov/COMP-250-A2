package assignment2;

public class TrainNetwork {
	final int swapFreq = 2;
	TrainLine[] networkLines;

    public TrainNetwork(int nLines) {
    	this.networkLines = new TrainLine[nLines];
    }
    
    public void addLines(TrainLine[] lines) {
    	this.networkLines = lines;
    }
    
    public TrainLine[] getLines() {
    	return this.networkLines;
    }
    
    // method to sort train network
    public void dance() {
    	System.out.println("The tracks are moving!");
    	for (int i = 0; i < networkLines.length; i++) {
    		networkLines[i].shuffleLine();
    	}
    }
    
    // method to shuffle train network
    public void undance() {
    	for (int i = 0; i < networkLines.length; i++) {
    		networkLines[i].sortLine();
    	}
    }
    
    public int travel(String startStation, String startLine, String endStation, String endLine) {
    	
    	TrainLine curLine = getLineByName(startLine); // current line.
    	TrainStation curStation = curLine.findStation(startStation); //current station. 
    	
    	int hoursCount = 0;
    	System.out.println("Departing from "+startStation);
  
    
    	TrainStation prev = null;
    	TrainStation temp;
    	try {
    	while(curStation.getName() != endStation) {
    		
    		if (hoursCount == 168) {
    			System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
    			return hoursCount;
    		}
    		temp = curStation;
    		curStation = curLine.travelOneStation(curStation, prev);
    		curLine = curStation.getLine(); 
    		prev = temp;
    	
    		hoursCount++;
    		
    		if (hoursCount % 2 == 0 && hoursCount != 0) {
        		dance();
        	}
    		else if (curStation.getName().equals(endStation) && curLine.getName().equals(endLine)) {
    			return hoursCount;
    		}
    		}
    	}
    	catch (Exception StationNotFoundException){
    		return 168;
    	}
    	//prints an update on your current location in the network.
    	System.out.println("Traveling on line "+curLine.getName()+":"+curLine.toString());
    	System.out.println("Hour "+hoursCount+". Current station: "+curStation.getName()+" on line "+curLine.getName());
    	System.out.println("=============================================");
    	System.out.println("Arrived at destination after "+hoursCount+" hours!");
    	
    	return hoursCount;
    }
    
    
    // returns line of specified name
    public TrainLine getLineByName(String lineName){
    	for (int i = 0; i < networkLines.length; i++) {
    		if (networkLines[i].getName().equals(lineName)) {
    			return networkLines[i];
    		}
    	}
    	throw new LineNotFoundException("Cannot find" + lineName);
    }
    
  //prints a plan of the network for you.
    public void printPlan() {
    	System.out.println("CURRENT TRAIN NETWORK PLAN");
    	System.out.println("----------------------------");
    	for(int i=0;i<this.networkLines.length;i++) {
    		System.out.println(this.networkLines[i].getName()+":"+this.networkLines[i].toString());
    		}
    	System.out.println("----------------------------");
    }
}

//exception when searching a network for a LineName and not finding any matching Line object.
class LineNotFoundException extends RuntimeException {
	   String name;

	   public LineNotFoundException(String n) {
	      name = n;
	   }

	   public String toString() {
	      return "LineNotFoundException[" + name + "]";
	   }
	}