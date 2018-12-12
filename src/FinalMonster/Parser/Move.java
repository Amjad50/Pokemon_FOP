
package FinalMonster.Parser;

public class Move {
	private String name;
	private String type;
	private int power;
	private int accuracy;

	public Move(String name, String type, int power, int accuracy) {
		this.name = name;
		this.type = type;
		this.power = power;
		this.accuracy = accuracy;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getPower() {
		return power;
	}

	public int getAccuracy() {
		return accuracy;
	}

	@Override
	public String toString() {
		return "Move{" +
				"name='" + name + '\'' +
				", type='" + type + '\'' +
				", power=" + power +
				", accuracy=" + accuracy +
				'}';
	}
}
