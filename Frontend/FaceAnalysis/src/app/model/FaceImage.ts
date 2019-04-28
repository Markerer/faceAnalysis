import utf8 from 'utf8';

export class FaceImage {
  link: string;
  id: string;

  public setIdFromLink(): void {
    if (this.link.length != 0) {
      var substrings = this.link.split('/');
      this.id = substrings[substrings.length - 1];
    }
  }
}
