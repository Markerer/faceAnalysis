import utf8 from 'utf8';

export class Hair {
  HairColor: string;
  IsInvisible: string;
  IsBald: string;

  constructor (h:Hair) {
    this.HairColor = utf8.decode(h.HairColor);
    this.IsInvisible = utf8.decode(h.IsInvisible);
    this.IsBald = utf8.decode(h.IsBald);
  }
  public toString(): string {
    var ret = "";

    ret += "Hajszín: " + this.HairColor + '\n';
    ret += "Kopasz: " + this.IsBald + '\n';

    return ret;
  }
}
