package se.ifmo.api.hit;

import java.io.Serializable;

public record HitResponse(Boolean hit, String timestamp, long execTime) implements Serializable {
}
