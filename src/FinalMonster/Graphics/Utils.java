
package FinalMonster.Graphics;

import FinalMonster.Graphics.Components.BattleScene;
import FinalMonster.Utils.Callback;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Utils {
	public static Timeline typeText(Label label, String str, Callback callable) {
		Timeline timeline = new Timeline();

		final IntegerProperty i = new SimpleIntegerProperty(0);
		timeline.getKeyFrames().add(new KeyFrame(
				Duration.millis(50),
				event -> {
					if ( i.get() > str.length() ) {
						timeline.stop();
						callable.call();
					} else {
						label.setText(str.substring(0, i.get()));
						i.set(i.get() + 1);
					}
				}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		return timeline;
	}

	public static void attackEffect(ImageView defending, ImageView attacking, BattleScene.Who who, Callback callable) {
		Path path = new Path();
		path.getElements().add(new MoveTo(attacking.getImage().getWidth() / 2, attacking.getImage().getHeight() / 2));
		if ( who == BattleScene.Who.PLAYER )
			path.getElements().add(new LineTo(250, -15));
		else
			path.getElements().add(new LineTo(-150, 125));
		path.getElements().add(new LineTo(attacking.getImage().getWidth() / 2, attacking.getImage().getHeight() / 2));

		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(190));
		pathTransition.setPath(path);
		pathTransition.setNode(attacking);
		pathTransition.setOrientation(PathTransition.OrientationType.NONE);
		pathTransition.setOnFinished(event -> {
			Timeline timeline = new Timeline();
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), event2 -> {
				defending.setOpacity(0);
			}));
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), event2 -> {
				defending.setOpacity(1);
			}));
			timeline.setCycleCount(5);
			timeline.setOnFinished(event3 -> callable.call());
			timeline.play();
		});
		pathTransition.play();
	}


}
