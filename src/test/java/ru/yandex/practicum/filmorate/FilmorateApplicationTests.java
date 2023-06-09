package ru.yandex.practicum.filmorate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.squareup.okhttp.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {
    Gson gson;
    GsonBuilder gsonBuilder;
    OkHttpClient client;
    ConfigurableApplicationContext server;

    @BeforeEach
    public void startServer() throws IOException {
        server = SpringApplication.run(FilmorateApplication.class);
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();
        client = new OkHttpClient();
    }

    @AfterEach
    public void stopServer() throws IOException {
        server.stop();
    }

    @Test
    public void contextLoadsFilm() throws IOException, InterruptedException {
        Film film = new Film("film", "descriptionFilm", LocalDate.of(2020, 5, 6), 100);
        String jsonFilm = gson.toJson(film);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonFilm);
        Request request = new Request.Builder()
                .url("http://localhost:8080/films")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        String responseBody = response.body().string();

        assertEquals(201, code);

        Film film1 = new Film("film1", "descriptionFilm1", LocalDate.of(2020, 5, 6), 100);
        film1.setId(1);
        jsonFilm = gson.toJson(film1);
        body = RequestBody.create(mediaType, jsonFilm);
        request = new Request.Builder()
                .url("http://localhost:8080/films")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        code = response.code();
        responseBody = response.body().string();

        assertEquals(200, code);

        Film film2 = new Film("film2", "descriptionFilm2", LocalDate.of(2020, 5, 6), 100);
        film2.setId(5);
        jsonFilm = gson.toJson(film2);
        body = RequestBody.create(mediaType, jsonFilm);
        request = new Request.Builder()
                .url("http://localhost:8080/films")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        code = response.code();
        responseBody = response.body().string();

        assertEquals(500, code);
        film2.setId(2);

        request = new Request.Builder()
                .url("http://localhost:8080/films")
                .get()
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        code = response.code();
        responseBody = response.body().string();
        List<LinkedTreeMap> list = gson.fromJson(responseBody, List.class);

        Film film3 = null;
        for (LinkedTreeMap map : list) {
            int id = (int) ((double) map.get("id"));
            String name = (String) map.get("name");
            String description = (String) map.get("description");

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate releaseDate = LocalDate.parse((String) map.get("releaseDate"), formatter);
            double d = (double) map.get("duration");
            int duration = (int) d;

            film3 = new Film(name, description, releaseDate, duration);

            assertEquals(200, code);
            assertEquals(film3, film1);
        }
    }

    @Test
    public void contextLoadsUser() throws IOException, InterruptedException {
        User user = new User("ya1@Ya.ru", "login1");
        user.setBirthday(LocalDate.of(1990, 5, 6));
        String jsonUser = gson.toJson(user);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonUser);
        Request request = new Request.Builder()
                .url("http://localhost:8080/users")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        String responseBody = response.body().string();

        assertEquals(201, code);

        User user1 = new User("ya1@Ya.ru", "login1");
        user1.setBirthday(LocalDate.of(1990, 5, 6));
        user1.setId(1);
        jsonUser = gson.toJson(user1);
        body = RequestBody.create(mediaType, jsonUser);
        request = new Request.Builder()
                .url("http://localhost:8080/users")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        code = response.code();
        responseBody = response.body().string();

        assertEquals(200, code);

        User user2 = new User("ya2@Ya.ru", "login2");
        user2.setBirthday(LocalDate.of(1989, 5, 6));
        user2.setId(5);
        jsonUser = gson.toJson(user2);
        body = RequestBody.create(mediaType, jsonUser);
        request = new Request.Builder()
                .url("http://localhost:8080/users")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        code = response.code();
        responseBody = response.body().string();

        assertEquals(500, code);
        user2.setId(2);

        request = new Request.Builder()
                .url("http://localhost:8080/users")
                .get()
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        code = response.code();
        responseBody = response.body().string();
        List<LinkedTreeMap> list = gson.fromJson(responseBody, List.class);

        User user3 = null;
        for (LinkedTreeMap map : list) {
            int id = (int) ((double) map.get("id"));
            String email = (String) map.get("email");
            String login = (String) map.get("login");

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthday = LocalDate.parse((String) map.get("birthday"), formatter);

             user3 = new User(email, login);
             user3.setBirthday(birthday);
        }
        assertEquals(200, code);
        assertEquals(user3, user1);

    }

    public static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            jsonWriter.value(duration.toSeconds());
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            String str = jsonReader.nextString();
            return Duration.ofSeconds(Long.parseLong(str));
        }
    }

    public static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
            if (localDate != null)

                jsonWriter.value(localDate.format(formatter));
            else
                jsonWriter.nullValue();
        }

        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            return LocalDate.parse(jsonReader.nextString(), formatter);
        }
    }
}
