import { Component, OnInit } from '@angular/core';
import { WebcamImage } from 'ngx-webcam';
import { MainService } from '../main.service';
import { VerifyResult } from '../model/VerifyResult';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import * as $ from 'jquery';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  lastFileName: string;

  lastVerifyResult: VerifyResult = new VerifyResult();

  successUpload: string;
  successMessage: string;
  private _success = new Subject<string>();
  private _successUpload = new Subject<string>();

  latestImage = document.getElementById("latestImage");

  constructor(private mainService: MainService) { }


  // latest snapshot
  public webcamImage: WebcamImage = null;

  handleImage(webcamImage: WebcamImage) {
    this.webcamImage = webcamImage;
  }

  handleCameraOpened() {
    this.webcamImage = null;
    this.lastVerifyResult.Image = null;
    document.getElementById('mostSimilarText').style.visibility = "hidden";
  }

  // Hiba esetén Modal dialog megnyitása
  openDangerModal(msg: string) {
    document.getElementById('dangerModalText').textContent = msg;
    document.getElementById('openDangerModalButton').click();
  }

  // Sikeres Modal dialog megnyitása
  openSuccessModal(msg: string) {
    document.getElementById('successTitle').textContent = "Sikeres azonosítás!";
    document.getElementById('successModalText').textContent = msg;
    document.getElementById('successModalText').style.cursor = "pointer";
    document.getElementById('successModalText').onclick = this.onNavigate;
    document.getElementById('openSuccessModalButton').click();
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

    this.mainService.uploadAdminImage(formData).subscribe(response => {
      console.log(response);
      this.runFaceComparison();
    }, error => {

      var errorResponse = new HttpErrorResponse(error);
      console.log(errorResponse.toString());
      console.log(errorResponse.error);
      if (errorResponse.status === 400) {
        this.openDangerModal(`${errorResponse.error}`);
      }

    });
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


  onSuccessfulLogin() {
    var instantRedirect = JSON.parse(localStorage.getItem("instantRedirect"));
    if(instantRedirect){
      console.log(instantRedirect);
      this.onNavigate();  
    }
    document.getElementById('mostSimilarText').style.visibility = "visible";
    var linkURI = localStorage.getItem("loginLink");
    this.openSuccessModal(linkURI);
  }

  onLoginFailed() {
    this.openDangerModal("Sikertelen azonosítás!");
  }

  runFaceComparison() {

    this.changeSuccessMessage();
    this.mainService.getFaceComparison(this.lastFileName).subscribe(
      response => {
        console.log(response);

        if (response.Confidence > 0.51) {
          this.onSuccessfulLogin();
          this.lastVerifyResult = response;
        } else {
          this.onLoginFailed();
        }
      }
    );
  }

  onLoginButtonClicked(): void {
    if (this.webcamImage != null) {
      this.changeSuccessUploadMsg();
      this.uploadImage();
      // kép eltüntetése
      this.lastVerifyResult.Image = null;
    }
  }

  onNavigate() {
    console.log("katt on link");
    if(JSON.parse(localStorage.getItem("instantRedirect"))){
      window.open(localStorage.getItem("loginLink"), "_self");
    }
    var textInTheLink = document.getElementById('successModalText').textContent;
    console.log(textInTheLink);
    if (textInTheLink === localStorage.getItem("loginLink")) {
      window.open(localStorage.getItem("loginLink"), "_blank");
    }
  }

  ngOnInit() {
    
    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(10000)
    ).subscribe(() => this.successMessage = null);

    //A sikeres üzenet
    this._successUpload.subscribe((message) => this.successUpload = message);
    this._successUpload.pipe(
      debounceTime(3000)
    ).subscribe(() => this.successUpload = null);
  }

  public changeSuccessMessage(): void {
    this._success.next(`Dolgozunk a kérésen...`);
  }

  public changeSuccessUploadMsg(): void {
    this._successUpload.next(`Épp feltöltjük a képet...`);
  }
}

