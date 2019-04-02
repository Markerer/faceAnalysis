import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { HttpMethod } from 'blocking-proxy/built/lib/webdriver_commands';


@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) {}


  public uploadImage(image: File): Observable<Object> {
    const formData = new FormData();

    formData.append('image', image);

    return this.http.post('/api/v1/image-upload', formData);
  }
}
