package wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

class MyEmotion{
    @JsonProperty("Name")
    String name;
    @JsonIgnore
    double value;

    @JsonProperty("Value")
    String valueInString;

    public MyEmotion(String name, double value) {
        this.name = name;
        this.value = value;
        setValue();
    }

    void setValue(){
        if(value < 0.2) {
            valueInString = "Nincs nyoma";
        }
        if(value > 0.2 && value < 0.5) {
            valueInString = "Minimálisan fellelhető";
        }
        if(value > 0.5 && value < 0.75) {
            valueInString = "Észlelhető";
        }
        if(value > 0.75) {
            valueInString = "Egyértelműen";
        }
    }

    public String toString() {
        String ret = name + ": ";
        if(value < 0.2) {
            ret += "Nincs nyoma";
        }
        if(value > 0.2 && value < 0.5) {
            ret += "Minimálisan fellelhető";
        }
        if(value > 0.5 && value < 0.75) {
            ret += "Észlelhető";
        }
        if(value > 0.75) {
            ret += "Egyértelműen";
        }
        ret += "\n";
        return ret;
    }
}
