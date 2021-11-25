package edu.sb.radio.persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Person", indexes = @Index(columnList = "email", unique = true))
@PrimaryKeyJoinColumn(name = "personIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public class Person extends BaseEntity {
	static public enum Group { ADMIN, USER }
	static private final String DEFAULT_PASSWORD_HASH = HashCodes.sha2HashText(256, "password");

	@Size(min = 64, max = 64)
	@Column(nullable = false, name = "passwordHash", updatable = true)
	private String passwordHash;
	
	@Valid
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "groupAlias", updatable = true)
	private Group group;
	
	@Embedded
	@Valid
	private Name name;
	
	@Embedded
	@Valid
	private Address address;
	
	private Set<String> phones;
	
	@Email
	@Size(min = 1, max = 128)
	@Column(nullable = false, name = "email", unique = true, updatable = true)
	private String email;
	
	@OneToMany(mappedBy = "negotiator", orphanRemoval = false, cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
	@Valid
	private Set<Negotiation> negotiations;
	
	@OneToMany(mappedBy = "owner", orphanRemoval = false, cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
	@Valid
	private Set<Track> tracks;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "avatarReference", nullable = false, updatable = true)
	private Document avatar;
	
	public Person() {
		this.passwordHash = DEFAULT_PASSWORD_HASH;
		this.group = Group.USER;
		this.name = new Name();
		this.address = new Address();
		this.phones = new HashSet<>();
		this.negotiations = Collections.emptySet();
		this.tracks = Collections.emptySet();
	}

	@JsonbTransient @XmlTransient
	public Set<Negotiation> getNegotiations() {
		return negotiations;
	}

	protected void setNegotiations(Set<Negotiation> negotiations) {
		this.negotiations = negotiations;
	}

	@JsonbTransient @XmlTransient
	public Set<Track> getTracks() {
		return tracks;
	}

	protected void setTracks(Set<Track> tracks) {
		this.tracks = tracks;
	}
	
	@JsonbTransient @XmlTransient
	public Document getAvatar() {
		return avatar;
	}

	protected void setAvatar(Document avatar) {
		this.avatar = avatar;
	}
	
	@JsonbTransient @XmlTransient
	public Set<String> getPhones() {
		return phones;
	}

	protected void setPhones(Set<String> phones) {
		this.phones = phones;
	}

	@JsonbProperty @XmlAttribute
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonbTransient @XmlTransient
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@JsonbProperty @XmlAttribute
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@JsonbProperty @XmlAttribute
	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	@JsonbProperty @XmlAttribute
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
