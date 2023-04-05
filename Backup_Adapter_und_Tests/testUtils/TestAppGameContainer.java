package testUtils;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TestAppGameContainer extends AppGameContainer {

	public TestAppGameContainer(Game game) throws SlickException {
		super(game, 1200, 900, false);
	}
	
	public TestAppGameContainer(Game game, int width, int height, boolean fullscreen) throws SlickException {
		super(game, width, height, fullscreen);
	}
	
	/**
	 * Analog zu AppGameContainer.start() wird das StateBasedGame
	 * im TestAppGameContainer gestartet, d.h. das Spiel wird ohne
	 * UI gestartet.
	 * 
	 * @param delta Verzoegerung zwischen Frames
	 * 
	 * @throws SlickException
	 */
	public void start(int delta) throws SlickException {

		this.input = new TestInput();
		game.update(this, delta);
		((StateBasedGame)game).initStatesList(this);
		game.init(this);
			
	}
	
	public TestInput getTestInput(){
		return (TestInput) input;
	}
	
	public void updateGame(int delta) throws SlickException{
		game.update(this, delta);
		game.update(this, delta);
	}
	
	@Override
	public void reinit(){
		try {
			game.init(this);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}

