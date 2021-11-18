package edu.sb.radio.persistence;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity(name = "Person")
@Table(name = "Person")
public class Person extends BaseEntity {
	// relationship
	@Embedded
	private Group group;
	
	@Embedded
	private Address address;
	
	@Embedded
	private Name name;

	// properties
	@Column(nullable = false, name = "email")
	@Email
	@Size(min = 1, max = 128)
	private String email;
	
	@Column(nullable = false, name = "passwordHash")
	@Size(min = 64, max = 64)
	private String passwordHash;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	protected Person() {
		this("test@gmail.com", "123456");
	}

	public Person(String email, String pH) {
		this.email = email;
		this.passwordHash = pH;
	}
}
