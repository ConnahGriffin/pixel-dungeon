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

    public enum Item_Index {
        //UNITS
        //Sewers
        MARSUPIAL_RAT   ( "Marsupial Rat", "8", "Marsupial rats are aggressive, but rather weak denizens of the sewers. They can be dangerous only in big numbers. ", 0), //add new entries here
        ALBINO_RAT      ( "Albino Rat", "15", "Marsupial rats are aggressive, but rather weak denizens of the sewers. They can be dangerous only in big numbers. ", 1),
        GNOLL_SCOUT     ( "Gnoll Scout", "12", "Gnolls are hyena-like humanoids. They dwell in sewers and dungeons, venturing up to raid the surface from time to time. Gnoll scouts are regular members of their pack, they are not as strong as brutes and not as intelligent as shamans.", 2),
        SEWER_CRAB      ( "Sewer Crab", "15", "These huge crabs are at the top of the food chain in the sewers. They are extremely fast and their thick exoskeleton can withstand heavy blows.", 3),
        //Prison
        SKELETON        ( "Skeleton", "25", "Skeletons are composed of corpses bones from unlucky adventurers and inhabitants of the dungeon, animated by emanations of evil magic from the depths below. After they have been damaged enough, they disintegrate in an explosion of bones.", 4),
        THIEF_BANDIT    ("Crazy Thief/Bandit", "20", "Deeper levels of the dungeon have always been a hiding place for all kinds of criminals. Not all of them could keep a clear mind during their extended periods so far from daylight. Long ago, these crazy thieves and bandits have forgotten who they are and why they steal.", 5),
        SWARM_OF_FLIES  ( "Swarm of Flies", "80", "The deadly swarm of flies buzzes angrily. Every non-magical attack will split it into two smaller but equally dangerous swarms.", 6),
        GNOLL_SHAMAN    ( "Gnoll Shaman", "18", "The most intelligent gnolls can master shamanistic magic. Gnoll shamans prefer battle spells to compensate for lack of might, not hesitating to use them on those who question their status in a tribe.", 7),
        //CAVES
        VAMPIRE_BAT     ( "Vampire Bat", "30","These brisk and tenacious inhabitants of cave domes may defeat much larger opponents by replenishing their health with each successful attack.", 8),
        BRUTES          ( "Gnoll Brute", "40", "Brutes are the largest, strongest and toughest of all gnolls. When severely wounded, they go berserk, inflicting even more damage to their enemies.", 9),
        CAVE_SPINNER    ( "Cave Spider", "50", "These greenish furry cave spiders try to avoid direct combat, preferring to wait in the distance while their victim, entangled in the spinner's excreted cobweb, slowly dies from their poisonous bite.", 10),
        //Dwarven city,
        FIRE_ELEMENTAL  ( "Fire Elemental","65", "Wandering fire elementals are a byproduct of summoning greater entities. They are too chaotic in their nature to be controlled by even the most powerful demonologist.", 11),
        DWARF_WARLOCK   ( "Dwarf Warlock", "70","When dwarves' interests have shifted from engineering to arcane arts, warlocks have come to power in the city. They started with elemental magic, but soon switched to demonology and necromancy.", 12),
        MONK            ( "Dwarf Monk", "70", "These monks are fanatics, who devoted themselves to protecting their city's secrets from all aliens. They don't use any armor or weapons, relying solely on the art of hand-to-hand combat.", 13),
        GOLEM           ( "Golem", "85", "The Dwarves tried to combine their knowledge of mechanisms with their newfound power of elemental binding. They used spirits of earth as the \"soul\" for the mechanical bodies of golems, which were believed to be most controllable of all. Despite this, the tiniest mistake in the ritual could cause an outbreak.", 14),
        //Demon halls
        SUCCUBUS        ( "Succubus", "80", "The succubi are demons that look like seductive (in a slightly gothic way) girls. Using its magic, the succubus can charm a hero, who will become unable to attack anything until the charm wears off.", 15),
        EVIL_EYE        ( "Evil Eye", "80", "One of this demon's other names is \"orb of hatred\", because when it sees an enemy, it uses its deathgaze recklessly, often ignoring its allies and wounding them.", 16),
        SCORPIO         ( "Scorpio", "95", "These huge arachnid-like demonic creatures avoid close combat by all means, firing crippling serrated spikes from long distances.", 17),//College

        //Special
        WRAITH          ( "Wraith", "1", "A wraith is a vengeful spirit of a sinner, whose grave or tomb was disturbed. Being an ethereal entity, it is very hard to hit with a regular weapon.", 18),
        ANIMATED_STATUE ( "Animated Statue", "depthx5+15", "You would think that it's just another ugly statue of this dungeon, but its red glowing eyes give itself away. While the statue itself is made of stone, the <weapon name>, it's wielding, looks real.", 19),
        GIANT_PIRANHA   ( "Giant Piranha", "depthx5+10", "These carnivorous fish are not natural inhabitants of underground pools. They were bred specifically to protect flooded treasure vaults.", 20),
        //QUEST_RELATED
        FETID_RAT       ( "Fetid Rat", "15", "This marsupial rat in much larger, than a regurlar one. It is surrounded by a foul cloud", 21),
        CURSE_PERSONIFICATION
                        ( "Curses Personification", "depth*3+10", "This creature resembles the sad ghost, but it swirls with dakness. Its face bears an expression of despair.", 22),
        //SUMMONED
        MIMIC           ( "Mimic", "12+levelx4", "Mimics are magical creatures which can take any shape they wish. In dungeons they almost always choose a shape of a treasure chest, because they know how to beckon an adventurer", 23),
        UNDEAD_DWARF    ( "Undead Dwarf", "28", "These undead dwarves, risen by the will of the King of Dwarves, were members of his court. They appear as skeletons with a stunning amount of facial hair", 24),
        ROTTING_FIST    ( "Rotting Fist", "300", "The embodiment of pestilence, Yog-Dzewa never pulls his punches.", 25),
        Burning_FIST    ( "Burning Fist", "200","The most dangerous of the arcane elements, fire. Yog-Dzewa doesn't pull his punches", 26),
        LARVAE          ( "God's Larva", "25", "Yog-Dzewa is an Old God, a powerful entity from the realms of chaos. A century ago, the ancient dwarves barely won the war against its army of demons, but were unable to kill the god itself. Instead, they then imprisoned it in the halls below their city, believing it to be too weak to rise ever again.", 27),
        //Bosses
        GOO             ( "Goo", "80", "Little known about The Goo. It's quite possible that it is not even a creature, but rather a conglomerate of substances from the sewers that gained rudiments of free will.", 28),
        TENGU           ( "Tengu", "120", "Tengu are members of the ancient assassins clan, which is also called Tengu. These assassins are noted for extensive use of shuriken and traps.", 28),
        DM_300          ( "DM-300", "200", "This machine was created by the Dwarves several centuries ago. Later, Dwarves started to replace machines with golems, elementals and even demons. Eventually it led their civilization to the decline. The DM-300 and similar machines were typically used for construction and mining, and in some cases, for city defense.", 29),
        KING_OF_DWARVES ( "King of Dwarves", "300", "The last king of dwarves was known for his deep understanding of processes of life and death. He has persuaded members of his court to participate in a ritual, that should have granted them eternal youthfulness. In the end he was the only one, who got it - and an army of undead as a bonus.", 30),
        YOG_DZEWA       ( "Yog-Dzewa", "300", " Yog-Dzewa is an Old God, a powerful entity from the realms of chaos. A century ago, the ancient dwarves barely won the war against its army of demons, but were unable to kill the god itself. Instead, they then imprisoned it in the halls below their city, believing it to be too weak to rise ever again.", 31),
        HEADMASTER      ("Headmaster", "350", "The most powerful being in existence known within the College realm, abuse of power may happen but none would know if there's none to report it", 32),

        //ITEMS
        //Tier-1
        SHORT_SWORD     ( "Short sword", "", "It is indeed quite short, just a few inches longer,[sic] than a dagger. This short sword is a tier-1 melee weapon. Its average damage is 6 points per hit.", 33),
        DAGGER          ( "Dagger", "", "A simple iron dagger with a well worn wooden handle. This Dagger is a tier-1 melee weapon. Its typical average damage is 4 points per hit; and it usually requires 10 points of strength. This is a rather accurate weapon.", 33),
        WEAPON          {},
        Weapon2         {},
        WEAPON3         {},
        Weapon4         {},
        Weapon5         {},
        Weapon6         {},
        Weapon7         {},
        Weapon8         {},
        Weapon          {},
        Weapon22        {},
        Weapon21        {},
        Weapon23        {},
        Weapon24        {},
        Weapon25        {}
        ;

        public final boolean meta;

        public final String description;
        public final String hp;
        public final String name;
        public final int image;


        Item_Index(String description, int image) {
            this(description, image, false, "0", "Unlisted");
        }

        Item_Index(String name, String hp, String description, int image) {
            this.description = description;
            this.hp = hp;
            this.image = image;
            this.name = name;
            meta = false;
        }

        Item_Index(String description, int image, boolean meta, String hp, String name) {
            this.description = description;
            this.image = image;
            this.meta = meta;
            this.hp = hp;
            this.name = name;
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

    public static void loadLocal( Bundle bundle ) {
        local = restore( bundle );
    }

    public static void saveLocal( Bundle bundle ) {
        store( bundle, local );
    }

    public static void loadGlobal() {
        if (global == null) {
            try {
                InputStream input = Game.instance.openFileInput( ALMANAC_FILE );
                Bundle bundle = Bundle.read( input );
                input.close();

                global = restore( bundle );

            } catch ( IOException e) {
                global = new HashSet<>();
            }
        }
    }

    public static void saveGlobal() {

        Bundle bundle;

        if (saveNeeded) {

            bundle = new Bundle();
            store( bundle, global );

            try {
                OutputStream output = Game.instance.openFileOutput( ALMANAC_FILE, Game.MODE_PRIVATE );
                Bundle.write(bundle, output);
                output.close();
                saveNeeded = false;
            } catch (IOException ignored) {

            }
        }
    }

    public static void validateBossKills() {
        Item_Index almanac = null;

        if(!local.contains( Item_Index.GOO ) && Statistics.deepestFloor >= 6) {
            almanac = Item_Index.GOO;
            local.add( almanac);
            displayIcon( almanac);
        }
        if(!local.contains( Item_Index.TENGU ) && Statistics.deepestFloor >= 11) {
            almanac = Item_Index.TENGU;
            local.add( almanac );
            displayIcon( almanac );
        }
        if(!local.contains( Item_Index.DM_300 ) && Statistics.deepestFloor >= 16) {
            almanac = Item_Index.DM_300;
            local.add( almanac );
            displayIcon( almanac );
        }
        if(!local.contains( Item_Index.KING_OF_DWARVES ) && Statistics.deepestFloor >= 21) {
            almanac = Item_Index.KING_OF_DWARVES;
            local.add( almanac );
            displayIcon( almanac );
        }
        if(!local.contains( Item_Index.YOG_DZEWA ) && Statistics.deepestFloor >= 26) {
            almanac = Item_Index.YOG_DZEWA;
            local.add( almanac );
            displayIcon( almanac );
        }
        if(!local.contains( Item_Index.HEADMASTER ) && Statistics.amuletObtained) {
            almanac = Item_Index.HEADMASTER;
            local.add( almanac );
            displayIcon( almanac );
        }
    }

    private static void displayIcon(Item_Index almanac) {

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
