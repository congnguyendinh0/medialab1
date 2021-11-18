module edu.sb.radio.model {
	requires transitive java.logging;
	requires transitive java.validation;
	requires transitive javax.annotation.api;

	requires transitive java.xml.bind;
	requires transitive java.json.bind;
	requires transitive java.ws.rs;
	requires transitive javax.persistence;
	requires transitive eclipselink.minus.jpa;

	opens edu.sb.radio.persistence;
	exports edu.sb.radio.persistence;
	exports edu.sb.radio.service;
	exports edu.sb.radio.util;
}