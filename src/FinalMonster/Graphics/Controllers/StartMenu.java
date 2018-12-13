package FinalMonster.Graphics.Controllers;

import FinalMonster.Graphics.Components.BattleScene;
import FinalMonster.Parser.PokemonList;
import FinalMonster.Utils.RandomChoice;
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
		root.getScene().setRoot(new BattleScene("player", RandomChoice.randomPokemons(PokemonList.Normal(), 3),
				"bot", RandomChoice.randomPokemons(PokemonList.Hard(), 3)));
	}

	@FXML
	private void exit(ActionEvent actionEvent) {
		Platform.exit();
	}
}
