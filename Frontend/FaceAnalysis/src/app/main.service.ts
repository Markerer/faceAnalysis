import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AppSettings } from './appsettings';
import { VerifyResult } from './model/VerifyResult';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  constructor(private http: HttpClient) { }


  public uploadImage(uploadData: FormData): Observable<Object> {
      return this.http.post(AppSettings.API_ROOT + '/admin', uploadData, {
      responseType: 'text'
     });
  }


  public getImage(filepath: string): Observable<Blob> {
    return this.http.get(filepath, {
      responseType: 'blob'
    });
  }

  public getFaceAnalysis(filename: string): Observable<Object[]> {
    return this.http.get<Object[]>(AppSettings.API_ROOT + "analysis?filename=" + filename);
  }

  public getFaceComparison(filename: string): Observable<VerifyResult> {
    return this.http.get<VerifyResult>(AppSettings.API_ROOT + 'compareAdmin?filename=' + filename);
  }

}
