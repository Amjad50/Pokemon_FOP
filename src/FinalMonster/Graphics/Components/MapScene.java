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
import java.util.Random;
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
	private SavedMapState state;

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
				new Image(ImageDB.BG[3]),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(Constrains.ROOT_WIDTH, Constrains.ROOT_HEIGHT - 100, false, false, false, false)
		)));
	}

	public MapScene(Player player, SavedMapState state) throws IOException {
		this(player);

		if ( state == null ) {
			this.state = new SavedMapState();
			bots = RandomChoice.random(Arrays.asList(Player.bots), 2);
			wildPokemons = getWildPokemonsForPlayer(2);
			putOpponents();
			putPlayer();
		}
	}

	private void putPlayer() {
		state.setPlayerLocation(750 / 2, 500 / 2);
		player_char_img.setImage(player.getMapImg());
		player_char_img.setLayoutX(state.getPlayerLocation().getX() - player.getMapImg().getWidth() / 2);
		player_char_img.setLayoutY(state.getPlayerLocation().getY() + player.getMapImg().getHeight() / 2);
	}

	private void putOpponents() {
		Random rand = new Random();
		ArrayList<Player> all = new ArrayList<>();
		all.addAll(bots);
		all.addAll(wildPokemons);
		for ( int i = 0; i < all.size(); i++ ) {
			Point2D cpoint = new Point2D(rand.nextInt(650 - 50) + 50, rand.nextInt(400 - 50) + 50);
			boolean broke = false;
			for ( Point2D p : state.getPokemonLocations().values() ) {
				if ( cpoint.distance(p) < 100 ) {
					i--;
					broke = true;
					break;
				}
			}
			if ( !broke ) {
				state.getPokemonLocations().put(all.get(i), cpoint);
				ImageView img = new ImageView(all.get(i).getMapImg());
				img.setLayoutX(cpoint.getX());
				img.setLayoutY(cpoint.getY());
				chars.getChildren().add(img);
			}
		}
	}

	private ArrayList<Player> getWildPokemonsForPlayer(int length) {
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
		private HashMap<Player, Point2D> pokemonLocations = new HashMap<>();
		private Point2D playerLocation;

		private SavedMapState() {

		}

		private HashMap<Player, Point2D> getPokemonLocations() {
			return pokemonLocations;
		}

		private Point2D getPlayerLocation() {
			return playerLocation;
		}

		private void setPlayerLocation(Point2D playerLocation) {
			this.playerLocation = playerLocation;
		}

		private void setPlayerLocation(double x, double y) {
			this.playerLocation = new Point2D(x, y);
		}
	}
}
