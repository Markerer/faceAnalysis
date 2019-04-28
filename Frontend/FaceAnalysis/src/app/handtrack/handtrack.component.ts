import { Component, OnInit } from '@angular/core';
import * as handTrack from 'handtrackjs';

@Component({
  selector: 'app-handtrack',
  templateUrl: './handtrack.component.html',
  styleUrls: ['./handtrack.component.scss']
})
export class HandtrackComponent implements OnInit {

  static video = document.getElementById("myvideo");
  static handimg = <HTMLImageElement> document.getElementById("handimage");
  static canvas =  <HTMLCanvasElement> document.getElementById("canvas");
  static context = null;
  static trackButton =  <HTMLButtonElement> document.getElementById("trackbutton");
  static updateNote = <HTMLDivElement> document.getElementById("updatenote");


  static imgindex = 1;
  static isVideo = false;
  static model = null;
  static modelParams = {
    flipHorizontal: true,   // flip e.g for video
    maxNumBoxes: 20,        // maximum number of boxes to detect
    iouThreshold: 0.5,      // ioU threshold for non-max suppression
    scoreThreshold: 0.6,    // confidence threshold for predictions.
}

  constructor() {}

  ngOnInit() {

  }

  ngAfterViewInit() {
    HandtrackComponent.video = document.getElementById("myvideo");
    HandtrackComponent.handimg = <HTMLImageElement> document.getElementById("handimage");
    HandtrackComponent.canvas =  <HTMLCanvasElement> document.getElementById("canvas");
    HandtrackComponent.trackButton =  <HTMLButtonElement> document.getElementById("trackbutton");
    HandtrackComponent.updateNote = <HTMLDivElement> document.getElementById("updatenote");
    HandtrackComponent.context = HandtrackComponent.canvas.getContext("2d");

    handTrack.load(HandtrackComponent.modelParams).then(lmodel => {
      // detect objects in the image.
      HandtrackComponent.model = lmodel
      HandtrackComponent.updateNote.innerText = "Model betöltve!"
      HandtrackComponent.trackButton.disabled = false;
  });
  }

  flipVideo(){
    HandtrackComponent.modelParams.flipHorizontal ? false : true ;
  }

  startVideo() {
    handTrack.startVideo(HandtrackComponent.video).then(function (status) {
        console.log("video started", status);
        if (status) {
            HandtrackComponent.updateNote.innerText = "Videó elindult. Kéz követése"
            HandtrackComponent.isVideo = true
            HandtrackComponent.runDetection()
        } else {
            HandtrackComponent.updateNote.innerText = "Kérlek engedélyezd a videót!"
        }
    });
}

static runDetection() {

  HandtrackComponent.model.detect(HandtrackComponent.video).then(predictions => {
      //console.log("Predictions: ", predictions);
      HandtrackComponent.model.setModelParameters(HandtrackComponent.modelParams)
      HandtrackComponent.model.renderPredictions(predictions, HandtrackComponent.canvas, HandtrackComponent.context, HandtrackComponent.video);
      if (HandtrackComponent.isVideo) {
          requestAnimationFrame(HandtrackComponent.runDetection);
      }
  });
}

static runDetectionImage(img) {
  HandtrackComponent.model.detect(img).then(predictions => {
      //console.log("Predictions: ", predictions);
      HandtrackComponent.model.renderPredictions(predictions, HandtrackComponent.canvas, HandtrackComponent.context, img);
  });
}

toggleVideo() {
    if (!HandtrackComponent.isVideo) {
        HandtrackComponent.updateNote.innerText = "Videó indítása"
        this.startVideo();
    } else {
        HandtrackComponent.updateNote.innerText = "Videó leállítása"
        handTrack.stopVideo(HandtrackComponent.video)
        HandtrackComponent.isVideo = false;
        HandtrackComponent.updateNote.innerText = "Videó leállt"
    }
}



}
