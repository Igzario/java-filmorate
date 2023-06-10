package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa getMpa(int id) throws EntityNotFoundException;

    List<Mpa> getAllMpa() throws EntityNotFoundException;
}
