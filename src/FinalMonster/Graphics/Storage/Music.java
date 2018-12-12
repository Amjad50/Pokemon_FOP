package FinalMonster.Graphics.Storage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class Music {

	public enum Place {
		START("start.mp3"),
		BATTLE("battle.mp3"),
		MAP("map.mp3");

		private String filename;

		private Place(String filename) {
			this.filename = Place.class.getResource("/FinalMonster/resources/" + filename).toString();
		}


	}

	private static Map<Place, MediaPlayer> players;

	static {
		players = new HashMap<>();
		for ( Place key : Place.values() ) {
			players.put(key, new MediaPlayer(new Media(key.filename)));
		}
	}

	private static Map<Place, MediaPlayer> getPlayers() {
		return players;
	}
}
