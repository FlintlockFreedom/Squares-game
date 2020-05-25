
public class Player {
	public int playerScore;
	public String playerName;
	public boolean secondTurn;
	private boolean isHuman;
	private int difficulty;

	Player(String name, int turnOrder){
		playerName = name;
		isHuman = true;
		playerScore = 0;
		secondTurn = false;
	}

	Player(int difficulty, boolean isHuman){
		playerName = "Computer";
		this.isHuman = false;
	}
	
	public static int playerScore(Player currentPlayer) {
		return currentPlayer.playerScore;
	}
	
	public static String getName(Player currentPlayer) {
		return currentPlayer.playerName;
	}
}
