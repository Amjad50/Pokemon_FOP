package FinalMonster.Graphics.Components;

import FinalMonster.Player;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.stream.Collectors;

public class MapScene extends StackPane {

	@FXML
	private StackPane root;
	@FXML
	private Pane chars;
	@FXML
	private ImageView player_img;
	@FXML
	private ListView<PokemonMapSelect> pokemonChooseList;

	private Player player;

	public MapScene(Player player) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("map_scene.fxml"));

		loader.setRoot(this);
		loader.setController(this);
		loader.load();

		this.player = player;

		pokemonChooseList.setItems(FXCollections.observableArrayList(
				player.getPokemons().stream().map(PokemonMapSelect::new).collect(Collectors.toList())
		));
	}
}
