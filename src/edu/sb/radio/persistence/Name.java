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
@XmlType
@XmlRootElement
public class Name implements Comparable<Name> {
	static private final Comparator<Name> COMPARATOR = Comparator
			.comparing(Name::getTitle, Comparator.nullsLast(Comparator.naturalOrder()))
			.thenComparing(Name::getFamily)
			.thenComparing(Name::getGiven);

	@Size(min = 1, max = 15)
	@Column(nullable = true, updatable = true, length = 15)
	private String title;

	@NotNull
	@Size(min = 1, max = 31)
	@Column(nullable = false, updatable = true, length = 31)
	private String family;

	@NotNull
	@Size(min = 1, max = 31)
	@Column(nullable = false, updatable = true, length = 31)
	private String given;

	@JsonbProperty @XmlAttribute
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonbProperty @XmlAttribute
	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	@JsonbProperty @XmlAttribute
	public String getGiven() {
		return given;
	}

	public void setGiven(String given) {
		this.given = given;
	}

	@Override
	public int compareTo(Name other) {
		return COMPARATOR.compare(this, other);
	}
}
