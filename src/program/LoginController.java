package program;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jsonObjects.contestant;

import java.io.File;
import java.io.IOException;

public class LoginController {


	public void login(ActionEvent actionEvent) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File userFile = new File("user.json");
		contestant user = mapper.readValue(userFile, new TypeReference<contestant>() {
		});
		Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
		Parent root;
		if(user.getTrackID() == 0) {
			root = FXMLLoader.load(getClass().getResource("contestSelectInterface.fxml"));
			stage.setTitle("Select Track Interface");
		} else {
			root = FXMLLoader.load(getClass().getResource("mainInterface.fxml"));
			stage.setTitle("Status Interface");
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
