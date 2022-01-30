public class Elevator {

	private final int capacity = 6; 			//indicates the maximum number of passengers in an elevator
	private int passangers; 					//indicates the current number of passengers
	private ElevatorState es;					//indicates if the elevator is moving and in which direction
	private int currFloor;						//the current location of the elevator
	private int destination;					//the final destination of the current movement; It can be changed if further passengers get in the elevator.
	private int[] stopsLeaves;					//where should the elevator stop for passengers to get out
	private int[] stopsPickUp;					//where should the elevator stop for passengers to get in
	public int totalPass = 0;					//Variable, recording the total amount of passengers, which have used the elevator.
	
	public Elevator() {
		
		this.es = ElevatorState.WAITING;				//elevators are initialized in a waiting state
		this.currFloor = 0;								//elevators are initialized on the ground floor
		this.passangers = 0;							//elevators are initialized empty
		this.destination = 0;							//elevators are initialized with a final destination of ground floor
		this.stopsLeaves = new int[this.capacity];		//initialization of a buffer with the stops where passengers should be brought
		for(int i=0;i<this.stopsLeaves.length;i++)
			this.stopsLeaves[i]=-1;
		this.stopsPickUp = new int[this.capacity];		//initialization of a buffer with the stops where passengers should be picked up
		for(int i=0;i<this.stopsPickUp.length;i++)
			this.stopsPickUp[i]=-1;						//the buffers are initialized with -1, which is an invalid floor (Ground floor = 0, 1st floor and above are numbers)
	}
	
	public boolean callElevator(int floor, int dest) {
		//floor corresponds to the call origin
		//if floor == 0 the person should be moved upwards to their work floor, which is indicated from the second variable -> "dest"
		//if floor > 0 the person should be moved to 0 (Ground Floor)
				
		if (floor == 0) {
			
			// an elevator can pick up a passenger from the ground floor if the elevator is on the ground floor, or is waiting for an order 
			if (this.currFloor != 0 && this.es != ElevatorState.WAITING)		
				return false;
			
			//the order is sent to the elevator and "true" is returned, when it has successfully been added to the pickUp and leave buffers
			return this.addStopPickUp(0) && this.addStopLeave(dest);
			
		}
		else { // if(floor > 0), in this case elevators will only be used for transport of passengers to the Ground Floor
			
			//the order is automatically discarded if the elevator is moving passengers upwards
			if (this.es == ElevatorState.UPWARDS)
				return false;
			
			//if the elevator is moving downwards, it can pick up only passengers, which are below it
			else if (this.es == ElevatorState.DOWNWARDS) {
				if (this.currFloor < floor)
					return false;
				else {//if(this.currFloor >= floor)
					return this.addStopPickUp(floor) && this.addStopLeave(0);
				}
			}
			
			//if the elevator is waiting it can always receive a new order
			else if (this.es == ElevatorState.WAITING) {
				return this.addStopPickUp(floor) && this.addStopLeave(0);
			}
			
		}
		
		//in case no of the an exception the elevator call returns a false
		return false;
	}
	
	public int checkPickUp() { 	//the function returns the available places for passenger pickup orders; 
								//the return corresponds to the empty places in the pickUp buffer - the current amount of passengers in the elevator	
		int totalPickUps = 0;
		for(int i : this.stopsPickUp)
			if(i == -1)
				totalPickUps++;
		return totalPickUps-this.passangers; 
	}
	
	public int checkLeave() { //the function returns the available places in the leave order buffer 
		int totalLeaves = 0;
		for(int i : this.stopsLeaves)
			if(i == -1)
				totalLeaves++;
		return totalLeaves;
	}
	
	private boolean addStopPickUp(int stop) {	//adds a stop to the pickUp buffer
		
		//checks if there are empty positions in the pick up stops buffer
		if(this.checkPickUp() > 0) 			
			
			//if there is at least one empty position the input variable is added to the buffer
			for(int i = 0; i < this.stopsPickUp.length; i++)
				if (this.stopsPickUp[i] == -1) {
					this.stopsPickUp[i] = stop;
					return true;
					}
		
		return false; 
			
	}
	
	private boolean addStopLeave(int stop) {  //adds a stop to the leave buffer
		
		//checks if there are empty positions in the leave stops buffer
		if(this.checkLeave() > 0)	
			
			//if there is at least one empty position the input variable is added to the buffer
			for(int i = 0; i < this.stopsLeaves.length; i++)
				if (this.stopsLeaves[i] == -1) {
					this.stopsLeaves[i] = stop;
					return true;
					}
		
		return false;
	}
	
	private void makeStop() {		// make a stop for people to get in or out
		
//		if(this.es == ElevatorState.WAITING)
//			return;
		if(this.passangers > 0)		//if there are any passengers in the elevator, a check is made if any of them has to get off at the current floor
			
			//the function goes through the leaves buffer removes the passengers which are to get off at the current floor
			//in the case of a passenger transport between the different floors (for example 3->15) further checks should be made
			//but the function works properly for the use cases described in the problem description 
			for(int i = 0; i < this.stopsLeaves.length; i++)
				if(this.stopsLeaves[i] == this.currFloor) {
					this.stopsLeaves[i] = -1;
					this.passangers--;
					this.totalPass++;
				}
		
		//the function continues to pick up the passengers which are to be picked up on the current floor
		//the capacity of the elevator is considered when the pick up stop is added to the buffer, so no further checks re needed at this point
//		if(this.passangers<this.capacity)
		for(int i = 0; i < this.stopsPickUp.length; i++)
			if(this.stopsPickUp[i]==this.currFloor) {
				this.stopsPickUp[i]=-1;
				this.passangers++;
			}
		
	}
	
	public int currentLocation() {					//returns the current locations of the elevator
		return this.currFloor;
	}
	
	public void ElevatorStep() {	//one step in time corresponds to one floor movement
		//TODO convert to a java thread later

		//the elevator destination is updated before the next step, 
		//to ensure the elevator is moving in the right direction after picking up passengers or after emptying
		
//		this.updateDest();
		this.makeStop();
		this.updateDest();
		
		if (this.es == ElevatorState.WAITING) {
			return;
		}
		
		else if(this.es == ElevatorState.DOWNWARDS) {
			this.currFloor--;
		}
		
		else if(this.es == ElevatorState.UPWARDS) {
			this.currFloor++;
		}
		
	}
	
	public void updateDest() {		//this function is updating the movement state (Elevator State) and final destination of the elevator
		
		// when the elevator is empty (the pickUp and Leave buffer are empty), the Elevator State is set to WAITING
		if(this.checkLeave() == this.capacity && this.checkPickUp() == this.capacity) {
			this.es = ElevatorState.WAITING;
			return;
		}
		
		// if there are any records in the pickUp buffer, the elevator starts moving towards the uppermost of them
		else if(this.checkPickUp() < this.capacity) { 
			this.destination = this.maxPickUp();
			if (this.currFloor < this.maxPickUp())
				this.es = ElevatorState.UPWARDS;
			if (this.currFloor > this.maxPickUp())
				this.es = ElevatorState.DOWNWARDS;
		}
		
		// in case all passengers are picked up, the elevator searches only for locations to leave passengers
		if(this.maxPickUp() == 0 && this.checkLeave() < this.capacity) { 
			this.destination = this.maxLeave();
			if (this.currFloor < this.maxLeave())
				this.es = ElevatorState.UPWARDS;
			if (this.currFloor > this.maxLeave())
				this.es = ElevatorState.DOWNWARDS;
		}
		
	}
		
	public String PrintState() {
		// the state of the elevator is printed in the format 
		//"Current Floor" -> "Destination Floor" {"Number of passengers inside the elevator"} "Total number of transported passengers";
		return this.currFloor + "->"+ this.destination +/*" " + this.es+*/" {" + this.passangers +"} "+ this.totalPass+"; \t";  
	}
	
	public int maxPickUp() {	//the function returns the maximum integer from the PickUp buffer
	int i = 0;
		for(int j : this.stopsPickUp)
			if (j>i)
				i=j;
		return i;
	}
	
	public int maxLeave() {		//the function returns the maximum integer from the leave buffer
		int i = 0;
		for(int j : this.stopsLeaves)
			if(j>i)
				i=j;
		return i;
	}
}