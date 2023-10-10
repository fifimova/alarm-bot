package ru.fifimova.alarmbot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "notification_tasks")
@Data
@NoArgsConstructor
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "notification")
    private String notification;

    @Column(name = "alarm_date")
    private LocalDateTime alarmDate;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    public NotificationTask(Long chatId, String notification, LocalDateTime alarmDate, LocalDateTime addedAt) {
        this.chatId = chatId;
        this.notification = notification;
        this.alarmDate = alarmDate;
        this.addedAt = addedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Уведомление \"%s\" будет отправлено, когда часы пробьют %s, добавлено: %s"
                , notification, alarmDate, addedAt);
    }
}
