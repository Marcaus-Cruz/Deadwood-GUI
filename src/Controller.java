import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Controller {
	@FXML
	private TextArea gameplayInfo;
	@FXML
	private Label currentPlayerInfo, daysLeftCounter;
	@FXML
	private BorderPane gameScreen;
	@FXML
	private Pane introScreen;
	// buttons for "choose number of players"
	@FXML
	private Button twobtn, threebtn, fourbtn, fivebtn, sixbtn, sevenbtn, eightbtn;
	// player action buttons
	@FXML
	private Button moveBtn, actBtn, rehearseBtn, upgradeBtn, takeRoleBtn;
	// room containers
	@FXML
	private Polygon trainStation, jail, generalStore, ranch, church, saloon, bank, hotel, secretHideout, castingOffice,
			trailers, mainStreet;
	// upgrade rank buttons
	@FXML
	private Rectangle rank2, dollar2, credit2, rank3, dollar3, credit3, rank4, dollar4, credit4, rank5, dollar5,
			credit5, rank6, dollar6, credit6;
	// player dice
	@FXML
	private ImageView player1, player2, player3, player4, player5, player6, player7, player8;
	//
	@FXML
	private ImageView dieImg;
	// list of roles
	@FXML
	private Button role1, role2, role3, role4, role5, role6, role7;
	// role dropdown menu
	@FXML
	private ChoiceBox<String> chooseRole;
	// scene buttons
	@FXML
	private ImageView tsScene, msScene, gsScene, jScene, sScene, hScene, bScene, cScene, shScene, rScene;
	@FXML
	private ImageView boardImg;
	@FXML
	private ImageView ts1, ts2, ts3, gs1, gs2, ms1, ms2, ms3, j1, s2, s1, r1, r2, b1, c2, c1, h1, h2, h3, sh2, sh1, sh3;

	// whether current player has pressed Move or not
	Boolean promptMove = false;
	// whether current player has already moved this turn
	Boolean hasMoved = false, tookRole = false;
	// for upgradePlayer
	Boolean desiredRank = false, paymentMethod = false;

	DropShadow borderGlow;

	private ImageView[] playerImages;

	public Controller() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	private void initialize() {
		introScreen.setVisible(true);
		gameScreen.setVisible(false);
	}

	void flipCard(Set dest) {
		ImageView scenebtn = getSceneBtnFromRoom(dest.getName());
		DWScene scene = getSceneFromImg(scenebtn);
		if (!scene.isActive()) {
			scenebtn.setImage(scene.getImg());
			scene.setActive();
		}
	}

	@FXML
	public void removeShot(Set source) {
		if (source.getSceneCard() != null && source.getSceneCard().isActive()) {
			source.removeShot();
			String abbrev = "";
			if (source.getName().equals("Train Station")) {
				abbrev = "ts";
			} else if (source.getName().equals("Jail")) {
				abbrev = "j";
			} else if (source.getName().equals("Main Street")) {
				abbrev = "ms";
			} else if (source.getName().equals("General Store")) {
				abbrev = "gs";
			} else if (source.getName().equals("Secret Hideout")) {
				abbrev = "sh";
			} else if (source.getName().equals("Saloon")) {
				abbrev = "s";
			} else if (source.getName().equals("Hotel")) {
				abbrev = "h";
			} else if (source.getName().equals("Bank")) {
				abbrev = "b";
			} else if (source.getName().equals("Church")) {
				abbrev = "c";
			} else if (source.getName().equals("Ranch")) {
				abbrev = "r";
			}
			abbrev += source.getTotalShots() - source.getRemainingShots();
			ImageView shotMarker = (ImageView) gameScreen.lookup("#" + abbrev);
			shotMarker.setVisible(false);
		}
	}

	@FXML
	public void chooseNumPlayers(MouseEvent e) throws ParserConfigurationException {
		int numPlayers = 0;
		if (e.getSource() == twobtn) {
			numPlayers = 2;
		} else if (e.getSource() == threebtn) {
			numPlayers = 3;
		} else if (e.getSource() == fourbtn) {
			numPlayers = 4;
		} else if (e.getSource() == fivebtn) {
			numPlayers = 5;
		} else if (e.getSource() == sixbtn) {
			numPlayers = 6;
		} else if (e.getSource() == sevenbtn) {
			numPlayers = 7;
		} else if (e.getSource() == eightbtn) {
			numPlayers = 8;
		}
		introScreen.setVisible(false);
		gameScreen.setVisible(true);
		gameplayInfo.setScrollTop(Double.MAX_VALUE);
		gameplayInfo.setText("Welcome to Deadwood!");
		Deadwood.generatePlayers(numPlayers);
		// border glow effect for active player
		int depth = 80;
		borderGlow = new DropShadow();
		borderGlow.setColor(Color.WHITE.brighter().brighter().brighter());
		borderGlow.setOffsetX(0f);
		borderGlow.setOffsetY(0f);
		borderGlow.setHeight(depth);
		borderGlow.setWidth(depth);
		// take role combobox
		chooseRole.setVisible(false);
		// die for rollin
		dieImg.setVisible(true);
		// make dem player dice, also only make visible the ones that are being used by
		// players
		playerImages = new ImageView[numPlayers];
		playerImages[0] = player1;
		player1.setVisible(true);
		playerImages[0].setEffect(borderGlow);
		playerImages[1] = player2;
		player2.setVisible(true);
		if (numPlayers > 2) {
			playerImages[2] = player3;
			player3.setVisible(true);
			if (numPlayers > 3) {
				playerImages[3] = player4;
				player4.setVisible(true);
				if (numPlayers > 4) {
					playerImages[4] = player5;
					player5.setVisible(true);
					if (numPlayers > 5) {
						playerImages[5] = player6;
						player6.setVisible(true);
						if (numPlayers > 6) {
							playerImages[6] = player7;
							player7.setVisible(true);
							if (numPlayers > 7) {
								playerImages[7] = player8;
								player8.setVisible(true);
							}
						}
					}
				}
			}
		}
		updateSidePanel("Number of days to play is " + Deadwood.NUMBER_OF_DAYS + ".");
		Deadwood.currentPlayer = Deadwood.PLAYERS.get(0);
		updateSidePanel("Generating " + numPlayers + " players each with rank " + Deadwood.currentPlayer.getRank()
				+ " and " + Deadwood.currentPlayer.getNumCredits() + " extra starting credits.");
		updateBindings();
		Deadwood.shuffleDeck();
		// deal scene cards
		int deckIndex = 0;
		for (Room r : Board.getRooms()) {
			if (r instanceof Set) {
				((Set) r).setScene(Deadwood.SCENES[deckIndex]);
				deckIndex++;
			}
		}
		resetSceneImgs();
		updateBindings();
		disableAllRooms();
	}

	// Returns the proper room name from the given fx:id abbreviation (for shot
	// markers and scene cards)
	private String getFullRoomName(String abbrev) {
		if (abbrev.startsWith("ts")) {
			return "Train Station";
		} else if (abbrev.startsWith("j")) {
			return "Jail";
		} else if (abbrev.startsWith("gs")) {
			return "General Store";
		} else if (abbrev.startsWith("r")) {
			return "Ranch";
		} else if (abbrev.startsWith("sh")) {
			return "Secret Hideout";
		} else if (abbrev.startsWith("ms")) {
			return "Main Street";
		} else if (abbrev.startsWith("s")) {
			return "Saloon";
		} else if (abbrev.startsWith("b")) {
			return "Bank";
		} else if (abbrev.startsWith("c")) {
			return "Church";
		} else if (abbrev.startsWith("h")) {
			return "Hotel";
		} else {
			return null;
		}
	}

	/***
	 * call this every time the game information updates (player ranks, roles,
	 * locations, etc)
	 ***/
	private void updateBindings() {
		initPlayerInfoTable();
		bindControlButtons();
		initDayCounter();
	}

	// Returns the scene object corresponding to the given image in the GUI
	private DWScene getSceneFromImg(ImageView iv) {
		return ((Set) Board.lookUpRoom(getFullRoomName(iv.getId().substring(0, 2)))).getSceneCard();
	}

	// Sets the control buttons to be disabled if the current player can't do those
	// actions
	private void bindControlButtons() {

		BooleanBinding canAct = Bindings.and(new SimpleBooleanProperty(!tookRole),
				Bindings.notEqual("(none)", Deadwood.currentPlayer.currentRoleProperty()));
		actBtn.disableProperty().bind(canAct.not());
		BooleanBinding canRehearse = Bindings.and(canAct, Deadwood.currentPlayer.getCanRehearse());
		rehearseBtn.disableProperty().bind(canRehearse.not());
		BooleanBinding canMove = Bindings.and(new SimpleBooleanProperty(!hasMoved),
				Bindings.equal("(none)", Deadwood.currentPlayer.currentRoleProperty()));
		moveBtn.disableProperty().bind(canMove.not());
		BooleanBinding canUpgrade = Bindings.and(
				Bindings.equal("Casting Office", Deadwood.currentPlayer.locationProperty()),
				new SimpleBooleanProperty(!Deadwood.currentPlayer.isMaxRank()));
		upgradeBtn.disableProperty().bind(canUpgrade.not());

		// ughhhh this was so tedious to make
		BooleanBinding canTakeRole = Bindings.and(
				Bindings.equal("(none)", Deadwood.currentPlayer.currentRoleProperty()),
				Bindings.isNotEmpty(Deadwood.currentPlayer.getLocation().getRolesProperty(Deadwood.currentPlayer)));
		takeRoleBtn.disableProperty().bind(canTakeRole.not());
	}

	private void updateSidePanel(String newline) {
		gameplayInfo.setText(gameplayInfo.getText() + "\n" + newline);
		gameplayInfo.appendText("");
	}

	private void initDayCounter() {
		daysLeftCounter.textProperty().bind(Bindings.format("Days left: %d", Deadwood.DAYS_REMAINING));
	}

	// Set up the player info table at the bottom of the screen
	private void initPlayerInfoTable() {
		currentPlayerInfo.textProperty().bind(Bindings.format(
				"Player %d (%s)\nRank: %d\nLocation: %s\nRole: %s\nPractice Chips: %d\nCredits: %d\nDollars: %d\n",
				Deadwood.currentPlayer.playerNumProperty(), Deadwood.currentPlayer.playerColorProperty(),
				Deadwood.currentPlayer.rankProperty(), Deadwood.currentPlayer.locationProperty(),
				Deadwood.currentPlayer.currentRoleProperty(), Deadwood.currentPlayer.numPracticeChipsProperty(),
				Deadwood.currentPlayer.numCreditsProperty(), Deadwood.currentPlayer.numDollarsProperty()));
	}

	public void disableAllRooms() {
		// disable all rooms for currentPlayer and then enable the neighbors
		trainStation.setDisable(true);
		jail.setDisable(true);
		generalStore.setDisable(true);
		ranch.setDisable(true);
		church.setDisable(true);
		saloon.setDisable(true);
		bank.setDisable(true);
		hotel.setDisable(true);
		secretHideout.setDisable(true);
		castingOffice.setDisable(true);
		trailers.setDisable(true);
		mainStreet.setDisable(true);
	}

	@FXML
	public void promptMove(MouseEvent e) {

		disableAllRooms();

		String[] neighborNames = Deadwood.currentPlayer.getLocation().getNeighbors();
		for (String name : neighborNames) {
			Polygon roombtn = getButtonFromRoom(name);
			roombtn.setDisable(false);

		}
		updateSidePanel("Choose the room you want to move to.");
		promptMove = true;
	}

	private Polygon getButtonFromRoom(String name) {
		if (name.equals("Train Station")) {
			return trainStation;
		} else if (name.equals("Jail")) {
			return jail;
		} else if (name.equals("Casting Office")) {
			return castingOffice;
		} else if (name.equals("Secret Hideout")) {
			return secretHideout;
		} else if (name.equals("Ranch")) {
			return ranch;
		} else if (name.equals("General Store")) {
			return generalStore;
		} else if (name.equals("Saloon")) {
			return saloon;
		} else if (name.equals("Bank")) {
			return bank;
		} else if (name.equals("Church")) {
			return church;
		} else if (name.equals("Hotel")) {
			return hotel;
		} else if (name.equals("Main Street")) {
			return mainStreet;
		} else {
			return trailers;
		}
	}

	private ImageView getSceneBtnFromRoom(String name) {
		if (name.equals("Train Station")) {
			return tsScene;
		} else if (name.equals("Jail")) {
			return jScene;
		} else if (name.equals("Secret Hideout")) {
			return shScene;
		} else if (name.equals("Ranch")) {
			return rScene;
		} else if (name.equals("General Store")) {
			return gsScene;
		} else if (name.equals("Saloon")) {
			return sScene;
		} else if (name.equals("Bank")) {
			return bScene;
		} else if (name.equals("Church")) {
			return cScene;
		} else if (name.equals("Hotel")) {
			return hScene;
		} else {
			return msScene;
		}
	}

	private Room getRoomFromButton(String ID) {
		if (ID.equals("trainStation")) {
			return Board.lookUpRoom("Train Station");
		} else if (ID.equals("jail")) {
			return Board.lookUpRoom("Jail");
		} else if (ID.equals("castingOffice")) {
			return Board.lookUpRoom("Casting Office");
		} else if (ID.equals("ranch")) {
			return Board.lookUpRoom("Ranch");
		} else if (ID.equals("saloon")) {
			return Board.lookUpRoom("Saloon");
		} else if (ID.equals("secretHideout")) {
			return Board.lookUpRoom("Secret Hideout");
		} else if (ID.equals("bank")) {
			return Board.lookUpRoom("Bank");
		} else if (ID.equals("church")) {
			return Board.lookUpRoom("Church");
		} else if (ID.equals("hotel")) {
			return Board.lookUpRoom("Hotel");
		} else if (ID.equals("mainStreet")) {
			return Board.lookUpRoom("Main Street");
		} else if (ID.equals("generalStore")) {
			return Board.lookUpRoom("General Store");
		} else
			return Board.lookUpRoom("Trailers");
	}

	@FXML
	public void movePlayer(MouseEvent e) {
		if (promptMove) {
			promptMove = false;
			Room source = Deadwood.currentPlayer.getLocation();
			Room dest = getRoomFromButton(((Node) e.getSource()).getId());
			updateSidePanel(String.format("Moved from %s to %s.", Deadwood.currentPlayer.getLocation().getName(),
					dest.getName()));
			Deadwood.currentPlayer.move(dest);
			hasMoved = true;
			updateBindings();
			// not working for some reason
			playerImages[Deadwood.currentPlayer.getPlayerNum()]
					.setLayoutX(e.getSceneX() - player1.getLayoutBounds().getWidth() / 2);
			playerImages[Deadwood.currentPlayer.getPlayerNum()]
					.setLayoutY(e.getSceneY() - player1.getLayoutBounds().getHeight() / 2);
			if (dest instanceof Set && !((Set) dest).isActive()) {
				((Set) dest).activate();
				flipCard((Set) dest);
			}
			for (String neighbor : source.getNeighbors()) {
				getButtonFromRoom(neighbor).setDisable(true);
			}
		}
	}

	private void setPlayerImg(Player player) {
		int rank = player.getRank();
		int num = player.getPlayerNum() + 1;
		char color = ' ';
		if (num == 1) {
			color = 'r';
		} else if (num == 2) {
			color = 'g';
		} else if (num == 3) {
			color = 'o';
		} else if (num == 4) {
			color = 'c';
		} else if (num == 5) {
			color = 'p';
		} else if (num == 6) {
			color = 'b';
		} else if (num == 7) {
			color = 'y';
		} else {
			color = 'v';
		}
		String imageSource = "dice/" + color + rank + ".png";
		playerImages[player.getPlayerNum()].setImage(new Image(getClass().getResource(imageSource).toExternalForm()));
	}

	@FXML
	public void rehearse(MouseEvent e) {
		if (Deadwood.currentPlayer.getNumPracticeChips() + 1 < ((Set) Deadwood.currentPlayer.getLocation())
				.getSceneCard().getBudget()) {
			Deadwood.currentPlayer.rehearse();
			updateSidePanel(
					"You now have " + Deadwood.currentPlayer.getNumPracticeChips() + " practice chips. You need "
							+ (((Set) Deadwood.currentPlayer.getLocation()).getSceneCard().getBudget()
									- (1 + Deadwood.currentPlayer.getNumPracticeChips())
									+ " more for guaranteed success."));
			updateBindings();
			switchActivePlayer();
		} else {
			updateSidePanel("You are guarenteed success. Go Act star!");
		}
	}

	@FXML
	public void act(MouseEvent e) {
		Set currentRoom = ((Set) Deadwood.currentPlayer.getLocation());
		DWScene currentScene = currentRoom.getSceneCard();
		int roll = rollDie();
		int result = roll + Deadwood.currentPlayer.getNumPracticeChips();
		int B = ((Set) Deadwood.currentPlayer.getLocation()).getSceneCard().getBudget();
		updateSidePanel("You rolled a " + result);
		updateSidePanel("The budget is " + B);
		if (result >= B) {
			updateSidePanel("Nice job! You did it!");
			removeShot((Set) Deadwood.currentPlayer.getLocation());
			// if they are working a role off-the-card, they get 1 dollar and 1 credit
			for (Role role : currentRoom.getRoles()) {
				if (Deadwood.currentPlayer.getCurrentRole() == role) {
					Deadwood.currentPlayer.addCredits(1);
					Deadwood.currentPlayer.addDollars(1);
					// if they are working a role on-the-card, they get 2 credits
				} else {
					Deadwood.currentPlayer.addCredits(2);
				}
			}
			// wrap scene
			if (currentRoom.getRemainingShots() == 0) {
				updateSidePanel("That's a wrap!");
				if (currentScene.actorsOnCard()) {
					Deadwood.payout(currentRoom, currentScene);
				}
				for (Role r : currentScene.getRoles()) {
					if (r.isWorked()) {
						r.endRole();
					}
				}
				removeSceneImg(currentRoom);
				currentRoom.removeSceneCard();
				for (Role r : currentRoom.getRoles()) {
					if (r.isWorked()) {
						r.endRole();
					}
				}
				if (Deadwood.BOARD.getSceneCount() == 0) {
					endDay();
				}
			}
		} else {
			updateSidePanel("Oops! You failed to perform the scene. Try again next round.");
			// if they are working on a role off-the-card, they receive 1 dollar (otherwise
			// no reward)
			for (Role role : currentRoom.getRoles()) {
				if (Deadwood.currentPlayer.getCurrentRole() == role) {
					Deadwood.currentPlayer.addDollars(1);
				}
			}
		}
		updateBindings();
		switchActivePlayer();
	}

	private void endDay() {
		updateSidePanel("End of day " + (Deadwood.NUMBER_OF_DAYS - Deadwood.DAYS_REMAINING + 1) + ".");
		// decrement days remaining
		Deadwood.DAYS_REMAINING--;
		if (Deadwood.DAYS_REMAINING == 0) {
			endGame();
		}
		// move players to trailers
		for (int i = 0; i < playerImages.length; i++) {
			Deadwood.PLAYERS.get(i).move(Board.lookUpRoom("Trailers"));
			playerImages[i].setLayoutX(1010 + (i % 4) * 40);
			if (i < 4) {
				playerImages[i].setLayoutY(310);
			} else {
				playerImages[i].setLayoutY(355);
			}
		}
		// re-deal scene cards
		int deckIndex = ((Deadwood.NUMBER_OF_DAYS - Deadwood.DAYS_REMAINING) - 1) * 10;
		for (Room r : Board.getRooms()) {
			if (r instanceof Set) {
				((Set) r).setScene(Deadwood.SCENES[deckIndex]);
				deckIndex++;
			}
		}
		
		resetSceneImgs();
		updateBindings();
	}

	private void resetSceneImgs() {
		tsScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		tsScene.setVisible(true);
		msScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		msScene.setVisible(true);
		gsScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		gsScene.setVisible(true);
		sScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		sScene.setVisible(true);
		hScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		hScene.setVisible(true);
		cScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		cScene.setVisible(true);
		bScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		bScene.setVisible(true);
		shScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		shScene.setVisible(true);
		jScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		jScene.setVisible(true);
		rScene.setImage(new Image(getClass().getResource("cards/CardBack.jpg").toExternalForm()));
		rScene.setVisible(true);
		ts1.setVisible(true);
		ts2.setVisible(true);
		ts3.setVisible(true);
		gs1.setVisible(true); 
		gs2.setVisible(true); 
		ms1.setVisible(true); 
		ms2.setVisible(true); 
		ms3.setVisible(true); 
		j1.setVisible(true); 
		s2.setVisible(true); 
		s1.setVisible(true); 
		r1.setVisible(true); 
		r2.setVisible(true); 
		b1.setVisible(true); 
		c2.setVisible(true); 
		c1.setVisible(true); 
		h1.setVisible(true); 
		h2.setVisible(true); 
		h3.setVisible(true); 
		sh2.setVisible(true); 
		sh1.setVisible(true); 
		sh3.setVisible(true);
	}

	private void endGame() {
		updateSidePanel("You've reached the end!");
		Deadwood.scoring();
		System.exit(0);
	}

	private void removeSceneImg(Set room) {
		ImageView sceneImg = getSceneBtnFromRoom(room.getName());
		sceneImg.setVisible(false);
	}

	// Simply prompts player to select their upgrade and performs the upgrade
	@FXML
	public void upgradeRank(MouseEvent e) {
		if (e.getSource() == upgradeBtn) {
			// reset e
			if (Deadwood.currentPlayer.getRank() < 2) {
				rank2.setDisable(false);
				rank2.setVisible(true);
			}

			if (Deadwood.currentPlayer.getRank() < 3) {
				rank3.setDisable(false);
				rank3.setVisible(true);
			}

			if (Deadwood.currentPlayer.getRank() < 4) {
				rank4.setDisable(false);
				rank4.setVisible(true);
			}

			if (Deadwood.currentPlayer.getRank() < 5) {
				rank5.setDisable(false);
				rank5.setVisible(true);
			}

			if (Deadwood.currentPlayer.getRank() < 6) {
				rank6.setDisable(false);
				rank6.setVisible(true);
			}

			updateSidePanel("What rank would you like to upgrade to?");
			updateSidePanel("(Click on the rank in Casting Office)");
			desiredRank = true;
		}
	}

	@FXML
	public void disableRanks() {
		rank2.setDisable(true);
		rank2.setVisible(false);
		rank3.setDisable(true);
		rank3.setVisible(false);
		rank4.setDisable(true);
		rank4.setVisible(false);
		rank5.setDisable(true);
		rank5.setVisible(false);
		rank6.setDisable(true);
		rank6.setVisible(false);
	}

	@FXML
	public void desiredRank(MouseEvent e) {
		if (desiredRank) {
			disableRanks();
			desiredRank = false;
			if (e.getSource() == rank2) {

				updateSidePanel("You have selected rank 2");
				updateSidePanel("Click on what you would like to spend");
				dollar2.setDisable(false);
				credit2.setDisable(false);
				dollar2.setVisible(true);
				credit2.setVisible(true);
				paymentMethod = true;
			} else if (e.getSource() == rank3) {
				updateSidePanel("You have selected rank 3");
				updateSidePanel("Click on what you would like to spend");
				dollar3.setDisable(false);
				credit3.setDisable(false);
				dollar3.setVisible(true);
				credit3.setVisible(true);
				paymentMethod = true;
			} else if (e.getSource() == rank4) {
				updateSidePanel("You have selected rank 4");
				updateSidePanel("Click on what you would like to spend");
				dollar4.setDisable(false);
				credit4.setDisable(false);
				dollar4.setVisible(true);
				credit4.setVisible(true);
				paymentMethod = true;
			} else if (e.getSource() == rank5) {
				updateSidePanel("You have selected rank 5");
				updateSidePanel("Click on what you would like to spend");
				dollar5.setDisable(false);
				credit5.setDisable(false);
				dollar5.setVisible(true);
				credit5.setVisible(true);
				paymentMethod = true;
			} else if (e.getSource() == rank6) {
				updateSidePanel("You have selected rank 6");
				updateSidePanel("Click on what you would like to spend");
				dollar6.setDisable(false);
				credit6.setDisable(false);
				dollar6.setVisible(true);
				credit6.setVisible(true);
				paymentMethod = true;
			} else {
				updateSidePanel("Please select a rank to upgrade or choose a different action");
			}
		}
	}

	private int rollDie() {
		Random rand = new Random();
		int roll = (int) ((Math.random() * 6) + 1);
		String imageSource = "dice/w" + roll + ".png";
		dieImg.setImage(new Image(getClass().getResource(imageSource).toExternalForm()));
		dieImg.setLayoutX((double) (rand.nextInt((int) boardImg.getLayoutBounds().getCenterX())));
		dieImg.setLayoutY((double) (rand.nextInt((int) boardImg.getLayoutBounds().getCenterY())));
		return roll;
	}

	@FXML
	public void disablePayment() {
		credit2.setDisable(true);
		dollar2.setVisible(false);
		credit2.setVisible(false);

		dollar3.setDisable(true);
		credit3.setDisable(true);
		dollar3.setVisible(false);
		credit3.setVisible(false);

		dollar4.setDisable(true);
		credit4.setDisable(true);
		dollar4.setVisible(false);
		credit4.setVisible(false);

		dollar5.setDisable(true);
		credit5.setDisable(true);
		dollar5.setVisible(false);
		credit5.setVisible(false);

		dollar6.setDisable(true);
		credit6.setDisable(true);
		dollar6.setVisible(false);
		credit6.setVisible(false);
	}

	@FXML
	public void paymentMethod(MouseEvent e) {
		if (paymentMethod) {
			paymentMethod = false;

			if (e.getSource() == dollar2) {
				// check if currentPlayer has enough dollars
				if (Deadwood.currentPlayer.getNumDollars() < 4) {
					updateSidePanel("Looks like you can't afford that!");
				} else {
					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 2");
					Deadwood.upgradePlayerRank(2, "dollars");
					setPlayerImg(Deadwood.currentPlayer);
				}

			} else if (e.getSource() == credit2) {
				// check if currentPlayer has enough credits
				if (Deadwood.currentPlayer.getNumCredits() < 5) {
					updateSidePanel("Looks like you can't afford that!");
				} else {
					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 2");
					Deadwood.upgradePlayerRank(2, "credits");
					setPlayerImg(Deadwood.currentPlayer);
				}
			}

			if (e.getSource() == dollar3) {

				if (Deadwood.currentPlayer.getNumDollars() < 10) {
					updateSidePanel("Looks like you can't afford that!");
				} else {
					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 3");
					Deadwood.upgradePlayerRank(3, "dollars");
					setPlayerImg(Deadwood.currentPlayer);
				}

			} else if (e.getSource() == credit3) {
				if (Deadwood.currentPlayer.getNumCredits() < 10) {
					updateSidePanel("Looks like you can't afford that!");
				} else {
					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 3");
					Deadwood.upgradePlayerRank(3, "credits");
					setPlayerImg(Deadwood.currentPlayer);
				}
			}

			if (e.getSource() == dollar4) {

				if (Deadwood.currentPlayer.getNumDollars() < 18) {
					updateSidePanel("Looks like you can't afford that!");
				} else {

					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 4");
					Deadwood.upgradePlayerRank(4, "dollars");
					setPlayerImg(Deadwood.currentPlayer);
				}

			} else if (e.getSource() == credit4) {
				if (Deadwood.currentPlayer.getNumCredits() < 15) {
					updateSidePanel("Looks like you can't afford that!");
				} else {
					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 4");
					Deadwood.upgradePlayerRank(4, "credits");
					setPlayerImg(Deadwood.currentPlayer);
				}
			}

			if (e.getSource() == dollar5) {
				if (Deadwood.currentPlayer.getNumDollars() < 28) {
					updateSidePanel("Looks like you can't afford that!");
				} else {

					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 5");
					Deadwood.upgradePlayerRank(5, "dollars");
					setPlayerImg(Deadwood.currentPlayer);
				}

			} else if (e.getSource() == credit5) {
				if (Deadwood.currentPlayer.getNumCredits() < 20) {
					updateSidePanel("Looks like you can't afford that!");
				} else {
					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 5");
					Deadwood.upgradePlayerRank(5, "credits");
					setPlayerImg(Deadwood.currentPlayer);
				}
			}

			if (e.getSource() == dollar6) {

				if (Deadwood.currentPlayer.getNumDollars() < 40) {
					updateSidePanel("Looks like you can't afford that!");
				} else {

					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 6");
					Deadwood.upgradePlayerRank(6, "dollars");
					setPlayerImg(Deadwood.currentPlayer);
				}

			} else if (e.getSource() == credit6) {

				if (Deadwood.currentPlayer.getNumCredits() < 25) {
					updateSidePanel("Looks like you can't afford that!");
				} else {
					updateSidePanel("Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + " has upgraded from rank "
							+ Deadwood.currentPlayer.getRank() + " to rank 6");
					Deadwood.upgradePlayerRank(6, "credits");
					setPlayerImg(Deadwood.currentPlayer);
				}
			}
		}
		disablePayment();
		updateBindings();
	}

	@FXML
	public void takeRole(MouseEvent e) {
		chooseRole.setVisible(true);
		Set location = (Set) Deadwood.currentPlayer.getLocation();
		ObservableList<Role> roles = location.getRolesProperty(Deadwood.currentPlayer);
		ObservableList<String> options = FXCollections.observableArrayList();
		for (Role r : roles) {
			options.add(r.getName() + ", rank " + r.getRank());
		}
		chooseRole.setItems(options);
		// get the user's selection from the list
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String roleName = chooseRole.getValue();
				for (Role r : roles) {
					if (roleName != null && roleName.toLowerCase().startsWith(r.getName().toLowerCase())) {
						Deadwood.currentPlayer.setCurrentRole(r);
						updateSidePanel(String.format("You are now working %s in %s, scene %s.", r.getName(),
								((Set) Deadwood.currentPlayer.getLocation()).getSceneCard().getTitle(),
								((Set) Deadwood.currentPlayer.getLocation()).getSceneCard().getSceneNum()));
						break;
					}
				}
				chooseRole.getItems().clear();
				chooseRole.setVisible(false);
				updateBindings();
			}
		};
		chooseRole.setOnAction(event);
		tookRole = true;
	}

	@FXML
	public void end(MouseEvent e) {
		switchActivePlayer();
	}

	private void switchActivePlayer() {
		updateSidePanel("End of Player " + (Deadwood.currentPlayer.getPlayerNum() + 1) + "'s turn.");
		playerImages[Deadwood.currentPlayer.getPlayerNum()].setEffect(null);
		Deadwood.endTurn = true;
		if (Deadwood.currentPlayer.getPlayerNum() == Deadwood.PLAYERS.size() - 1) {
			Deadwood.currentPlayer = Deadwood.PLAYERS.get(0);
		} else {
			Deadwood.currentPlayer = Deadwood.PLAYERS.get(Deadwood.currentPlayer.getPlayerNum() + 1);
		}
		playerImages[Deadwood.currentPlayer.getPlayerNum()].setEffect(borderGlow);
		hasMoved = false;
		tookRole = false;
		chooseRole.setVisible(false);
		updateBindings();
	}

	@FXML
	public void quit(MouseEvent e) {
		if (Deadwood.PLAYERS != null)
			updateSidePanel("Player " + Deadwood.currentPlayer.getPlayerNum() + " has ended the game :(");
		System.exit(0);
	}
}
