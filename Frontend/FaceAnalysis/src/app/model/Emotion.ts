import utf8 from 'utf8';

export class Emotion {
  Name: string;
  Value: string;

  constructor (em: Emotion){
    this.Name = utf8.decode(em.Name);
    this.Value = utf8.decode(em.Value);
  }

  public toString(): string {
    var ret = "";

    ret += this.Name + ": " + this.Value + '\n';

    return ret;
  }
}
