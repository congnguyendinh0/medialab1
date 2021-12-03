package edu.sb.radio.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Track", indexes = { 
		@Index(columnList = "artist", unique = false),
		@Index(columnList = "genre", unique = false) 
})
@PrimaryKeyJoinColumn(name = "trackIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public class Track extends BaseEntity {
	@ManyToOne(optional = false)
	@JoinColumn(name = "ownerReference", nullable = false, updatable = false, insertable = true)
	private Person owner;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "albumReference", nullable = false, updatable = false, insertable = true)
	private Album album;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "recordingReference", nullable = false, updatable = false, insertable = true)
	private Document recording;
	
	@Column(nullable = false, updatable = true, length = 127)
	@NotNull @Size(min = 1, max = 127)
	private String name;
	
	@Column(nullable = false, updatable = true, length = 127)
	@NotNull @Size(min = 1, max = 127)
	private String artist;
	
	@Column(nullable = false, updatable = true, length = 31)
	@NotNull @Size(min = 1, max = 31)
	private String genre;
	
	@PositiveOrZero
	@Column(nullable = false, updatable = true)
	private byte ordinal;

	protected Track() {
		this(null, null, null);
	}

	public Track(Person owner, Album album, Document recording) {
		this.owner = owner;
		this.album = album;
		this.recording = recording;
	}

	@JsonbTransient @XmlTransient
	public Person getOwner() {
		return owner;
	}

	protected void setOwner(Person owner) {
		this.owner = owner;
	}
	
	@JsonbTransient @XmlTransient
	public Album getAlbum() {
		return album;
	}

	protected void setAlbum(Album album) {
		this.album = album;
	}

	@JsonbTransient @XmlTransient
	public Document getRecording() {
		return recording;
	}

	protected void setRecording(Document recording) {
		this.recording = recording;
	}

	@JsonbProperty @XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonbProperty @XmlAttribute
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	@JsonbProperty @XmlAttribute
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	@JsonbProperty @XmlAttribute
	public byte getOrdinal() {
		return ordinal;
	}
	
	public void setOrdinal(byte ordinal) {
		this.ordinal = ordinal;
	}
	
	
	/**
	 * Aufgabe 2
	 */
//	@JsonbProperty @XmlAttribute
//	protected Long getAlbumReference();
//	
//	@JsonbProperty @XmlAttribute
//	protected Long getOwnerReference();
//	
//	@JsonbProperty @XmlAttribute
//	protected Long getRecordingReference();
}
