import Controller from "./controller.js";

// private constants
const LYRICS_API_URI = "https://orion.apiseeds.com/api/music/lyric";
const LYRICS_API_KEY = null; // TODO
const SECOND_MILLIES = 1000;

/**
 * Server radio controller type.
 * Copyright (c) 2019 Sascha Baumeister
 */
class ServerRadioController extends Controller {

	/**
	 * Initializes a new controller instance.
	 */
	constructor () {
		super();

		Object.defineProperty(this, "audioSource", { enumerable: true, value: null, writable: true });
		Object.defineProperty(this, "recordingPromise", { enumerable: true, value: null, writable: true });
		Object.defineProperty(this, "fader", { enumerable: true, value: null, writable: true });
	}


	/**
	 * Resets this controller.
	 */
 	reset () {
 		super.reset();
		if (this.audioSource) {
			this.audioSource.stop();
			this.audioSource.disconnect();
			this.audioSource = null;
		}

		this.recordingPromise = null;
		this.fader = null;
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
	 * Expands the playlist with tracks matching the given search criteria.
	 * @param {Array} genres the genres
	 * @param {Array} artists the artists
	 */
	async addMatchingTracks (genres, artists) {
		// TODO
	}


	/**
	 * Displays the lyrics for the given audio track.
	 * @param {Object} track the audio track
	 */
	async displayLyrics (track) {
		// TODO
	}


	/**
	 * Initiates playback of the top playback entry.
	 */
	async startNextTrackPlayback () {
		if (!this.active) return;

		// TODO
	}
}


/**
 * Performs controller event listener registration during DOM load event handling.
 */
window.addEventListener("load", event => {
	let controller = new ServerRadioController();
	let anchors = document.querySelectorAll("header li > a");
	anchors.forEach((anchor, index) => anchor.addEventListener("click", event => controller.active = (index === 2)));
	window.addEventListener("beforeunload", event => controller.active = false);
});