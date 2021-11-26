import Controller from "./controller.js";

// constants
const HALF_MINUTE_MILLIES = 30 * 1000;
const QUARTER_HOUR_MILLIES = 15 * 60 * 1000;


/**
 * Peer radio listener controller type.
 * Copyright (c) 2019 Sascha Baumeister
 */
class PeerRadioListenerController extends Controller {

	/**
	 * Initializes a new controller instance.
	 */
	constructor () {
		super();

		Object.defineProperty(this, "audioSource", { enumerable: true, value: null, writable: true });
		Object.defineProperty(this, "connection", { enumerable: true, value: null, writable: true });
	}


	/**
	 * Resets this controller.
	 */
	async reset () {
 		super.reset();
		if (this.audioSource) {
			this.audioSource.stop();
			this.audioSource.disconnect();
			this.audioSource = null;
		}

		if (this.connection) {
			if (this.connection.channel) this.connection.channel.close();
			this.connection.close();
			this.connection = null;
		}

		if (Controller.sessionOwner && Controller.sessionOwner.negotiation) {
			delete Controller.sessionOwner.negotiation;
			storeSessionOwner();
		}
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
			// TODO
		} catch (error) {
			this.displayError(error);
		}
	}


	/**
	 * Handles offer/answer negotiation for a new connection. This implies
	 * creating a new connection, setting the associated connection's remote
	 * description to the sender's offer, and setting the associated connection's
	 * local description to a matching answer.
	 * @param {Object} sender the sender
	 */
	async negotiatePeerConnection (sender) {
		// TODO
	}


	/**
	 * Adds the given track metadata to the top of the playlist.
	 * @param {String} metadata the track metadata
	 */
	addPlaylistTrack (metadata) {
		// TODO
	}


	/**
	 * Initiates playback of a new audio source based on the given audio array buffer.
	 * @param {ArrayBuffer} audioBuffer the audio array buffer
	 * @return {Promise} a promise that resolves once the audio source is started
	 */
	async startNextTrackPlayback (audioBuffer) {
		// TODO
	}
}


/**
 * Returns a promise that resolves after the session owner's current state has been
 * stored using a webservice call.
 * @return {Promise} a promise that resolves after the webservice call
 */
async function storeSessionOwner () {
	let response = await fetch("/services/people", { method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(Controller.sessionOwner), credentials: "include" });
	if (!response.ok) throw new Error("HTTP " + response.status + " " + response.statusText);
	Controller.sessionOwner.version += 1;
}


/**
 * Performs controller event listener registration during DOM load event handling.
 */
window.addEventListener("load", event => {
	let controller = new PeerRadioListenerController();
	let anchors = document.querySelectorAll("header li > a");
	anchors.forEach((anchor, index) => anchor.addEventListener("click", event => controller.active = (index === 3)));
	window.addEventListener("beforeunload", event => controller.active = false);
});