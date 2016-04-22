package program;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jsonObjects.contestObject;
import jsonObjects.contestant;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.geometry.HPos.RIGHT;

public class ContestSelectController implements Initializable {
	public VBox VBoxContestList;
	private List<contestObject> contests;
	private contestant user;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		readFiles();
		loadTracks();
	}

	private void loadTracks() {
		for(contestObject contest : contests) {
			makePane(contest);
		}
	}

	private void makePane(contestObject contest) {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPrefWidth(250);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPrefWidth(80);
		gridPane.getColumnConstraints().addAll(col1, col2);

		Text trackName = new Text(contest.getTrackName());
		trackName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		gridPane.add(trackName, 0, 0);

		String startDate = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss").format(new Date(contest.getStartDate() * 1000));
		Text startDateText = new Text("Start date: " + startDate);
		gridPane.add(startDateText, 0, 1);

		String endDate = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss").format(new Date(contest.getEndDate() * 1000));
		Text endDateText = new Text("End date: " + endDate);
		gridPane.add(endDateText, 0, 2);

		Text difficulty = new Text("Difficulty: " + contest.getDifficulty());
		gridPane.add(difficulty, 0, 3);

		Text goal = new Text("Goal: " + contest.getTrackLength());
		gridPane.add(goal, 0, 4);

		Button btnJoin = new Button("Join");
		btnJoin.setId("" + contest.getTrackID());
		btnJoin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				joinTrack(event);
			}
		});
		gridPane.add(btnJoin, 1, 0, 1, 2);

		Button btnJoinAnon = new Button("Join as anonymous");
		btnJoinAnon.setTextAlignment(TextAlignment.CENTER);
		btnJoinAnon.setWrapText(true);
		btnJoinAnon.setId("" + contest.getTrackID());
		btnJoinAnon.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				user.setAnon(true);
				joinTrack(event);
			}
		});
		gridPane.add(btnJoinAnon, 1, 2, 1, 2);
		gridPane.getColumnConstraints().get(1).setHalignment(RIGHT);
		VBoxContestList.getChildren().add(gridPane);
		VBoxContestList.setVgrow(gridPane, Priority.ALWAYS);
		VBoxContestList.getChildren().add(new Separator());
	}

	private void joinTrack(ActionEvent actionEvent) {
		int id = Integer.parseInt(((Button) actionEvent.getSource()).getId());
		setUserTrackID(id);
		Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("mainInterface.fxml"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		stage.setTitle("Status Interface");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	private void setUserTrackID(int id) {
		user.setTrackID(id);
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("user.json"), user);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	private void readFiles() {
		ObjectMapper mapper = new ObjectMapper();
		File contestFile = new File("tracks.json");
		File userFile = new File("user.json");
		try {
			contests = mapper.readValue(contestFile, new TypeReference<List<contestObject>>() {
			});
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			user = mapper.readValue(userFile, new TypeReference<contestant>() {
			});
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
