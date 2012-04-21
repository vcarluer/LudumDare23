package org.devince.tinyworld.items;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends GameItem {
	
	private static final int RIGHT = 1;
	private static final int LEFT = -1;
	private static final int NONE = 0;
	private static final int Acceleration_Base = 20;
	private static final float DAMP = 0.9f;
	private static final float MAX_VELOCITY = 6f;
	private int direction;
	
	private Vector2 acceleration;
	private Vector2 velocity;

	public Player(float x, float y) {
		this.setSprite("data/player.png");
		this.x = x;
		this.y = y;
		
		this.acceleration = new Vector2();
		this.velocity = new Vector2();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.RIGHT:
			this.direction = RIGHT;
			break;
		case Keys.LEFT:
			this.direction = LEFT;
			break;
		default:
			return false;
		}
		
		this.acceleration.x = this.direction * Acceleration_Base;
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean cancelMove = false;
		switch(keycode) {
		case Keys.RIGHT:
			if (this.direction == RIGHT) {
				cancelMove = true;
			}
			
			break;
		case Keys.LEFT:
			if (this.direction == LEFT) {
				cancelMove = true;
			}
			
			break;
		default:
			return false;
		}
		
		if (cancelMove) {
			this.direction = NONE;
			this.acceleration.x = 0;
		}
		
		return true;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (this.acceleration.x != 0) {
			float acceTime = this.acceleration.x * delta;
			this.velocity.x += acceTime;
		} else {
			this.velocity.x *= DAMP; // not based on delta here...
		}
		
		if (this.velocity.x > MAX_VELOCITY) {
			this.velocity.x = MAX_VELOCITY;
		}
		
		if (this.velocity.x < - MAX_VELOCITY) {
			this.velocity.x = - MAX_VELOCITY;
		}
		
		this.x += this.velocity.x;
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected float getRefereceWidth() {
		return 8;
	}

	@Override
	protected float getReferenceHeight() {
		return 8;
	}

}
