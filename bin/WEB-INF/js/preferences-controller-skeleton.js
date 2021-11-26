import Controller from "./controller.js";
import xhr from "./xhr.js";


/**
 * Preferences controller type.
 * Copyright (c) 2019 Sascha Baumeister
 */
class PreferencesController extends Controller {

	/**
	 * Initializes a new controller instance.
	 */
	constructor () {
		super();
	}


	/**
	 * Displays the view associated with this controller.
	 */
	async display () {
		if (!Controller.sessionOwner) {
			document.querySelector("header li:first-of-type > a").click();
			return;
		}

		super.display();
		this.displayError();
		try {
			let section = document.querySelector("#preferences-template").content.cloneNode(true).firstElementChild;
			document.querySelector("main").append(section);
			section.querySelector("button").addEventListener("click", event => this.storeSessionOwner());
			section.querySelector("img").addEventListener("drop", event => this.storeSessionOwnerAvatar(event.dataTransfer.files[0]));

			this.displaySessionOwner();
		} catch (error) {
			this.displayError(error);
		}
	}


	/**
	 * Displays the session owner details.
	 */
	displaySessionOwner () {
		let sessionOwner = Controller.sessionOwner;
		let section = document.querySelector("section.preferences");

		let elements = section.querySelectorAll("img, input");
		elements[0].src = "/services/documents/" + sessionOwner.avatarReference + "?cache-bust=" + Date.now();
		elements[1].value = sessionOwner.email;
		elements[2].value = "";
		elements[3].value = sessionOwner.name.given;
		elements[4].value = sessionOwner.name.family;
		elements[5].value = sessionOwner.group;
		elements[6].value = sessionOwner.address.street;
		elements[7].value = sessionOwner.address.postcode;
		elements[8].value = sessionOwner.address.city;
	}


	/**
	 * Returns a promise that resolves after the session owner's current state has been
	 * stored using a webservice call.
	 * @return {Promise} a promise that resolves after the webservice call
	 */
	async storeSessionOwner () {
		this.displayError();
		try {
			// TODO
		} catch (error) {
			document.querySelector("header li:first-of-type > a").click();
			this.displayError(error);
		}
	}


	/**
	 * Returns a promise that resolves after the given file has been stored as the session
	 * owner's current avatar using a webservice call.
	 * @param {File} avatarFile the avatar file
	 * @return {Promise} a promise that resolves after the webservice call
	 */
	async storeSessionOwnerAvatar (avatarFile) {
		this.displayError();
		try {
			// TODO: call fetch(POST /services/documents and /services/people/{id}) to store the given avatar file.
			// Throw an exception if the call fails. If it succeeds, increment the version of
			// Controller.sessionOwner by 1. In case of an error, call this.displayError(error).
			// In any case, alter the src-property of the image to "/services/documents/{id}?cache-bust=" + Date.now()
			// in order to bypass the browser's image cache and display the modified image.
		} catch (error) {
			this.displayError(error);
		}
	}
}


/**
 * Performs controller event listener registration during DOM load event handling.
 */
window.addEventListener("load", event => {
	let controller = new PreferencesController();
	let anchors = document.querySelectorAll("header li > a");
	anchors.forEach((anchor, index) => anchor.addEventListener("click", event => controller.active = (index === 1)));
	window.addEventListener("beforeunload", event => controller.active = false);
});