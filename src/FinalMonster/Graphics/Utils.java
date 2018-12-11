
package FinalMonster.Graphics;

import FinalMonster.Utils.Callback;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
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


}
