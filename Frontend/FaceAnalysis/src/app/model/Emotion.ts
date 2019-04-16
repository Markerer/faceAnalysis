export class Emotion {
  Name: string;
  Value: string;

  constructor (em: Emotion){
    this.Name = em.Name;
    this.Value = em.Value;
  }

  public toString(): string {
    var ret = "";

    ret += this.Name + ": " + this.Value + '\n';

    return ret;
  }
}
