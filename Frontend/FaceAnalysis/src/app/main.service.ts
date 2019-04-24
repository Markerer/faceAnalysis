import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AppSettings } from './appsettings';
import { VerifyResult } from './model/VerifyResult';
import { DetectedFace } from './model/DetectedFace';
import { map, catchError } from 'rxjs/operators';
import { jsonpCallbackContext, HttpClientJsonpModule } from '@angular/common/http/src/module';
import { resolveComponentResources } from '@angular/core/src/metadata/resource_loading';


@Injectable({
  providedIn: 'root'
})
export class MainService {

  constructor(private http: HttpClient) { }


  public uploadAdminImage(uploadData: FormData): Observable<any> {
    return this.http.post(AppSettings.API_ROOT + '/admin', uploadData, {
      observe: 'response',
      responseType: 'text'
    });
  }

  public deleteAdminImage(filename: string): Observable<Object> {
    return this.http.delete(AppSettings.API_ROOT + 'files/admin?filename=' + filename, {
      responseType: 'text'
    });
  }

  public deleteAllAdminImages(): Observable<Object> {
    return this.http.delete(AppSettings.API_ROOT + 'files/admin/all', {
      responseType: 'text'
    });
  }


  public uploadImage(uploadData: FormData): Observable<Object> {
    return this.http.post(AppSettings.API_ROOT, uploadData, {
      responseType: 'text'
    });
  }

  public deleteImage(filename: string): Observable<Object> {
    return this.http.delete(AppSettings.API_ROOT + 'files?filename=' + filename, {
      responseType: 'text'
    });
  }

  public deleteAllImages(): Observable<Object> {
    return this.http.delete(AppSettings.API_ROOT + 'files/all', {
      responseType: 'text'
    });
  }

  public getImage(filepath: string): Observable<Blob> {
    return this.http.get(filepath, {
      responseType: 'blob'
    });
  }

  public getAllImages(): Observable<string[]> {
    return this.http.get<string[]>(AppSettings.API_ROOT + 'files/', {
      responseType: 'json'
    });
  }

  public getAllAdminImages(): Observable<string[]> {
    return this.http.get<string[]>(AppSettings.API_ROOT + 'files/admin/', {
      responseType: 'json'
    });
  }

  public getFaceAnalysis(filename: string): Observable<DetectedFace[]> {
    return this.http.get<DetectedFace[]>(AppSettings.API_ROOT + "analysis?filename=" + filename, {
      responseType: 'json'
    }).pipe( map( response => {
      var dfa = [];
      for (let df of response){
        dfa.push(new DetectedFace(df));
      }
      return dfa;
    }));
  }

  public getFaceComparison(filename: string): Observable<VerifyResult> {
    return this.http.get<VerifyResult>(AppSettings.API_ROOT + 'compareAdmin?filename=' + filename);
  }

}
