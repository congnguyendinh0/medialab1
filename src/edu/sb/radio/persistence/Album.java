package edu.sb.radio.persistence;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;

@Entity(name = "Album")
@Table(name = "Album")
@PrimaryKeyJoinColumn(name = "discriminator_id")
public class Album extends BaseEntity {
	private int id;
	private int docuID;
	private Set<Track> tracks;
	private String title;
	private Integer releaseYear;
	private Integer trackCount;

	protected Album() {
		this(1, "some title", 2021, 12);
	}

	public Album(int docuID, String title, Integer releaseYear, Integer trackCount) {
		this.docuID = docuID;
		this.title = title;
		this.releaseYear = releaseYear;
		this.trackCount = trackCount;
	}

	@Id
	@Column(name = "albumIdentity")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "coverReference", referencedColumnName = "documentIdentity", updatable = true)
	public int getDocuID() {
		return docuID;
	}

	protected void setDocuID(int docuID) {
		this.docuID = docuID;
	}

	@OneToMany(mappedBy = "albumID", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
	@Valid
	public Set<Track> getTracks() {
		return tracks;
	}

	protected void setTracks(Set<Track> tracks) {
		this.tracks = tracks;
	}

	@Column(nullable = false, name = "title", updatable = true)
	@Size(min = 0, max = 127)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false, name = "releaseYear", updatable = true)
	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	@Column(nullable = false, name = "trackCount", updatable = true)
	public Integer getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(Integer trackCount) {
		this.trackCount = trackCount;
	}
}
