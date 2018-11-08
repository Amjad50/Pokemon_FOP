package pokemon.Graphics;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pokemon.Graphics.Controllers.Main;

import java.io.IOException;

public class PokemonApplication extends Application {

	private Main controller;

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/pokemon/Graphics/fxml/main.fxml"));

		Parent root = loader.load();

		this.controller = loader.getController();

		Scene scene = new Scene(root);


		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public void run() {
		PlatformImpl.startup(() -> {});
		runThread(()-> {
			try {
				start(new Stage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void runThread(Runnable r){
		Platform.runLater(r);
	}
}
