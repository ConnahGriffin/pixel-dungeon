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

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.pdextended.noosa.BitmapText;
import com.pdextended.noosa.Camera;
import com.pdextended.noosa.Game;
import com.pdextended.noosa.Image;
import com.pdextended.noosa.audio.Music;
import com.pdextended.noosa.audio.Sample;
import com.pdextended.noosa.ui.Button;
import com.pdextended.pixeldungeonextended.Assets;
import com.pdextended.pixeldungeonextended.BuildConfig;
import com.pdextended.pixeldungeonextended.PixelDungeonExtended;
import com.pdextended.pixeldungeonextended.effects.BannerSprites;
import com.pdextended.pixeldungeonextended.effects.Fireball;
import com.pdextended.pixeldungeonextended.ui.Archs;
import com.pdextended.pixeldungeonextended.ui.ExitButton;
import com.pdextended.pixeldungeonextended.ui.PrefsButton;

public class TitleScene extends PixelScene {

	private static final String TXT_PLAY		= "Play";
	private static final String TXT_HIGHSCORES	= "Rankings";
	private static final String TXT_BADGES		= "Badges";
	private static final String TXT_ABOUT		= "About";
	private static final String TXT_ALMANAC		= "Almanac";
	
	@Override
	public void create() {
		
		super.create();
		
		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );
		
		float height = title.height + 
			(PixelDungeonExtended.landscape() ? DashboardItem.SIZE : DashboardItem.SIZE * 2);
		
		title.x = (w - title.width()) / 2;
		title.y = (h - height) / 2;
		
		placeTorch( title.x + 18, title.y + 20 );
		placeTorch( title.x + title.width - 18, title.y + 20 );
		
		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = (float)Math.sin( -(time += Game.elapsed) );
			}
			@Override
			public void draw() {
				GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
				super.draw();
				GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
			}
		};
		signs.x = title.x;
		signs.y = title.y;
		add( signs );
		
		DashboardItem btnBadges = new DashboardItem( TXT_BADGES, 3 ) {
			@Override
			protected void onClick() {
				PixelDungeonExtended.switchNoFade( BadgesScene.class );
			}
		};
		add( btnBadges );
		
		DashboardItem btnAbout = new DashboardItem( TXT_ABOUT, 1 ) {
			@Override
			protected void onClick() {
				PixelDungeonExtended.switchNoFade( AboutScene.class );
			}
		};
		add( btnAbout );
		
		DashboardItem btnPlay = new DashboardItem( TXT_PLAY, 0 ) {
			@Override
			protected void onClick() {
				PixelDungeonExtended.switchNoFade( StartScene.class );
			}
		};
		add( btnPlay );
		
		DashboardItem btnHighscores = new DashboardItem( TXT_HIGHSCORES, 2 ) {
			@Override
			protected void onClick() { PixelDungeonExtended.switchNoFade( RankingsScene.class );
			}
		};
		add( btnHighscores );

        DashboardItem btnAlmanac = new DashboardItem( TXT_ALMANAC, 4 ) {
            @Override
            protected void onClick() { PixelDungeonExtended.switchNoFade( AlmanacScene.class );
            }
        };
		add( btnAlmanac );
		
		BitmapText version = new BitmapText( "v " + Game.version, font1x );
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width();
		version.y = h - version.height();
		add( version );
		
		PrefsButton btnPrefs = new PrefsButton();
		btnPrefs.setPos( 0, 0 );
		add( btnPrefs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( w - btnExit.width(), 0 );
		add( btnExit );

		if (PixelDungeonExtended.landscape()) {
			float y = (h + height) / 2 - DashboardItem.SIZE;
			btnHighscores	.setPos( w / 2 - btnHighscores.width(), y );
			btnBadges		.setPos( w / 2, y );
			btnPlay			.setPos( btnHighscores.left() - btnPlay.width(), y );
			btnAbout		.setPos( btnBadges.right(), y );
			btnAlmanac		.setPos( 0, btnPrefs.height() + btnAlmanac.height() );
		} else {
			btnBadges.setPos( w / 2 - btnBadges.width(), (h + height) / 2 - DashboardItem.SIZE );
			btnAbout.setPos( w / 2, (h + height) / 2 - DashboardItem.SIZE );
			btnPlay.setPos( w / 2 - btnPlay.width(), btnAbout.top() - DashboardItem.SIZE );
			btnHighscores.setPos( w / 2, btnPlay.top() );
			btnAlmanac		.setPos( 0, btnPrefs.height() + btnAlmanac.height() );
		}

		fadeIn();
	}
	
	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}
	
	private static class DashboardItem extends Button {

		public static final float SIZE	= 48;
		
		private static final int IMAGE_SIZE	= 32;
		
		private Image image;
		private BitmapText label;
		
		public DashboardItem( String text, int index ) {
            super();

            image.frame(image.texture.uvRect(index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE));
            this.label.text(text);
            this.label.measure();
            if (!text.equals(TXT_ALMANAC)) {
                setSize(SIZE, SIZE);
            } else {
                setSize(30, 10);
            }
        }
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			image = new Image( Assets.DASHBOARD );
			add( image );
			
			label = createText( 9 );
			add( label );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			image.x = align( x + (width - image.width()) / 2 );
			image.y = align( y );
			
			label.x = align( x + (width - label.width()) / 2 );
			label.y = align( image.y + image.height() +2 );
		}
		
		@Override
		protected void onTouchDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 0.8f );
		}
		
		@Override
		protected void onTouchUp() {
			image.resetColor();
		}
	}
}