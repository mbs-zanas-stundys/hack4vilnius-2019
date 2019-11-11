package lt.vilnius.waste.system;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class UploadEvent {

    @Id
    public String id;
    public LocalDateTime date;
    public UploadType type;

    public static UploadEvent now(UploadType type) {
        UploadEvent event = new UploadEvent();
        event.type = type;
        event.date = LocalDateTime.now();
        return event;
    }

    public UploadEvent() {

    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UploadType getType() {
        return type;
    }

    public void setType(UploadType type) {
        this.type = type;
    }
}
