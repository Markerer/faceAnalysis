export class Makeup {
  EyeMakeup: string;
  LipMakeup: string;


  toString(): string {
    var ret = "";
    ret += "Szem smink: " + this.EyeMakeup + '\n';
    ret += "Ajak smink: " + this.LipMakeup + '\n';

    return ret;
  }
}
