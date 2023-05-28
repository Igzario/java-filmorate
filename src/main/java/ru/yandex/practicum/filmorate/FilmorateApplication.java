package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Написать ниже не могу, а есть необходимость прописывать методы toString и equals? - toString да, т.к. есть места
//лог пишется User и Film, и если не использовать toString то будет ошибка с записью коллекции в лог...
//log.info("Отправлен список общих друзей пользователей: {} и {} ", user, userFriend); - вот здесь например

//private final Set<User> friends = new LinkedHashSet<>(); - поменял на LinkedHashSet, т.к. постман тесты не проходятся
// если порядок не соблюден

@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }
}