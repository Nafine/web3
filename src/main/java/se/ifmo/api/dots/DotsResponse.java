package se.ifmo.api.dots;

import java.util.List;

public record DotsResponse(int page, int size, boolean hasBefore, boolean hasNext, List<Dot> dots) {
}
