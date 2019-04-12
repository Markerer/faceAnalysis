export class FacialHair {
  Moustache: string;
  Beard: string;
  Sideburns: string;


  toString(): string {
    var ret = "";

    ret += "Bajusz: " + this.Moustache + '\n';
    ret += "Szakáll: " + this.Beard + '\n';
    ret += "Oldalszakáll: " + this.Sideburns + '\n';

    return ret;
  }
}
