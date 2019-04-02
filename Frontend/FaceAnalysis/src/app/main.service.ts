import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AppSettings } from './appsettings';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  constructor(private http: HttpClient) { }


  public uploadImage(image: File): Observable<Object> {
    const formData = new FormData();

    formData.append('file', image, image.name);

    return this.http.post(AppSettings.API_ROOT + '', formData, {
      responseType: 'text'
     });
  }


  public getImage(filename: string): Observable<Blob> {
    return this.http.get(AppSettings.API_ROOT + 'files/' + filename, {
      responseType: 'blob'
    });
  }

  public getFaceAnalysis(filename: string): Observable<Object[]> {
    return this.http.get<Object[]>(AppSettings.API_ROOT + "analysis?filename=" + filename);
  }

  public getFaceComparison(filename: string): Observable<Object> {
    return this.http.get(AppSettings.API_ROOT + 'compareAll?filename=' + filename);
  }

}
