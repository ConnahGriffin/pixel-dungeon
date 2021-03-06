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
import com.pdextended.pixeldungeonextended.Almanac;
import com.pdextended.pixeldungeonextended.PixelDungeonExtended;
import com.pdextended.pixeldungeonextended.items.Item;
import com.pdextended.pixeldungeonextended.items.potions.Potion;
import com.pdextended.pixeldungeonextended.items.scrolls.Scroll;
import com.pdextended.pixeldungeonextended.scenes.GameScene;
import com.pdextended.pixeldungeonextended.scenes.PixelScene;
import com.pdextended.pixeldungeonextended.sprites.ItemSprite;
import com.pdextended.pixeldungeonextended.ui.ScrollPane;
import com.pdextended.pixeldungeonextended.ui.Window;
import com.pdextended.pixeldungeonextended.utils.Utils;
import com.pdextended.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

public class WndAlaman extends WndTabbed {

	private static final int WIDTH_P	= 112;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 128;
	private static final int HEIGHT_L	= 128;

	private static final int ITEM_HEIGHT	= 18;

	private static final int TAB_WIDTH		= 50;


	private static final String TXT_MOBS	= "Mobs";
	private static final String TXT_ITEMS	= "Items";
	private static final String TXT_TITLE	= "Almanac";

	private final BitmapText txtTitle;
	private final ScrollPane list;

	private final ArrayList<ListItem> items = new ArrayList<>();

	private static boolean showItems = true;

	public WndAlaman() {

		super();

		if (PixelDungeonExtended.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
			resize( WIDTH_P, HEIGHT_P );
		}

		txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
		add( txtTitle );

		list = new ScrollPane( new Component() ) {
			@Override
			public void onClick( float x, float y ) {
				int size = items.size();
				for (int i=0; i < size; i++) {
					if (items.get( i ).onClick( x, y )) {
						break;
					}
				}
			}
		};
		add( list );
		list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );

		boolean showItems = WndAlaman.showItems;
		Tab[] tabs = {
			new LabeledTab( TXT_ITEMS ) {
				protected void select( Boolean value ) {
					super.select( value );
					WndAlaman.showItems = value;
					updateList();
				}
			},
			new LabeledTab( TXT_MOBS ) {
				protected void select( Boolean value ) {
					super.select( value );
					WndAlaman.showItems = !value;
					updateList();
				}
			}
		};
		for (Tab tab : tabs) {
			tab.setSize( TAB_WIDTH, tabHeight() );
			add( tab );
		}

		select( showItems ? 0 : 1 );
	}

	private void updateList() {

		txtTitle.text( Utils.format( TXT_TITLE, showItems ? TXT_ITEMS : TXT_MOBS ) );
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width()) / 2 );

		items.clear();

		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );

		float pos = 0;
		for (Class<? extends Item> itemClass : showItems ? Potion.getKnown() : Scroll.getKnown()) {
			ListItem item = new ListItem( itemClass );
			item.setRect( 0, pos, width, ITEM_HEIGHT );
			content.add( item );
			items.add( item );

			pos += item.height();
		}

		for (Class<? extends Item> itemClass : showItems ? Potion.getUnknown() : Scroll.getUnknown()) {
			ListItem item = new ListItem( itemClass );
			item.setRect( 0, pos, width, ITEM_HEIGHT );
			content.add( item );
			items.add( item );

			pos += item.height();
		}

		content.setSize( width, pos );
		list.setSize( list.width(), list.height() );
	}

	private static class ListItem extends Component {
		
		private Item item;
		private boolean identified;
		
		private ItemSprite sprite;
		private BitmapText label;
		
		public ListItem( Class<? extends Item> cl ) {
			super();
			
			try {
				item = cl.newInstance();
				if (identified = item.isIdentified()) {
					sprite.view( item.image(), null );
					label.text( item.name() );
				} else {
					sprite.view( 127, null );
					label.text( item.trueName() );
					label.hardlight( 0xCCCCCC );
				}
			} catch (Exception e) {
				// Do nothing
			}
		}
		
		@Override
		protected void createChildren() {
			sprite = new ItemSprite();
			add( sprite );
			
			label = PixelScene.createText( 8 );
			add( label );
		}
		
		@Override
		protected void layout() {
			sprite.y = PixelScene.align( y + (height - sprite.height) / 2 );
			
			label.x = sprite.x + sprite.width;
			label.y = PixelScene.align( y + (height - label.baseLine()) / 2 );
		}
		
		public boolean onClick( float x, float y ) {
			if (identified && inside( x, y )) {
				GameScene.show( new WndInfoItem( item ) );
				return true;
			} else {
				return false;
			}
		}
	}
}
