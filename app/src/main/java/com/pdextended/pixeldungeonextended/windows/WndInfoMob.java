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
package com.pdextended.pixeldungeonextended.windows;

import com.pdextended.noosa.BitmapText;
import com.pdextended.noosa.ui.Component;
import com.pdextended.pixeldungeonextended.actors.mobs.Mob;
import com.pdextended.pixeldungeonextended.scenes.PixelScene;
import com.pdextended.pixeldungeonextended.sprites.CharSprite;
import com.pdextended.pixeldungeonextended.ui.BuffIndicator;
import com.pdextended.pixeldungeonextended.ui.HealthBar;
import com.pdextended.pixeldungeonextended.utils.Utils;

public class WndInfoMob extends WndTitledMessage {

	public WndInfoMob( Mob mob ) {
		
		super( new MobTitle( mob ), desc( mob ) );
		
	}
	
	private static String desc( Mob mob ) {
		
		StringBuilder builder = new StringBuilder( mob.description() );
		
		builder.append("\n\n").append(mob.state.status()).append(".");
		
		return builder.toString();
	}
	
	private static class MobTitle extends Component {
		
		private static final int GAP	= 2;
		
		private final CharSprite image;
		private final BitmapText name;
		private final HealthBar health;
		private final BuffIndicator buffs;
		
		public MobTitle( Mob mob ) {
			
			name = PixelScene.createText( Utils.capitalize( mob.name ), 9 );
			name.hardlight( TITLE_COLOR );
			name.measure();	
			add( name );
			
			image = mob.sprite();
			add( image );
			
			health = new HealthBar();
			health.level( (float)mob.HP / mob.HT );
			add( health );
			
			buffs = new BuffIndicator( mob );
			add( buffs );
		}
		
		@Override
		protected void layout() {
			
			image.x = 0;
			image.y = Math.max( 0, name.height() + GAP + health.height() - image.height );
			
			name.x = image.width + GAP;
			name.y = image.height - health.height() - GAP - name.baseLine();
			
			float w = width - image.width - GAP;
			
			health.setRect( image.width + GAP, image.height - health.height(), w, health.height() ); 
			
			buffs.setPos( 
				name.x + name.width() + GAP, 
				name.y + name.baseLine() - BuffIndicator.SIZE );

			height = health.bottom();
		}
	}
}
