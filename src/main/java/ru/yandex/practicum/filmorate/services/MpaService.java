package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public ResponseEntity getMpa(int id) throws EntityNotFoundException {
        Mpa mpa = mpaStorage.getMpa(id);
        log.info("Отправлен Mpa: {}", mpa);
        return new ResponseEntity<>(mpa, HttpStatus.valueOf(200));
    }

    public ResponseEntity getAllMpa() throws EntityNotFoundException {
        List<Mpa> mpaList = mpaStorage.getAllMpa();
        log.info("Отправлен список Mpa: {}", mpaList);
        return new ResponseEntity<>(mpaList, HttpStatus.valueOf(200));
    }
}