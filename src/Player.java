import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

public class Player {
	
	private int numCredits;
	private int numDollars;
	private int rank;
	private Role currentRole;
	private Room location;
	private int numPracticeChips;
	private int numPlayer;
	private boolean maxRank;
	
	/* Used for modifications to gameplay due to having more or less than 4 players */
	public Player(int numCredits, int rank, int index) {
		this.numCredits = numCredits;
		this.numDollars = 0;
		this.rank = rank;
		this.numPracticeChips = 0;
		this.location = Board.lookUpRoom("Trailers");
		this.numPlayer = index;
		this.maxRank = false;
	}
	
	/* Standard setup for 4 players */
	public Player(int index) {
		this.numCredits = 0;
		this.numDollars = 0;
		this.rank = 1;
		this.location = Board.lookUpRoom("Trailers");
		this.numPlayer = index;
	}
	
	/* Boolean for binding the "rehearse" button to be disabled unless the following is true */
	public ObservableBooleanValue getCanRehearse() {
		return new SimpleBooleanProperty((this.currentRole != null) && (this.location instanceof Set) && (this.numPracticeChips + 1) < ((Set) this.location).getSceneCard().getBudget());
	}
	/* Boolean for binding "upgrade" button */
	public ObservableBooleanValue getCanUpgrade() {
		return new SimpleBooleanProperty((this.location.equals(Board.lookUpRoom("Casting Office"))));
	}
	/* Boolean for binding "take role" button */
	
	/* Property getters for gameplayInfo table in GUI */
	
	public SimpleIntegerProperty playerNumProperty() {
		return new SimpleIntegerProperty(this.numPlayer + 1);
	}
	
	public SimpleIntegerProperty rankProperty() {
		return new SimpleIntegerProperty(this.rank);
	}
	
	public SimpleStringProperty locationProperty() {
		return new SimpleStringProperty(this.location.getName());
	}
	public SimpleStringProperty isWorkingProperty() {
		String result = "";
		if(this.isWorking()) {
			result = "yes";
		} else {
			result = "no";
		}
		return new SimpleStringProperty(result);
	}
	public SimpleStringProperty currentRoleProperty() {
		String resultString = "(none)";
		if(this.currentRole != null) {
			resultString = this.currentRole.getName() + ", " + this.numPracticeChips + "practice chips";
		}
		return new SimpleStringProperty(resultString);
	}
	
	public SimpleIntegerProperty numCreditsProperty() {
		return new SimpleIntegerProperty(this.numCredits);
	}
	
	public SimpleIntegerProperty numDollarsProperty() {
		return new SimpleIntegerProperty(this.numDollars);
	}
	
	public SimpleIntegerProperty numPracticeChipsProperty() {
		return new SimpleIntegerProperty(this.numPracticeChips);
	}
	
	//To get BooleanBinding true or false the hardcode way
	public SimpleStringProperty easy() {
		return new SimpleStringProperty("true");
	}	
	
	public SimpleStringProperty playerColorProperty() {
		SimpleStringProperty color = new SimpleStringProperty();
		if(this.numPlayer == 0) {
			color.setValue("red");
		} else if(this.numPlayer == 1) {
			color.setValue("green");
		} else if(this.numPlayer == 2) {
			color.setValue("orange");
		} else  if(this.numPlayer == 3) {
			color.setValue("cyan");
		} else if(this.numPlayer == 4) {
			color.setValue("pink");
		} else if(this.numPlayer == 5) {
			color.setValue("blue");
		} else if(this.numPlayer == 6) {
			color.setValue("yellow");
		} else if(this.numPlayer == 7) {
			color.setValue("purple");
		}
		return color;
	}
	
	/* Getters */
	public int getNumCredits() {
		return this.numCredits;
	}
	
	public int getNumDollars() {
		return this.numDollars;
	}
	
	public int getRank() {
		return rank;
	}
	
	public boolean isMaxRank() {
		if(this.rank == 6) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isWorking() {
		return this.currentRole != null;
	}
	
	public Role getCurrentRole() {
		return this.currentRole;
	}
	
	public Room getLocation() {
		return this.location;
	}
	
	public int getNumPracticeChips() {
		return this.numPracticeChips;
	}
	
	/* Setters */
	public void addCredits(int credits) {
		this.numCredits += credits;
	}
	
	public void addDollars(int dollars) {
		this.numDollars += dollars;
	}
	
	// based on the original Deadwood boardgame's price list for upgrading rank
	public int upgradeRank(int rank) {
		int success = 0;
		if(rank == 2) {
			if(this.numCredits >= 5 || this.numDollars >= 4) {
				success = 1;
				this.rank = 2;
			}
		} else if(rank == 3) {
			if(this.numCredits >= 10 || this.numDollars >= 10) {
				success = 1;
				this.rank = 3;
			}
		} else if(rank == 4) {
			if(this.numCredits >= 15 || this.numDollars >= 18) {
				success = 1;
				this.rank = 4;
			}
		} else if(rank == 5) {
			if(this.numCredits >= 20 || this.numDollars >= 28) {
				success = 1;
				this.rank = 5;
			}
		} else if(rank == 6) {
			if(this.numCredits >= 25 || this.numDollars >= 40) {
				success = 1;
				this.rank = 6;
			}
		}
		return success;
	}
	
	public void setCurrentRole(Role role) {
		if(role != null) {
			role.setActor(this);
		} else {
			this.numPracticeChips = 0;
		}
		this.currentRole = role;
	}
	
	public int move(Room room) {
		this.location = room;
		return 1;
	}
	
	public void rehearse() {
		this.numPracticeChips++;
	}
	
	public void modifyDollars(int dollars) {
		this.numDollars = this.numDollars - dollars;
	}
	
	public void modifyCredits(int credits) {
		this.numCredits = this.numCredits - credits;
	}

	public int getPlayerNum() {
		return this.numPlayer;
	}
	
}
