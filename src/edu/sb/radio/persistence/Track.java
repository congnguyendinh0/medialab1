package edu.sb.radio.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity(name = "Track")
@Table(name = "Track", indexes = { @Index(name = "artist", columnList = "artist", unique = true),
		@Index(name = "genre", columnList = "genre", unique = true) })
@PrimaryKeyJoinColumn(name = "discriminator_id")
public class Track extends BaseEntity {
	private int id;
	private int albumID;
	private int personID;
	private int docuID;
	private String name;
	private String artist;
	private String genre;
	private Integer ordinal;

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	protected Track() {
		this(1, 1, 1, "some name", "some artist", "some genre", 1);
	}

	public Track(int albumID, int personID, int docuID, String name, String artist, String genre, Integer ordinal) {
		this.albumID = albumID;
		this.personID = personID;
		this.docuID = docuID;
		this.name = name;
		this.artist = artist;
		this.genre = genre;
		this.ordinal = ordinal;
	}

	@Id
	@Column(name = "trackIdentity")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "albumReference", referencedColumnName = "albumIdentity", updatable = true)
	public int getAlbumID() {
		return albumID;
	}

	protected void setAlbumID(int albumID) {
		this.albumID = albumID;
	}

	@ManyToOne
	@JoinColumn(name = "ownerReference", referencedColumnName = "personIdentity", updatable = true)
	public int getPersonID() {
		return personID;
	}

	protected void setPersonID(int personID) {
		this.personID = personID;
	}

	@ManyToOne
	@JoinColumn(name = "recordingReference", referencedColumnName = "documentIdentity", updatable = true)
	public int getDocuID() {
		return docuID;
	}

	protected void setDocuID(int docuID) {
		this.docuID = docuID;
	}

	@Column(nullable = false, name = "name", updatable = true)
	@Size(min = 1, max = 127)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, name = "artist", updatable = true)
	@Size(min = 1, max = 127)
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Column(nullable = false, name = "genre", updatable = true)
	@Size(min = 1, max = 31)
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Column(nullable = false, name = "ordinal", updatable = true)
	public Integer getOrdinal() {
		return ordinal;
	}
}
