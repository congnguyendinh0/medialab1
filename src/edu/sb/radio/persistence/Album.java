package edu.sb.radio.persistence;

import java.util.Collections;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Album")
@PrimaryKeyJoinColumn(name = "albumIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public abstract class Album extends BaseEntity {
	@Size(max = 127)
	@Column(nullable = false, name = "title", updatable = true, length = 129)
	private String title;
	
	@Column(nullable = false, name = "releaseYear", updatable = true)
	private Integer releaseYear;
	
	@Column(nullable = false, name = "trackCount", updatable = true)
	private Integer trackCount;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "coverReference", nullable = true, updatable = true)
	private Document cover;
	
	@NotNull
	@OneToMany(mappedBy = "album", orphanRemoval = false, cascade = { CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
	@Valid
	private Set<Track> tracks;
	
	public Album() {
		this.tracks = Collections.emptySet();
	}

	@JsonbTransient @XmlTransient
	public Document getCover() {
		return cover;
	}

	protected void setCover(Document cover) {
		this.cover = cover;
	}

	@JsonbTransient @XmlTransient
	public Set<Track> getTracks() {
		return tracks;
	}

	protected void setTracks(Set<Track> tracks) {
		this.tracks = tracks;
	}

	@JsonbProperty @XmlAttribute
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonbProperty @XmlAttribute
	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	@JsonbProperty @XmlAttribute
	public Integer getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(Integer trackCount) {
		this.trackCount = trackCount;
	}
	
	/**
	 * Aufgabe 2
	 */
	@JsonbProperty @XmlAttribute
	protected abstract Long getCoverReference();
	
	@JsonbProperty @XmlAttribute
	protected abstract long[] getTrackReferences();
}
