import Controller from "./controller.js";
import xhr from "./xhr.js";

/**
 * Welcome controller type.
 * Copyright (c) 2019 Sascha Baumeister
 */
class WelcomeController extends Controller {

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
		super.display();
		this.displayError();
		try {
			Controller.sessionOwner = null;

			let section = document.querySelector("#welcome-template").content.cloneNode(true).firstElementChild;
			section.querySelector("button").addEventListener("click", event => this.login());
			document.querySelector("main").append(section);
		} catch (error) {
			this.displayError(error);
		}
	}


	/**
	 * Performs a login check on the given user data, assigns the controller's
	 * user object if the login was successful, and initiates rendering of the
	 * preferences view.
	 */
	async login () {
 		if (!Controller.audioContext) {
 			Controller.audioContext = new AudioContext();
 			Controller.audioVolume = Controller.audioContext.createGain();
 		}

		this.displayError();
		try {
			let inputs = document.querySelectorAll("section.welcome input");
			const email = inputs[0].value.trim();
			const password = inputs[1].value.trim();

			// Although fetch() supports sending credentials from a browser's hidden Basic-Auth credentials store, it lacks
			// support for storing them securely. This workaround uses a classic XMLHttpRequest invocation as a workaround.
			Controller.sessionOwner = await xhr("/services/people/0", "GET", {"Accept": "application/json"}, null, "json", email, password);

			document.querySelector("header li:nth-of-type(2) > a").click();
		} catch (error) {
			this.displayError(error);
		}
	}
}


/**
 * Performs controller event listener registration during DOM load event handling.
 */
window.addEventListener("load", event => {
	let controller = new WelcomeController();
	let anchors = document.querySelectorAll("header li > a");
	anchors.forEach((anchor, index) => anchor.addEventListener("click", event => controller.active = (index === 0)));
	window.addEventListener("beforeunload", event => controller.active = false);
	anchors[0].click();
});