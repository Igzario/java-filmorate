package ru.yandex.practicum.filmorate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Почему прописал @NotNull для даты релиза? Перечитай ТЗ - валидация по дате релиза присутствует в методе валидации
// а проверка на null просто как дополнительная проверка? т.к. если туда попадет null-будет npe

//А почему для каких-то полей ты прописываешь final, а для каких-то нет? - для name не прописываю final
// т.к. его нужно изменять если поле пустое на значение login*a

// Где реализация метода get? - я ее убрал специально и оставил @Getter на List с фильмами и юзерами

// А если name=" "? - поправил

// В этот метод будет передан User, у которого name=null, что будет? - забыл добавить валидацию в методы put...

// PS как по мне, то чувствуется какая-то спешка или невнимательность с твоей стороны (возможно я не прав).
// Если это так, то спешить не надо - это не помогает, а чаще всего еще и мешает) - к сожалению много работы сейчас
// не успеваю физически уделять столько времени, сколько хотелось-бы, учебе(( из-за этого глупые ошибки...
// но я всё исправлю, прошу понять и простить))

@SpringBootApplication
public class FilmorateApplication {

    public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
    }
}