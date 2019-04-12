import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';

import { WebcamImage } from 'ngx-webcam';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { DetectedFace } from '../model/DetectedFace';

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


  constructor(private mainService: MainService) { }

  // latest snapshot
  public webcamImage: WebcamImage = null;

  handleImage(webcamImage: WebcamImage) {
    this.webcamImage = webcamImage;
  }


  selectedFile: ImageSnippet;

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
    });
  }

  runFaceAnalysis() {

    this.changeSuccessMessage();
    this.mainService.getFaceAnalysis(this.lastFileName).subscribe(
      response => {
        console.log(response.toString());
        for (let df in response['0']) {
          console.log(df.toString());
         
          this.changeAnalyisMessage(df.toString());
        }
      }
    );
  }

  changeAnalyisMessage(msg: string){
    var div = document.getElementById("div");
    div.textContent = msg;
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

  processFile(imageInput: any) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {

      this.selectedFile = new ImageSnippet(event.target.result, file);
      const formData = new FormData();

      formData.append('file', file, file.name);
      this.mainService.uploadImage(formData).subscribe(
        (res) => {
          console.log(res);
        },
        (err) => {
          console.log(err);
        })
    });

    reader.readAsDataURL(file);
  }

  onAnalyseButtonClicked(){
    if (this.webcamImage != null) {
      this.uploadImage();
    }
  }

}
