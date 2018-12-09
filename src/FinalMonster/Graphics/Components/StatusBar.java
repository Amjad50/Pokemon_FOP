package FinalMonster.Graphics.Components;

import FinalMonster.Utils.Callback;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;

public class StatusBar extends HBox {

	private static final String
			GREEN = "green",
			YELLOW = "#eeee1f",
			RED = "red";

	private static final double
			MLIMIT_GREEN = 0.5,
			MLIMIT_YELLOW = 0.25;

	private int full_hp;
	private int current_hp;
	private String color;

	@FXML
	private Label name;

	@FXML
	private ProgressBar health;

	@FXML
	private Label health_text;

	@FXML
	private ProgressBar accumulator;

	public StatusBar() throws IOException {
		this("poke1", 100);
	}

	public StatusBar(String name, int full_hp) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("status_bar.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();

		this.setName(name);
		this.setFull_hp(full_hp);
		setHealth(full_hp);
	}

	public void setHealth(int value, Callback callable) {
		Timeline n = new Timeline();

		n.getKeyFrames().add(
				new KeyFrame(Duration.millis(34), event -> {
					if ( this.current_hp != value ) {
						int tmp = this.current_hp - value;
						tmp = ( tmp > 0 ) ? 1 : -1;
						this.setHealth(this.current_hp - tmp);
					} else {
						n.stop();
						callable.call();
					}
				})
		);
		n.setCycleCount(Animation.INDEFINITE);

		n.play();
	}

	public void setHealth(int value) {
		this.current_hp = value;

		double percentage = 1;
		if ( full_hp != 0 ) {
			percentage = value / (double) full_hp;

			if ( percentage > MLIMIT_GREEN ) {
				this.setColor(GREEN);
			} else if ( percentage > MLIMIT_YELLOW ) {
				this.setColor(YELLOW);
			} else {
				this.setColor(RED);
			}
		}
		this.health_text.setText(String.format("%d/%d", value, full_hp));

		health.progressProperty().set(percentage);
	}

	public int getHealth() {
		return this.current_hp;
	}

	public double getAccumulator() {
		return accumulator.progressProperty().get();
	}

	public void setAccumulator(int value_, Callback callable) {
		Timeline n = new Timeline();


		double value = value_ / 100.0;
		assert value >= getAccumulator();

		n.getKeyFrames().add(
				new KeyFrame(Duration.millis(34), event -> {
					double current_a = getAccumulator();
					if ( current_a < value ) {
						double tmp = current_a - value;
						tmp = ( tmp > 0 ) ? 0.01 : -0.01;
						this.setAccumulator(current_a - tmp);
					} else {
						n.stop();
						callable.call();
					}
				})
		);
		n.setCycleCount(Animation.INDEFINITE);
		n.play();
	}

	public void setAccumulator(int value) {
		double percentage = value / 100.0;
		accumulator.progressProperty().set(percentage);
	}

	public void setAccumulator(double value) {
		accumulator.progressProperty().set(value);
	}

	public String getName() {
		return name.textProperty().get();
	}

	public void setName(String value) {
		name.textProperty().set(value);
	}


	@Override
	public String toString() {

		return "StatusBar " +
				"{" +
				"Name: \"" + getName() + "\", " +
				"Health: " + getHealth() + ", " +
				"Accumulator: " + getAccumulator() +
				"}";
	}

	public int getFull_hp() {
		return full_hp;
	}

	public void setFull_hp(int full_hp) {
		this.full_hp = full_hp;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if ( this.color != null && this.color.equals(color) )
			return;
		this.color = color;
		this.health.setStyle("-fx-accent: " + color + ";");
	}
}
