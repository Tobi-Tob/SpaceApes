package testUtils;

import org.newdawn.slick.Input;

public class TestInput extends Input {
	
	private Integer[] pressedKeys;
	private Integer[] downKeys;
	private int mouseButtonPressed;
	private int mouseButtonDown;
	
	private int mouseX, mouseY;


	public TestInput() {
		super(0);
		this.clearPressedKeys();
		this.clearDownKeys();
		this.clearMouseButtonPressed();
		this.clearMouseButtonDown();
		mouseX = mouseY = 0;
	}
	
	@Override
	public boolean isMouseButtonDown(int button){
		if(this.mouseButtonDown ==- 1) return false;
		if(this.mouseButtonDown == button){
			this.clearMouseButtonDown();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isMousePressed(int button){
		if(this.mouseButtonPressed ==- 1) return false;
		if(this.mouseButtonPressed == button){
			this.clearMouseButtonPressed();
			return true;
		}
		return false;
	}
	
	@Override
	public int getMouseX() {
		return mouseX;
	}
	
	
	@Override
	public int getMouseY() {
		return mouseY;
	}
	
	
	@Override
	public boolean isKeyDown(int code){
		for(Integer key : downKeys){
			if(key.equals(code)) {
				this.clearDownKeys();
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean isKeyPressed(int code){
		for(Integer key : pressedKeys){
			if(key.equals(code)) {
				this.clearPressedKeys();
				return true;
			}
		}
		return false;
	}
	
	public void setMouseButtonDown(int button){
		this.mouseButtonDown = button;
	}
	
	public void clearMouseButtonDown(){
		this.mouseButtonDown = -1;
	}
	
	public void setMouseButtonPressed(int button){
		this.mouseButtonPressed = button;
	}
	
	public void clearMouseButtonPressed(){
		this.mouseButtonPressed = -1;
	}
	
	
	public void setKeyDown(Integer... keys){
		this.downKeys = keys;
	}
	
	public void clearDownKeys() {
		downKeys = new Integer[0];
		
	}
	
	public void setKeyPressed(Integer... keys){
		this.pressedKeys = keys;
	}
	
	public void clearPressedKeys(){
		this.pressedKeys = new Integer[0];
	}
	
	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}

}
