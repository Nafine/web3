package se.ifmo.faces;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class SliderView implements Serializable {
    private float r = 1.0f;

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }
}
