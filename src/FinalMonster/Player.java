package FinalMonster;

import FinalMonster.Parser.Pokemon;

import java.util.ArrayList;

public class Player {

	private String name;
	private ArrayList<Pokemon> pokemons;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}
}
