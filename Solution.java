package statisticProject;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Solution {
	
	static Random rng = new Random();
	
	public static void main(String[] args) {
		//Variables used in the loop to run simulations
		int numOfDays = 0;
		int infectedTracker = 1;
		int numOfInfected = 0;
		int numOfComputers = 20;
		int numOfTrials = 10;
		int nTrial;
		int numOfRemovals = 5;
		int removalCount;
		boolean isInfected;
		int listToArrayInt;
		int listToArrayCount;
		
		//Variables for total amounts - based on array
		double allInfectedOnceCount = 0;
		double totalNumOfDaysForAllTrials = 0;
		double totalNumOfInfectedForAllTrials = 0;
		
		//Variables for function usage
		double probability = 0.1;
		
		//Scanner for user input if we want to change values
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter desired number of trials: ");
		numOfTrials = sc.nextInt();
		System.out.print("\nEnter desired number of computers: ");
		numOfComputers = sc.nextInt();
		System.out.print("\nEnter desired probability of infection: ");
		probability = sc.nextDouble();
		System.out.print("\nEnter desired number of removals per day: ");
		numOfRemovals = sc.nextInt();
		removalCount = numOfRemovals;
		nTrial = numOfTrials;
		
		
		//Array to keep track of day to day infections.
		//For loop initializes array with 1 infected (denoted with value 1)
		//and the rest as uninfected (denoted with value 0).
		int[] dayToDayArr = new int[numOfComputers];
		arrayReset(dayToDayArr);
		
		//Array to keep track if a computer has been infected before.
		//For loop initializes array with 1 infected (denoted with value 1)
		//and the rest as uninfected (denoted with value 0).
		int[] infectedCompTrackerArr = new int[numOfComputers];
		arrayReset(infectedCompTrackerArr);
		
		
		//Temp ArrayList to store index number (computer #/index) so that we dont consider it in the infection loop of that day
		ArrayList<Integer> tempArr = new ArrayList<Integer>();
		
		//While loop to iterate until desired number of trials are completed
		while (nTrial != 0) {
			//Iterate through until technician cleans all computers
			while(infectedTracker > 0) {
				numOfDays++;
				//Loop to see if infected computer will infect another
				for(int i = 0; i < numOfComputers; i++) {
					//Conditional too see if specified computer is infected
					if(dayToDayArr[i] == 1) {
						//If infected run bernoulli trial for other computers to see if the virus spreads
						for(int j = 0; j < numOfComputers; j++) {
							isInfected = bernoulli(probability);
							if((j!=i && isInfected) && ((!(tempArr.contains(j))) && (dayToDayArr[j] != 1))) {
								tempArr.add(j);
							}
						}
					}
				}
				
				listToArrayCount = tempArr.size() - 1;
				//Uses value in tempArr to give infected status to computers at corresponding index value
				while(listToArrayCount != -1) {
					listToArrayInt = (tempArr.get(listToArrayCount)).intValue();
					dayToDayArr[listToArrayInt] = 1;
					listToArrayCount--;
					infectedTracker++;
				}
				
				//Keeps track of which computers are infected
				for(int i = 0; i < numOfComputers; i++) {
					if(infectedCompTrackerArr[i] != 1 && dayToDayArr[i] == 1) {
						infectedCompTrackerArr[i] = 1;
					}
				}
				
				//Technician removal step
				for(int i = dayToDayArr.length-1; i >= 0; i--) {
					if((removalCount != 0) && (dayToDayArr[i] == 1)) {
						dayToDayArr[i] = 0;
						removalCount--;
						infectedTracker--;
					}
				}
				//Reset values in case some computers are still infected.
				removalCount = numOfRemovals;
				tempArr.clear();
			}
			//Update infectedCompTrackerArr so we can calculate which computers were infected and which were clean
			for(int i = 0; i < numOfComputers; i++) {
				numOfInfected = infectedCompTrackerArr[i] + numOfInfected;
			}
			//Check to see if all computers were infected atleast once
			if(numOfInfected == numOfComputers) {
				allInfectedOnceCount++;
			}
			//Accumulators to calculate total after each trial
			totalNumOfDaysForAllTrials = numOfDays + totalNumOfDaysForAllTrials;
			totalNumOfInfectedForAllTrials = numOfInfected + totalNumOfInfectedForAllTrials;
			
			//nth Trial information
			System.out.println("Trial " + nTrial + " numOfDays: " + numOfDays + ", allInfectedOnceCount: " + allInfectedOnceCount + ", numofInfected: " + numOfInfected);
			
			//Reset variables for next trial
			nTrial--;
			infectedTracker = 1;
			numOfInfected = 0;
			numOfDays = 0;
			arrayReset(dayToDayArr);
			arrayReset(infectedCompTrackerArr);
		}
		//Print statements for desired value
		System.out.println("Expected time to remove the virus from the whole network: " + totalNumOfDaysForAllTrials/numOfTrials + " days");
		System.out.println("Probability that each computer gets infected at least once: " + allInfectedOnceCount/numOfTrials);
		System.out.println("Expected number of computers that get infected: " + totalNumOfInfectedForAllTrials/numOfTrials);
		
		sc.close();
	}
	
	//Setting up RNG with range 0 to 1 for Bernoulli Trial function
	public static double uniform() {
		return rng.nextDouble();
	}

	//Bernoulli Trial function to determine infection based off probability
	public static boolean bernoulli(double probability) {
		if (!(probability >= 0.0 && probability <= 1.0))
			throw new IllegalArgumentException("Probability must be between 0.0 and 1.0 for RNG");
		return uniform() < probability;
	}
	//Function to reset/intialize correct values for array when doing a fresh trial
	public static void arrayReset(int[] arr) {
		for(int i = 0; i < arr.length; i++) {
			if(i == 0) {
				arr[i] = 1;
			}
			else {
				arr[i] = 0;
			}
		}
	}

}
