package se.ifmo.faces;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import se.ifmo.api.dots.Dot;
import se.ifmo.api.hit.HitRequest;
import se.ifmo.api.hit.HitResponse;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Named
@SessionScoped
public class ResultsBin implements Serializable {
    private final List<Dot> dots = new LinkedList<>();
    private int currentPage = 1;
    private int pageSize = 10;

    @Inject
    private FormBean form;

    public void checkHit() {
        long start = System.nanoTime();
        double x = form.getX();
        double y = form.getY();
        double r = form.getR();
        boolean hit = (Math.abs(x) <= r && Math.abs(y) <= r && (
                (x >= 0 && y >= 0 && (x * x + y * y <= r * r / 4)) ||
                        (x <= 0 && x >= -r / 2 && y >= 0 && y <= r) ||
                        (x <= 0 && y <= 0 && (Math.abs(x) + Math.abs(y) <= r / 2)))
        );
        dots.add(new Dot(new HitRequest(x, y, r), new HitResponse(hit, LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                System.nanoTime() - start)));
    }

    public List<Dot> getDots() {
        int from = (currentPage - 1) * pageSize;
        int to = Math.min(currentPage * pageSize, dots.size());

        if (from >= dots.size()) {
            return Collections.emptyList();
        }

        return dots.subList(from, to);
    }

    public void clear() {
        dots.clear();
        currentPage = 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        System.out.printf("New page size: %d\n", pageSize);
        this.pageSize = pageSize;
        currentPage = 1;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void prevPage() {
        if (currentPage > 1) {
            currentPage--;
        }
    }

    public void nextPage() {
        if (currentPage * pageSize < dots.size()) {
            currentPage++;
        }
    }

    public void onPageSizeChange() {
        currentPage = 1;
    }
}
