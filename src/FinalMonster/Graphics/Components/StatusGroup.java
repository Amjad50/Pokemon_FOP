package FinalMonster.Graphics.Components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class StatusGroup extends VBox {
	@FXML
	private ArrayList<StatusBar> status_bars;

	public StatusGroup() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("status_group.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();
	}

	private ArrayList<StatusBar> getStatusBars() {
		return status_bars;
	}
}
