
package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;

public class SelectionScene extends AnchorPane {

	@FXML
	private AnchorPane root;
	@FXML
	private RadioButton opponent;
	@FXML
	private RadioButton player;

	public ArrayList pokes = new ArrayList();

	public SelectionScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("selection_scene.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();
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
            RadioButton pokemons = (RadioButton) e.getSource();
		pokes.add(pokemons.getId());
                System.out.println(pokemons.getId());
	}
        
        @FXML
        public void play(ActionEvent e) throws IOException{
        
            root.getScene().setRoot(new BattleScene(pokes));
            
        }

}