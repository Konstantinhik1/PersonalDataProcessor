import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PersonalDataProcessor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processData(scanner);
        scanner.close();
    }

    private static void processData(Scanner scanner) {
        System.out.print("Введите фамилию, имя, отчество, дату рождения (дд.мм.гггг), номер телефона, пол (f/m), разделенные пробелом: ");
        String input = scanner.nextLine();
        String[] data = input.split(" ");
        if (data.length != 6) {
            System.err.println("Ошибка: Необходимо ввести 6 элементов данных!");
            processData(scanner);
            return;
        }
        processData(scanner, data);
    }

    private static void processData(Scanner scanner, String[] data) {
        try {
            String surname = data[0];
            String name = data[1];
            String patronymic = data[2];
            Date birthDate = new SimpleDateFormat("dd.MM.yyyy").parse(data[3]);
            String phoneNumberStr = data[4];
            char gender = data[5].toLowerCase().charAt(0);
    
            // Проверка номера телефона с помощью регулярного выражения
            String phoneNumberRegex = "^\\d+$";
            if (!Pattern.matches(phoneNumberRegex, phoneNumberStr)) {
                throw new InvalidPhoneNumberException("Ошибка: Неверный формат номера телефона (должно быть целое беззнаковое число без форматирования)");
            }
    
            if (gender != 'f' && gender != 'm') {
                throw new InvalidGenderException("Ошибка: Неверно указан пол (должно быть 'f' или 'm')");
            }
    
            String fileName = surname + ".txt";
            String formattedData = surname + " " + name + " " + patronymic + " " + new SimpleDateFormat("dd.MM.yyyy").format(birthDate) + " " + phoneNumberStr + gender;
    
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {
                writer.write(formattedData);
                writer.newLine();
                System.out.println("Данные успешно записаны в файл " + fileName);
            } catch (IOException e) {
                System.err.println("Ошибка при записи в файл:");
                e.printStackTrace();
            }
        } catch (ParseException e) {
            System.err.println("Ошибка: Неверный формат даты рождения (должно быть дд.мм.гггг)");
            processData(scanner);
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: Неверный формат номера телефона (должно быть целое беззнаковое число без форматирования)");
            processData(scanner);
        } catch (InvalidGenderException | InvalidPhoneNumberException e) {
            System.err.println(e.getMessage());
            processData(scanner);
        }
    }
    
    static class InvalidGenderException extends RuntimeException {
        InvalidGenderException(String message) {
            super(message);
        }
    }
    
    static class InvalidPhoneNumberException extends RuntimeException {
        InvalidPhoneNumberException(String message) {
            super(message);
        }
    }
}
