import { Component, OnInit } from '@angular/core';

import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';
import { MainService } from '../main.service';
import { FaceImage } from '../model/FaceImage';
import { isUndefined } from 'util';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import * as $ from 'jquery';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  private _success = new Subject<string>();
  successMessage: string;

  updateLoginLinkForm: FormGroup;

  faceImages: FaceImage[];

  loginLinkString: string;
  instantRedirect: boolean;

  selectedFile: File;

  constructor(private fb: FormBuilder, private mainService: MainService) {
    this.updateLoginLinkForm = fb.group({
      'loginLink': ['', Validators.required],
      'instantRedirect': ['']
    });

    this.loginLinkString = localStorage.getItem("loginLink");
    this.instantRedirect = JSON.parse(localStorage.getItem("instantRedirect"));
  }

  onChanges() {
    this.updateLoginLinkForm.get('loginLink').valueChanges
    .subscribe(loginLink => {
        if (loginLink === '' || loginLink.length === 0) {
            this.updateLoginLinkForm.get('instantRedirect').reset();
            this.updateLoginLinkForm.get('instantRedirect').disable();
        }
        else {
            this.updateLoginLinkForm.get('instantRedirect').enable();
        }
    });
}

  ngOnInit() {

    this.faceImages = [];
    this.getImages();

    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);

    this.onChanges();
  }

  ngOnDestroy() {
    this.removeItems();
  }

  public changeSuccessUploadMsg(msg: string): void {
    this._success.next(msg);
  }

  // Másik fájl ki lett választva
  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
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

  // Kép feltöltése
  uploadImage() {
    if (!(this.selectedFile === null || this.selectedFile === undefined)) {
      this.changeSuccessUploadMsg('Épp feltöltjük a képet...');
      const uploadData = new FormData();
      uploadData.append('file', this.selectedFile, this.selectedFile.name);
      this.mainService.uploadAdminImage(uploadData).subscribe(object => {
          this.openSuccessModal("A kép feltöltése sikeres volt!");
          this.getImages();
      }, error => {

          var errorResponse = new HttpErrorResponse(error);
          console.log(errorResponse.toString());
          console.log(errorResponse.error);
          if (errorResponse.status === 400) {
            this.openDangerModal(`${errorResponse.error}`);
          }

        });
    }
  }

  // Adott kép törlése
  deleteAdminImage(imageID: string): void {
    this.mainService.deleteAdminImage(imageID).subscribe(response => {
       console.log(response);
      this.getImages();
    });
  }

  // Képek lekérése
  getImages() {
    this.mainService.getAllAdminImages().subscribe(response => {
      console.log(response);
      this.faceImages = [];
      for (let i = 0; i < response.length; i++) {
        var temp = new FaceImage();
        temp.link = response[i];
        temp.setIdFromLink();
        console.log(temp.link + '\t' + temp.id);
        this.faceImages.push(temp);
      }
    });
  }


  updateLoginLink(loginLink: string, instantRedirect: boolean) {
    if (loginLink != undefined || loginLink != null) {
      if (!loginLink.includes('http') && loginLink.length > 0) {
        var temp = 'https://';
        temp += loginLink;
        loginLink = temp;
      }
      this.loginLinkString = loginLink;
      localStorage.setItem("instantRedirect", JSON.stringify(instantRedirect));
      localStorage.setItem("loginLink", loginLink);
      this.openSuccessModal("A link frissítve!");
    }
  }


  removeItems(): void {
    if (!isUndefined(this.faceImages)) {
      this.faceImages.splice(0);
    }
  }

}
