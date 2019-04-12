export class Emotion {
  Name: string;
  Value: string;


  toString(): string {
    var ret = "";

    ret += this.Name + ": " + this.Value + '\n';

    return ret;
  }
}
