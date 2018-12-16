package FinalMonster.Graphics.Controllers;

import FinalMonster.Graphics.Components.GotoBattleScene;
import FinalMonster.Parser.PokemonList;
import FinalMonster.Player;
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
		Player p1 = new Player("player", PokemonList.Easy());
		Player p2 = Player.bots[0];
		p2.setPokemons(PokemonList.Hard());

		root.getScene().setRoot(new GotoBattleScene(p1, RandomChoice.random(p1.getPokemons(), 3),
				p2, RandomChoice.random(p2.getPokemons(), 3), false));
	}

	@FXML
	private void exit(ActionEvent actionEvent) {
		Platform.exit();
	}
}
