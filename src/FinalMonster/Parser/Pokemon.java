
package FinalMonster.Parser;

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
