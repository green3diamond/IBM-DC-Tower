
public class Passenger {
	//the Passenger object has only one attribute, which is the floor at which the Passenger is working
	//Its main purpose is to help for representing the variable inputs in the Elevator call functions
	//the Passenger class description can be extended with further information and functionality
	
	private int workFloor;
	
	public Passenger(int floor) {
		this.workFloor = floor;
	}
	
	public void changeLoc(int newFloor) {
		this.workFloor = newFloor;
	}
	
	public int workFloor() {
		return this.workFloor;
	}
	
}
