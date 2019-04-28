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
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);
  }

  ngAfterViewInit(){
  }


  constructor(private mainService: MainService) {}


  static lastResponse = {};

  static ct  = <HTMLCanvasElement> document.getElementById("ct");
  static ctx = null;

  img= new Image();
  faceNumber: number;
  actualFaceNumber : number = 0;
  analText = [];

  // latest snapshot
  public webcamImage: WebcamImage = null;

  handleImage(webcamImage: WebcamImage) {
    this.webcamImage = webcamImage;
    FaceAnalysisComponent.ct  = <HTMLCanvasElement> document.getElementById("ct");
    FaceAnalysisComponent.ctx = FaceAnalysisComponent.ct.getContext("2d");
    var ez = this;
    this.img.onload = function() {
      FaceAnalysisComponent.ctx.drawImage(ez.img,0,0);
    };
    this.img.src=this.webcamImage.imageAsDataUrl;
    this.resetPicture();
  }


  selectedFile: ImageSnippet;

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


  uploadImage() {
    const formData = new FormData();

    var base64 = this.webcamImage.imageAsBase64;
    const date = new Date().valueOf();

    // Replace extension according to your media type
    const imageName = date + '.jpg';
    this.lastFileName = imageName;
    // call method that creates a blob from dataUri
    const imageBlob = this.dataURItoBlob(base64);
    const imageFile = new File([imageBlob], imageName, { type: 'image/jpg' });
    formData.append('file', imageFile, imageFile.name);

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
    this.changeSuccessMessage();
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
    FaceAnalysisComponent.ctx.drawImage(this.img,0,0);
  }

  drawFace(number: number){
    let response = <DetectedFace[]> FaceAnalysisComponent.lastResponse;
    FaceAnalysisComponent.ctx.strokeRect(response[number].FaceRectangle[0],response[number].FaceRectangle[1],
      response[number].FaceRectangle[2],response[number].FaceRectangle[3]);
  }

  changeAnalyisMessage(num: number){
    var div = document.getElementById("div");
    div.textContent = this.analText[num];
    }

  public changeSuccessMessage(): void {
    this._success.next(`Dolgozunk a kérésen...`);
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
    }
  }

}

