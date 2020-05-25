import java.util.*;
import java.util.stream.Collectors;

class Squares {

	private boolean owned;
	private String owner;
	private List<Squares.sides> sidesOpen;
	public enum sides { TOP, LEFT, BOTTOM, RIGHT};
	private EnumMap<sides, Boolean> sidesMap = new EnumMap<sides, Boolean>(sides.class);
	private List<Squares.sides> sidesClaimed;
	
	Squares(){
		owned = false;
		owner = "";
		sidesOpen = new ArrayList<Squares.sides>();
		sidesClaimed = new ArrayList<Squares.sides>();
		sidesMap.put(sides.TOP, false);
		sidesMap.put(sides.LEFT, false);
		sidesMap.put(sides.BOTTOM, false);
		sidesMap.put(sides.RIGHT, false);		
	}
	
	public static boolean isSquareOwned(Squares square){
		//returns true if all sides of a square are claimed
		square.sidesClaimed = square.sidesMap.entrySet().stream()
				.filter(e -> e.getValue().equals(true))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		return square.sidesClaimed.size() == 4;
	}

	
	
	
	public static List<Squares.sides> sidesOpen(Squares square) {
		//check what sides are open on the current square. Used to generate list of player choices.
		square.sidesOpen = square.sidesMap.entrySet().stream()
				.filter(e -> e.getValue().equals(false))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		return square.sidesOpen;			
	}
	
	public static void setOwned(Squares square, sides side, Player currentPlayer) {
		square.sidesMap.put(side, true);
		if(Squares.isSquareOwned(square)) {
			System.out.println("You have claimed a square, take another turn!");
			square.owner = Player.getName(currentPlayer);
			square.owned = true;
			currentPlayer.playerScore++;
			currentPlayer.secondTurn = true;
		}

		//probably do something with playerScore here. Need player as object to hold score?
		//TODO probably calling for wrong arguments
	}
}
