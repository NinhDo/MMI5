package program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Timer;

public class Main extends Application {

	private static Timer timer = new Timer();

	static Timer getTimer() {
		return timer;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		primaryStage.setTitle("Login");
		primaryStage.setScene(new Scene(root, 360, 640));
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setOnCloseRequest(event -> timer.cancel());
	}
}
