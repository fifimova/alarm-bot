# Alarm-bot - Telegram Reminder Bot

[![Telegram](https://img.shields.io/badge/Telegram-%40fifififiififi__bot-blue)](https://t.me/fifififiififi_bot)

## Overview

Alarm bot is a Java study project to help users set reminders and receive notifications at specified times. It allows users to add reminders in the format 'dd.MM.yyyy HH:mm reminder text'. The bot will then send notifications to remind users of their scheduled tasks.

## Getting Started

1. **Telegram Bot:** Start a conversation with the bot on Telegram: [Telegram Reminder Bot](https://t.me/fifififiififi_bot)

2. **Commands:**
   - `/start`: Displays a welcome message and instructions on how to use the bot.
   - To set a reminder: Enter a message in the format 'dd.MM.yyyy HH:mm reminder text'.

## Code Structure

The code is structured into two main components:

### 1. NotificationTaskServiceImpl

This service is responsible for processing user messages, validating the date format, and saving reminders to the database.

- **Scheduled Task:** The bot checks the database every minute for reminders that need to be sent and notifies users accordingly.

### 2. TelegramBotUpdatesListener

This listener processes updates received from Telegram and delegates the processing to the `NotificationTaskService`.

## How to Use

1. Start a conversation with the bot.
2. Use the `/start` command to receive a welcome message and instructions.
3. Set a reminder by sending a message in the format 'dd.MM.yyyy HH:mm reminder text'.

## Development

### Prerequisites

- Java 17
- Spring Boot 3.1.4
- PostgreSQL

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/telegram-reminder-bot.git

2. Configure your PostgreSQL database in application.yml.
3. Build and run the application
  ```bash 
  ./mvnw spring-boot:run
  ```
4. Interact
