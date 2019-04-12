export class Hair {
  HairColor: string;
  IsInvisible: string;
  IsBald: string;

  toString(): string {
    var ret = "";

    ret += "Hajszín: " + this.HairColor + '\n';
    ret += "Kopasz: " + this.IsBald + '\n';

    return ret;
  }
}
