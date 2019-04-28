import { Emotion } from './Emotion';
import { FacialHair } from './FacialHair';
import { Hair } from './Hair';
import { Makeup } from './Makeup';
import { Occlusion } from './Occlusion';
import utf8 from 'utf8';

export  class DetectedFace {

  Accessories: string[];
  Emotions: Emotion[];
  FacialHair: FacialHair;
  Hair: Hair;
  Makeup: Makeup;
  Occlusion: Occlusion;
  Age: number;
  Gender: string;
  Smile: string;
  Glasses: string;
  FaceRectangle: number[];

  constructor(face: DetectedFace){
    this.Accessories = utf8.decode(face.Accessories);
    this.Emotions = [];
    for (let em of face.Emotions){
      this.Emotions.push(new Emotion(em));
    }
    this.FacialHair = new FacialHair(face.FacialHair);
    this.Hair = new Hair(face.Hair);
    this.Makeup = new Makeup(face.Makeup);
    this.Occlusion = new Occlusion(face.Occlusion);
    this.Age = face.Age;
    this.Gender = utf8.decode(face.Gender);
    this.Smile = utf8.decode(face.Smile);
    this.Glasses = utf8.decode(face.Glasses);
    this.FaceRectangle = face.FaceRectangle;
  }

  public toString(): string{
    var ret = "";
    if (this.Accessories.length > 0) {
      for(let a of this.Accessories) {
        ret += a + '\n';
      }
    }
    if (this.Emotions.length > 0) {
      for (let e of this.Emotions) {
        ret += e.toString();
      }
    }

    ret += this.FacialHair.toString();
    ret += this.Hair.toString();
    ret += this.Makeup.toString();
    ret += this.Occlusion.toString();
    ret += "Kor: " + this.Age + '\n';
    ret += "Nem: " + this.Gender + '\n';
    ret += "Mosoly: " + this.Smile + '\n';
    ret += "Szemüveg: " + this.Glasses;

    return ret;
  }
}
