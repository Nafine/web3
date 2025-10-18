package se.ifmo.api.dots;

import se.ifmo.api.hit.HitRequest;
import se.ifmo.api.hit.HitResponse;

import java.io.Serializable;

public record Dot(HitRequest req, HitResponse res) implements Serializable {
}
