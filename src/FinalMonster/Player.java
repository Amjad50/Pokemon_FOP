package FinalMonster;

import FinalMonster.Parser.Pokemon;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {

	public static Player[] bots = {new Player("Alder", true), new Player("Bianca", true), new Player("Hilbert", true)};

	public static Player wildPokemon = new Player("Wild Pokemon", true);

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
	}

	public Player(String name, Pokemon[] pokemons) {
		this(name, new ArrayList<>(Arrays.asList(pokemons)));
	}

	private Player(String name, boolean isBot) {
		this.name = name;
		this.isBot = isBot;
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
			toNextLevel += tmp;
			return true;
		}
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
				VSImg = new Image(getClass().getResource(imagePath("VS")).toString());
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
