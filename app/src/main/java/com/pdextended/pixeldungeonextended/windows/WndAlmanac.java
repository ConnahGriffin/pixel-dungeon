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
import com.pdextended.noosa.BitmapTextMultiline;
import com.pdextended.noosa.Image;
import com.pdextended.pixeldungeonextended.Almanac;
import com.pdextended.pixeldungeonextended.effects.AlmanacBanner;
import com.pdextended.pixeldungeonextended.scenes.PixelScene;
import com.pdextended.pixeldungeonextended.ui.Window;

public class WndAlmanac extends Window {

	private static final int WIDTH = 120;
	private static final int MARGIN = 4;

	public WndAlmanac(Almanac.Item_Index almanac ) {
		
		super();
		
		Image icon = AlmanacBanner.image( almanac.image );
		icon.scale.set( 2 );
		add( icon );
		
		BitmapTextMultiline info = PixelScene.createMultiline( almanac.description, 8 );
		info.maxWidth = WIDTH - MARGIN * 2;
		info.measure();
		
		float w = Math.max( icon.width(), info.width() ) + MARGIN * 2;
		
		icon.x = (w - icon.width()) / 2;
		icon.y = MARGIN;
		
		float pos = icon.y + icon.height() + MARGIN;
		for (BitmapText line : info.new LineSplitter().split()) {
			line.measure();
			line.x = PixelScene.align( (w - line.width()) / 2 );
			line.y = PixelScene.align( pos );
			add( line );
			
			pos += line.height(); 
		}

		resize( (int)w, (int)(pos + MARGIN) );
		
		AlmanacBanner.highlight( icon, almanac.image );
	}
}
