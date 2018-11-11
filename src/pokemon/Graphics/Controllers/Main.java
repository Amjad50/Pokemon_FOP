package pokemon.Graphics.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Main {

	private final double
			ROOT_HEIGHT = 800,
			ROOT_WIDTH = 1200,
			STATUS_HEIGHT = ( 50 + 10 ) * 3,
			STATUS_WIDTH = 250,
			MARGIN_SIZE = 20;


	@FXML
	private Pane root;
	@FXML
	private VBox status_opponent;
	@FXML
	private VBox status_player;

	@FXML
	private void initialize() {
		double status_allY = ROOT_HEIGHT - MARGIN_SIZE - STATUS_HEIGHT,
				status_opponentX = MARGIN_SIZE,
				status_playerX = ROOT_WIDTH - MARGIN_SIZE - STATUS_WIDTH;

		status_opponent.setLayoutX(status_opponentX);
		status_opponent.setLayoutY(status_allY);
		status_player.setLayoutX(status_playerX);
		status_player.setLayoutY(status_allY);
	}

	public void addChild(Node node) {
		this.root.getChildren().add(node);
	}
}
