package edu.sb.radio.persistence;

import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import edu.sb.radio.util.HashCodes;

@Entity(name = "Person")
@Table(name = "Person")
@PrimaryKeyJoinColumn(name = "discriminator_id")
public class Person extends BaseEntity {
	public enum Group {
		ADMIN, USER
	}

	// properties
	private int id;
	private Set<Negotiation> negotiations;
	private Set<Track> tracks;
	private int docuID;
	private String email;
	private String passwordHash;
	private Group group;
	private Name name;
	private Address address;

	// Constructor
	protected Person() {
		this(1, "test@gmail.com", HashCodes.sha2HashText(256, "password"), Group.USER, null, "Vu", "Huong", "street",
				"plz", "city", "land");
	}

	public Person(int docuID, String email, String passwordHash, Group group, String title, String surname,
			String forename, String street, String postcode, String city, String land) {
		this.docuID = docuID;
		this.email = email;
		this.passwordHash = passwordHash;
		this.group = group;
		this.name = new Name(title, surname, forename);
		this.address = new Address(street, postcode, city, land);
	}

	// get set method
	@Id
	@Column(name = "personIdentity")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "avatarReference", referencedColumnName = "documentIdentity", updatable = true)
	public int getDocuID() {
		return docuID;
	}

	protected void setDocuID(int docuID) {
		this.docuID = docuID;
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

	@Column(nullable = false, name = "email", unique = true, updatable = true)
	@Email
	@Size(min = 1, max = 128)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false, name = "passwordHash", updatable = true)
	@Size(min = 64, max = 64)
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
