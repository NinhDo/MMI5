package program;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jsonObjects.Contestant;
import jsonObjects.Track;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static javafx.geometry.HPos.*;

public class MainController implements Initializable {
	public Button btnBoard;
	public Button btnMap;
	public GridPane leaderboard;
	public GridPane playerGrid;
	public GridPane regularScreen;
	public GridPane playerProgressGrid;
	public VBox playerList;
	public Text playerPlace;
	public Text playerName;
	public Text playerSteps;
	public Text trackName;
	public Text trackDifficulty;
	public Text playerProgress;
	public Text timeRemaining;
	public Button btnTrackInfo;
	public GridPane map;
	public Button btnLeave;
	public Text trackInfoFinishLine;
	public Text trackInfoDifficulty;
	public Text trackInfoEndDate;
	public Text trackInfoStartDate;
	public Text trackInfoName;
	public Button btnBack;
	public GridPane trackInfo;
	public Text compareeSteps;
	public Text compareeName;
	public Text compareePlace;
	public Text compareeAchievements;
	public GridPane compareeGrid;
	public Text userSteps;
	public Text userName;
	public Text userPlace;
	public Text userAchievements;
	public GridPane userGrid;
	public GridPane comparisonMap;
	public Button btnBack2;
	public GridPane comparisonGrid;
	private int trackID = 0;
	private int trackLength;
	private long trackEnd;
	private List<Contestant> Contestants = new ArrayList<>();
	private Contestant user;
	private Contestant firstPlace;
	private Contestant secondPlace;
	private Contestant thirdPlace;
	private Contestant comparee;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		makeList();
		loadInfo();
		drawMap();
	}

	private void drawMap() {
		Polyline line = new Polyline();
		line.getPoints().addAll(
				0.0, 0.0,
				0.0, 10.0,
				0.0, 5.0,
				300.0, 5.0,
				300.0, 10.0,
				300.0, 0.0
		);
		line.setStrokeWidth(2);

		Polyline firstPlaceLine = new Polyline();
		firstPlaceLine.setStroke(Color.GOLD);
		Double firstDistance = (firstPlace.getDistance() * 300.0) / trackLength;
		firstPlaceLine.getPoints().addAll(
				0.0, 0.0,
				0.0, 100.0
		);
		firstPlaceLine.setTranslateX(firstDistance);
		firstPlaceLine.setTranslateY(50);
		firstPlaceLine.setStrokeWidth(4);

		Polyline secondPlaceLine = new Polyline();
		secondPlaceLine.setStroke(Color.SILVER);
		Double secondDistance = (secondPlace.getDistance() * 300.0) / trackLength;
		secondPlaceLine.getPoints().addAll(
				0.0, 0.0,
				0.0, 80.0
		);
		secondPlaceLine.setTranslateX(secondDistance);
		secondPlaceLine.setTranslateY(-40);
		secondPlaceLine.setStrokeWidth(4);

		Polyline thirdPlaceLine = new Polyline();
		thirdPlaceLine.setStroke(Color.valueOf("#cd7f32"));
		Double thirdDistance = (thirdPlace.getDistance() * 300.0) / trackLength;
		thirdPlaceLine.getPoints().addAll(
				0.0, 0.0,
				0.0, 40.0
		);
		thirdPlaceLine.setTranslateX(thirdDistance);
		thirdPlaceLine.setTranslateY(20);
		thirdPlaceLine.setStrokeWidth(4);

		Text firstName = new Text(firstPlace.getName());
		firstName.setUnderline(true);
		firstName.setTranslateX(firstDistance + 10);
		firstName.setTranslateY(100);
		firstName.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		Text secondName = new Text(secondPlace.getName());
		secondName.setUnderline(true);
		secondName.setTranslateX(secondDistance + 10);
		secondName.setTranslateY(-80);
		secondName.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		Text thirdName = new Text(thirdPlace.getName());
		thirdName.setUnderline(true);
		thirdName.setTranslateX(thirdDistance + 10);
		thirdName.setTranslateY(40);
		thirdName.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		Text markOne = new Text("0%");
		markOne.setTranslateX(-5);
		markOne.setTranslateY(20);

		Text markTwo = new Text("100%");
		markTwo.setTranslateX(290);
		markTwo.setTranslateY(20);

		map.getChildren().addAll(line, firstPlaceLine, secondPlaceLine, thirdPlaceLine, firstName, secondName, thirdName, markOne, markTwo);
		map.setStyle("-fx-background-color: #e6ffe6");

		if(Contestants.indexOf(user) > 2) {
			Polyline userLine = new Polyline();
			userLine.setStroke(Color.BLUE);
			Double userDistance = (user.getDistance() * 300.0) / trackLength;
			userLine.getPoints().addAll(
					0.0, 0.0,
					0.0, 150.0
			);
			userLine.setTranslateX(userDistance);
			userLine.setTranslateY(75);
			userLine.setStrokeWidth(4);

			Text userName = new Text(user.getName());
			userName.setUnderline(true);
			userName.setTranslateX(userDistance + 10);
			userName.setTranslateY(150);
			userName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
			map.getChildren().addAll(userLine, userName);
		}
	}

	private void loadInfo() {
		ObjectMapper mapper = new ObjectMapper();
		List<Track> tracks = new ArrayList<>();
		try {
			tracks = mapper.readValue(new File("tracks.json"), new TypeReference<List<Track>>() {
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		tracks.stream().filter(track -> track.getTrackID() == trackID).forEach(track -> {
			trackName.setText(track.getTrackName());
			trackDifficulty.setText(track.getDifficulty());
			trackLength = track.getTrackLength();
			int percentage = (user.getDistance() * 100) / trackLength;
			playerProgress.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(user.getDistance()) + "/" + NumberFormat.getNumberInstance(Locale.getDefault()).format(trackLength) + " (" + percentage + "%)");

			trackEnd = track.getEndDate();
			long deltaTime = trackEnd - System.currentTimeMillis() / 1000;
			timeRemaining.setText(new SimpleDateFormat("d 'd' k 'h' m 'm' s 's'").format(new Date(deltaTime * 1000)));
			trackInfoName.setText(track.getTrackName());
			trackInfoStartDate.setText(new SimpleDateFormat("dd.MM.yy - kk:mm:ss").format(new Date(track.getStartDate() * 1000)));
			trackInfoEndDate.setText(new SimpleDateFormat("dd.MM.yy - kk:mm:ss").format(new Date(track.getEndDate() * 1000)));
			trackInfoDifficulty.setText(track.getDifficulty());
			trackInfoFinishLine.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(track.getTrackLength()) + " Steps");
		});

		Main.getTimer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				long deltaTime = trackEnd - System.currentTimeMillis() / 1000;
				timeRemaining.setText(new SimpleDateFormat("d 'd' k 'h' m 'm' s 's'").format(new Date(deltaTime * 1000)));
			}
		}, 0, 1000);
	}

	private void makeList() {
		ObjectMapper mapper = new ObjectMapper();
		List<Contestant> allContestants = new ArrayList<>();
		try {
			allContestants = mapper.readValue(new File("contestants.json"), new TypeReference<List<Contestant>>() {
			});
			user = mapper.readValue(new File("user.json"), new TypeReference<Contestant>() {
			});
		} catch(IOException e) {
			e.printStackTrace();
		}
		trackID = user.getTrackID();
		Contestants.addAll(allContestants.stream().filter(Contestant -> Contestant.getTrackID() == trackID).collect(Collectors.toList()));
		Contestants.add(user);
		Collections.sort(Contestants, (o1, o2) -> o1.getDistance() < o2.getDistance() ? 1 : o1.getDistance() > o2.getDistance() ? -1 : 0);
		for(Contestant contestant : Contestants) {
			if(Contestants.indexOf(contestant) == 0) {
				firstPlace = contestant;
			} else if(Contestants.indexOf(contestant) == 1) {
				secondPlace = contestant;
			} else if(Contestants.indexOf(contestant) == 2) {
				thirdPlace = contestant;
			}
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

			Text placement = new Text("" + (Contestants.indexOf(contestant) + 1));
			if(contestant.getName().equals("You")) {
				playerPlace.setText("" + (Contestants.indexOf(contestant) + 1));
			}
			placement.setFont(Font.font("Arial", FontWeight.BOLD, 18));
			grid.add(placement, 0, 0, 1, 2);

			Text name = new Text(contestant.getName());
			if(contestant.getName().equals("You")) {
				if(user.isAnon()) {
					name.setText("You (Anonymous)");
					playerName.setText("You (Anonymous)");
				}
			}
			name.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			grid.add(name, 1, 0);

			Text achievements = new Text("Achievements: " + contestant.getAchievements());
			achievements.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
			grid.add(achievements, 1, 1);

			Text steps = new Text(NumberFormat.getNumberInstance(Locale.getDefault()).format(contestant.getDistance()) + " Steps");
			if(contestant.getName().equals("You")) {
				playerSteps.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(contestant.getDistance()) + " Steps");
			}
			steps.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			grid.add(steps, 2, 0);

			if(!contestant.getName().equals("You")) {
				Button btnCompare = new Button("Compare");
				btnCompare.setId("" + contestant.getId());
				btnCompare.setOnAction(event -> {
					System.out.println("HALLO");
					compare(event);
				});
				grid.add(btnCompare, 2, 1);
			}

			playerList.getChildren().add(grid);
			VBox.setVgrow(grid, Priority.ALWAYS);
			playerList.getChildren().add(new Separator());
		}
		playerList.getChildren().get(0).setStyle("-fx-background-color: gold");
		playerList.getChildren().get(2).setStyle("-fx-background-color: silver");
		playerList.getChildren().get(4).setStyle("-fx-background-color: #cd7f32");
	}

	private void compare(ActionEvent event) {
		regularScreen.setVisible(false);
		comparisonGrid.setVisible(true);
		int id = Integer.parseInt(((Button) event.getSource()).getId());
		for(Contestant contestant : Contestants) {
			if(contestant.getId() == id) {
				comparee = contestant;
				break;
			}
		}
		userName.setText(user.getName());
		userPlace.setText("" + (Contestants.indexOf(user) + 1));
		userSteps.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(user.getDistance()) + " Steps");
		userAchievements.setText("Achievements: " + user.getAchievements());
		compareeName.setText(comparee.getName());
		compareePlace.setText("" + (Contestants.indexOf(comparee) + 1));
		compareeSteps.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(comparee.getDistance()) + " Steps");
		compareeAchievements.setText("Achievements: " + comparee.getAchievements());

		Polyline line = new Polyline();
		line.getPoints().addAll(
				0.0, 0.0,
				0.0, 10.0,
				0.0, 5.0,
				300.0, 5.0,
				300.0, 10.0,
				300.0, 0.0
		);
		line.setStrokeWidth(2);

		Polyline firstPlaceLine = new Polyline();
		firstPlaceLine.setStroke(Color.LIGHTBLUE);
		Double firstDistance = (user.getDistance() * 300.0) / trackLength;
		firstPlaceLine.getPoints().addAll(
				0.0, 0.0,
				0.0, 50.0
		);
		firstPlaceLine.setTranslateX(firstDistance);
		firstPlaceLine.setTranslateY(25);
		firstPlaceLine.setStrokeWidth(4);

		Polyline secondPlaceLine = new Polyline();
		secondPlaceLine.setStroke(Color.LIGHTPINK);
		Double secondDistance = (comparee.getDistance() * 300.0) / trackLength;
		secondPlaceLine.getPoints().addAll(
				0.0, 0.0,
				0.0, 50.0
		);
		secondPlaceLine.setTranslateX(secondDistance);
		secondPlaceLine.setTranslateY(-25);
		secondPlaceLine.setStrokeWidth(4);

		Text firstName = new Text(user.getName());
		firstName.setUnderline(true);
		firstName.setTranslateX(firstDistance + 10);
		firstName.setTranslateY(50);
		firstName.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		Text secondName = new Text(comparee.getName());
		secondName.setUnderline(true);
		secondName.setTranslateX(secondDistance + 10);
		secondName.setTranslateY(-50);
		secondName.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		Text markOne = new Text("0%");
		markOne.setTranslateX(-5);
		markOne.setTranslateY(20);

		Text markTwo = new Text("100%");
		markTwo.setTranslateX(290);
		markTwo.setTranslateY(20);

		comparisonMap.getChildren().addAll(line, firstPlaceLine, secondPlaceLine, firstName, secondName, markOne, markTwo);
		comparisonMap.setStyle("-fx-background-color: #e6ffe6");
	}

	public void showBoard(ActionEvent actionEvent) {
		System.out.println(actionEvent.getSource());
		map.setVisible(false);
		leaderboard.setVisible(true);
	}

	public void showMap(ActionEvent actionEvent) {
		System.out.println(actionEvent.getSource());
		leaderboard.setVisible(false);
		map.setVisible(true);
	}

	public void showTrackInfo(ActionEvent actionEvent) {
		System.out.println(actionEvent.getSource());
		regularScreen.setVisible(false);
		playerProgressGrid.setVisible(false);
		trackInfo.setVisible(true);
	}

	public void leaveCompetition(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Leave Competition");
		alert.setHeaderText("Leave Competition?");
		alert.setContentText("Are you sure you want to leave?");
		ButtonType buttonYes = new ButtonType("Leave");
		ButtonType buttonNo = new ButtonType("Stay");
		alert.getButtonTypes().setAll(buttonYes, buttonNo);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.isPresent() && result.get() == buttonYes) {
			user.setTrackID(0);
			user.setAnon(false);
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File("user.json"), user);
			} catch(IOException e) {
				e.printStackTrace();
			}

			Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
			Parent root = null;
			try {
				root = FXMLLoader.load(getClass().getResource("contestSelectInterface.fxml"));
			} catch(IOException e) {
				e.printStackTrace();
			}
			stage.setTitle("Select Track Interface");
			assert root != null;
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	public void backToMain(ActionEvent actionEvent) {
		System.out.println(actionEvent.getSource());
		trackInfo.setVisible(false);
		comparisonGrid.setVisible(false);
		regularScreen.setVisible(true);
		playerProgressGrid.setVisible(true);
		comparisonMap.getChildren().clear();
	}
}
