package se.ifmo.api;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dot")
@Data
@NoArgsConstructor
public class Dot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double x;
    private Double y;
    private Double r;

    private boolean hit;
    private String hit_time;

    private Long execution_time;

    public Dot(Double x, Double y, Double r, boolean hit, String hit_time, Long execution_time) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.hit_time = hit_time;
        this.execution_time = execution_time;
    }
}
