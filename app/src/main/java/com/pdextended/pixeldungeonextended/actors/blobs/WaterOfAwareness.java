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
package com.pdextended.pixeldungeonextended.actors.blobs;

import com.pdextended.noosa.audio.Sample;
import com.pdextended.pixeldungeonextended.Assets;
import com.pdextended.pixeldungeonextended.Badges;
import com.pdextended.pixeldungeonextended.Dungeon;
import com.pdextended.pixeldungeonextended.DungeonTilemap;
import com.pdextended.pixeldungeonextended.Journal;
import com.pdextended.pixeldungeonextended.Journal.Feature;
import com.pdextended.pixeldungeonextended.actors.buffs.Awareness;
import com.pdextended.pixeldungeonextended.actors.buffs.Buff;
import com.pdextended.pixeldungeonextended.actors.hero.Hero;
import com.pdextended.pixeldungeonextended.effects.BlobEmitter;
import com.pdextended.pixeldungeonextended.effects.Identification;
import com.pdextended.pixeldungeonextended.effects.Speck;
import com.pdextended.pixeldungeonextended.items.Item;
import com.pdextended.pixeldungeonextended.levels.Level;
import com.pdextended.pixeldungeonextended.levels.Terrain;
import com.pdextended.pixeldungeonextended.scenes.GameScene;
import com.pdextended.pixeldungeonextended.utils.GLog;

public class WaterOfAwareness extends WellWater {

	private static final String TXT_PROCCED =
		"As you take a sip, you feel the knowledge pours into your mind. " +
		"Now you know everything about your equipped items. Also you sense " +
		"all items on the level and know all its secrets.";
	
	@Override
	protected boolean affectHero( Hero hero ) {
		
		Sample.INSTANCE.play( Assets.SND_DRINK );
		emitter.parent.add( new Identification( DungeonTilemap.tileCenterToWorld( pos ) ) );
		
		hero.belongings.observe();
		
		for (int i=0; i < Level.LENGTH; i++) {
			
			int terr = Dungeon.level.map[i];
			if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
				
				Level.set( i, Terrain.discover( terr ) );						
				GameScene.updateMap( i );
				
				if (Dungeon.visible[i]) {
					GameScene.discoverTile( i, terr );
				}
			}
		}
		
		Buff.affect( hero, Awareness.class, Awareness.DURATION );
		Dungeon.observe();

		Dungeon.hero.interrupt();
	
		GLog.p( TXT_PROCCED );
		
		Journal.remove( Feature.WELL_OF_AWARENESS );
		
		return true;
	}
	
	@Override
	protected Item affectItem( Item item ) {
		if (item.isIdentified()) {
			return null;
		} else {
			item.identify();
			Badges.validateItemLevelAquired( item );
			
			emitter.parent.add( new Identification( DungeonTilemap.tileCenterToWorld( pos ) ) );
			
			Journal.remove( Feature.WELL_OF_AWARENESS );
			
			return item;
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );	
		emitter.pour( Speck.factory( Speck.QUESTION ), 0.3f );
	}
	
	@Override
	public String tileDesc() {
		return 
			"Power of knowledge radiates from the water of this well. " +
			"Take a sip from it to reveal all secrets of equipped items.";
	}
}
