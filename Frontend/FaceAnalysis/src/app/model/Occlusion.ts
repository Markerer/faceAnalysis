import utf8 from 'utf8';

export class Occlusion {
  ForeheadOccluded: string;
  EyeOccluded: string;
  MouthOccluded: string;

  constructor(occ: Occlusion){
    this.ForeheadOccluded = utf8.decode(occ.ForeheadOccluded);
    this.EyeOccluded = utf8.decode(occ.EyeOccluded);
    this.MouthOccluded = utf8.decode(occ.MouthOccluded);
  }

  public toString(): string {
    var ret = "";

    ret += "Homlok eltakarva: " + this.ForeheadOccluded + '\n';
    ret += "Szem eltakarva: " + this.EyeOccluded + '\n';
    ret += "Száj eltakarva: " + this.MouthOccluded + '\n';

    return ret;
  }
}
