package org.devince.tinyworld.items;

import java.util.UUID;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GameItem extends Actor {
	protected Sprite sprite;
	protected Point galaxyPoint;
	protected Rectangle boundingBox;
	private UUID uid;
	protected Vector2 normal;
	
	public GameItem() {
		this.width = this.getRefereceWidth();
		this.height = this.getReferenceHeight();
		this.galaxyPoint = new Point();
		this.boundingBox = new Rectangle();
		this.setUid(UUID.randomUUID());
		this.normal = new Vector2(0, 1);
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(TinyWorld.get().getCamera().combined);
		
		if (this.sprite != null) {
			this.sprite.setPosition(this.x - this.getRefereceWidth() / 2f, this.y - this.getReferenceHeight() / 2f);
			this.sprite.setRotation(this.rotation);
			this.sprite.setScale(this.scaleX, this.scaleY);
			this.sprite.draw(batch);
		}
	}
	
	protected void setSprite(String path) {
		this.sprite = new Sprite(new Texture(Gdx.files.internal(path)));
	}
	
	protected abstract float getRefereceWidth();
	protected abstract float getReferenceHeight();
	
	public Point getGalaxyPoint() {
		TinyWorld.get().getGalaxy().getGalaxyCoordinate(this.galaxyPoint, this.x, this.y);
		return this.galaxyPoint;
	}
	
	public Rectangle getBoundingBox() {
		return this.getBoundingBox(this.x, this.y);
	}
	
	public Rectangle getBoundingBox(float x, float y) {
		this.boundingBox.x = x - this.width / 2f;
		this.boundingBox.y = y - this.height / 2f;
		this.boundingBox.width = this.width;
		this.boundingBox.height = this.height;
		return this.boundingBox;
	}
	
	public Rectangle createBoundingBox(float x, float y) {
		return new Rectangle(x - this.width / 2f, y - this.height / 2f, this.width, this.height);
	}
	
	public void handleContact(GameItem item) {
	}

	public UUID getUid() {
		return uid;
	}

	protected void setUid(UUID uid) {
		this.uid = uid;
	}
	
	public float getMiddleX(Planet planet) {
		if (this.normal.y == 1) {
			return planet.x;
		}
		
		if (this.normal.x == 1) {
			return planet.x + planet.width / 2f + this.width / 2f;
		}
		
		if (this.normal.y == -1) {
			return planet.x;
		}
		
		if (this.normal.x == -1) {
			return planet.x - planet.width / 2f - this.width / 2f;
		}
		
		return 0;
	}
	
	public float getMiddleY(Planet planet) {
		if (this.normal.y == 1) {
			return planet.y + planet.height / 2f + this.height / 2f;
		}
		
		if (this.normal.x == 1) {
			return planet.y;
		}
		
		if (this.normal.y == -1) {
			return planet.y - planet.height / 2f - this.height / 2f;
		}
		
		if (this.normal.x == -1) {
			return planet.y;
		}
		
		return 0;
	}
	
	public void setNormal(int direction) {
		switch (direction) {
		case Galaxy.TOP:
			this.normal.x = 0;
			this.normal.y = 1;
			break;
		case Galaxy.RIGHT:
			this.normal.x = 1;
			this.normal.y = 0;
			break;
		case Galaxy.BOTTOM:
			this.normal.x = 0;
			this.normal.y = -1;
			break;
		case Galaxy.LEFT:
			this.normal.x = -1;
			this.normal.y = 0;
			break;
		}
	}
}
