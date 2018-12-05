package FinalMonster.Graphics.Controllers;

import FinalMonster.Graphics.Constrains;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Main {

	private final double
			ROOT_STATUS_GROUP_RATIO_H = 40.0 / 9.0,
			ROOT_STATUS_GROUP_RATIO_W = 24.0/ 5.0,
			STATUS_SPACING = 10,
			N_STATUS_ELEMENTS = 3,
			MARGIN_SIZE = 20;

	private double STATUS_GROUP_HEIGHT,
			STATUS_GROUP_WIDTH,
			STATUS_WIDTH,
			STATUS_HEIGHT;

	{
		double root_h = Constrains.ROOT_HEIGHT, root_w = Constrains.ROOT_WIDTH;

		STATUS_GROUP_HEIGHT = root_h  / ROOT_STATUS_GROUP_RATIO_H;
		STATUS_GROUP_WIDTH = root_w / ROOT_STATUS_GROUP_RATIO_W;
		STATUS_HEIGHT = (STATUS_GROUP_HEIGHT - (N_STATUS_ELEMENTS - 1) * 10) / N_STATUS_ELEMENTS;
		STATUS_WIDTH = STATUS_GROUP_WIDTH;
	}


	@FXML
	private Pane root;
	@FXML
	private VBox status_opponent;
	@FXML
	private VBox status_player;

	@FXML
	private void initialize() {
		initSizes();

		double status_allY = Constrains.ROOT_HEIGHT - MARGIN_SIZE - STATUS_GROUP_HEIGHT,
				status_opponentX = MARGIN_SIZE,
				status_playerX = Constrains.ROOT_WIDTH - MARGIN_SIZE - STATUS_GROUP_WIDTH;

		status_opponent.setLayoutX(status_opponentX);
		status_opponent.setLayoutY(status_allY);
		status_player.setLayoutX(status_playerX);
		status_player.setLayoutY(status_allY);
	}

	private void initSizes() {

		root.setPrefHeight(Constrains.ROOT_HEIGHT);
		root.setPrefWidth(Constrains.ROOT_WIDTH);

		status_opponent.setPrefHeight(STATUS_GROUP_HEIGHT);
		status_opponent.setPrefWidth(STATUS_GROUP_WIDTH);
		status_opponent.setSpacing(STATUS_SPACING);
		status_player.setPrefHeight(STATUS_GROUP_HEIGHT);
		status_player.setPrefWidth(STATUS_GROUP_WIDTH);
		status_player.setSpacing(STATUS_SPACING);
	}

	public void addChild(Node node) {
		this.root.getChildren().add(node);
	}
}
