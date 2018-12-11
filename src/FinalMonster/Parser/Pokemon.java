
package FinalMonster.Parser;

public class Pokemon {
	private String id;
	private String name;
	private String type;
	private int hp;
	private int attack;
	private int defense;
	private int speed;
	private Move moves[];

	public Pokemon(String id, String name, String type, int hp, int attack, int defense, int speed, Move[] moves) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.moves = moves;
	}

	private String getId() {
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
		String a = "Pokemon(" + id + "): " + name + "\nType: " + type + "\nHP: " + hp + "\nAttack: " + attack + "\nDefense: " + defense + "\nSpeed: " + speed;
		for ( int i = 0; i < 4; i++ ) {
			a += moves[i].toString();
		}
		;

		return a;
	}


}
