package se.ifmo.faces;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Named
@SessionScoped
public class FormBean implements Serializable {
    private double y = 1;
    private double x = 1;
    private double r = 1;
}
