import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.w3c.dom.Element;

public class Deadwood extends Application {

	public static final String GAME_SCREEN = null;
	public static int NUMBER_OF_DAYS = 4;
	public static int DAYS_REMAINING;
	public static Player currentPlayer;
	public static Board BOARD;
	public static DWScene[] SCENES;
	public static ArrayList<Player> PLAYERS;
	public static Scanner scan = new Scanner(System.in);
	public static boolean endTurn;
	
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DW.fxml"));
		Controller controller = new Controller();
		loader.setController(controller);
		AnchorPane root = loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Welcome to Deadwood");
		stage.show();
	}

	private static void parseBoard() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document board = null;

		try {
			board = builder.parse("resources/board.xml");
			Deadwood.BOARD = new Board((Element) board.getElementsByTagName("board").item(0));
		} catch (Exception ex) {
			System.out.println("XML parse failure");
			ex.printStackTrace();
		}
	}

	private static DWScene[] parseCards() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document cards;
		DWScene[] scenes;

		try {
			cards = builder.parse("resources/cards.xml");
			scenes = new DWScene[cards.getElementsByTagName("card").getLength()];
			for (int i = 0; i < scenes.length; i++) {
				if (cards.getElementsByTagName("card").item(i).getNodeType() == Node.ELEMENT_NODE) {
					scenes[i] = new DWScene((Element) cards.getElementsByTagName("card").item(i));
				}
			}
			return scenes;
		} catch (Exception ex) {
			System.out.println("XML parse failure");
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws ParserConfigurationException {	
		parseBoard();
		Deadwood.SCENES = parseCards();
		Application.launch(args);
	}
	
	/*
	 * Creates the board, scene cards, and players based on the number of players
	 * specified
	 */
	static void setUpGame() {
		shuffleDeck();
	}

	/* Shuffles and deals the deck of scene cards */
	static void shuffleDeck() {
		Random rand = new Random();
		int randomIndex;
		DWScene temp;
		for (int i = 0; i < Deadwood.SCENES.length; i++) {
			randomIndex = rand.nextInt(Deadwood.SCENES.length);
			temp = Deadwood.SCENES[randomIndex];
			Deadwood.SCENES[randomIndex] = Deadwood.SCENES[i];
			Deadwood.SCENES[i] = temp;
		}

	}

	/*
	 * Creates players based on any modifications needed due to having a number of
	 * players other than 4
	 */
	static void generatePlayers(int numPlayers) {
		PLAYERS = new ArrayList<Player>(numPlayers);
		for (int i = 0; i < numPlayers; i++) {
			// if 2 or 3 players, only play 3 days instead of 4
			if (numPlayers < 4) {
				Deadwood.NUMBER_OF_DAYS = 3;
				Deadwood.DAYS_REMAINING = Deadwood.NUMBER_OF_DAYS;
				PLAYERS.add(new Player(i));
				// if 4 players, make no changes (standard playthrough)
			} else if (numPlayers == 4) {
				PLAYERS.add(new Player(i));
				// if 5 players, start everyone with 2 credits
			} else if (numPlayers == 5) {
				PLAYERS.add(new Player(2, 1, i));
				// if 6 players, start everyone with 4 credits
			} else if (numPlayers == 6) {
				PLAYERS.add(new Player(4, 1, i));
				// if 7 or 8 players, start everyone at rank 2 instead
			} else {
				PLAYERS.add(new Player(0, 2, i));
			}
		}
		 DAYS_REMAINING = NUMBER_OF_DAYS;
		currentPlayer = PLAYERS.get(0);
	}
	
	/*
	 * Payout at the end of a scene - assumes there was at least one actor working
	 * on-the-card
	 */
	static void payout(Set room, DWScene scene) {
		// sort the on-card ranks from highest to lowest
		Role[] orderedRanks = scene.getRoles();
		Arrays.sort(orderedRanks);
		// roll the die as many times as the budget ($4 million budget = 4 die rolls)
		int numRolls = scene.getBudget();
		int result = 0;
		// distribute that roll's amount in dollars to each rank on the card, highest to
		// lowest
		for (int i = 0; i < numRolls; i++) {
			for (Role star : orderedRanks) {
				result = rollDie();
				if (star.isWorked()) {
					star.getActor().addDollars(result);
				}
				i++;
			}
		}
		// extras all get the dollar amount equivalent to their role's rank
		for (Role extra : room.getRoles()) {
			if (extra.isWorked()) {
				extra.getActor().addDollars(extra.getRank());
			}
		}
	}

	/* Provides a random integer between 1 and 6 */
	static int rollDie() {
		return (int) ((Math.random() * 6) + 1);
	}

	/*
	 * Returns whether or not the current player is allowed to upgrade to desired rank; upgrades
	 * if true
	 */
	public static void upgradePlayerRank(int desired, String payment) {

		if(desired == 2) {
			if(payment == "dollars"){
				currentPlayer.upgradeRank(2);
				currentPlayer.modifyDollars(4);
			} else if(payment == "credits"){
				currentPlayer.upgradeRank(2);
				currentPlayer.modifyCredits(5);
			}
		} else if(desired == 3) {
			if(payment == "dollars"){
				currentPlayer.upgradeRank(3);
				currentPlayer.modifyDollars(10);			
			} else if(payment == "credits"){
				currentPlayer.upgradeRank(3);
				currentPlayer.modifyCredits(10);
			}
		} else if(desired == 4) {
			if(payment == "dollars"){
				currentPlayer.upgradeRank(4);
				currentPlayer.modifyDollars(18);
			} else if(payment == "credits"){
				currentPlayer.upgradeRank(4);
				currentPlayer.modifyCredits(15);
			}			
		} else if(desired == 5) {
			if(payment == "dollars"){
				currentPlayer.upgradeRank(5);
				currentPlayer.modifyDollars(28);
			} else if(payment == "credits"){
				currentPlayer.upgradeRank(5);
				currentPlayer.modifyCredits(20);
			}			
		} else if(desired == 6) {
			if(payment == "dollars"){
				currentPlayer.upgradeRank(6);
				currentPlayer.modifyDollars(40);
			} else if(payment == "credits"){
				currentPlayer.upgradeRank(6);
				currentPlayer.modifyCredits(25);
			}			
		}
	}

	/*
	 * For scoring at the end of the game: credits + dollars + five times your rank
	 */
	static void scoring() {
		ArrayList<Player> players = PLAYERS;
		int score = 0;
		int max = 0;
		int winnerIndex = 0;
		// calculate and display each player's score
		for (int i = 0; i < players.size(); i++) {
			score = players.get(i).getNumCredits() + players.get(i).getNumDollars() + (players.get(i).getRank() * 5);
			if (score > max) {
				max = score;
				winnerIndex = i;
			}
			System.out.println("Player " + players.get(i).getPlayerNum() + "'s score: " + score);
		}
		// announce winner (highest score)
		System.out.println("The winner is: " + "Player " + (winnerIndex + 1) + "!");
	}

}
