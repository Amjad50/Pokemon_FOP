package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import FinalMonster.Parser.Pokemon;
import FinalMonster.Parser.PokemonList;
import FinalMonster.Player;
import FinalMonster.Utils.RandomChoice;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MapScene extends StackPane {

	private static final int MOVEMENT_DISTANCE = 7;
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
	@FXML
	private BorderPane bottom_bar;
	@FXML
	private ImageView opponent_fight_img;
	@FXML
	private Label opponent_fight_name;
	@FXML
	private Button fight_btn;
	@FXML
	private Button toBattle;

	private Player player;
	private Set<Integer> selected;
	private ArrayList<Player> bots;
	private ArrayList<Player> wildPokemons;
	private SavedMapState state;
	private Player toFight;
	private boolean toFightIsWild;

	private Timeline gameloop;
	private boolean isRight;
	private boolean isLeft;
	private boolean isUp;
	private boolean isDown;


	private MapScene(Player player) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("map_scene.fxml"));

		loader.setRoot(this);
		loader.setController(this);
		loader.load();

		this.player = player;
		player.addExp(90);
		setUpPlayer();

		pokemonChooseList.setItems(FXCollections.observableArrayList(
				player.getPokemons().stream().map(PokemonMapSelect::new).collect(Collectors.toList())
		));
		pokemonChooseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		selected = new HashSet<>();

		pokemonChooseList.setOnMouseClicked(event -> {
			List<Integer> comming_selection = pokemonChooseList.getSelectionModel().getSelectedIndices();

			boolean changed = selected.addAll(comming_selection);

			if ( !changed ) {
				selected.removeAll(comming_selection);
			}

			int[] finalSelection = new int[selected.size()];
			Integer[] tmpArr = selected.toArray(new Integer[0]);
			for ( int i = 0; i < finalSelection.length; i++ ) {
				finalSelection[i] = tmpArr[i];
			}
			pokemonChooseList.getSelectionModel().clearSelection();
			if ( finalSelection.length > 0 )
				pokemonChooseList.getSelectionModel().selectIndices(finalSelection[0], finalSelection);

			if ( finalSelection.length == 3 ) {
				toBattle.setDisable(false);
			} else {
				toBattle.setDisable(true);
			}
		});

		chars.setBackground(new Background(new BackgroundImage(
				new Image(ImageDB.BG[4]),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(Constrains.ROOT_WIDTH, Constrains.ROOT_HEIGHT - 100, false, false, false, false)
		)));

		fight_btn.setOnAction(this::fight);
		toBattle.setOnAction(this::toBattle);

		chars.setFocusTraversable(true);

		chars.setOnKeyPressed(event -> {
			switch ( event.getCode() ) {
				case RIGHT:
					isRight = true;
					break;
				case LEFT:
					isLeft = true;
					break;
				case UP:
					isUp = true;
					break;
				case DOWN:
					isDown = true;
					break;
			}
		});

		chars.setOnKeyReleased(event -> {
			switch ( event.getCode() ) {
				case RIGHT:
					isRight = false;
					break;
				case LEFT:
					isLeft = false;
					break;
				case UP:
					isUp = false;
					break;
				case DOWN:
					isDown = false;
					break;
			}
		});

		startGameLoop();
	}

	public MapScene(Player player, SavedMapState state, Pokemon[] opponentToChoose) throws IOException {
		this(player);

		bots = RandomChoice.random(Arrays.asList(Player.bots), 2);
		wildPokemons = getWildPokemonsForPlayer(2);
		for ( Player bot : bots ) {
			bot.setPokemons(RandomChoice.random(Arrays.asList(opponentToChoose), 3));
		}
		if ( state == null ) {
			this.state = new SavedMapState();
			this.state.setPlayerLocation(750 / 2, 500 / 2);
		} else {
			this.state = new SavedMapState();
			this.state.playerLocation = state.getPlayerLocation();
		}
		putOpponents();
		putPlayer();
	}

	private void startGameLoop() {
		gameloop = new Timeline(
				new KeyFrame(Duration.millis(33), this::loop)
		);
		gameloop.setCycleCount(Animation.INDEFINITE);
		gameloop.play();
	}

	private void loop(ActionEvent actionEvent) {
		int toMoveX = 0;
		int toMoveY = 0;
		double newX, newY;
		if ( isRight )
			toMoveX += MOVEMENT_DISTANCE;
		if ( isLeft )
			toMoveX -= MOVEMENT_DISTANCE;
		if ( isUp )
			toMoveY -= MOVEMENT_DISTANCE;
		if ( isDown )
			toMoveY += MOVEMENT_DISTANCE;

		newX = player_char_img.getLayoutX() + toMoveX;
		newY = player_char_img.getLayoutY() + toMoveY;

		if ( newX > 0 && newX < Constrains.ROOT_WIDTH - player_char_img.getImage().getWidth() )
			player_char_img.setLayoutX(newX);
		if ( newY > 100 && newY < Constrains.ROOT_HEIGHT - player_char_img.getImage().getHeight() - 10 )
			player_char_img.setLayoutY(newY);
		Point2D player = new Point2D(player_char_img.getLayoutX(), player_char_img.getLayoutY() - 100);

		toFight = null;
		for ( Player other : state.getPokemonLocations().keySet() ) {
			if ( state.getPokemonLocations().get(other).distance(player) < 30 ) {
				toFight = other;
				toFightIsWild = wildPokemons.contains(other);
				break;
			}
		}

		displayOpponentFight();

	}

	private void displayOpponentFight() {
		if ( toFight == null ) {
			bottom_bar.setVisible(false);
		} else {
			opponent_fight_img.setImage(toFight.getVSImg());
			opponent_fight_name.setText(toFight.getName());
			bottom_bar.setVisible(true);
		}
	}


	private void putPlayer() {
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

	private void fight(ActionEvent actionEvent) {
		showSelection();
	}

	private void toBattle(ActionEvent actionEvent) {
		ArrayList<Pokemon> choosen = new ArrayList<>(3);
		for ( int i : selected ) {
			choosen.add(player.getPokemons().get(i));
		}
		try {
			this.getScene().setRoot(new GotoBattleScene(player, choosen, toFight, toFight.getPokemons(), toFightIsWild, state));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showSelection() {
		selection.setVisible(true);
		selection.setManaged(true);
		selection.setDisable(false);
	}

	@FXML
	private void hideSelection() {
		pokemonChooseList.getSelectionModel().clearSelection();
		selected.clear();
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
		double percentage = 1 - (double) player.getToNextLevel() / player.nextLevelExp();
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
