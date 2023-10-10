package ru.fifimova.alarmbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.fifimova.alarmbot.model.NotificationTask;
import ru.fifimova.alarmbot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class NotificationTaskServiceImpl implements NotificationTaskService {

    private final NotificationTaskRepository repository;
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(NotificationTaskServiceImpl.class);
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("([0-9\\.:\\s]{16})(\\s)(.+)");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    @Override
    public void process(Update update) {
        if (update.message() == null) {
            logger.info("user sent empty message");
            return;
        }

        long chatId = update.message().chat().id();
        String clientMessage = update.message().text();

        if (clientMessage == null) {
            telegramBot.execute(new SendMessage(chatId, "Для начала работы с ботом, отправь /start"));
            return;
        }

        if (clientMessage.equals("/start")) {
            sendWelcomeMessage(chatId);
            return;
        }

        Matcher matcher = MESSAGE_PATTERN.matcher(clientMessage);
        if (matcher.find()) {
            dateFormatterValidation(chatId, matcher);
        } else {
            telegramBot.execute(new SendMessage(chatId, "Добавить напоминание можно только в формате: 'dd.MM.yyyy HH:mm текст напоминания'"));
            return;
        }
        LocalDateTime alarmDate = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
        String notification = matcher.group(3);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        saveEntity(chatId, notification, alarmDate, now);
    }

    private void dateFormatterValidation(long chatId, Matcher matcher) {
        String dateString = matcher.group(1);
        try {
            LocalDateTime alarmDate = LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);

            if (!alarmDate.isAfter(LocalDateTime.now())) {
                logger.warn("Invalid date format");
                telegramBot.execute(new SendMessage(chatId, "Напоминание в прошлое невозможно отправить :("));
                return;
            }

        } catch (DateTimeParseException e) {
            telegramBot.execute(new SendMessage(chatId, "Добавить напоминание можно только в формате: 'dd.MM.yyyy HH:mm текст напоминания'"));
        }
    }

    private void saveEntity(Long chatId, String notification, LocalDateTime alarmDate, LocalDateTime addedAt) {
        NotificationTask notificationTask = new NotificationTask(chatId, notification, alarmDate, addedAt);
        repository.save(notificationTask);
        logger.info("Напоминание сохранено" + notificationTask);
        telegramBot.execute(new SendMessage(chatId, "Напоминание сохранено! " + notificationTask));
    }

    private void sendWelcomeMessage(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Добро пожаловать, мой забывчивый друг :) " +
                "Я позабочусь о том, чтобы ты всё успевал и ничего не забывал! " +
                "Можешь добавить напоминание в формате: 'dd.MM.yyyy HH:mm текст напоминания'"));
    }

    @Override
    @Scheduled(cron = "0 0/1 * * * *")
    public void fetchDatabaseRecords() {
        List<NotificationTask> records = repository
                .findByAlarmDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        records.forEach(record -> {
            logger.info("Notification was sent");
            telegramBot.execute(new SendMessage(record.getChatId(), String.format("Привет! Не забудь:\n%s" +
                    " , в %s", record.getNotification(), record.getAlarmDate())));
        });
    }
}
