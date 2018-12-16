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
			if ( !cmove.getType().isEmpty() )
				moves.get(i).setStyle(cmove.getTypeStyle());
			else
				moves.get(i).setStyle("");
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
		private String type;
		private Callback callable;

		public MoveHandler(String name, String type, Callback callable) {
			this.name = name;
			this.type = type;
			this.callable = callable;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private String getType() {
			return type;
		}

		private void setType(String type) {
			this.type = type;
		}

		public Callback getCallable() {
			return callable;
		}

		public void setCallable(Callback callable) {
			this.callable = callable;
		}

		private String getTypeStyle() {
			String result = "-fx-background-color: ";
			switch ( this.type ) {
				case "Normal":
					result += "#A8A77A";
					break;
				case "Fire":
					result += "#EE8130";
					break;
				case "Water":
					result += "#6390F0";
					break;
				case "Electric":
					result += "#F7D02C";
					break;
				case "Grass":
					result += "#7AC74C";
					break;
				case "Ice":
					result += "#96D9D6";
					break;
				case "Fighting":
					result += "#C22E28";
					break;
				case "Poison":
					result += "#A33EA1";
					break;
				case "Ground":
					result += "#E2BF65";
					break;
				case "Flying":
					result += "#A98FF3";
					break;
				case "Psychic":
					result += "#F95587";
					break;
				case "Bug":
					result += "#A6B91A";
					break;
				case "Rock":
					result += "#B6A136";
					break;
				case "Ghost":
					result += "#735797";
					break;
				case "Dragon":
					result += "#6F35FC";
					break;
				case "Dark":
					result += "#705746";
					break;
				case "Steel":
					result += "#B7B7CE";
					break;
				case "Fairy":
					result += "#D685AD";
					break;
				default:
					result += "";
					break;
			}
			return result + ";";
		}
	}
}
