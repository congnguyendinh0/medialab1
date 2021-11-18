-- MariaDB structure definition script for schema "radio"
-- best import using MariaDB client command "source <path to this file>"

SET CHARACTER SET utf8mb4;
DROP DATABASE IF EXISTS radio;
CREATE DATABASE radio CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE radio;

-- define tables, indices, etc.
CREATE TABLE BaseEntity (
	identity BIGINT NOT NULL AUTO_INCREMENT,
	discriminator ENUM("Document", "Person", "Negotiation", "Album", "Track") NOT NULL,
	version INTEGER NOT NULL DEFAULT 1,
	creationTimestamp BIGINT NOT NULL,
	modificationTimestamp BIGINT NOT NULL,
	PRIMARY KEY (identity),
	KEY (discriminator),
	KEY (creationTimestamp),
	KEY (modificationTimestamp)
);

CREATE TABLE Document (
	documentIdentity BIGINT NOT NULL,
	hash CHAR(64) NOT NULL,
	type VARCHAR(63) NOT NULL,
	content LONGBLOB NOT NULL,
	PRIMARY KEY (documentIdentity),
	FOREIGN KEY (documentIdentity) REFERENCES BaseEntity (identity) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE KEY (hash),
	KEY (type)
);

CREATE TABLE Person (
	personIdentity BIGINT NOT NULL,
	avatarReference BIGINT NOT NULL,
	email CHAR(128) NOT NULL,
	passwordHash CHAR(64) NOT NULL,
	groupAlias ENUM("USER", "ADMIN") NOT NULL,
	title VARCHAR(15) NULL,
	surname VARCHAR(31) NOT NULL,
	forename VARCHAR(31) NOT NULL,
	street VARCHAR(63) NOT NULL,
	postcode VARCHAR(15) NOT NULL,
	city VARCHAR(63) NOT NULL,
	country VARCHAR(63) NOT NULL,
	PRIMARY KEY (personIdentity),
	FOREIGN KEY (personIdentity) REFERENCES BaseEntity (identity) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (avatarReference) REFERENCES Document (documentIdentity) ON DELETE RESTRICT ON UPDATE CASCADE,
	UNIQUE KEY (email)
);

CREATE TABLE Negotiation (
	negotiationIdentity BIGINT NOT NULL,
	negotiatorReference BIGINT NOT NULL,
	type ENUM("WEB_RTC") NOT NULL,
	offer VARCHAR(2046) NOT NULL,
	answer VARCHAR(2046) NULL,
	PRIMARY KEY (negotiationIdentity),
	FOREIGN KEY (negotiationIdentity) REFERENCES BaseEntity (identity) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (negotiatorReference) REFERENCES Person (personIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
	KEY (type),
	KEY (offer),
	KEY (answer)
);

CREATE TABLE Album (
	albumIdentity BIGINT NOT NULL,
	coverReference BIGINT NULL,
	title VARCHAR(127) NOT NULL,
	releaseYear SMALLINT NOT NULL,
	trackCount TINYINT NOT NULL,
	PRIMARY KEY (albumIdentity),
	FOREIGN KEY (albumIdentity) REFERENCES BaseEntity (identity) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (coverReference) REFERENCES Document (documentIdentity) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Track (
	trackIdentity BIGINT NOT NULL,
	albumReference BIGINT NOT NULL,
	ownerReference BIGINT NOT NULL,
	recordingReference BIGINT NOT NULL,
	name VARCHAR(127) NOT NULL,
	artist VARCHAR(127) NOT NULL,
	genre VARCHAR(31) NOT NULL,
	ordinal TINYINT NOT NULL,
	PRIMARY KEY (trackIdentity),
	FOREIGN KEY (trackIdentity) REFERENCES BaseEntity (identity) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (albumReference) REFERENCES Album (albumIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (ownerReference) REFERENCES Person (personIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (recordingReference) REFERENCES Document (documentIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
	KEY (artist),
	KEY (genre)
);

CREATE TABLE PhoneAssociation (
	personReference BIGINT NOT NULL,
	phone CHAR(16) NOT NULL,
	PRIMARY KEY (personReference, phone),
	FOREIGN KEY (personReference) REFERENCES Person (personIdentity) ON DELETE CASCADE ON UPDATE CASCADE
);
