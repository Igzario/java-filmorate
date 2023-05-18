package ru.yandex.practicum.filmorate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Если name=null, что выдаст выражение user.getName().isBlank()? - будет npe, но там же для этого есть проверка на null
// и проблем с передачей туда null не возникает
// https://ru.stackoverflow.com/questions/712136/%D0%92-%D1%87%D1%91%D0%BC-%D0%BE%D1%82%D0%BB%D0%B8%D1%87%D0%B8%D0%B5-isblank-vs-isempty
// да, я читал описание когда искал как лучше сделать, но в реальности как бы я не делал получается NPE...
// по логике должен остаться только user.getName().isBlank(), но так не работает...  применение .isBlank() к полю
// null приводит к NPE... может конечно я что-то не так делаю

@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }
}