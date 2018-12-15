package FinalMonster.Graphics.Components;

import FinalMonster.Parser.Pokemon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class PokemonMapSelect extends HBox {

	@FXML
	private ImageView img;
	@FXML
	private Label name;
	@FXML
	private Label type;
	@FXML
	private Label hp;
	@FXML
	private Label attack;
	@FXML
	private Label defence;
	@FXML
	private Label speed;

	public PokemonMapSelect(Pokemon pokemon) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("pokemon_map_select.fxml"));

		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setImg(pokemon.getFrontImg());
		setName(pokemon.getName());
		setType(pokemon.getType());
		setHp(pokemon.getFullHp());
		setAttack(pokemon.getAttack());
		setDefence(pokemon.getDefense());
		setSpeed(pokemon.getDefense());
	}

	public ImageView getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img.setImage(img);
	}

	public Label getName() {
		return name;
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public Label getType() {
		return type;
	}

	public void setType(String type) {
		this.type.setText(String.format("(%s)", type));
	}

	public Label getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp.setText(String.format("HP: %d", hp));
	}

	public Label getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack.setText(String.format("Attack: %d", attack));
	}

	public Label getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence.setText(String.format("Defence: %d", defence));
	}

	public Label getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed.setText(String.format("Speed: %d", speed));
	}
}
