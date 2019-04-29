import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { WebcamImage } from 'ngx-webcam';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { DetectedFace } from '../model/DetectedFace';
import { HttpErrorResponse } from '@angular/common/http';

class ImageSnippet {
  constructor(public src: string, public file: File) {}
}

@Component({
  selector: 'app-face-analysis',
  templateUrl: './face-analysis.component.html',
  styleUrls: ['./face-analysis.component.scss']
})
export class FaceAnalysisComponent implements OnInit {

  lastFileName: string;
  private _success = new Subject<string>();
  successMessage: string;

  ngOnInit() {
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(3500)
    ).subscribe(() => this.successMessage = null);
  }

  ngAfterViewInit(){
    var windowWidth = (window.innerWidth * 0.8);
    FaceAnalysisComponent.ct  = <HTMLCanvasElement> document.getElementById("ct");
    FaceAnalysisComponent.ctx = FaceAnalysisComponent.ct.getContext("2d");
    if (windowWidth > 600) {
      FaceAnalysisComponent.ct.width = 600;
      FaceAnalysisComponent.ct.height = FaceAnalysisComponent.ct.width / this.aspectRatio;
    } else if(windowWidth > 1000) {
      FaceAnalysisComponent.ct.width = 1000;
      FaceAnalysisComponent.ct.height = FaceAnalysisComponent.ct.width / this.aspectRatio;
    } else {
      FaceAnalysisComponent.ct.width = windowWidth;
      FaceAnalysisComponent.ct.height = (windowWidth / this.aspectRatio);
    }
    console.log("width after init:" + FaceAnalysisComponent.ct.width +  " height after init" + FaceAnalysisComponent.ct.height);
  }

  manualResize(){
    var windowWidth = (window.innerWidth * 0.8);
    if (windowWidth > 600) {
      FaceAnalysisComponent.ct.width = 600;
      FaceAnalysisComponent.ct.height = FaceAnalysisComponent.ct.width / this.aspectRatio;
    } else if(windowWidth > 1000) {
      FaceAnalysisComponent.ct.width = 1000;
      FaceAnalysisComponent.ct.height = FaceAnalysisComponent.ct.width / this.aspectRatio;
    } else {
      FaceAnalysisComponent.ct.width = windowWidth;
      FaceAnalysisComponent.ct.height = (windowWidth / this.aspectRatio);
    }
    console.log("width after init:" + FaceAnalysisComponent.ct.width +  " height after init" + FaceAnalysisComponent.ct.height);
  }

  constructor(private mainService: MainService) {}


  pictureChosen: boolean = false;

  static lastResponse = [];

  static ct  = <HTMLCanvasElement> document.getElementById("ct");
  static ctx = null;

  img= new Image();
  faceNumber: number;
  actualFaceNumber : number = 0;
  analText = [];

  uploadedFile: File;

  x = 1;
  y = 1;

  aspectRatio = 1.45;

  // latest snapshot
  public webcamImage: WebcamImage = null;

  handleImage(webcamImage: WebcamImage) {
    this.webcamImage = webcamImage;
    FaceAnalysisComponent.ct  = <HTMLCanvasElement> document.getElementById("ct");
    FaceAnalysisComponent.ctx = FaceAnalysisComponent.ct.getContext("2d");
    var ez = this;
    this.img.onload = function() {
      FaceAnalysisComponent.ctx.drawImage(ez.img,0,0, FaceAnalysisComponent.ct.width, FaceAnalysisComponent.ct.height);
      console.log(ez.img.height + "  " + ez.img.width);
      ez.setAspectRatio(ez.img.width, ez.img.height);
      ez.manualResize();
      ez.resetPicture();
    };
    this.img.src=this.webcamImage.imageAsDataUrl;
    this.resetPicture();
    this.pictureChosen = true;
    this.hideAnalysisMessage();
  }

  setAspectRatio(imgWidth: number, imgHeight: number){
    this.aspectRatio = (imgWidth / imgHeight);
    console.log("new aspect ratio:" + this.aspectRatio);
  }
  
  // Másik fájl ki lett választva
  onFileChanged(event) {
    if (event.target.files && event.target.files[0]) {
      var reader: any;
      target: EventTarget;
      reader = new FileReader();

      this.pictureChosen = true;
      this.uploadedFile = event.target.files[0];
      this.webcamImage = null;
      this.hideAnalysisMessage();

      reader.readAsDataURL(event.target.files[0]); // read file as data url

      reader.onload = (event) => { // called once readAsDataURL is completed
        FaceAnalysisComponent.ct  = <HTMLCanvasElement> document.getElementById("ct");
        FaceAnalysisComponent.ctx = FaceAnalysisComponent.ct.getContext("2d");
        var ez = this;
        this.img.onload = function() {
          FaceAnalysisComponent.ctx.drawImage(ez.img,0,0, FaceAnalysisComponent.ct.width, FaceAnalysisComponent.ct.height);
          console.log(ez.img.height + "  " + ez.img.width);
          ez.setAspectRatio(ez.img.width, ez.img.height);
          ez.manualResize();
          ez.resetPicture();
        };
        this.img.src = event.target.result;
        this.resetPicture();
      }
    }
  }

  // Hiba esetén Modal dialog megnyitása
  openDangerModal(msg: string) {
    document.getElementById('dangerModalText').textContent = msg;
    document.getElementById('openDangerModalButton').click();
  }

  // Sikeres Modal dialog megnyitása
  openSuccessModal(msg: string) {
    document.getElementById('successTitle').textContent = "Sikeres művelet!";
    document.getElementById('successModalText').textContent = msg;
    document.getElementById('openSuccessModalButton').click();
    // valamiért sosem fut le a bezárás eventje a modalnak..., ezért kell ez
    document.getElementById('successModalText').onclick = null;
    document.getElementById('successModalText').style.cursor = "default";
  }


  uploadImage(uploadedFile?: File) {
    const formData = new FormData();

    if(uploadedFile === undefined || uploadedFile === null){
      var base64 = this.webcamImage.imageAsBase64;
      const date = new Date().valueOf();

      // Replace extension according to your media type
      const imageName = date + '.jpg';
      this.lastFileName = imageName;
      // call method that creates a blob from dataUri
      const imageBlob = this.dataURItoBlob(base64);
      const imageFile = new File([imageBlob], imageName, { type: 'image/jpg' });
      formData.append('file', imageFile, imageFile.name);
    } else {
      console.log(uploadedFile.size);
      formData.append('file', uploadedFile, uploadedFile.name);
      this.lastFileName = uploadedFile.name;
    }
    this.changeSuccessMessage("Épp feltöltjük a képet...");
    this.mainService.uploadImage(formData).subscribe(response => {
      console.log(response);
      this.runFaceAnalysis();
    }, error => {

      var errorResponse = new HttpErrorResponse(error);
      console.log(errorResponse.toString());
      console.log(errorResponse.error);
      if (errorResponse.status === 400) {
        this.openDangerModal(`${errorResponse.error}`);
      }

    });
  }

  runFaceAnalysis() {
    var az = this;
    this.changeSuccessMessage("Dolgozunk a kérésen...");
    this.mainService.getFaceAnalysis(this.lastFileName).subscribe(
      response => {
        FaceAnalysisComponent.lastResponse = response;
        az.faceNumber = response.length;
        console.log("response:");
        console.log(response['0']);
        var str = "";
        console.log(response.toString());
        for (var df of response) {
          str="";
          console.log(" indexof df : " + response.indexOf(df));
          str += df.toString();
          str += "\n";
          this.analText.push(str);
        }
        
        this.openSuccessModal("Az arc(ok) elemezése megtörtént, görgess le a részletekért!");

        var card = document.getElementById("card");
        card.hidden=false;
        this.actualFaceNumber=0;
        this.changeAnalyisMessage(this.actualFaceNumber);
        this.drawFace(this.actualFaceNumber);
      }
    );
  }

  async changeFace(b: boolean){
    await this.resetPicture();

    console.log(this.actualFaceNumber);
    if (b)
    {
      this.actualFaceNumber == this.faceNumber-1 ? this.actualFaceNumber = 0 : this.actualFaceNumber++;
    }
    else
    {
      this.actualFaceNumber == 0 ? this.actualFaceNumber = this.faceNumber-1 : this.actualFaceNumber--;
    }
    this.changeAnalyisMessage(this.actualFaceNumber);
    this.drawFace(this.actualFaceNumber);
  }

  resetPicture(){
    FaceAnalysisComponent.ctx.drawImage(this.img,0,0, FaceAnalysisComponent.ct.width, FaceAnalysisComponent.ct.height);
  }

  drawFace(number: number){
    this.x = FaceAnalysisComponent.ct.width / this.img.width;
    this.y = FaceAnalysisComponent.ct.height / this.img.height;
    let response = <DetectedFace[]> FaceAnalysisComponent.lastResponse;
    FaceAnalysisComponent.ctx.strokeStyle = "#7cb947";
    FaceAnalysisComponent.ctx.lineWidth = 8;
    FaceAnalysisComponent.ctx.strokeRect(
      response[number].FaceRectangle[0] * this.x,
      response[number].FaceRectangle[1] * this.y,
      response[number].FaceRectangle[2] * this.x,
      response[number].FaceRectangle[3] * this.y);
  }

  changeAnalyisMessage(num: number){
    var div = document.getElementById("div");
    div.textContent = this.analText[num];
    }

  hideAnalysisMessage(){
    var card = document.getElementById("card");
    if(card != null && card != undefined){
        card.hidden = true;
    }
  }

  public changeSuccessMessage(msg: string): void {
    this._success.next(msg);
  }

  dataURItoBlob(dataURI) {
    const byteString = window.atob(dataURI);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer);
    for (let i = 0; i < byteString.length; i++) {
      int8Array[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([int8Array], { type: 'image/jpg' });
    return blob;
  }

  onAnalyseButtonClicked(){
    if (this.webcamImage != null) {
      this.analText= [];
      this.uploadImage();
    } else if(this.uploadedFile != null && this.uploadedFile != undefined){
      this.analText = [];
      this.uploadImage(this.uploadedFile);
    }
  }

  onResize(event) {
    var windowWidth = (event.target.innerWidth * 0.8);
    if (windowWidth > 600) {
      FaceAnalysisComponent.ct.width = 600;
      FaceAnalysisComponent.ct.height = FaceAnalysisComponent.ct.width / this.aspectRatio;
    } else if(windowWidth > 1000) {
      FaceAnalysisComponent.ct.width = 1000;
      FaceAnalysisComponent.ct.height = FaceAnalysisComponent.ct.width / this.aspectRatio;
    } else {
      FaceAnalysisComponent.ct.width = windowWidth;
      FaceAnalysisComponent.ct.height = (windowWidth / this.aspectRatio);
    }
    this.resetPicture();
    if(FaceAnalysisComponent.lastResponse.length > 0){
      this.changeAnalyisMessage(this.actualFaceNumber);
      this.drawFace(this.actualFaceNumber);
    }
    console.log("width after resize:" + FaceAnalysisComponent.ct.width +  " height after resize" + FaceAnalysisComponent.ct.height);
  }
}

