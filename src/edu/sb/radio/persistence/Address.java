package edu.sb.radio.persistence;

import java.util.Comparator;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Embeddable
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public class Address implements Comparable<Address> {
	static private final Comparator<Address> COMPARATOR = Comparator
			.comparing(Address::getCountry)
			.thenComparing(Address::getCity)
			.thenComparing(Address::getStreet)
			.thenComparing(Address::getPostcode);
	
	@NotNull
	@Size(max = 63)
	@Column(nullable = false, updatable = true, length = 63)
	private String street;

	@NotNull
	@Size(max = 15)
	@Column(nullable = false, updatable = true, length = 15)
	private String postcode;

	@NotNull
	@Size(min = 1, max = 63)
	@Column(nullable = false, updatable = true, length = 63)
	private String city;

	@NotNull
	@Size(min = 1, max = 63)
	@Column(nullable = false, updatable = true, length = 63)
	private String country;

	@JsonbProperty @XmlAttribute
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@JsonbProperty @XmlAttribute
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@JsonbProperty @XmlAttribute
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@JsonbProperty @XmlAttribute
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Override
	public int compareTo(Address other) {
		return COMPARATOR.compare(this, other);
	}
}
