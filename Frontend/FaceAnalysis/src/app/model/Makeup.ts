export class Makeup {
  EyeMakeup: string;
  LipMakeup: string;

constructor (m:Makeup) {
  this.EyeMakeup = m.EyeMakeup;
  this.LipMakeup = m.LipMakeup;
}

  public toString(): string {
    var ret = "";
    ret += "Szem smink: " + this.EyeMakeup + '\n';
    ret += "Ajak smink: " + this.LipMakeup + '\n';

    return ret;
  }
}
