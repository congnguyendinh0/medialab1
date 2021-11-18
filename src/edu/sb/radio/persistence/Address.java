package edu.sb.radio.persistence;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Address extends BaseEntity {
	private String street;
	private String postcode;
	private String city;
	private String land;

	protected Address() {
		this("some street", "some plz", "some city", "some land");
	}

	public Address(String street, String postcode, String city, String land) {
		this.street = street;
		this.postcode = postcode;
		this.city = city;
		this.land = land;
	}

	@NotNull
	@Size(min = 0, max = 63)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@NotNull
	@Size(min = 0, max = 15)
	public String getPostCode() {
		return postcode;
	}

	public void setPostCode(String postcode) {
		this.postcode = postcode;
	}

	@NotNull
	@Size(min = 1, max = 63)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@NotNull
	@Size(min = 1, max = 63)
	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}
}
