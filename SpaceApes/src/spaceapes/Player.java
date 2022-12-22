package spaceapes;

public class Player {
	public final String iD;
	private Ape ape;
	private int coins = 0;
	private boolean isInteractionAllowed;

	/**
	 * Konstruktor fuer ein Spielerobjekt.
	 * 
	 * @param playerID String
	 */
	public Player(String playerID) {

		this.iD = playerID;
	}

	public Ape getApe() {
		if (ape == null) {
			throw new RuntimeException("Spieler wurde kein Affe zugeordnet");
		}
		return ape;
	}

	public void setApe(Ape ape) {
		this.ape = ape;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public boolean isApeAlive() {
		if (ape.getHealth() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isInteractionAllowed() {
		return isInteractionAllowed;
	}
	
	public void setInteractionAllowed(boolean isInteractionAllowed) {
		this.isInteractionAllowed = isInteractionAllowed;
	}
}
