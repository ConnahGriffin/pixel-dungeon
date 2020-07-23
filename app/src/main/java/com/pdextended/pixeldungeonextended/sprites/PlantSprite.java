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
package com.pdextended.pixeldungeonextended.sprites;

import com.pdextended.noosa.Game;
import com.pdextended.noosa.Image;
import com.pdextended.noosa.TextureFilm;
import com.pdextended.pixeldungeonextended.Assets;
import com.pdextended.pixeldungeonextended.Dungeon;
import com.pdextended.pixeldungeonextended.DungeonTilemap;
import com.pdextended.pixeldungeonextended.levels.Level;
import com.pdextended.pixeldungeonextended.plants.Plant;

public class PlantSprite extends Image {

	private static final float DELAY = 0.2f;
	
	private enum State {
		GROWING, NORMAL, WITHERING
	}
	private State state = State.NORMAL;
	private float time;
	
	private static TextureFilm frames;
	
	private int pos = -1;
	
	public PlantSprite() {
		super( Assets.PLANTS );
		
		if (frames == null) {
			frames = new TextureFilm( texture, 16, 16 );
		}
		
		origin.set( 8, 12 );
	}
	
	public PlantSprite( int image ) {
		this();
		reset( image );
	}
	
	public void reset( Plant plant ) {
		
		revive();
		
		reset( plant.image );
		alpha( 1f );
		
		pos = plant.pos;
		x = (pos % Level.WIDTH) * DungeonTilemap.SIZE;
		y = (pos / Level.WIDTH) * DungeonTilemap.SIZE;
		
		state = State.GROWING;
		time = DELAY;
	}
	
	public void reset( int image ) {
		frame( frames.get( image ) );
	}
	
	@Override
	public void update() {
		super.update();
		
		visible = pos == -1 || Dungeon.visible[pos];
		
		switch (state) {
		case GROWING:
			if ((time -= Game.elapsed) <= 0) {
				state = State.NORMAL;
				scale.set( 1 );
			} else {
				scale.set( 1 - time / DELAY );
			}
			break;
		case WITHERING:
			if ((time -= Game.elapsed) <= 0) {
				super.kill();
			} else {
				alpha( time / DELAY );
			}
			break;
		default:
		}
	}
	
	@Override
	public void kill() {
		state = State.WITHERING;
		time = DELAY;
	}
}
