import { Component, OnInit } from '@angular/core';
import { WebcamImage } from 'ngx-webcam';
import { MainService } from '../main.service';
import { VerifyResult } from '../model/VerifyResult';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';


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

  private _alert = new Subject<string>();
  alertMessage: string;


  constructor(private mainService: MainService) { }


  // latest snapshot
  public webcamImage: WebcamImage = null;

  handleImage(webcamImage: WebcamImage) {
    this.webcamImage = webcamImage;
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
    var link = document.getElementById("link");
    link.textContent = localStorage.getItem("loginLink");
  }

  onLoginFailed() {
    var link = document.getElementById("link");
    link.textContent = "Sikertelen azonosítás!";
    this.changeAlertMessage("Sikertelen azonosítás!");
  }

  onUploadFailed() {
    var link = document.getElementById("link");
    link.textContent = "Sikertelen képfeltöltés!";
    this.changeAlertMessage("Sikertelen képfeltöltés!");
  }

  runFaceComparison() {

    this.changeSuccessMessage();
    this.mainService.getFaceComparison(this.lastFileName).subscribe(
      response => {
        console.log(response);
        this.lastVerifyResult = response;
        if (this.lastVerifyResult.IsIdentical === 'Igen') {
          this.onSuccessfulLogin();
        } else {
          this.onLoginFailed();
        }
      }
    );
  }

  onLoginButtonClicked(): void {
    if (this.webcamImage != null) {
      this.uploadImage();
      this.changeSuccessUploadMsg();
    }
  }

  ngOnInit() {

    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);

    //A sikeres üzenet
    this._successUpload.subscribe((message) => this.successUpload = message);
    this._successUpload.pipe(
      debounceTime(3000)
    ).subscribe(() => this.successUpload = null);

    this._alert.subscribe((message) => this.alertMessage = message);
    this._alert.pipe(
      debounceTime(5000)
    ).subscribe(() => this.alertMessage = null);
  }

  public changeSuccessMessage(): void {
    this._success.next(`Dolgozunk a kérésen...`);
  }

  public changeSuccessUploadMsg(): void {
    this._successUpload.next(`Épp feltöltjük a képet...`);
  }

  public changeAlertMessage(msg: string): void {
      this._alert.next(msg);
    }
}

