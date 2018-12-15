package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import FinalMonster.Parser.Pokemon;
import FinalMonster.Parser.PokemonList;
import FinalMonster.Player;
import FinalMonster.Utils.RandomChoice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectionScene extends BorderPane {

	@FXML
	private BorderPane root;
	@FXML
	private RadioButton opponent;
	@FXML
	private RadioButton player;
	@FXML
	private Button play;
	@FXML
	private GridPane grid;
	@FXML
	private TextField player_name;
	@FXML
	private Label name_warning;

	public ArrayList<Pokemon> playerPokemons = new ArrayList<>();
	public ArrayList<Pokemon> opponentPokemons = new ArrayList<>();
	public ArrayList rowPokemons = new ArrayList();
	public ArrayList colPokemons = new ArrayList();

	public Node[] pok;

	public SelectionScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("selection_scene.fxml"));

		loader.setRoot(this);
		loader.setController(this);
		loader.load();
		play.setDisable(true);

		Pokemon p[] = PokemonList.Easy();
		pok = new Node[p.length];
		for ( int i = 0; i < p.length; i++ ) {
			RadioButton r = new RadioButton();
//            r.setOpacity(0);
			r.setId(p[i].getName());
			r.setOnAction(event -> {
				addPokemons(event);
			});
			String url = p[i].imagePath(false);
			r.setStyle("-fx-visibility: hidden; -fx-margin: auto; -fx-min-width: 80px; -fx-min-height: 80px; -fx-background-image: url(" + url + "); -fx-background-repeat: stretch; -fx-background-size: contain; -fx-background-position: center center;");
			pok[i] = r;
			System.out.println(pok[i]);

		}

		play.setOnAction(this::play);
		for ( int j = 0; j < 6; j++ ) {

			for ( int k = 0; k < 4; k++ ) {
				grid.add(pok[j * 4 + k], j, k);

			}
		}
	}

	@FXML
	private void initialize() {

		root.setBackground(new Background(new BackgroundImage(
				new Image(ImageDB.BG[0]),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(Constrains.ROOT_WIDTH, Constrains.ROOT_HEIGHT, true, true, true, true)
		)));

	}

	@FXML
	public void addPokemons(ActionEvent e) {
		if ( playerPokemons.size() > 1 && playerPokemons.size() < 3 ) {
			play.setDisable(false);
		} else {
			play.setDisable(true);
		}
		RadioButton poke = (RadioButton) e.getSource();
		if ( !poke.isSelected() ) {
			System.out.println(playerPokemons.remove(PokemonList.getPokemon(poke.getId())));
		} else {
			playerPokemons.add(PokemonList.getPokemon(poke.getId()));
			System.out.println(poke.getId());
		}
		System.out.println(playerPokemons.size());
	}

	@FXML
	public void play(ActionEvent e) {
		System.out.println(Arrays.toString(playerPokemons.toArray()));
		try {
			if ( player_name.getText().isEmpty() ) {
				name_warning.setVisible(true);
				return;
			}
			Player player = new Player(player_name.getText(), playerPokemons);
			opponentPokemons = RandomChoice.randomPokemons(Arrays.asList(PokemonList.Normal()), 3);
			Player opponent = Player.bots[2];
			opponent.setPokemons(opponentPokemons);
			root.getScene().setRoot(new BattleScene(player, RandomChoice.randomPokemons(playerPokemons, 3), opponent, opponentPokemons, false));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
