package program;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import jsonObjects.contestant;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static javafx.geometry.HPos.CENTER;
import static javafx.geometry.HPos.LEFT;
import static javafx.geometry.HPos.RIGHT;

public class MainController implements Initializable {
	public Button btnBoard;
	public Button btnMap;
	public GridPane leaderboard;
	public VBox playerList;
	public Text playerPlace;
	public Text playerName;
	public Text playerSteps;
	public Text trackName;
	public Text trackDifficulty;
	public Text playerProgress;
	public Text timeRemaining;
	public Button btnTrackInfo;
	private List<contestant> contestants = new ArrayList<contestant>();
	private contestant user;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		makeList();
	}

	private void makeList() {
		ObjectMapper mapper = new ObjectMapper();
		List<contestant> allContestants = new ArrayList<contestant>();
		try {
			allContestants = mapper.readValue(new File("contestants.json"), new TypeReference<List<contestant>>() {
			});
			user = mapper.readValue(new File("user.json"), new TypeReference<contestant>() {
			});
		} catch(IOException e) {
			e.printStackTrace();
		}
		for(contestant contestant : allContestants) {
			if(contestant.getTrackID() == user.getTrackID())
				contestants.add(contestant);
		}
		contestants.add(user);
		Collections.sort(contestants, new Comparator<contestant>() {
			@Override
			public int compare(contestant o1, contestant o2) {
				return o1.getDistance() < o2.getDistance() ? 1 : o1.getDistance() > o2.getDistance() ? -1 : 0;
			}
		});
		for(contestant contestant : contestants) {
			GridPane grid = new GridPane();
			grid.setVgap(5);
			grid.setHgap(5);
			grid.setPadding(new Insets(5, 5, 5, 5));
			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPrefWidth(50);
			col1.setHalignment(CENTER);
			ColumnConstraints col2 = new ColumnConstraints();
			col2.setPrefWidth(150);
			col2.setHalignment(LEFT);
			ColumnConstraints col3 = new ColumnConstraints();
			col3.setPrefWidth(150);
			col3.setHalignment(RIGHT);
			grid.getColumnConstraints().addAll(col1, col2, col3);

			Text placement = new Text("" + (contestants.indexOf(contestant) + 1));
			placement.setFont(Font.font("Arial", FontWeight.BOLD, 18));
			grid.add(placement, 0, 0, 1, 2);

			Text name = new Text(contestant.getName());
			if(user.getName().equals("You")) {
				if(user.isAnon()) name.setText("You (Anonymous)");
			}
			name.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			grid.add(name, 1, 0);

			Text achievements = new Text("Achievements: " + contestant.getAchievements());
			achievements.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
			grid.add(achievements, 1, 1);

			Text steps = new Text(contestant.getDistance() + " Steps");
			steps.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			grid.add(steps, 2, 0);

			Button btnCompare = new Button("Compare");
			btnCompare.setId(contestant.getName() + contestant.getId());
			btnCompare.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

				}
			});
			grid.add(btnCompare, 2, 1);

			playerList.getChildren().add(grid);
			playerList.setVgrow(grid, Priority.ALWAYS);
			playerList.getChildren().add(new Separator());
		}
		playerList.getChildren().get(0).setStyle("-fx-background-color: gold");
		playerList.getChildren().get(2).setStyle("-fx-background-color: silver");
		playerList.getChildren().get(4).setStyle("-fx-background-color: #cd7f32");
	}

	public void showBoard(ActionEvent actionEvent) {
		leaderboard.setVisible(true);
	}

	public void showMap(ActionEvent actionEvent) {
		leaderboard.setVisible(false);
	}

	public void showTrackInfo(ActionEvent actionEvent) {

	}
}
