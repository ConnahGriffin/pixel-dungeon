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
package com.pdextended.pixeldungeonextended.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.pdextended.pixeldungeonextended.Almanac;
import com.pdextended.utils.Bundle;
import com.pdextended.utils.Random;

public class ItemStatusHandler<T extends Item> {

	private final Class<? extends T>[] items;
	
	private final HashMap<Class<? extends T>, Integer> images;
	private final HashMap<Class<? extends T>, String> labels;
	private final HashSet<Class<? extends T>> known;
	
	public ItemStatusHandler( Class<? extends T>[] items, String[] allLabels, Integer[] allImages ) {
		
		this.items = items;
		
		this.images = new HashMap<>();
		this.labels = new HashMap<>();
		known = new HashSet<>();
		
		ArrayList<String> labelsLeft = new ArrayList<>(Arrays.asList(allLabels));
		ArrayList<Integer> imagesLeft = new ArrayList<>(Arrays.asList(allImages));

		for (Class<? extends T> item : items) {

			int index = Random.Int(labelsLeft.size());

			labels.put(item, labelsLeft.get(index));
			labelsLeft.remove(index);

			images.put(item, imagesLeft.get(index));
			imagesLeft.remove(index);
		}
	}
	
	public ItemStatusHandler( Class<? extends T>[] items, String[] labels, Integer[] images, Bundle bundle ) {
		
		this.items = items;
		
		this.images = new HashMap<>();
		this.labels = new HashMap<>();
		known = new HashSet<>();
		
		restore( bundle, labels, images );
	}
	
	private static final String PFX_IMAGE	= "_image";
	private static final String PFX_LABEL	= "_label";
	private static final String PFX_KNOWN	= "_known";
	
	public void save( Bundle bundle ) {
		for (Class<? extends T> item : items) {
			String itemName = item.toString();
			bundle.put(itemName + PFX_IMAGE, images.get(item));
			bundle.put(itemName + PFX_LABEL, labels.get(item));
			bundle.put(itemName + PFX_KNOWN, known.contains(item));
			Almanac.saveLocal(bundle);
		}
	}
	
	private void restore( Bundle bundle, String[] allLabels, Integer[] allImages ) {
		
		ArrayList<String> labelsLeft = new ArrayList<>(Arrays.asList(allLabels));
		ArrayList<Integer> imagesLeft = new ArrayList<>(Arrays.asList(allImages));

		for (Class<? extends T> item : items) {

			String itemName = item.toString();

			if (bundle.contains(itemName + PFX_LABEL)) {

				String label = bundle.getString(itemName + PFX_LABEL);
				labels.put(item, label);
				labelsLeft.remove(label);

				Integer image = bundle.getInt(itemName + PFX_IMAGE);
				images.put(item, image);
				imagesLeft.remove(image);

				if (bundle.getBoolean(itemName + PFX_KNOWN)) {
					known.add(item);
				}

			} else {

				int index = Random.Int(labelsLeft.size());

				labels.put(item, labelsLeft.get(index));
				labelsLeft.remove(index);

				images.put(item, imagesLeft.get(index));
				imagesLeft.remove(index);

			}
		}
	}
	
	public int image( T item ) {
		return images.get( item.getClass() );
	}
	
	public String label( T item ) {
		return labels.get( item.getClass() );
	}
	
	public boolean isKnown( T item ) {
		return known.contains( item.getClass() );
	}
	
	@SuppressWarnings("unchecked")
	public void know( T item ) {
		known.add( (Class<? extends T>)item.getClass() );
		
		if (known.size() == items.length - 1) {
			for (Class<? extends T> aClass : items) {
				if (!known.contains(aClass)) {
					known.add(aClass);
					break;
				}
			}
		}
	}
	
	public HashSet<Class<? extends T>> known() {
		return known;
	}
	
	public HashSet<Class<? extends T>> unknown() {
		HashSet<Class<? extends T>> result = new HashSet<>();
		for (Class<? extends T> i : items) {
			if (!known.contains( i )) {
				result.add( i );
			}
		}
		return result;
	}
}
