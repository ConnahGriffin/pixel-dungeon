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
package com.pdextended.pixeldungeonextended.items.scrolls;

import com.pdextended.noosa.audio.Sample;
import com.pdextended.pixeldungeonextended.Assets;
import com.pdextended.pixeldungeonextended.actors.buffs.Invisibility;
import com.pdextended.pixeldungeonextended.actors.buffs.Weakness;
import com.pdextended.pixeldungeonextended.actors.hero.Hero;
import com.pdextended.pixeldungeonextended.effects.Flare;
import com.pdextended.pixeldungeonextended.effects.particles.ShadowParticle;
import com.pdextended.pixeldungeonextended.items.Item;
import com.pdextended.pixeldungeonextended.utils.GLog;

public class ScrollOfRemoveCurse extends Scroll {

	private static final String TXT_PROCCED	= 
		"Your pack glows with a cleansing light, and a malevolent energy disperses.";
	private static final String TXT_NOT_PROCCED	= 
		"Your pack glows with a cleansing light, but nothing happens.";
	
	{
		name = "Scroll of Remove Curse";
	}
	
	@Override
	protected void doRead() {
		
		new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		boolean procced = uncurse( curUser, curUser.belongings.backpack.items.toArray( new Item[0] ) ); 
		procced = uncurse( curUser, 
			curUser.belongings.weapon, 
			curUser.belongings.armor, 
			curUser.belongings.ring1, 
			curUser.belongings.ring2 ) || procced;
		
		Weakness.detach( curUser, Weakness.class );
		
		if (procced) {
			GLog.p( TXT_PROCCED );			
		} else {		
			GLog.i( TXT_NOT_PROCCED );		
		}
		
		setKnown();
		
		readAnimation();
	}
	
	@Override
	public String desc() {
		return
			"The incantation on this scroll will instantly strip from " +
			"the reader's weapon, armor, rings and carried items any evil " +
			"enchantments that might prevent the wearer from removing them.";
	}
	
	public static boolean uncurse( Hero hero, Item... items ) {
		
		boolean procced = false;
        for (Item item : items) {
            if (item != null && item.cursed) {
                item.cursed = false;
                procced = true;
            }
        }
		
		if (procced) {
			hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
		}
		
		return procced;
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
