package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import FinalMonster.Parser.Pokemon;
import FinalMonster.Parser.PokemonList;
import FinalMonster.Player;
import FinalMonster.Utils.RandomChoice;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MapScene extends StackPane {

	@FXML
	private ImageView player_img;
	@FXML
	private Label name;
	@FXML
	private ProgressBar level_progress;
	@FXML
	private Label level;
	@FXML
	private Pane chars;
	@FXML
	private ImageView player_char_img;
	@FXML
	private BorderPane selection;
	@FXML
	private ListView<PokemonMapSelect> pokemonChooseList;

	private Player player;
	private ArrayList<Player> bots;
	private ArrayList<Player> wildPokemons;

	private MapScene(Player player) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("map_scene.fxml"));

		loader.setRoot(this);
		loader.setController(this);
		loader.load();

		this.player = player;
		setUpPlayer();

		pokemonChooseList.setItems(FXCollections.observableArrayList(
				player.getPokemons().stream().map(PokemonMapSelect::new).collect(Collectors.toList())
		));

		chars.setBackground(new Background(new BackgroundImage(
				new Image(ImageDB.BG[0]),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(Constrains.ROOT_WIDTH, Constrains.ROOT_HEIGHT - 100, false, false, false, false)
		)));
	}

	public MapScene(Player player, SavedMapState state) throws IOException {
		this(player);

		if ( state == null ) {
			bots = RandomChoice.random(Arrays.asList(Player.bots), 2);
//			putBotPlayers();
			wildPokemons = getWildPokemonsForPlayer(2);
			System.out.println(bots);
			System.out.println(wildPokemons);
//			putWildPokemons();
		}
	}

	private ArrayList<Player> getWildPokemonsForPlayer(int length) {
		ArrayList<Player> result = new ArrayList<>();
		ArrayList<Player> pokemonsCanWild = new ArrayList<>();

		for ( Pokemon pokemon : PokemonList.AllPokemons() ) {
			Player tmp = Player.wild(pokemon);
			if ( tmp != null ) {
				pokemonsCanWild.add(tmp);
			}
		}

		return RandomChoice.random(pokemonsCanWild, length);
	}

	private void showSelection() {
		selection.setVisible(true);
		selection.setManaged(true);
		selection.setDisable(false);
	}

	private void hideSelection() {
		selection.setVisible(false);
		selection.setManaged(false);
		selection.setDisable(true);
	}

	private void setUpPlayer() {
		if ( player != null ) {
			this.setPlayerName();
			this.setLevel();
			this.setLevel_progress();
			this.setPlayerImg();
		}
	}

	private void setPlayerName() {
		this.name.setText(player.getName());
	}

	private void setLevel_progress() {
		int percentage = 1 - player.nextLevelExp() / player.getToNextLevel();
		this.level_progress.setProgress(percentage);
	}

	private void setLevel() {
		this.level.setText(String.format("LVL: %d", player.getLevel()));
	}

	private void setPlayerImg() {
		this.player_img.setImage(player.getVSImg());
	}

	public class SavedMapState {
		private HashMap<Pokemon, Point2D> pokemonLocations = new HashMap<>();

		private SavedMapState() {

		}

		private HashMap<Pokemon, Point2D> getPokemonLocations() {
			return pokemonLocations;
		}
	}
}
