package edu.sb.radio.persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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
	
	// properties
	@Column(nullable = false, name = "email", unique = true, updatable = true)
	@Email
	@Size(min = 1, max = 128)
	private String email;
	private String passwordHash;
	private Group group;
	private Name name;
	private Address address;
	private Set<String> phones;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "avatarReference", nullable = false, updatable = true)
	private Document avatar;
	private Set<Negotiation> negotiations;
	private Set<Track> tracks;

	// already corrected
	public Person() {
		this.passwordHash = DEFAULT_PASSWORD_HASH;
		this.group = Group.USER;
		this.name = new Name();
		this.address = new Address();
		this.phones = new HashSet<>();
		this.negotiations = Collections.emptySet();
		this.tracks = Collections.emptySet();
	}
	
	public Document getAvatar() {
		return avatar;
	}

	protected void setAvatar(Document avatar) {
		this.avatar = avatar;
	}

	@OneToMany(mappedBy = "personID", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE})
	@Valid
	public Set<Negotiation> getNegotiations() {
		return negotiations;
	}

	protected void setNegotiations(Set<Negotiation> negotiations) {
		this.negotiations = negotiations;
	}

	@OneToMany(mappedBy = "personID", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE})
	@Valid
	public Set<Track> getTracks() {
		return tracks;
	}

	protected void setTracks(Set<Track> tracks) {
		this.tracks = tracks;
	}

	
	@JsonbProperty @XmlAttribute
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false, name = "passwordHash", updatable = true)
	@Size(min = 64, max = 64)
	@JsonbTransient @XmlTransient
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "groupAlias", updatable = true)
	@Valid
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "title", column = @Column(nullable = true, length = 15, name = "title", updatable = true)),
			@AttributeOverride(name = "surname", column = @Column(nullable = false, length = 31, name = "surname", updatable = true)),
			@AttributeOverride(name = "forename", column = @Column(nullable = false, length = 31, name = "forename", updatable = true)) })
	@Valid
	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "street", column = @Column(nullable = false, length = 63, name = "street", updatable = true)),
			@AttributeOverride(name = "postcode", column = @Column(nullable = false, length = 15, name = "postcode", updatable = true)),
			@AttributeOverride(name = "city", column = @Column(nullable = false, length = 63, name = "city", updatable = true)),
			@AttributeOverride(name = "country", column = @Column(nullable = false, length = 63, name = "country", updatable = true)) })
	@Valid
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
