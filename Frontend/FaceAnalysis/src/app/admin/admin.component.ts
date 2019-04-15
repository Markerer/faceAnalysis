import { Component, OnInit } from '@angular/core';

import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';
import { MainService } from '../main.service';
import { FaceImage } from '../model/FaceImage';
import { isUndefined } from 'util';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  private _success = new Subject<string>();
  successMessage: string;

  private _successLinkUpdate = new Subject<string>();
  successLinkUpdateMessage: string;

  updateLoginLinkForm: FormGroup;

  faceImages: FaceImage[];

  loginLinkString: string;

  selectedFile: File;

  constructor(private fb: FormBuilder, private mainService: MainService) {
    this.updateLoginLinkForm = fb.group({
      'loginLink': ["", Validators.required]
    });

    this.loginLinkString = localStorage.getItem("loginLink");
  }

  ngOnInit() {

    this.faceImages = [];
    this.getImages();

    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);

    //A sikeres üzenet
    this._successLinkUpdate.subscribe((message) => this.successLinkUpdateMessage = message);
    this._successLinkUpdate.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successLinkUpdateMessage = null);
  }

  ngOnDestroy() {
    this.removeItems();
  }

  public changeSuccessUploadMsg(msg: string): void {
    this._success.next(msg);
  }

  public changeSuccessLinkUpdateMsg(): void {
    this._successLinkUpdate.next("Link frissítése sikeres!");
  }


  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  // Kép feltöltése
  uploadImage() {
    if (!(this.selectedFile === null || this.selectedFile === undefined)) {
      this.changeSuccessUploadMsg('Épp feltöltjük a képet...');
      const uploadData = new FormData();
      uploadData.append('file', this.selectedFile, this.selectedFile.name);
      this.mainService.uploadAdminImage(uploadData).subscribe(object => {
          console.log(object);
        this.changeSuccessUploadMsg('A kép feltöltése sikeres!');
        this.getImages();
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


  updateLoginLink(loginLink: string) {
    if (loginLink != undefined || loginLink != null) {
      if (!loginLink.includes('http')) {
        var temp = 'https://';
        temp += loginLink;
        loginLink = temp;
      }
      localStorage.setItem("loginLink", loginLink);
      this.changeSuccessLinkUpdateMsg();
    }
  }


  removeItems(): void {
    if (!isUndefined(this.faceImages)) {
      this.faceImages.splice(0);
    }
  }

}
