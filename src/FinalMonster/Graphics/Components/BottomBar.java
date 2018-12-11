package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Utils;
import FinalMonster.Utils.Callback;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;

public class BottomBar extends HBox {

	@FXML
	private Label text;

	@FXML
	private ArrayList<Button> moves;

	@FXML
	private HBox moves_bar;

	@FXML
	private Button btnFight;

	@FXML
	private Button btnPokemon;

	@FXML
	private Button btnRun;

	private boolean movesShown;
	private String typingString;
	private Timeline currentAnim;

	public BottomBar() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("bottom_bar.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();
	}

	public boolean setText(String string) {
		if ( currentAnim != null && currentAnim.getStatus() != Animation.Status.STOPPED ) {
			this.text.setText(typingString);
			currentAnim.stop();
			return false;
		}
		typingString = string;
		currentAnim = Utils.typeText(this.text, string, () -> {
		});
		return true;
	}

	public void setMoves(MoveHandler[] moves_) {
		assert moves_.length == moves.size();
		for ( int i = 0; i < moves_.length; i++ ) {
			MoveHandler cmove = moves_[i];
			moves.get(i).setText(cmove.name);
			moves.get(i).setOnAction(event -> cmove.callable.call());
		}
	}

	public void setMovesVisibility(boolean value) {
		if ( value != this.movesShown ) {
			this.movesShown = value;
			this.moves_bar.setVisible(value);
			this.moves_bar.setManaged(value);
		}
	}

	public boolean isMovesVisible() {
		return this.movesShown;
	}


	public void setFightAction(EventHandler<ActionEvent> handler) {
		this.btnFight.setOnAction(handler);
	}

	public void setRunAction(EventHandler<ActionEvent> handler) {
		this.btnRun.setOnAction(handler);
	}

	public void setPokemonAction(EventHandler<ActionEvent> handler) {
		this.btnPokemon.setOnAction(handler);
	}

	public class MoveHandler {
		private String name;
		private Callback callable;
	}
}
