package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

public class Sun extends Planet {

	public Sun(int x, int y) {
		super(x, y);
	}

	@Override
	protected String getSpritePath() {
		return "data/sun.png";
	}

	@Override
	public void handleContact(GameItem item) {
		if (item instanceof Player) {
			Player p = (Player) item;
			p.hurt(this);
			if (TinyWorld.get().isTier1()) {
				p.hurt(this);
			}
			if (TinyWorld.get().isTier2()) {
				p.hurt(this);
			}
		}
		
		super.handleContact(item);
	}
	
	 @Override
	 protected boolean getPrimary() {
		return true;
	}
}
