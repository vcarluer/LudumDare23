package org.devince.tinyworld.items;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.world.Galaxy;

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
	protected GameItem me;
	protected boolean enable;
	private Vector2 vecPosition;
	protected String name;
	
	public GameItem() {
		this.width = this.getRefereceWidth();
		this.height = this.getReferenceHeight();
		this.galaxyPoint = new Point();
		this.boundingBox = new Rectangle();
		this.setUid(TinyWorld.get().getNextID());
		this.normal = new Vector2(0, 1);
		this.me = this;
		this.vecPosition = new Vector2();
		
		this.scaleX = 0;
		this.scaleY = 0;
		ScaleTo st = ScaleTo.$(1, 1, 0.1f);
		this.action(st);
		
		this.enable = true;
		this.name = "";
	}
	
	public void destroy() {
		this.destroy(new OnActionCompleted() {
			
			@Override
			public void completed(Action action) {
				TinyWorld.get().addItemToRemove(me);
			}
		});
	}
	
	public void destroy(OnActionCompleted onActionCompleted) {
		this.enable = false;
		ScaleTo st = ScaleTo.$(0, 0, 0.3f);
		this.action(st);
		st.setCompletionListener(onActionCompleted);
	}

	public void draw(SpriteBatch batch, float parentAlpha) {		
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
		this.sprite =new Sprite(this.baseTexture);
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
	
	public Vector2 getPosition() {
		this.vecPosition.x = this.x;
		this.vecPosition.y = this.y;
		return this.vecPosition;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected String getRandomSurname() {
		int size = 27;
		int rand = (int) Math.floor(Math.random() * size);
		String[] names = new String[size];
		names[0] = "Willy Foo Foo";
		names[1] = "Mr. Magnificent";
		names[2] = "Sparky";
		names[3] = "Mister Googlehead";
		names[4] = "Dollface";
		names[5] = "Tons of Fun";
		names[6] = "Mr. Fabulous";
		names[7] = "Captain Awesome";
		names[8] = "Porkchop";
		names[9] = "Captain Obvious";
		names[10] = "Pissy Missy";
		names[11] = "Miracle Boy";
		names[12] = "DangerRoss";
		names[13] = "Tits McGee";
		names[14] = "Sister Boom Boom";
		names[15] = "Sad Keanu";
		names[16] = "Squirt";
		names[17] = "Mr. Wonderful";		
		names[18] = "Awesome McAwesome";
		names[19] = "Joe Kickass";
		names[20] = "Snaggletooth";
		names[21] = "Pissy Baby";
		names[22] = "He Hate Me";
		names[23] = "Spunky";
		names[24] = "Ross the Boss";		
		names[25] = "Weird Beard";
		names[26] = "Spanky";
		
		return "\"" + names[rand] + "\"";
	}
}
