package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public ResponseEntity getMpa(int id) throws EntityNotFoundException {
        Mpa mpa = mpaDbStorage.getMpa(id);
        log.info("Отправлен Mpa: {}", mpa);
        return new ResponseEntity<>(mpa, HttpStatus.valueOf(200));
    }

    public ResponseEntity getAllMpa() throws EntityNotFoundException {
        List<Mpa> mpaList = mpaDbStorage.getAllMpa();
        log.info("Отправлен список Mpa: {}", mpaList);
        return new ResponseEntity<>(mpaList, HttpStatus.valueOf(200));
    }
}