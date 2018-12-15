
package FinalMonster.Parser;

import java.util.Random;

public class PokemonList {

	private static final int EASY_START = 0, EASY_LEN = 24 - EASY_START + 1,
			NORMAL_START = 25, NORMAL_LEN = 49 - NORMAL_START + 1,
			HARD_START = 50, HARD_LEN = 72 - HARD_START + 1,
			LEGEND_START = 73, LEGEND_LEN = 110 - LEGEND_START + 1;
	private static String[] PokemonNames = {"Bulbasaur", "Charmander", "Squirtle", "Chikorita", "Cyndaquil", "Totodile", "Treecko", "Torchihc", "Mudkip", "Turtwig", "Chimchar", "Piplup", "Snivy", "Tepig", "Oshawott", "Ratata", "Pidgey", "Sentret", "Hoothoot", "Zigzagoon", "Poochyena", "Bidoof", "Starly", "Patrat", "Pidove", "Ivysaur", "Charmeleon", "Wartortle", "Bayleef", "Quilava", "Croconaw", "Grovyle", "Combusken", "Marshtomp", "Grotle", "Monferno", "Prinplup", "Raticate", "Pidgeotto", "Furret", "Noctowl", "Linoone", "Mightyena", "Bibarel", "Staravia", "Watchog", "Tranquill", "Venusaur", "Charizard", "Blastoise", "Meganium", "Typhlosion", "Feraligatr", "Sceptile", "Blaziken", "Torterra", "Infernape", "Empoleon", "Serperior", "Emboar", "Samurott", "Pidgeot", "Staraptor", "Unfezant", "Dragonite", "Metagross", "Salamence", "Garchomp", "Hydreigon", "Mewtwo", "Lugia", "Ho-oh", "Rayquaza", "Groudon", "Kyogre", "Deoxys", "Dialga", "Palkia", "Giratina", "Zekrom", "Reshiram", "Kyurem", "Genesect", "Arceus"};
	private static Pokemon[] pokemons;

	private static String[] Player = new String[3];
	private static Pokemon[] Easy;
	private static Pokemon[] Normal;
	private static Pokemon[] Hard;
	private static Pokemon[] Legend;

	static {
		pokemons = Parser.scan();
		System.out.println(pokemons.length);
	}

	public static Pokemon[] Easy() {
		if ( Easy == null ) {
			Easy = new Pokemon[EASY_LEN];
			System.arraycopy(pokemons, EASY_START, Easy, 0, EASY_LEN);
		}
		return Easy;
	}

	public static Pokemon[] Normal() {
		if ( Normal == null ) {
			Normal = new Pokemon[NORMAL_LEN];
			System.arraycopy(pokemons, NORMAL_START, Normal, 0, NORMAL_LEN);
		}
		return Normal;
	}

	public static Pokemon[] Hard() {
		if ( Hard == null ) {
			Hard = new Pokemon[HARD_LEN];
			System.arraycopy(pokemons, HARD_START, Hard, 0, HARD_LEN);
		}
		return Hard;
	}

	public static Pokemon[] Legend() {
		if ( Legend == null ) {
			Legend = new Pokemon[LEGEND_LEN];
			System.arraycopy(pokemons, LEGEND_START, Legend, 0, LEGEND_LEN);
		}
		return Legend;
	}

	public static Pokemon getPokemon(String name) {

		Pokemon[] p = pokemons;
		Pokemon chosen = null;
		Random r = new Random();
		if ( name.equalsIgnoreCase("random") ) {
			return p[r.nextInt(100)];
		}
		for ( int i = 0; i < p.length; i++ ) {
			if ( p[i].getName().equalsIgnoreCase(name) ) {
				chosen = p[i];
			}
		}
		return chosen;
	}

}
