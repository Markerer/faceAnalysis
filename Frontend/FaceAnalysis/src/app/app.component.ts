
import { Component } from '@angular/core';
import * as $ from 'jquery';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'FaceAnalysis';

  constructor() {
    $(document).on('hidden.bs.modal', '#ModalSuccess', function () {
      console.log("App component is doing fine.");
      document.getElementById('successModalText').onclick = null;
      document.getElementById('successModalText').style.cursor = "default";
    });
  }
}
