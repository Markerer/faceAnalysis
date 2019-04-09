import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { FaceAnalysisComponent } from './face-analysis/face-analysis.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { HomeComponent } from './home/home.component';
import { MainService } from './main.service';
import { WebcamModule} from 'ngx-webcam';
import { HttpClientModule } from '@angular/common/http';
import { CameraComponent } from './camera/camera.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminComponent,
    FaceAnalysisComponent,
    NavbarComponent,
    FooterComponent,
    HomeComponent,
    CameraComponent
  ],
  imports: [
    NgbModule.forRoot(),
    FormsModule,
    RouterModule,
    BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule,
    WebcamModule,
    HttpClientModule
  ],
  providers: [MainService],
  bootstrap: [AppComponent]
})
export class AppModule { }
