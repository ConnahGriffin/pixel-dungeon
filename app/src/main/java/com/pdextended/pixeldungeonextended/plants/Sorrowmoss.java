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
package com.pdextended.pixeldungeonextended.plants;

import com.pdextended.pixeldungeonextended.Dungeon;
import com.pdextended.pixeldungeonextended.actors.Char;
import com.pdextended.pixeldungeonextended.actors.buffs.Buff;
import com.pdextended.pixeldungeonextended.actors.buffs.Poison;
import com.pdextended.pixeldungeonextended.effects.CellEmitter;
import com.pdextended.pixeldungeonextended.effects.particles.PoisonParticle;
import com.pdextended.pixeldungeonextended.items.potions.PotionOfToxicGas;
import com.pdextended.pixeldungeonextended.sprites.ItemSpriteSheet;

public class Sorrowmoss extends Plant {

	private static final String TXT_DESC = 
		"A Sorrowmoss is a flower (not a moss) with razor-sharp petals, coated with a deadly venom.";
	
	{
		image = 2;
		plantName = "Sorrowmoss";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.affect( ch, Poison.class ).set( Poison.durationFactor( ch ) * (4 + Dungeon.depth / 2) );
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.center( pos ).burst( PoisonParticle.SPLASH, 3 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Sorrowmoss";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_SORROWMOSS;
			
			plantClass = Sorrowmoss.class;
			alchemyClass = PotionOfToxicGas.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
