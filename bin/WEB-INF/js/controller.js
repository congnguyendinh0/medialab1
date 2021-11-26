/**
 * semi-abstract controller type.
 * Copyright (c) 2019 Sascha Baumeister
 */
export default class Controller extends EventTarget {

	/**
	 * Initializes a new controller instance.
	 * @throws {TypeError} if a semi-abstract type is instantiated
	 */
	constructor () {
		super();
		if (this.constructor === Controller) throw new TypeError("abstract type");

		let active = false;
		Object.defineProperty(this, "active", {
			enumerable: true,
			get: function () { return active; },
			set: function (value) { if (active !== (active = value)) this.dispatchEvent(new CustomEvent("switch", { detail: value })); }
		});

		this.addEventListener("switch", event => event.detail ? this.display() : this.reset());
	}


	/**
	 * Resets this controller.
	 */
 	reset () {}


	/**
	 * Displays the view associated with this controller.
	 */
 	display () {
		let main = document.querySelector("main");
		while (main.childElementCount > 0) main.lastElementChild.remove();
 	}


	/**
	 * Displays the given error in the footer, or resets it if none is given.
	 * @param error {Object} the optional error
	 */
	displayError (error) {
		let output = document.querySelector("body > footer output");
		if (error) {
			console.error(error);
			output.value = error instanceof Error ? error.message : error;
		} else {
			output.value = "";
		}
	}


	/**
	 * Creates a new audio source based on the given audio byte buffer. The operation
	 * establishes a processing chain consisting of a newly created audio source
	 * based on the given buffer, the given audio processors in sequence, and finally
	 * the audio context destination. Each element of the processing chain (except
	 * the destination) is connected to it's successor within the processing chain
	 * before starting the audio source.
	 * @param {ArrayBuffer} audioBuffer the audio array buffer
	 * @param {Array} audioProcessors the audio processors to be chained (rest parameter)
	 * @return a promise of the audio source created
	 */
	static async startTrackPlayback (audioBuffer, ...audioProcessors) {
		let audioSource = this.audioContext.createBufferSource();
		audioSource.buffer = await this.audioContext.decodeAudioData(audioBuffer);

		let processingChain = [ audioSource ];
		processingChain.push.apply(processingChain, audioProcessors);
		processingChain.push(this.audioContext.destination);
		for (let index = 1; index < processingChain.length; ++index)
			processingChain[index - 1].connect(processingChain[index]);

		audioSource.start();
		return audioSource;
	}


	/**
	 * Returns a promise that resolves to an RTC peer connection's updated local description
	 * once the given connection's ICE candidates have been negotiated.
	 * @param {RTCPeerConnection} connection the RTC peer connection
	 * @param {Boolean} offering wether an offer or an answer shall be negotiated
	 * @return {Promise} the promise of an updated local description
	 */
	static negotiateLocalDescription (connection, offering) {
		return new Promise((resolve, reject) => {
			connection.onicecandidate = event => {
				if (!event.candidate) {
					delete connection.onicecandidate;
					resolve(connection.localDescription);
				}
			};

			let promise = offering ? connection.createOffer() : connection.createAnswer();
			promise.then(sessionDescription => connection.setLocalDescription(sessionDescription));
		});
	}
};


/**
 * The local audio system context. Note that the Chrome browser requires the audio context
 * to be created as part of a user interaction.
 */
Object.defineProperty(Controller, "audioContext", { enumerable: true, value: null, writable: true });


/**
 * The audio volume gain node. Note that the Chrome browser requires the associated audio
 * context to be created as part of a user interaction.
 */
Object.defineProperty(Controller, "audioVolume", { enumerable: true, value: null, writable: true });


/**
 * The user currently logged in.
 */
Object.defineProperty(Controller, "sessionOwner", { enumerable: true, value: null, writable: true });


/**
 * Performs controller event listener registration during DOM load event handling.
 * The listeners handle menu item selection.
 */
window.addEventListener("load", event => {
	for (let anchor of document.querySelectorAll("header nav a")) {
		anchor.addEventListener("click", function (event) {
			let selectedItem = event.currentTarget.parentElement;
			for (let item of selectedItem.parentElement.children) item.classList.remove("selected");
			selectedItem.classList.add("selected");
		});
	}
});