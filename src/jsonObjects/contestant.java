package jsonObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"name", "anon", "id", "distance", "achievements", "trackID"})
public class contestant {
	private String name = "Unknown Runner";
	private boolean anon = false;
	private int id = 1;
	private int distance = 0;
	private int achievements = 0;
	private int trackID = 0;

	public contestant() {
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("anon")
	public boolean isAnon() {
		return anon;
	}

	@JsonProperty("anon")
	public void setAnon(boolean anon) {
		this.anon = anon;
	}

	@JsonProperty("id")
	public int getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty("distance")
	public int getDistance() {
		return distance;
	}

	@JsonProperty("distance")
	public void setDistance(int distance) {
		this.distance = distance;
	}

	@JsonProperty("achievements")
	public int getAchievements() {
		return achievements;
	}

	@JsonProperty("achievements")
	public void setAchievements(int achievements) {
		this.achievements = achievements;
	}

	@JsonProperty("trackID")
	public int getTrackID() {
		return trackID;
	}

	@JsonProperty("trackID")
	public void setTrackID(int trackID) {
		this.trackID = trackID;
	}
}
