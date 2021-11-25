package edu.sb.radio.persistence;

import java.util.Collections;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Document", indexes = { @Index(name = "type", columnList = "type", unique = true) })
@PrimaryKeyJoinColumn(name = "documentIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public class Document extends BaseEntity {
	@Size(min = 64, max = 64)
	@Column(nullable = false, name = "hash", unique = true, updatable = true)
	private String hash;
	
	@Size(min = 1, max = 63)
	@Column(nullable = false, name = "type", updatable = true)
	private String type;
	
	@Size(min = 1, max = 16777215)
	@Column(nullable = false, name = "content", updatable = true)
	private byte[] content;
	
	@OneToMany(mappedBy = "avatar", orphanRemoval = false, cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
	@Valid
	private Set<Person> avatars;
	
	@OneToMany(mappedBy = "recording", orphanRemoval = false, cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
	@Valid
	private Set<Track> recordings;
	
	@OneToMany(mappedBy = "cover", orphanRemoval = false, cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
	@Valid
	private Set<Album> covers;

	protected Document() {
		this(null);
	}

	public Document(byte[] content) {
		this.hash = HashCodes.sha2HashText(256, content);
		this.type = "image/jpeg"; // hash image
		this.content = content;
		this.avatars = Collections.emptySet();
		this.recordings = Collections.emptySet();
		this.covers = Collections.emptySet();
	}
	
	@JsonbTransient @XmlTransient
	public Set<Person> getAvatars() {
		return avatars;
	}

	protected void setAvatars(Set<Person> avatars) {
		this.avatars = avatars;
	}
	
	@JsonbTransient @XmlTransient
	public Set<Track> getRecordings() {
		return recordings;
	}

	protected void setRecordings(Set<Track> recordings) {
		this.recordings = recordings;
	}
	
	@JsonbTransient @XmlTransient
	public Set<Album> getCovers() {
		return covers;
	}

	protected void setCovers(Set<Album> covers) {
		this.covers = covers;
	}

	@JsonbProperty @XmlAttribute
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@JsonbProperty @XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonbTransient @XmlTransient
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
