package FinalMonster.Graphics.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class StartMenu {
	@FXML
	private BorderPane root;

	@FXML
	private void newGame(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FinalMonster/Graphics/fxml/main.fxml"));
		Parent p = loader.load();
		root.getScene().setRoot(p);
	}

	@FXML
	private void exit(ActionEvent actionEvent) {
		Platform.exit();
	}
}
