package FinalMonster.Utils;

import FinalMonster.Graphics.Components.BattleScene;
import javafx.animation.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SwitchPokemon {
	public enum SwitchMode {
		SET,
		DIED,
		START,
		ENTER,
		EXIT
	}

	public static void animateSwitch(ImageView image, ColorAdjust colorAdjust, Image pokeimg, BattleScene.Who who, SwitchMode mode, Callback callable) {
		colorAdjust.setBrightness(1);
		switch ( mode ) {
			case ENTER:
				image.setImage(pokeimg);
				if ( who == BattleScene.Who.PLAYER ) {
					image.setLayoutX(BattleScene.PLAYER_X_CENTER - ( pokeimg.getWidth() / 2 ));
					image.setLayoutY(BattleScene.PLAYER_Y_CENTER - ( pokeimg.getHeight() / 2 ));
				} else {
					image.setLayoutX(BattleScene.OPPONENT_X_BOTTOM);
					image.setLayoutY(BattleScene.OPPONENT_Y_BOTTOM - pokeimg.getHeight());
				}
				FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), image);
				fadeTransition.setFromValue(0);
				fadeTransition.setToValue(1);
				fadeTransition.setOnFinished(event -> {
					System.out.println("hahah");
					System.out.println(colorAdjust);
					Timeline timeline = new Timeline();
					timeline.getKeyFrames().add(
							new KeyFrame(Duration.millis(33), event1 -> {
								if ( colorAdjust.getBrightness() > 0 ) {
									colorAdjust.setBrightness(colorAdjust.getBrightness() - 0.03);
								} else {
									timeline.stop();
									callable.call();
								}
							}));
					timeline.setCycleCount(Animation.INDEFINITE);
					timeline.play();
				});
				fadeTransition.play();
				break;
			case EXIT:
				fadeTransition = new FadeTransition(Duration.seconds(1), image);
				fadeTransition.setFromValue(1);
				fadeTransition.setToValue(0);
				fadeTransition.setOnFinished(event -> callable.call());
				fadeTransition.play();
				break;

			default:
				throw new IllegalArgumentException(mode + " is not a valid type in SwitchPokemon::animateSwitch");
		}
	}
}
