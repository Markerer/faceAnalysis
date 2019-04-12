import { Emotion } from './Emotion';
import { FacialHair } from './FacialHair';
import { Hair } from './Hair';
import { Makeup } from './Makeup';
import { Occlusion } from './Occlusion';

export class DetectedFace {

  Accessories: string[];
  Emotions: Emotion[];
  FacialHair: FacialHair;
  Hair: Hair;
  Makeup: Makeup;
  Occlusion: Occlusion;
  Gender: string;
  Smile: string;
  Glasses: string;
  FaceRectangle: number[];


  toString(): string{
    var ret = "";
    if (this.Accessories.length > 0) {
      for(let a in this.Accessories) {
        ret += a + '\n';
      }
    }
    if (this.Emotions.length > 0) {
      for (let e in this.Emotions) {
        ret += e.toString();
      }
    }

    ret += this.FacialHair.toString();
    ret += this.Hair.toString();
    ret += this.Makeup.toString();
    ret += this.Occlusion.toString();
    ret += "Nem: " + this.Gender + '\n';
    ret += "Mosoly: " + this.Smile + '\n';
    ret += "Szemüveg: " + this.Glasses;

    return ret;
  }
}
