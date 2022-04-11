import org.w3c.dom.Element;

import javafx.scene.image.Image;

public class DWScene {

	private String title;
	private Image icon;
	private int budget;
	private String sceneNum;
	private String desc;
	private Role[] roles;
	private boolean active;

	public DWScene(Element card) {
		this.title = card.getAttribute("name");
		this.icon = new Image(getClass().getResource("cards/" + card.getAttribute("img")).toExternalForm());
		this.budget = Integer.parseInt(card.getAttribute("budget"));
		this.sceneNum = ((Element) card.getElementsByTagName("scene").item(0)).getAttribute("number");
		this.desc = ((Element) card.getElementsByTagName("scene").item(0)).getTextContent().trim();
		this.roles = new Role[card.getElementsByTagName("part").getLength()];
		for (int i = 0; i < roles.length; i++) {
			this.roles[i] = new Role(((Element) card.getElementsByTagName("part").item(i)));
		}
		this.active = false;
	}

	/* Getters */
	public int getBudget() {
		return this.budget;
	}

	public String getTitle() {
		return this.title;
	}

	public String getSceneDesc() {
		return this.desc;
	}

	public String getSceneNum() {
		return this.sceneNum;
	}

	public Role[] getRoles() {
		return this.roles;
	}

	public boolean isActive() {
		return this.active;
	}
	/* Setters */

	public boolean actorsOnCard() {
		boolean result = false;
		for (Role r : this.roles) {
			if (r.isWorked()) {
				result = true;
			}
		}
		return result;
	}

	public String listAvailableRoles(int rank) {
		String result = "";
		if (this.roles[0].getRank() <= rank && !this.roles[0].isWorked()) {
			result = this.roles[0].getName() + ", rank " + this.roles[0].getRank();
		}
		for (int i = 1; i < this.roles.length; i++) {
			if (this.roles[i].getRank() <= rank && !this.roles[i].isWorked()) {
				result += ", " + this.roles[i].getName() + ", rank " + this.roles[i].getRank();
			}
		}
		return result;
	}

	public Role lookUpRole(String desiredRole) {
		for (Role role : roles) {
			if (getTitle().equalsIgnoreCase(desiredRole)) {
				return role;
			}
		}
		return null;
	}

	public Image getImg() {
		return this.icon;
	}

	public void setActive() {
		this.active = true;
	}
}
