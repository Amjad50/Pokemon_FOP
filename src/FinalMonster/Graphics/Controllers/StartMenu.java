package FinalMonster.Graphics.Controllers;

import FinalMonster.Graphics.Components.SelectionScene;
import FinalMonster.Parser.PokemonList;
import FinalMonster.Player;
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
		Player p = new Player("player", PokemonList.Normal());
		root.getScene().setRoot(new SelectionScene());
	}

	@FXML
	private void exit(ActionEvent actionEvent) {
		Platform.exit();
	}
}
