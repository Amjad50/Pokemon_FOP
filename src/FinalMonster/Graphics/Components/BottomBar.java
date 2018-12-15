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
import javafx.scene.layout.GridPane;
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
	private GridPane controls;

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

	public boolean setText(String string, Callback callable) {
		if ( currentAnim != null && currentAnim.getStatus() != Animation.Status.STOPPED ) {
			this.text.setText("");
			currentAnim.stop();
//			return false;
		}
		typingString = string;
		currentAnim = Utils.typeText(this.text, string, callable);
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

	public void setMovesVisibile(boolean value) {
		if ( value != this.movesShown ) {
			this.movesShown = value;
			this.moves_bar.setVisible(value);
			this.moves_bar.setManaged(value);
		}
	}

	public boolean isMovesVisible() {
		return this.movesShown;
	}

	public void setControlsVisibile(boolean value) {
		this.controls.setVisible(value);
	}

	public boolean isControlsVisible() {
		return this.controls.isVisible();
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

	public static class MoveHandler {
		private String name;
		private Callback callable;

		public MoveHandler(String name, Callback callable) {
			this.name = name;
			this.callable = callable;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Callback getCallable() {
			return callable;
		}

		public void setCallable(Callback callable) {
			this.callable = callable;
		}
	}
}
