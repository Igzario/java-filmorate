package ru.yandex.practicum.filmorate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Не поправил
// Чтобы не отлавливать руками такие вещи (плюс всякие отступы и пробелы), используй комбинацию Ctrl+Alt+L- я не знаю почему,
// у меня там один таб

//Если name=null, что выдаст выражение user.getName().isBlank()? - будет npe, но там же для этого есть проверка на null
// и проблем с передачей туда null не возникает

// Лучше прописать отдельный метод валидации имени и использовать его потом везде - да вот он изначально был
// метод валидации, потом я его убрал...

@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }
}