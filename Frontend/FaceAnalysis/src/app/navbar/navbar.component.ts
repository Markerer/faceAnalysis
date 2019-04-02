import { Component, OnInit, ElementRef } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  private toggleButton: any;
  private sidebarVisible: boolean;

  constructor(public location: Location, private element: ElementRef, private router: Router) {
    this.sidebarVisible = false;
  }

  ngOnInit() {
    const navbar: HTMLElement = this.element.nativeElement;
    this.toggleButton = navbar.getElementsByClassName('navbar-toggler')[0];
  }
  sidebarOpen() {
    const toggleButton = this.toggleButton;
    const html = document.getElementsByTagName('html')[0];

    setTimeout(function () {
      toggleButton.classList.add('toggled');
    }, 500);
    html.classList.add('nav-open');
    html.style.paddingRight = '225px';
    html.style.transitionDuration = '750ms';
    this.sidebarVisible = true;
  };
  sidebarClose() {
    const html = document.getElementsByTagName('html')[0];
    // console.log(html);
    this.toggleButton.classList.remove('toggled');
    this.sidebarVisible = false;
    html.style.paddingRight = '0';
    html.style.transitionDuration = '750ms';
    html.classList.remove('nav-open');
  };
  sidebarToggle() {
    if (this.sidebarVisible === false) {
      this.sidebarOpen();
    } else {
      this.sidebarClose();
    }
  };


  navigateToAdmin(): void {
    this.router.navigate(['admin']);
    this.sidebarClose();
  }

  navigateToLogin(): void {
    this.router.navigate(['login']);
    this.sidebarClose();
  }

  navigateToFaceAnalysis(): void {
    this.router.navigate(['faceanalysis']);
    this.sidebarClose();
  }

  navigateToHome(): void {
    this.router.navigate(['']);
    this.sidebarClose();
  }



}
