package com.vinogradov.weather.utils;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    /**
     * Метод для отображения сообщений об ошибке
     * @param message
     */
    public static void showErrorMessage(String message) {
        // Выводим диалоговое окно с ошибкой
        Dialog errorDialog = new Dialog();
        errorDialog.add(new Text(message));
        Button closeButton = new Button("Закрыть", event -> errorDialog.close());
        errorDialog.add(closeButton);
        errorDialog.open();
    }

    /**
     * Метод форматирования стандартного вывода LocalDateTime
     * @param dateTime
     * @return String дата в формате "dd MMM yyyy HH:mm:ss"
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
        if (dateTime == null) {
            return "Дата не задана";
        }
        return dateTime.format(formatter);
    }
}
