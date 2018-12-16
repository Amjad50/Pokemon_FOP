package FinalMonster.Graphics.Controllers;

import FinalMonster.Graphics.Components.SelectionScene;
import FinalMonster.Graphics.Storage.Music;
import FinalMonster.Parser.PokemonList;
import FinalMonster.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

public class StartMenu {
	@FXML
	private BorderPane root;
	@FXML
	private Button soundBtn;
	private MediaPlayer mediaPlayer;

	@FXML
	private void initialize() {
		mediaPlayer = Music.getPlayers().get(Music.Place.START);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();
	}

	@FXML
	private void newGame(ActionEvent actionEvent) throws IOException {
		Player p = new Player("player", PokemonList.Normal());
		root.getScene().setRoot(new SelectionScene());
	}

	@FXML
	private void exit(ActionEvent actionEvent) {
		mediaPlayer.stop();
		Platform.exit();
	}

	@FXML
	private void soundControl(ActionEvent actionEvent) {
		if ( Music.isMute ) {
			Music.isMute = false;
			soundBtn.setText("\uD83D\uDD0A");
			mediaPlayer.play();
		} else {
			Music.isMute = true;
			soundBtn.setText("\uD83D\uDD08");
			mediaPlayer.stop();
		}
	}
}
