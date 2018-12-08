package FinalMonster.Graphics.Controllers;

import FinalMonster.Graphics.Components.StatusBar;
import FinalMonster.Graphics.Constrains;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Main {

	private final double MARGIN_SIZE = 20,
			STATUS_WIDTH = 250,
			STATUS_HEIGHT = 50;
	@FXML
	private Pane status_pane;
	@FXML
	private Pane normal_pane;

	{
		double root_h = Constrains.ROOT_HEIGHT, root_w = Constrains.ROOT_WIDTH;
	}


	@FXML
	private Pane root;
	@FXML
	private StatusBar status_opponent;
	@FXML
	private StatusBar status_player;

	@FXML
	private void initialize() {
		initSizes();

		double status_playerY = Constrains.ROOT_HEIGHT - MARGIN_SIZE - STATUS_HEIGHT,
				status_opponentY = MARGIN_SIZE,
				status_opponentX = Constrains.ROOT_WIDTH - MARGIN_SIZE - STATUS_WIDTH,
				status_playerX = MARGIN_SIZE;

		status_opponent.setLayoutX(status_opponentX);
		status_opponent.setLayoutY(status_opponentY);
		status_player.setLayoutX(status_playerX);
		status_player.setLayoutY(status_playerY);

		status_player.setHealth((status_player.getHealth()  - 80), ()->{
			System.out.println("done");
		});



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
