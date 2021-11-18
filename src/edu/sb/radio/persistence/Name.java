package edu.sb.radio.persistence;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Name {
	private String title;
	private String surname;
	private String forename;

	protected Name() {
		this(null, "Vu", "Huong");
	}

	public Name(String title, String surname, String forename) {
		this.title = title;
		this.surname = surname;
		this.forename = forename;
	}

	@Size(min = 0, max = 15)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotNull
	@Size(min = 1, max = 31)
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@NotNull
	@Size(min = 1, max = 31)
	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}
}
