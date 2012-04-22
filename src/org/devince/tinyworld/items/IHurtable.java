package org.devince.tinyworld.items;

public interface IHurtable {
	int getLife();
	void hurt(GameItem from);
}
