export class FacialHair {
  Moustache: string;
  Beard: string;
  Sideburns: string;

  constructor(fh: FacialHair) {
    this.Moustache = fh.Moustache;
    this.Beard = fh.Beard;
    this.Sideburns = fh.Sideburns;
  }

  public toString(): string {
    var ret = "";

    ret += "Bajusz: " + this.Moustache + '\n';
    ret += "Szakáll: " + this.Beard + '\n';
    ret += "Oldalszakáll: " + this.Sideburns + '\n';

    return ret;
  }
}
