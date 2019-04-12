export class Occlusion {
  ForeheadOccluded: string;
  EyeOccluded: string;
  MouthOccluded: string;


  toString(): string {
    var ret = "";

    ret += "Homlok eltakarva: " + this.ForeheadOccluded + '\n';
    ret += "Szem eltakarva: " + this.EyeOccluded + '\n';
    ret += "Száj eltakarva: " + this.MouthOccluded + '\n';

    return ret;
  }
}
