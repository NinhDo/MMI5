package jsonObjects;

/**
 * Created by Ninh Do on 18/04/2016.
 */


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"trackName", "trackLength", "difficulty", "startDate", "endDate", "sponsors", "trackID"})
public class contestObject {
	private String trackName = "";
	private int trackLength = 0;
	private String difficulty = "";
	private long startDate;
	private long endDate;
	private String sponsors;
	private int trackID = 0;

	public contestObject() {
		trackName = "New Track";
		trackLength = 0;
		difficulty = "Very Easy";
		startDate = 0;
		endDate = 0;
		sponsors = "No sponsors";
		trackID = 0;
	}

	@JsonProperty("trackName")
	public String getTrackName() {
		return trackName;
	}

	@JsonProperty("trackName")
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	@JsonProperty("trackLength")
	public int getTrackLength() {
		return trackLength;
	}

	@JsonProperty("trackLength")
	public void setTrackLength(int trackLength) {
		this.trackLength = trackLength;
	}

	@JsonProperty("difficulty")
	public String getDifficulty() {
		return difficulty;
	}

	@JsonProperty("difficulty")
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	@JsonProperty("startDate")
	public long getStartDate() {
		return startDate;
	}

	@JsonProperty("startDate")
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	@JsonProperty("endDate")
	public long getEndDate() {
		return endDate;
	}

	@JsonProperty("endDate")
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	@JsonProperty("sponsors")
	public String getSponsors() {
		return sponsors;
	}

	@JsonProperty("sponsors")
	public void setSponsors(String sponsors) {
		this.sponsors = sponsors;
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
