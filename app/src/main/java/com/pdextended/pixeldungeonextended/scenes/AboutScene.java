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
package com.pdextended.pixeldungeonextended.scenes;

import android.content.Intent;
import android.net.Uri;

import com.pdextended.input.Touchscreen.Touch;
import com.pdextended.noosa.BitmapTextMultiline;
import com.pdextended.noosa.Camera;
import com.pdextended.noosa.Game;
import com.pdextended.noosa.Image;
import com.pdextended.noosa.TouchArea;
import com.pdextended.pixeldungeonextended.PixelDungeonExtended;
import com.pdextended.pixeldungeonextended.effects.Flare;
import com.pdextended.pixeldungeonextended.ui.Archs;
import com.pdextended.pixeldungeonextended.ui.ExitButton;
import com.pdextended.pixeldungeonextended.ui.Icons;
import com.pdextended.pixeldungeonextended.ui.Window;

public class AboutScene extends PixelScene {
	private static final String TXT_EXT =
		"Pixel Dungeon Extended\n" +
		"Code/Graphic/Design: Connah Griffin\n"+
		"Inspired by the will to pass the semester and more";
	private static final String TXT_WATA =
		"Pixel Dungeon\n" +
		"Code & graphics: Watabou\n" +
		"Music: Cube_Code\n\n" + 
		"This game is inspired by Brian Walker's Brogue. " +
		"Try it on Windows, Mac OS or Linux - it's awesome! ;)\n\n" +
		"Please visit official website for additional info:";
	
	private static final String LNK_WATA = "pixeldungeon.watabou.ru";
	
	@Override
	public void create() {
        super.create();

        BitmapTextMultiline text_ext = createMultiline( TXT_EXT, 7 );
        text_ext.maxWidth = Math.min( Camera.main.width, 120 );
        text_ext.measure();
        add( text_ext );


        BitmapTextMultiline text_wata = createMultiline( TXT_WATA, 7 );
        text_wata.maxWidth = Math.min( Camera.main.width, 120 );
        text_wata.measure();
        add( text_wata );

	    if(!PixelDungeonExtended.landscape()){
		//Connah's section
		text_ext.x = align( (Camera.main.width - text_ext.width()) / 2 );
		text_ext.y = align( (Camera.main.height - text_ext.height()) / 4 );

		//watabou's section
		text_wata.x = text_ext.x;
		text_wata.y = text_ext.y + text_wata.height();

		}else{
	        //Connah's Section
	        text_ext.x = 10;
	        text_ext.y = align( (Camera.main.height - text_ext.height()) / 2 );

	        //Watabou's Section
            text_wata.x = align( Camera.main.width - text_wata.width() - 10 );
            text_wata.y = align( (Camera.main.height - text_wata.height()) / 2 );

        }

        BitmapTextMultiline link = createMultiline( LNK_WATA, 5 );
        link.maxWidth = Math.min( Camera.main.width, 120 );
        link.measure();
        link.hardlight( Window.TITLE_COLOR );
        add( link );

        link.x = text_wata.x;
        link.y = text_wata.y + text_wata.height();

        TouchArea hotArea = new TouchArea( link ) {
            @Override
            protected void onClick( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK_WATA ) );
                Game.instance.startActivity( intent );
            }
        };
        add( hotArea );

        Image wata = Icons.WATA.get();
        wata.x = text_wata.x + text_wata.width() / 3;
        wata.y = text_wata.y - wata.height - 8;
        add( wata );

        new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		PixelDungeonExtended.switchNoFade( TitleScene.class );
	}
}
