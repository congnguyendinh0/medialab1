package edu.sb.radio.persistence;

import java.util.Collections;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbVisibility;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Album")
@PrimaryKeyJoinColumn(name = "albumIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public class Album extends BaseEntity {
	private String title;
	private short releaseYear;
	private byte trackCount;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "coverReference", nullable = true, updatable = true)
	private Document cover;
	
	@NotNull
	@OneToMany(mappedBy = "album", orphanRemoval = false, cascade = { CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
	private Set<Track> tracks;
	
	public Album() {
		this.tracks = Collections.emptySet();
	}

	public Document getCover() {
		return cover;
	}

	protected void setCover(Document cover) {
		this.cover = cover;
	}

	
	public Set<Track> getTracks() {
		return tracks;
	}

	protected void setTracks(Set<Track> tracks) {
		this.tracks = tracks;
	}

	@Column(nullable = false, name = "title", updatable = true)
	@Size(min = 0, max = 127)
	@JsonbProperty @XmlAttribute
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false, name = "releaseYear", updatable = true)
	@JsonbProperty @XmlAttribute
	public short getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(short releaseYear) {
		this.releaseYear = releaseYear;
	}

	@Column(nullable = false, name = "trackCount", updatable = true)
	@JsonbProperty @XmlAttribute
	public byte getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(byte trackCount) {
		this.trackCount = trackCount;
	}
}
