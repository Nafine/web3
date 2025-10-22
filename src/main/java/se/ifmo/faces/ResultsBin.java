package se.ifmo.faces;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.primefaces.PrimeFaces;
import se.ifmo.api.Dot;
import se.ifmo.db.DotManager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Named
@SessionScoped
public class ResultsBin implements Serializable {
    private final DotManager dm = new DotManager();
    @Getter
    private int currentPage = 1;
    @Getter
    private int pageSize = 10;
    @Inject
    private FormBean form;

    public void checkHit() {
        long start = System.nanoTime();
        double x = form.getX();
        double y = form.getY();
        double r = form.getR();
        boolean hit = (Math.abs(x) <= r && Math.abs(y) <= r && (
                (x <= 0 && y >= 0 && (2 * x + y <= r)) ||
                        (x >= 0 && x <= r && y >= 0 && y <= r) ||
                        (x >= 0 && y <= 0 && (x * x + y * y <= r * r / 4)))
        );
        dm.add(new Dot(x, y, r, hit, LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                System.nanoTime() - start));
    }

    public List<Dot> getDots() {
        PrimeFaces.current().executeScript("drawDots()");
        int from = (currentPage - 1) * pageSize;
        int to = Math.min(currentPage * pageSize, dm.get().size());

        if (from >= dm.get().size()) {
            return Collections.emptyList();
        }

        System.out.println(dm.get().size());

        return dm.get().subList(from, to);
    }

    public void clear() {
        dm.clear();
        currentPage = 1;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        currentPage = 1;
    }

    public void prevPage() {
        if (currentPage > 1) {
            currentPage--;
        }
    }

    public void nextPage() {
        if (currentPage * pageSize < dm.get().size()) {
            currentPage++;
        }
    }

    public void onPageSizeChange() {
        currentPage = 1;
    }
}
