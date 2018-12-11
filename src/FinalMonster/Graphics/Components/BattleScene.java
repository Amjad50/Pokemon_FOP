package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;

public class BattleScene extends StackPane {

	private final double MARGIN_SIZE = 20,
			STATUS_WIDTH = 250,
			STATUS_HEIGHT = 50,
			BOTTOM_HEIGHT = 100;
	@FXML
	private Pane status_pane;
	@FXML
	private Pane normal_pane;

	@FXML
	private Pane root;
	@FXML
	private StatusBar status_opponent;
	@FXML
	private StatusBar status_player;

	@FXML
	private BottomBar bottomBar;

	public BattleScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("battle_scene.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();
	}

	@FXML
	private void initialize() {
		initSizes();

		double status_playerY = Constrains.ROOT_HEIGHT - MARGIN_SIZE - STATUS_HEIGHT - BOTTOM_HEIGHT - 20,
				status_playerX = Constrains.ROOT_WIDTH - MARGIN_SIZE - STATUS_WIDTH - 20,
				status_opponentY = MARGIN_SIZE,
				status_opponentX = MARGIN_SIZE;

		status_opponent.setLayoutX(status_opponentX);
		status_opponent.setLayoutY(status_opponentY);
		status_player.setLayoutX(status_playerX);
		status_player.setLayoutY(status_playerY);
		bottomBar.setLayoutY(Constrains.ROOT_HEIGHT - BOTTOM_HEIGHT);

		root.setBackground(new Background(new BackgroundImage(
				new Image(ImageDB.BG[0]),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(Constrains.ROOT_WIDTH, Constrains.ROOT_HEIGHT, false, false, false, false)
		)));
	}

	private void initSizes() {

		root.setPrefHeight(Constrains.ROOT_HEIGHT);
		root.setPrefWidth(Constrains.ROOT_WIDTH);
		status_pane.setPrefHeight(Constrains.ROOT_HEIGHT);
		status_pane.setPrefWidth(Constrains.ROOT_WIDTH);

	}

	public void addChild(Node node) {
		this.normal_pane.getChildren().add(node);
	}
}
