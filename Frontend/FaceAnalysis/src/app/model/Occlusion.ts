export class Occlusion {
  ForeheadOccluded: string;
  EyeOccluded: string;
  MouthOccluded: string;

  constructor(occ: Occlusion){
    this.ForeheadOccluded = occ.ForeheadOccluded;
    this.EyeOccluded = occ.EyeOccluded;
    this.MouthOccluded = occ.MouthOccluded;
  }

  public toString(): string {
    var ret = "";

    ret += "Homlok eltakarva: " + this.ForeheadOccluded + '\n';
    ret += "Szem eltakarva: " + this.EyeOccluded + '\n';
    ret += "Száj eltakarva: " + this.MouthOccluded + '\n';

    return ret;
  }
}
