import utf8 from 'utf8';

export class FacialHair {
  Moustache: string;
  Beard: string;
  Sideburns: string;

  constructor(fh: FacialHair) {
    this.Moustache = utf8.decode(fh.Moustache);
    this.Beard = utf8.decode(fh.Beard);
    this.Sideburns = utf8.decode(fh.Sideburns);
  }

  public toString(): string {
    var ret = "";

    ret += "Bajusz: " + this.Moustache + '\n';
    ret += "Szakáll: " + this.Beard + '\n';
    ret += "Oldalszakáll: " + this.Sideburns + '\n';

    return ret;
  }
}
