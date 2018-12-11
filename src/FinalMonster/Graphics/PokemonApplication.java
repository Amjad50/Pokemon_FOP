package FinalMonster.Graphics;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PokemonApplication extends Application {


	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FinalMonster/Graphics/fxml/start_menu.fxml"));

		Parent root = loader.load();

		Scene scene = new Scene(root);
		scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public void run() {
		PlatformImpl.startup(() -> {
		});
		runThread(() -> {
			try {
				start(new Stage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void runThread(Runnable r) {
		Platform.runLater(r);
	}
}
