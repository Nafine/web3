package se.ifmo.faces;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Named
@ViewScoped
public class ClockView implements Serializable {
    private LocalDateTime currentTime = LocalDateTime.now();

    public String getDateTime() {
        return currentTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void update() {
        currentTime = LocalDateTime.now();
    }
}
