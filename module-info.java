module edu.sb.radio.server {
	requires transitive java.sql;
	requires transitive java.activation;
	requires transitive java.instrument;
	requires transitive javax.annotation.api;
	requires transitive jdk.httpserver;

	requires transitive eclipselink.minus.jpa;
	requires transitive jersey.server;
	requires transitive jersey.container.jdk.http;

	requires transitive edu.sb.radio.model;
	exports edu.sb.radio.server;
}