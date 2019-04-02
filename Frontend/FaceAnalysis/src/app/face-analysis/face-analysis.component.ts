import { Component, OnInit } from '@angular/core';
import { ImageService } from '../image.service';

import { WebcamImage } from 'ngx-webcam';

class ImageSnippet {
  constructor(public src: string, public file: File) {}
}

@Component({
  selector: 'app-face-analysis',
  templateUrl: './face-analysis.component.html',
  styleUrls: ['./face-analysis.component.scss']
})
export class FaceAnalysisComponent implements OnInit {

  ngOnInit() {
  }


  constructor(private imageService: ImageService) { }

  // latest snapshot
  public webcamImage: WebcamImage = null;

  handleImage(webcamImage: WebcamImage) {
    this.webcamImage = webcamImage;
  }


  selectedFile: ImageSnippet;


  processFile(imageInput: any) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {

      this.selectedFile = new ImageSnippet(event.target.result, file);

      this.imageService.uploadImage(this.selectedFile.file).subscribe(
        (res) => {

        },
        (err) => {

        })
    });

    reader.readAsDataURL(file);
  }

}
