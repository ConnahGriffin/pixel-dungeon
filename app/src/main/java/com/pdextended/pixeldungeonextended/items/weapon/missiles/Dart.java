/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.pdextended.pixeldungeonextended.items.weapon.missiles;

import com.pdextended.pixeldungeonextended.items.Item;
import com.pdextended.pixeldungeonextended.sprites.ItemSpriteSheet;
import com.pdextended.utils.Random;

public class Dart extends MissileWeapon {

	{
		name = "dart";
		image = ItemSpriteSheet.DART;
	}
	
	public Dart() {
		this( 1 );
	}
	
	public Dart( int number ) {
		super();
		quantity = number;
	}
	
	@Override
	public int min() {
		return 1;
	}
	
	@Override
	public int max() {
		return 4;
	}
	
	@Override
	public String desc() {
		return 
			"These simple metal spikes are weighted to fly true and " +
			"sting their prey with a flick of the wrist.";
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 5, 15 );
		return this;
	}
	
	@Override
	public int price() {
		return quantity * 2;
	}
}
