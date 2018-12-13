
package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import FinalMonster.Parser.Parser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.Button;

public class SelectionScene extends AnchorPane {

	@FXML
	private AnchorPane root;
	@FXML
	private RadioButton opponent;
	@FXML
	private RadioButton player;
        @FXML
        private Button play;
        

	public ArrayList playerPokemons = new ArrayList();
	public ArrayList opponentPokemons = new ArrayList();

	public SelectionScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("selection_scene.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
                play.setDisable(true);

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
            if(playerPokemons.size() > 1 && playerPokemons.size() < 3){
                play.setDisable(false);
            }
            else{
                play.setDisable(true);
            }
            RadioButton poke = (RadioButton) e.getSource();
            if(!poke.isSelected()){
                System.out.println(Arrays.toString(playerPokemons.toArray()));
            }
            Parser p = new Parser();
            RadioButton pokemons = (RadioButton) e.getSource();
            playerPokemons.add(p.getPokemon(pokemons.getId()));
            opponentPokemons.add(p.getPokemon("random"));
	}
        
        @FXML
        public void play(ActionEvent e) throws IOException{
            System.out.println(Arrays.toString(playerPokemons.toArray()));
            root.getScene().setRoot(new BattleScene("player", playerPokemons, "opponent", opponentPokemons));
            
        }

}