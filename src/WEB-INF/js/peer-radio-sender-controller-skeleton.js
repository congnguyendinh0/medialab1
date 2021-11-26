import Controller from "./controller.js";

// constants
const TEN_SECOND_MILLIES = 10 * 1000;
const QUARTER_HOUR_MILLIES = 15 * 60 * 1000;
const MAX_CONNECTION_COUNT = 10;


/**
 * Peer radio sender controller type.
 * Copyright (c) 2019 Sascha Baumeister
 */
class PeerRadioSenderController extends Controller {

	/**
	 * Initializes a new controller instance.
	 */
	constructor () {
		super();

		Object.defineProperty(this, "audioSource", { enumerable: true, value: null, writable: true });
		Object.defineProperty(this, "connections", { enumerable: true, value: [] });
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

		for (let connection of this.connections) {
			if (connection.channel) connection.channel.close();
			connection.close();
		}
		this.connections.length = 0;

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
	 * Handles offer/answer negotiation for new connections. This implies setting
	 * the associated connection's local description to an offer, and it's remote
	 * description to an answer.
	 */
	async negotiatePeerConnections () {
		// TODO
	}


	/**
	 * Initiates playback of the topmost playlist track.
	 * @return {Promise} a promise that resolves once the audio source is started
	 */
	async startNextTrackPlayback () {
		if (!this.active) return;

		// TODO
	}


	/**
	 * Removes the given tracks from the playlist.
	 * @param {Array} files the track files to be removed
	 */
	removePlaylistTracks (files) {
		// TODO
	}


	/**
	 * Adds the given tracks to the playlist.
	 * @param {Array} files the track files to be added
	 */
	addPlaylistTracks (files) {
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
 * Returns a promise that resolves after the given delay.
 * @param {Number} delay the amount of milliseconds to sleep
 * @return {Promise} a promise that resolves after the given delay
 */
function sleep (delay) {
   return new Promise((resolve, reject) => window.setTimeout(resolve, delay));
}


/**
 * Performs controller event listener registration during DOM load event handling.
 */
window.addEventListener("load", event => {
	let controller = new PeerRadioSenderController();
	let anchors = document.querySelectorAll("header li > a");
	anchors.forEach((anchor, index) => anchor.addEventListener("click", event => controller.active = (index === 4)));
	window.addEventListener("beforeunload", event => controller.active = false);
});