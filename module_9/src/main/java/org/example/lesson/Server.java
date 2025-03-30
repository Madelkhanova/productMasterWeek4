package org.example.lesson;

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int TIMEOUT_SECONDS = 30;

    // База данных пользователей (логин -> пароль)
    private static final Map<String, String> users = new HashMap<>();

    static {
        users.put("user1", "1234");
        users.put("user2", "1234556");
        users.put("user3", "3242342342");
    }

    public static void main(String[] args) {
        int port = 7182;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер включен, ждем соединения...");

            try (Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader consoleInput = new BufferedReader(
                    new InputStreamReader(System.in))) {

                // Авторизация клиента
                out.println("Введите логин:");
                String login = in.readLine();

                out.println("Введите пароль:");
                String password = in.readLine();

                if (!users.containsKey(login) || !Objects.equals(users.get(login), password)) {
                    out.println("Ошибка авторизации. Соединение закрыто.");
                    System.out.println("Клиент ввел неверные данные, соединение закрыто.");
                    clientSocket.close();
                    return;
                }

                out.println("Добро пожаловать, " + login + "!");
                System.out.println("Клиент успешно авторизован: " + login);

                System.out.println("Клиент подкючился: " + clientSocket.getInetAddress());

                // Создаем таймер для контроля активности
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                Runnable timeoutTask = () -> {
                    System.out.println("Клиент неактивен. Разрываем соединение...");
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                };

                // Запускаем таймер (если нет сообщений 30 секунд, соединение разрывается)
                scheduler.schedule(timeoutTask, TIMEOUT_SECONDS, TimeUnit.SECONDS);

                String clientMessage;
                String serverMessage;

                while (true) {
                    clientMessage = in.readLine();

                    // Сбрасываем таймер при каждом полученном сообщении
                    scheduler.shutdownNow();
                    scheduler = Executors.newScheduledThreadPool(1);
                    scheduler.schedule(timeoutTask, TIMEOUT_SECONDS, TimeUnit.SECONDS);

                    if (Objects.isNull(clientMessage) || clientMessage.equalsIgnoreCase("exit")) {
                        System.out.println("Клиент отключился: " + clientSocket.getInetAddress());
                        break;
                    }

                    System.out.println("Клиент: " + clientMessage);

                    // Если клиент отправил "/time", отправляем текущее время
                    if (clientMessage.equalsIgnoreCase("/time")) {
                        String currentTime = LocalTime.now().toString();
                        out.println(currentTime);
                        System.out.println("Сервер: " + currentTime);
                        continue;
                    }

                    System.out.print("Сервер: ");
                    serverMessage = consoleInput.readLine();
                    out.println(serverMessage);
                    if (serverMessage.equalsIgnoreCase("exit")) {
                        System.out.println("Завершаем соединение...");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}