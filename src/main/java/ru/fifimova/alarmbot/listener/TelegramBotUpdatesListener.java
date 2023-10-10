package ru.fifimova.alarmbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.fifimova.alarmbot.service.NotificationTaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener{

    private final NotificationTaskService notificationService;
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            notificationService.process(update);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
