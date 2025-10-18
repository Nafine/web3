package se.ifmo.api.hit;

import java.io.Serializable;

public record HitRequest(double x, double y, double r) implements Serializable {
}
