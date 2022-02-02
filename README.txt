README

Elevator management and simulation program by Zlatan Dimitrov.

The purpose of this program is to manage and print out elevator movement requests. There are two types of requests. A passenger can move from the ground floor to their working floor, or one can be moved from their working floor to the ground floor.

There are 3 classes (Passenger, Elevator, System) and an enum variable (ElevatorState) defined in the program.

The Passenger class is defining an object with the purpose of saving each passenger’s working floor. The enum variable is defining the state of the elevator. The Elevator class is defining the functions each separate elevator entity has, and the System class is defining all management software functions in a single entity.

For the testing of the program, a random request generator is defined in the main() method. The entire system works with system steps instead of seconds. One step consists of order requests, pick-ups, leaves of people, and floor iterations. For each step of the simulation, a random order is placed. At the end of each step, the system state is printed.

Further explanation of the separate functions is defined in the comments throughout the code.
