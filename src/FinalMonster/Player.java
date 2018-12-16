package FinalMonster;

import FinalMonster.Parser.Pokemon;
import FinalMonster.Parser.PokemonList;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Player {

	public static Player[] bots = {new Player("Alder", true), new Player("Bianca", true), new Player("Hilbert", true)};

	private String name;
	private ArrayList<Pokemon> pokemons;
	private Image backImg;
	private Image VSImg;
	private Image mapImg;

	private int level;
	private int toNextLevel;

	private boolean isBot;

	public Player(String name, ArrayList<Pokemon> pokemons) {
		this.name = name;
		this.pokemons = pokemons;
		this.isBot = false;
		this.levelUp();
	}

	public Player(String name, Pokemon[] pokemons) {
		this(name, new ArrayList<>(Arrays.asList(pokemons)));
	}

	private Player(String name, boolean isBot) {
		this.name = name;
		this.isBot = isBot;
		this.pokemons = new ArrayList<>();
	}

	public static Player wild(Pokemon pokemon) {
		if ( Player.class.getResource("/FinalMonster/resources/players/" + pokemon.getName() + "OD" + ".png") == null ) {
			return null;
		} else {
			Player wild = new Player(pokemon.getName(), true);
			wild.getPokemons().add(pokemon);
			return wild;
		}
	}

	public String getName() {
		return name;
	}

	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setPokemons(ArrayList<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}

	public void setPokemons(Pokemon[] pokemons) {
		this.pokemons = new ArrayList<>(Arrays.asList(pokemons));
	}

	public int getLevel() {
		return level;
	}

	public int getToNextLevel() {
		return toNextLevel;
	}

	public int nextLevelExp() {
		return this.level * 30 + 10;
	}

	public void levelUp() {
		this.toNextLevel = ++this.level * 30 + 10;
	}

	public boolean addExp(int exp) {
		if ( exp < toNextLevel ) {
			toNextLevel -= exp;
			return false;
		} else {
			int tmp = exp - toNextLevel;
			levelUp();
			addExp(tmp);
			return true;
		}
	}

	public Pokemon[] getPokemonsForMe() {
		if ( level < 2 )
			return PokemonList.Easy();
		else if ( level < 3 )
			return PokemonList.Normal();
		else if ( level < 5 )
			return PokemonList.Hard();
		else
			return PokemonList.Legend();
	}

	private String imagePath(String suffix) {
		if ( isBot )
			return "/FinalMonster/resources/players/" + name + suffix + ".png";
		else
			return "/FinalMonster/resources/players/Player" + suffix + ".png";
	}

	public Image getBackImg() {
		if ( isBot )
			return null;
		if ( backImg == null ) {
			try {
				backImg = new Image(getClass().getResource(imagePath("Back")).toString());
			} catch (ExceptionInInitializerError e) {
				e.printStackTrace();
				return null;
			}
		}

		return backImg;
	}

	public Image getVSImg() {
		if ( VSImg == null ) {
			try {
				URL imagetmp = getClass().getResource(imagePath("VS"));
				if ( imagetmp == null )
					VSImg = pokemons.get(0).getFrontImg();
				else
					VSImg = new Image(imagetmp.toString());
			} catch (ExceptionInInitializerError e) {
				e.printStackTrace();
				return null;
			}
		}

		return VSImg;
	}

	public Image getMapImg() {
		if ( mapImg == null ) {
			try {
				mapImg = new Image(getClass().getResource(imagePath("OD")).toString());
			} catch (ExceptionInInitializerError e) {
				e.printStackTrace();
				return null;
			}
		}

		return mapImg;
	}
}
