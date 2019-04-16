export class Hair {
  HairColor: string;
  IsInvisible: string;
  IsBald: string;

  constructor (h:Hair) {
    this.HairColor = h.HairColor;
    this.IsInvisible = h.IsInvisible;
    this.IsBald = h.IsBald;
  }
  public toString(): string {
    var ret = "";

    ret += "Hajszín: " + this.HairColor + '\n';
    ret += "Kopasz: " + this.IsBald + '\n';

    return ret;
  }
}
