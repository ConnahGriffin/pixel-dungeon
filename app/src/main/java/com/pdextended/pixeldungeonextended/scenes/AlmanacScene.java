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

import com.pdextended.noosa.BitmapText;
import com.pdextended.noosa.Camera;
import com.pdextended.noosa.Game;
import com.pdextended.noosa.Image;
import com.pdextended.noosa.audio.Music;
import com.pdextended.noosa.audio.Sample;
import com.pdextended.noosa.ui.Button;
import com.pdextended.pixeldungeonextended.Assets  ;
import com.pdextended.pixeldungeonextended.Almanac;
import com.pdextended.pixeldungeonextended.PixelDungeonExtended;
import com.pdextended.pixeldungeonextended.effects.AlmanacBanner;
import com.pdextended.pixeldungeonextended.ui.Archs;
import com.pdextended.pixeldungeonextended.ui.ExitButton;
import com.pdextended.pixeldungeonextended.ui.Window;
import com.pdextended.pixeldungeonextended.windows.WndAlmanac;
import com.pdextended.utils.Callback;
import com.pdextended.utils.Random;

import java.util.List;

public class AlmanacScene extends PixelScene {

	public static final String TXT_TITLE = "Almanac";
	public static final String TXT_INFO = "Listed";


	@Override
	public void create() {

		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		int pw = (int)Math.min( w, (PixelDungeonExtended.landscape() ? MIN_WIDTH_L : MIN_WIDTH_P) * 3 ) - 16;
		int ph = (int)Math.min( h, (PixelDungeonExtended.landscape() ? MIN_HEIGHT_L : MIN_HEIGHT_P) * 3 ) - 32;

		float size = (float)Math.sqrt( pw * ph / 27f );
		int nCols = (int)Math.ceil( pw / size );
		int nRows = (int)Math.ceil( ph / size );
		size = Math.min( pw / nCols, ph / nRows );

		float left = (w - size * nCols) / 2;
		float top = (h - size * nRows) / 2;

		BitmapText title = PixelScene.createText( TXT_TITLE, 9 );
		title.hardlight( Window.TITLE_COLOR );
		title.measure();
		title.x = align( (w - title.width()) / 2 );
		title.y = align( (top - title.baseLine()) / 2 );
		add( title );

        Almanac.loadGlobal();

        List<Almanac.Item_Index> almanac = Almanac.filtered( true );
        for (int i=0; i < nRows; i++) {
            for (int j=0; j < nCols; j++) {
                int index = i * nCols + j;
                Almanac.Item_Index b = index < almanac.size() ? almanac.get( index ) : null;
                AlmanacButton button = new AlmanacButton( b );
                button.setPos(
                        left + j * size + (size - button.width()) / 2,
                        top + i * size + (size - button.height()) / 2);
                add( button );
            }
        }

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
        Almanac.loadingListener = new Callback() {
            @Override
            public void call() {
                if (Game.scene() == AlmanacScene.this) {
                    PixelDungeonExtended.switchNoFade( AlmanacScene.class );
                }
            }
        };
	}

	@Override
	public void destroy() {
	    Almanac.saveGlobal();
	    Almanac.loadingListener = null;

	    super.destroy();
	}

    @Override
    protected void onBackPressed() {
        PixelDungeonExtended.switchNoFade( TitleScene.class );
    }

    private static class AlmanacButton extends Button {

        private final Almanac.Item_Index almanac;

        private final Image icon;

        public AlmanacButton( Almanac.Item_Index almanac ) {
            super();

            this.almanac = almanac;
            active = (almanac != null);

            icon = active ? AlmanacBanner.image( almanac.image ) : new Image( Assets.LOCKED );
            add(icon);

            setSize( icon.width(), icon.height() );
        }

        @Override
        protected void layout() {
            super.layout();

            icon.x = align( x + (width - icon.width()) / 2 );
            icon.y = align( y + (height - icon.height()) / 2 );
        }

        @Override
        public void update() {
            super.update();

            if (Random.Float() < Game.elapsed * 0.1) {
                AlmanacBanner.highlight( icon, almanac.image );
            }
        }

        @Override
        protected void onClick() {
            Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
            Game.scene().add( new WndAlmanac( almanac ) );
        }
    }
}
