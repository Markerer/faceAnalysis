import { Component, OnInit } from '@angular/core';
import * as handTrack from 'handtrackjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})

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
  modelParams = {
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

    handTrack.load(this.modelParams).then(lmodel => {
      // detect objects in the image.
      HandtrackComponent.model = lmodel
      HandtrackComponent.updateNote.innerText = "Loaded Model!"
      HandtrackComponent.trackButton.disabled = false;
  });
  }

  startVideo() {
    handTrack.startVideo(HandtrackComponent.video).then(function (status) {
        console.log("video started", status);
        if (status) {
            HandtrackComponent.updateNote.innerText = "Video started. Now tracking"
            HandtrackComponent.isVideo = true
            HandtrackComponent.runDetection()
        } else {
            HandtrackComponent.updateNote.innerText = "Please enable video"
        }
    });
}

static runDetection() {

  HandtrackComponent.model.detect(HandtrackComponent.video).then(predictions => {
      //console.log("Predictions: ", predictions);
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
        HandtrackComponent.updateNote.innerText = "Starting video"
        this.startVideo();
    } else {
        HandtrackComponent.updateNote.innerText = "Stopping video"
        handTrack.stopVideo(HandtrackComponent.video)
        HandtrackComponent.isVideo = false;
        HandtrackComponent.updateNote.innerText = "Video stopped"
    }
}



}
