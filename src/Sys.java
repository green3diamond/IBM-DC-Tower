
public class Sys {
	
	//the entire program works with a System entity, where the elevators are defined

	private final int totalElevators = 7;			//the total elevators parameters is used to adjust the system for various elevator numbers
	private Elevator[] elevators;					//the elevators are defined as objects in an Array
	
	private int[][] buffer = new int[100][2];		//Besides the elevators, the System class has a buffer, with orders which have to be delayed, because of limited elevator availability.
													//The order buffer has two 100 record arrays. 
													//The first is recording the origin of the order, the second is recording the working floor of the ordering person.
	
	public Sys() {
		//the Sys() constructor initializes the two arrays. The order buffer is initialized with -1 on all positions, since that is an invalid floor.
		this.elevators = new Elevator[this.totalElevators];
		for(int i =0; i < this.buffer.length;i++) {
			this.buffer[i][0]=-1;
			this.buffer[i][1]=-1;
		}
	}
	
	public boolean startElevator() {//this function initializes the separate elevator entities
		for(int i = 0;i<this.totalElevators;i++)
			if(this.elevators[i]==null) {
				this.elevators[i] = new Elevator();
				return true;
			}
		return false;				//returns false if all elevators have already been started
	}
	
	public boolean saveCmd(int floor, Passenger p) { //this function saves an order to the delayed orders buffer
		for(int i = 0; i < this.buffer.length; i++)
			if(this.buffer[i][0]==-1) {
				this.buffer[i][0] = floor;
				this.buffer[i][1] = p.workFloor();
				return true;
			}
		
		//In case no free places are found in the buffer, meaning that there are already 100 buffered orders, the function returns false and prints an error message.
		System.out.println("Too many delayied orders. Order cancelled.");
		return false;
	}
	
	public boolean runOld() {				
		//This function is repeating orders from the buffer, when called. 
		//Upon a call the first order is pushed into the Sys and is deleted from the buffer in a FIFO manner.
		//In case of a repeated fail to add the order, the Sys will record it again at the end of the buffer.
			
		if(this.callElevator(buffer[0][0], new Passenger(this.buffer[0][1]))) {
			int j;
			
			for( j =0; this.buffer[j][0]!=-1 && j < this.buffer.length-1; j++) {
				this.buffer[j][0] = this.buffer[j+1][0];
				this.buffer[j][1] = this.buffer[j+1][1];
			}
			
			this.buffer[j][0] = -1;
			this.buffer[j][1] = -1;
			return true;
			}
			
		//The function would return false only in case the order is rejected and is unsuccessfully saved at the end of the buffer.
		return false;
	}
	
	public boolean callElevator(int floor, Passenger p) {
		//The callElevator function is taking an order in and is sending it to all elevators sequentially, until one of them responds with "true" to the request.
		for(Elevator e: this.elevators)
			if(e.callElevator(floor, p.workFloor()))
				return true;
		
		//If none of the elevators is available the order is saved in the delayed order buffer.
		return this.saveCmd(floor, p);
	}
	
	public void SystemStep(){
		//The program works in steps, where each step consists of a one floor movement, one stop on a floor, one refresh of the new order requests, one buffer entry push, and one Print of the system state.
		this.PrintState();
		
		for(Elevator e: this.elevators)
			e.ElevatorStep();
		
		//If the buffer is not empty, the uppermost order is ran again.
		if(this.buffer[0][0]!=-1)
			this.runOld();
		
//		try {
//			this.wait(1000);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
	}
	
	public void PrintState() {
		//The PrintState function prints the current state of all elevators and ends with a new row command.
		for(Elevator e:this.elevators)
			System.out.print(e.PrintState());
		System.out.println("");
	}
	
	public static void main(String[] args) {
		//The main method initializes the Sys entity and starts the Elevators.
		Sys s = new Sys();
		for(int i = 0; i< s.totalElevators;i++)
			System.out.println("Elevator started: " + s.startElevator());
		
		//A for loop is created for random request testing.
		for(int i =0; i<100; i++) {
			if(Math.random()>=0.5) {
			s.callElevator((int)(Math.random()*35), new Passenger((int)(Math.random()*35)));
			}
			else
				s.callElevator(0, new Passenger((int)(Math.random()*35)));
			
				s.SystemStep();
		}
		
		}
	}

