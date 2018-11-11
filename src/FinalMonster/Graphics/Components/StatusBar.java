package FinalMonster.Graphics.Components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class StatusBar extends HBox {

	@FXML
	private Label name;

	@FXML
	private ProgressBar health;

	@FXML
	private ProgressBar accumulator;

	public StatusBar() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("status_bar.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();
	}

	public StatusBar(String name) throws IOException {
		this();

		setName(name);
	}

	public void setHealth(double value) {
		health.progressProperty().set(value);
	}

	public double getHealth() {
		return health.progressProperty().get();
	}

	public double getAccumulator() {
		return accumulator.progressProperty().get();
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
}
