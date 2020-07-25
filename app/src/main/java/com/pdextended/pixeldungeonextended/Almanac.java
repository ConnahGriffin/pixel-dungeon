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
package com.pdextended.pixeldungeonextended;

import com.pdextended.noosa.Game;
import com.pdextended.pixeldungeonextended.actors.mobs.Acidic;
import com.pdextended.pixeldungeonextended.actors.mobs.Albino;
import com.pdextended.pixeldungeonextended.actors.mobs.Bandit;
import com.pdextended.pixeldungeonextended.actors.mobs.Mob;
import com.pdextended.pixeldungeonextended.actors.mobs.Senior;
import com.pdextended.pixeldungeonextended.actors.mobs.Shielded;
import com.pdextended.pixeldungeonextended.items.Item;
import com.pdextended.pixeldungeonextended.items.bags.ScrollHolder;
import com.pdextended.pixeldungeonextended.items.bags.SeedPouch;
import com.pdextended.pixeldungeonextended.items.bags.WandHolster;
import com.pdextended.pixeldungeonextended.items.potions.Potion;
import com.pdextended.pixeldungeonextended.items.rings.Ring;
import com.pdextended.pixeldungeonextended.items.rings.RingOfHaggler;
import com.pdextended.pixeldungeonextended.items.rings.RingOfThorns;
import com.pdextended.pixeldungeonextended.items.scrolls.Scroll;
import com.pdextended.pixeldungeonextended.items.wands.Wand;
import com.pdextended.pixeldungeonextended.scenes.PixelScene;
import com.pdextended.pixeldungeonextended.utils.GLog;
import com.pdextended.utils.Bundle;
import com.pdextended.utils.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Almanac {

    public enum Item_Index {;
        //add new entries here

        public boolean meta;

        public String description;
        public int image;

        Item_Index(String description, int image) {
            this(description, image, false);
        }

        Item_Index(String description, int image, boolean meta) {
            this.description = description;
            this.image = image;
            this.meta = meta;
        }

        Item_Index() {
            this("", -1);
        }
    }

    private static HashSet<Item_Index> global;
    private static HashSet<Item_Index> local = new HashSet<>();

    private static boolean saveNeeded = false;

    public static Callback loadingListener = null;

    public static void reset() {
        local.clear();
        loadGlobal();
    }

    private static final String ALMANAC_FILE = "almanac.dat";
    private static final String ALMANAC = "almanac";

    private static HashSet<Item_Index> restore(Bundle bundle) {
        HashSet<Item_Index> almanac = new HashSet<>();

        String[] names = bundle.getStringArray(ALMANAC);
        for (String name : names) {
            try {
                almanac.add(Item_Index.valueOf(name));
            } catch (Exception ignored) {
            }
        }

        return almanac;
    }

    private static void store(Bundle bundle, HashSet<Item_Index> almanac) {
        int count = 0;
        String[] names = new String[almanac.size()];

        for (Item_Index item:almanac) {
            names[count++] = item.toString();
        }
        bundle.put(ALMANAC, names);
    }

    public static void loadLocal(Bundle bundle) {
        local = restore(bundle);
    }

    public static void saveLocal(Bundle bundle) {
        store(bundle, local);
    }

    public static void loadGlobal() {
        if (global == null) {
            try {
                InputStream input = Game.instance.openFileInput(ALMANAC_FILE);
                Bundle bundle = Bundle.read(input);
                input.close();

                global = restore(bundle);

            } catch (IOException e) {
                global = new HashSet<>();
            }
        }
    }

    public static void saveGlobal() {

        Bundle bundle = null;

        if (saveNeeded) {

            bundle = new Bundle();
            store(bundle, global);

            try {
                OutputStream output = Game.instance.openFileOutput(ALMANAC_FILE, Game.MODE_PRIVATE);
                Bundle.write(bundle, output);
                output.close();
                saveNeeded = false;
            } catch (IOException ignored) {

            }
        }
    }

    private static void displayBadge(Item_Index almanac) {

        if (almanac == null) {
            return;
        }

        if (global.contains( almanac )) {

            if (!almanac.meta) {
                GLog.h("Item_Index endorsed: %s", almanac.description);
            }

        } else {

            global.add( almanac );
            saveNeeded = true;

            if (almanac.meta) {
                GLog.h("New super almanac: %s", almanac.description);
            } else {
                GLog.h("New entry: %s", almanac.description);
            }
            PixelScene.showItem( almanac );
        }
    }

    public static boolean isUnlocked(Item_Index almanac) {
        return global.contains(almanac);
    }

    public static void disown(Item_Index almanac) {
        loadGlobal();
        global.remove(almanac);
        saveNeeded = true;
    }

    public static List<Item_Index> filtered( boolean global ) {

        HashSet<Item_Index> filtered = new HashSet<>(global ? Almanac.global : Almanac.local);

        {
            Iterator<Item_Index> iterator = filtered.iterator();
            while (iterator.hasNext()) {
                Item_Index almanac = iterator.next();
                if ((!global && almanac.meta) || almanac.image == -1) {
                    iterator.remove();
                }
            }
        }
/*
        leaveBest( filtered, Item_Index.MONSTERS_SLAIN_1, Item_Index.MONSTERS_SLAIN_2, Item_Index.MONSTERS_SLAIN_3, Item_Index.MONSTERS_SLAIN_4 );
        leaveBest( filtered, Item_Index.GOLD_COLLECTED_1, Item_Index.GOLD_COLLECTED_2, Item_Index.GOLD_COLLECTED_3, Item_Index.GOLD_COLLECTED_4 );
        leaveBest( filtered, Item_Index.BOSS_SLAIN_1, Item_Index.BOSS_SLAIN_2, Item_Index.BOSS_SLAIN_3, Item_Index.BOSS_SLAIN_4 );
        leaveBest( filtered, Item_Index.LEVEL_REACHED_1, Item_Index.LEVEL_REACHED_2, Item_Index.LEVEL_REACHED_3, Item_Index.LEVEL_REACHED_4 );
        leaveBest( filtered, Item_Index.STRENGTH_ATTAINED_1, Item_Index.STRENGTH_ATTAINED_2, Item_Index.STRENGTH_ATTAINED_3, Item_Index.STRENGTH_ATTAINED_4 );
        leaveBest( filtered, Item_Index.FOOD_EATEN_1, Item_Index.FOOD_EATEN_2, Item_Index.FOOD_EATEN_3, Item_Index.FOOD_EATEN_4 );
        leaveBest( filtered, Item_Index.ITEM_LEVEL_1, Item_Index.ITEM_LEVEL_2, Item_Index.ITEM_LEVEL_3, Item_Index.ITEM_LEVEL_4 );
        leaveBest( filtered, Item_Index.POTIONS_COOKED_1, Item_Index.POTIONS_COOKED_2, Item_Index.POTIONS_COOKED_3, Item_Index.POTIONS_COOKED_4 );
        leaveBest( filtered, Item_Index.BOSS_SLAIN_1_ALL_CLASSES, Item_Index.BOSS_SLAIN_3_ALL_SUBCLASSES );
        leaveBest( filtered, Item_Index.DEATH_FROM_FIRE, Item_Index.YASD );
        leaveBest( filtered, Item_Index.DEATH_FROM_GAS, Item_Index.YASD );
        leaveBest( filtered, Item_Index.DEATH_FROM_HUNGER, Item_Index.YASD );
        leaveBest( filtered, Item_Index.DEATH_FROM_POISON, Item_Index.YASD );
        leaveBest( filtered, Item_Index.ALL_POTIONS_IDENTIFIED, Item_Index.ALL_ITEMS_IDENTIFIED );
        leaveBest( filtered, Item_Index.ALL_SCROLLS_IDENTIFIED, Item_Index.ALL_ITEMS_IDENTIFIED );
        leaveBest( filtered, Item_Index.ALL_RINGS_IDENTIFIED, Item_Index.ALL_ITEMS_IDENTIFIED );
        leaveBest( filtered, Item_Index.ALL_WANDS_IDENTIFIED, Item_Index.ALL_ITEMS_IDENTIFIED );
        leaveBest( filtered, Item_Index.VICTORY, Item_Index.VICTORY_ALL_CLASSES );
        leaveBest( filtered, Item_Index.VICTORY, Item_Index.HAPPY_END );
        leaveBest( filtered, Item_Index.VICTORY, Item_Index.CHAMPION );
        leaveBest( filtered, Item_Index.GAMES_PLAYED_1, Item_Index.GAMES_PLAYED_2, Item_Index.GAMES_PLAYED_3, Item_Index.GAMES_PLAYED_4 );
*/
        ArrayList<Item_Index> list = new ArrayList<>(filtered);
        Collections.sort( list );

        return list;
    }

    private static void leaveBest( HashSet<Item_Index> list, Item_Index...almanac ) {
        for (int i=almanac.length-1; i > 0; i--) {
            if (list.contains( almanac[i])) {
                for (int j=0; j < i; j++) {
                    list.remove( almanac[j] );
                }
                break;
            }
        }
    }
}
