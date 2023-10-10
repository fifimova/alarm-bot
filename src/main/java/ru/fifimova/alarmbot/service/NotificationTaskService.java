package ru.fifimova.alarmbot.service;

import com.pengrad.telegrambot.model.Update;

public interface NotificationTaskService {
    void process(Update update);

    void fetchDatabaseRecords();
}
