package FinalMonster.Graphics.Controllers;

import FinalMonster.Graphics.Components.BattleScene;
import FinalMonster.Parser.Pokemon;
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

		root.getScene().setRoot(new BattleScene(p1, RandomChoice.randomPokemons(p1.getPokemons().toArray(new Pokemon[0]), 3),
				p2, RandomChoice.randomPokemons(p2.getPokemons().toArray(new Pokemon[0]), 3), false));
	}

	@FXML
	private void exit(ActionEvent actionEvent) {
		Platform.exit();
	}
}
