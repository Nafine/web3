package se.ifmo.faces;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Named
@ViewScoped
public class SliderView implements Serializable {
    private double r = 1;
}
