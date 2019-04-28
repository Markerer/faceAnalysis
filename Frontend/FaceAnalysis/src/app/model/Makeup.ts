import utf8 from 'utf8';

export class Makeup {
  EyeMakeup: string;
  LipMakeup: string;

constructor (m:Makeup) {
  this.EyeMakeup = utf8.decode(m.EyeMakeup);
  this.LipMakeup = utf8.decode(m.LipMakeup);
}

  public toString(): string {
    var ret = "";
    ret += "Szem smink: " + this.EyeMakeup + '\n';
    ret += "Ajak smink: " + this.LipMakeup + '\n';

    return ret;
  }
}
