package FinalMonster.Graphics.Controllers;

import FinalMonster.Graphics.Components.BattleScene;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class StartMenu {
	@FXML
	private BorderPane root;

	@FXML
	private void newGame(ActionEvent actionEvent) throws IOException {
		root.getScene().setRoot(new BattleScene());
	}

	@FXML
	private void exit(ActionEvent actionEvent) {
		Platform.exit();
	}
}
