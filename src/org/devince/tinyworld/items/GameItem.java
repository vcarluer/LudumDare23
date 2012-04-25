package org.devince.tinyworld.items;

import java.util.UUID;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;

public abstract class GameItem extends Actor {
	protected Sprite sprite;
	protected Point galaxyPoint;
	protected Rectangle boundingBox;
	private Long uid;
	protected Vector2 normal;
	private GameItem me;
	private boolean enable;
	
	public GameItem() {
		this.width = this.getRefereceWidth();
		this.height = this.getReferenceHeight();
		this.galaxyPoint = new Point();
		this.boundingBox = new Rectangle();
		this.setUid(TinyWorld.get().getNextID());
		this.normal = new Vector2(0, 1);
		this.me = this;
		
		this.scaleX = 0;
		this.scaleY = 0;
		ScaleTo st = ScaleTo.$(1, 1, 0.1f);
		this.action(st);
		
		this.enable = true;
	}
	
	public void destroy() {
		this.enable = false;
		ScaleTo st = ScaleTo.$(0, 0, 0.3f);
		this.action(st);
		st.setCompletionListener(new OnActionCompleted() {
			
			@Override
			public void completed(Action action) {
				TinyWorld.get().addItemToRemove(me);
			}
		});
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(TinyWorld.get().getCamera().combined);
		
		if (this.sprite != null) {
			this.sprite.setPosition(this.x - this.getRefereceWidth() / 2f, this.y - this.getReferenceHeight() / 2f);
			this.sprite.setRotation(this.rotation);
			this.sprite.setScale(this.scaleX, this.scaleY);
			this.sprite.setColor(this.color);
			this.sprite.draw(batch);
		}
	}
	
	protected void setSprite(String path) {
		this.baseTexture = Assets.getTexture(path);
		this.sprite = new Sprite(baseTexture);
	}
	
	protected Texture baseTexture;
	
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
	
	public void createBoundingBox(float x, float y, Rectangle r) {
		r.set(x - this.width / 2f, y - this.height / 2f, this.width, this.height);
	}
	
	public void handleContact(GameItem item) {
	}

	public Long getUid() {
		return uid;
	}

	protected void setUid(Long uid) {
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
	
	public boolean getEnable() {
		return this.enable;
	}
	
	public Sprite getBaseSprite() {
		return this.sprite;
	}
	
	protected Sound sndLoad(String path) {
		return Assets.getSound(path);
	}
}
