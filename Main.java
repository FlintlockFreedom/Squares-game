import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class Main {

	public static void main(String[] args) {

		// TODO Declare winner when mathematically impossible to overcome score
		// TODO Add AI player.
		// TODO Add difficulties? Random selection, prioritize capping squares?
		
		String [][] gameGrid =  {
			      {"*","  ","*","  ","*","  ","*","  ","*","  ","*"},
			      {" "," 1"," "," 2"," "," 3"," "," 4"," "," 5"," "},
			      {"*","  ","*","  ","*","  ","*","  ","*","  ","*"},
			      {" "," 6"," "," 7"," "," 8"," "," 9"," ","10"," "},
			      {"*","  ","*","  ","*","  ","*","  ","*","  ","*"},
			      {" ","11"," ","12"," ","13"," ","14"," ","15"," "},
			      {"*","  ","*","  ","*","  ","*","  ","*","  ","*"},
			      {" ","16"," ","17"," ","18"," ","19"," ","20"," "},
			      {"*","  ","*","  ","*","  ","*","  ","*","  ","*"},
			      {" ","21"," ","22"," ","23"," ","24"," ","25"," "},
			      {"*","  ","*","  ","*","  ","*","  ","*","  ","*"},

			    };
		
		//Create an array of Squares objects 0 - 25. 0 is never used, seems simpler than converting grid into 0 index.		
		Squares square[] = new Squares[26];
		int i = 0;
		while( i < 26) {
			square[i] = new Squares();
			i++;
		}
		
		boolean playersReady = false;
		int playerID = 1;
		List<Player> playerList = new ArrayList<Player>();
		while(!playersReady) {
			System.out.println("Welcome to Squares!");
			System.out.println("Enter the number of human players: ");
			Scanner scanner = new Scanner(System.in);
			while(!scanner.hasNextInt()) {
				System.out.println("Please enter a valid number: ");
				scanner.next();
			}
			int humanPlayers = scanner.nextInt();
			//Debug: Number of players working right?
			System.out.println("There will be " + humanPlayers + " human players.");
			for(int p = 0; p < humanPlayers; p++) {
				System.out.println("Please enter a name for player " + playerID + ": ");
				String name = scanner.next();				
				playerList.add(new Player(name, p));
				System.out.println("Player " + name + " has been created");
				playerID++;
			}
			playersReady = true;
			
			
			
			
		}
		
		//Game Loop
		int currentPlayer = 0;
		while(true) {
			printBoard(gameGrid);
			System.out.println("");
			System.out.println("");
			System.out.println("");
			playerTurn(gameGrid, square, playerList.get(currentPlayer).playerScore, playerList, playerList.get(currentPlayer));
			//Moves on to next player or gives player a second turn if they claimed a square
			if(!playerList.get(currentPlayer).secondTurn) {
				if(currentPlayer < playerList.size()-1) {
					//TODO Next sysout is debug only
					System.out.println("Moving to next player");
					currentPlayer++;
				} else {
					//TODO Next sysout is debug only
					System.out.println("Restarting the player list");
					currentPlayer = 0;
				}
			}
			playerList.get(currentPlayer).secondTurn = false;
			
			//Checks total score of players to determine if game is over
			int totalScore = playerList.stream()
					.collect(Collectors.summingInt(Player::playerScore));
			System.out.println("");
			System.out.println("Getting Streamed Score:" + totalScore);
			if(totalScore == 25) {
				//TODO Is this list sorting on score? I don't think so.
				List<Player> sortedList = playerList.stream()
						.sorted((p1, p2)-> p1.playerScore - p2.playerScore)
						.collect(Collectors.toList());
				Collections.reverse(sortedList); //I'm assuming there's a way to fix the lambda math to not need this but I'm not familiar enough with lamda
				for(Player p : sortedList) {
					System.out.println(Player.getName(p));
				}
				System.out.println("The winner is " + Player.getName(sortedList.get(0)));
				break;

			}
		}
		
	}

//prints the gameboard	
public static void printBoard(String[][] gameGrid) {
	
	for(String[] s : gameGrid) {
		for(String s2 : s) {
			System.out.print(s2);	
		}
		System.out.println("");
	}
}

public static void playerTurn(String[][] gameGrid, Squares[] square, int playerScore, List<Player> playerList, Player currentPlayer) {
	int pSquareChosen = 0;
	System.out.println(Player.getName(currentPlayer) + "'s turn!");
	System.out.println("Choose a Square (1-25)");
	Scanner scanner = new Scanner(System.in);
	while(!scanner.hasNextInt()) {
		System.out.println("That was not a valid choice, please choose again.");
		scanner.next();
	}
	pSquareChosen = scanner.nextInt();
	//make sure square is between 1-25
	while(pSquareChosen < 1 || pSquareChosen > 25) {
		System.out.println("Please choose a square between 1 and 25. " + pSquareChosen + " was not a valid option.");
		pSquareChosen = scanner.nextInt();
	}	
	//make sure square has available sides to choose. 
	//TODO Next line is for debug only
	System.out.println("Is square owned? " + Squares.isSquareOwned(square[pSquareChosen]));
	while(Squares.isSquareOwned(square[pSquareChosen])){
		System.out.println("There are no available sides on this square, please choose again: ");
		pSquareChosen = scanner.nextInt();
	}
	
	String pSideChosen = "";
	System.out.println("Choose a side");
	for(Squares.sides side : Squares.sidesOpen(square[pSquareChosen])) {
		System.out.println(side);
	}
	
	pSideChosen = scanner.next();
	ArrayList<String> validSides = new ArrayList<String>(4);
	validSides.add("TOP");
	validSides.add("BOTTOM");
	validSides.add("RIGHT");
	validSides.add("LEFT");
	while(!validSides.contains(pSideChosen.toUpperCase()) || !Squares.sidesOpen(square[pSquareChosen]).contains(Squares.sides.valueOf(pSideChosen.toUpperCase())) ) {
		//checks that player entered a valid side and that side is still available. validSides check necessary to avoid invalid enum errors.
		System.out.println("That was an invalid choice. Please choose again from the list below:");
		for(Squares.sides side : Squares.sidesOpen(square[pSquareChosen])) {
			System.out.println(side);
		}
		pSideChosen = scanner.next();
	}

	
	//Convert player choices to gameBoard array locations. Will need to replace the 5 with board width variable if game size is not static	
	int boardRow = 0;
	int boardCol = 0;
	if(pSquareChosen % 5 == 0) {
		boardRow = pSquareChosen / 5;
		boardCol = 5;
	} else {
		boardRow = (pSquareChosen / 5) + 1;
		boardCol = pSquareChosen % 5;
	}
	int pArrayRow = 0;
	int pArrayCol = 0;
	
	

	switch(Squares.sides.valueOf(pSideChosen.toUpperCase())) {
		case TOP:
			pArrayCol = (boardCol * 2) - 1;
			pArrayRow = (boardRow - 1) * 2;
			gameGrid[pArrayRow][pArrayCol] = "--";
			Squares.setOwned(square[pSquareChosen], Squares.sides.valueOf(pSideChosen.toUpperCase()), currentPlayer);
			if(pSquareChosen > 5) {
				Squares.setOwned(square[pSquareChosen - 5], Squares.sides.BOTTOM, currentPlayer);
			}
			break;
		
		case BOTTOM:
			pArrayCol = (boardCol * 2) - 1;
			pArrayRow = (boardRow * 2);
			gameGrid[pArrayRow][pArrayCol] = "--";
			Squares.setOwned(square[pSquareChosen], Squares.sides.valueOf(pSideChosen.toUpperCase()), currentPlayer);
			if(pSquareChosen < 21) {
				Squares.setOwned(square[pSquareChosen + 5], Squares.sides.TOP, currentPlayer);
			}
			break;
		
		case LEFT:
			pArrayCol = (boardCol - 1) * 2;
			pArrayRow = (boardRow * 2) -1;
			gameGrid[pArrayRow][pArrayCol] = "|";
			Squares.setOwned(square[pSquareChosen], Squares.sides.valueOf(pSideChosen.toUpperCase()), currentPlayer);
			if(pSquareChosen - 1 % 5 != 0) {
				Squares.setOwned(square[pSquareChosen - 1], Squares.sides.RIGHT, currentPlayer);
			}
			break;
		
		case RIGHT:
			pArrayCol = boardCol * 2;
			pArrayRow = (boardRow * 2) - 1;
			gameGrid[pArrayRow][pArrayCol] = "|";
			Squares.setOwned(square[pSquareChosen], Squares.sides.valueOf(pSideChosen.toUpperCase()), currentPlayer);
			if(pSquareChosen % 5 != 0) {
				Squares.setOwned(square[pSquareChosen + 1], Squares.sides.LEFT, currentPlayer);
			}
			break;
			
		default:
			System.out.println("I'm already error checking at the scanner. How the hell did you get here?");
			break;
	}
	
	
}
}
