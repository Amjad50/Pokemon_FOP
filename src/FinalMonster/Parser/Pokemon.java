
package FinalMonster.Parser;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Arrays;

public class Pokemon {
	private String id;
	private String name;
	private String type;
	private int hp;
	private int fullHp;
	private int attack;
	private int defense;
	private int speed;
	private Move moves[];

	private Image frontImg;
	private Image backImg;

	private MediaPlayer cry;

	public Pokemon(String id, String name, String type, int hp, int attack, int defense, int speed, Move[] moves) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.hp = hp;
		this.fullHp = hp;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.moves = moves;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getHp() {
		return hp;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getSpeed() {
		return speed;
	}

	public Move[] getMoves() {
		return moves;
	}

	public int getFullHp() {
		return fullHp;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setMoves(Move[] moves) {
		this.moves = moves;
	}

	public String imagePath(boolean back) {
		if ( back )
			if ( id.contains("_") )
				return "/FinalMonster/resources/PokemonsAll/" + id.replace("_", "b_") + ".gif";
			else
				return "/FinalMonster/resources/PokemonsAll/" + id + "b.gif";
		return "/FinalMonster/resources/PokemonsAll/" + id + ".gif";
	}

	public Image getFrontImg() {
		if ( frontImg == null ) {
			frontImg = new Image(getClass().getResource(imagePath(false)).toString());

			if ( frontImg.getException() != null )
				frontImg.getException().printStackTrace();
		}

		return frontImg;
	}

	public Image getBackImg() {
		if ( backImg == null ) {
			backImg = new Image(getClass().getResource(imagePath(true)).toString());

			if ( backImg.getException() != null )
				backImg.getException().printStackTrace();
		}

		return backImg;
	}

	private MediaPlayer getCry() {
		if ( cry == null ) {
			cry = new MediaPlayer(new Media(getClass().getResource("/FinalMonster/resources/PokemonsAll/" + id + "Cry.gif").toString()));
		}

		return cry;
	}

	@Override
	public String toString() {
		return "Pokemon{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", hp=" + hp +
				", fullHp=" + fullHp +
				", attack=" + attack +
				", defense=" + defense +
				", speed=" + speed +
				", moves=" + Arrays.toString(moves) +
				'}';
	}
}
