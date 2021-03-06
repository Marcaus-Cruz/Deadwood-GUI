import org.w3c.dom.Element;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Set extends Room {

	private DWScene sceneCard;
	private Role[] roles; //off-card
	// TODO: how to handle shots ? they will have their own "area" field(s) as well
	private int totalShots; //1-3
	private int remainingShots;
	
	private boolean active;
	
	public Set(Element set) {
		super(set);
		this.name = set.getAttribute("name");
		this.totalShots = ((Element) set.getElementsByTagName("takes").item(0)).getElementsByTagName("take").getLength();
		this.remainingShots = this.totalShots;
		this.roles = new Role[((Element) set.getElementsByTagName("parts").item(0)).getElementsByTagName("part").getLength()];
		for(int i = 0; i < roles.length; i++) {
			this.roles[i] = new Role(((Element) set.getElementsByTagName("parts").item(0)).getElementsByTagName("part").item(i));
		}
	}
	
	/* Getters */
	public DWScene getSceneCard() {
		return this.sceneCard;
	}
	
	public Role[] getRoles() {
		return this.roles;
	}
	
	public int getTotalShots() {
		return this.totalShots;
	}
	
	public int getRemainingShots() {
		return this.remainingShots;
	}
	
	/* Setters */
	
	public void setScene(DWScene scene) {
		this.sceneCard = scene;
	}
	
	public void replaceShots() {
		this.remainingShots = totalShots;
	}
	
	public void removeShot() {
		this.remainingShots--;
	}
	
	public void removeSceneCard() {
		this.sceneCard = null;
	}
	
	// Lists the name of each role in this.roles in a comma-separated list
	public String listAvailableRoles(int rank) {
		String result = "";
		if(this.roles[0].getRank() <= rank && !this.roles[0].isWorked()) {
			result = this.roles[0].getName() + ", rank " + this.roles[0].getRank();
		}
		for(int i = 1; i < this.roles.length; i++) {
			if(this.roles[i].getRank() <= rank && !this.roles[i].isWorked()) {
				result += ", " + this.roles[i].getName() + ", rank " + this.roles[i].getRank();
			}
		}
		return result;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	public void activate() {
		this.active = true;
	}
}
