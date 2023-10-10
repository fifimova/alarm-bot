package ru.fifimova.alarmbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fifimova.alarmbot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, UUID> {

    List<NotificationTask> findByAlarmDate(LocalDateTime alarmDate);
}
