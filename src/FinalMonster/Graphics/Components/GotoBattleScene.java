package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import FinalMonster.Parser.Pokemon;
import FinalMonster.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GotoBattleScene extends Pane {

	private final int PLAYER_START_Y = 336,
			OPPONENT_START_Y = 200;

	private ImageView player;
	private ImageView opponent;

	public GotoBattleScene(Player playerPlayer, ArrayList<Pokemon> playerpokemons, Player opponentPlayer, ArrayList<Pokemon> opponentpokemons, boolean isWild) throws IOException {
		setWidth(Constrains.ROOT_WIDTH);
		setHeight(Constrains.ROOT_HEIGHT);

		player = new ImageView(playerPlayer.getVSimg());
		opponent = new ImageView(opponentPlayer.getVSimg());
		Label VStext = new Label("VS");
		VStext.setFont(Font.font(0));
		VStext.setTextFill(Color.CHOCOLATE);
		VStext.setMinHeight(100);
		VStext.setMinWidth(100);
		VStext.setTextAlignment(TextAlignment.CENTER);

		this.setBackground(new Background(new BackgroundImage(
				new Image(ImageDB.BG[1]),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(Constrains.ROOT_WIDTH, Constrains.ROOT_HEIGHT, false, false, false, false)
		)));

		this.getChildren().addAll(player, opponent, VStext);
		opponent.setLayoutX(Constrains.ROOT_WIDTH);
		player.setLayoutX(-player.getImage().getWidth());
		VStext.setLayoutX(Constrains.ROOT_WIDTH / 2 - 50);
		VStext.setLayoutY(Constrains.ROOT_HEIGHT / 2 - 50);

		player.setLayoutY(PLAYER_START_Y);
		opponent.setLayoutY(OPPONENT_START_Y);

		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(1000), new KeyValue(player.layoutXProperty(), Constrains.ROOT_WIDTH / 2 - player.getImage().getWidth() / 2)),
				new KeyFrame(Duration.millis(1000), new KeyValue(opponent.layoutXProperty(), Constrains.ROOT_WIDTH / 2 - opponent.getImage().getWidth() / 2))
		);

		timeline.setOnFinished(event -> {
			Timeline timeline2 = new Timeline();
			timeline2.getKeyFrames().add(
					new KeyFrame(Duration.millis(30), event1 -> {
						if ( VStext.getFont().getSize() < 40 ) {
							VStext.setFont(Font.font(VStext.getFont().getSize() + 3));
						} else {
							Platform.runLater(() -> {
								try {
									this.getScene().setRoot(new BattleScene(playerPlayer, playerpokemons, opponentPlayer, opponentpokemons, isWild));
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
							timeline2.stop();
						}
					})
			);
			timeline2.setCycleCount(Animation.INDEFINITE);
			timeline2.play();
		});

		timeline.play();

	}

	public GotoBattleScene(Player playerPlayer, Pokemon[] playerpokemons, Player opponentPlayer, Pokemon[] opponentpokemons, boolean isWild) throws IOException {
		this(playerPlayer, new ArrayList<>(Arrays.asList(playerpokemons)), opponentPlayer, new ArrayList<>(Arrays.asList(opponentpokemons)), isWild);
	}
}
