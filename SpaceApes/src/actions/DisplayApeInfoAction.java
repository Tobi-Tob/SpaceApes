package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Ape;

public class DisplayApeInfoAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Ape ape = (Ape) event.getOwnerEntity();
		java.lang.System.out.println(ape.getID());
		java.lang.System.out.println("Health: " + ape.getHealth() + " Energie: " + ape.getEnergy() + " Coins: "
				+ ape.getCoins());
	}

}
