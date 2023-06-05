CREATE TABLE IF NOT EXISTS Users (
                                     "user_id" int,
                                     "email" varchar,
                                     "login" varchar,
                                     "name" varchar,
                                     "birthday" date,
                                     PRIMARY KEY ("user_id")
    );

CREATE TABLE IF NOT EXISTS "Friends" (
                                         "user_1" int,
                                         "user_2" int,
                                         "status" int
);

CREATE TABLE IF NOT EXISTS "Films" (
                                       "film_id" int,
                                       "genre_id" int,
                                       "rating_id" int,
                                       "name" varchar,
                                       "description" varchar,
                                       "releaseDate" date,
                                       "duration" int,
                                       PRIMARY KEY ("film_id")
    );

CREATE TABLE IF NOT EXISTS "Genre" (
                                       "genre_id" int,
                                       "name" varchar,
                                       PRIMARY KEY ("genre_id")
    );

CREATE TABLE IF NOT EXISTS "Likes" (
                                       "like_id" int,
                                       "film_id" int,
                                       "user_id" int,
                                       PRIMARY KEY ("like_id")
    );

CREATE TABLE IF NOT EXISTS Mpa (
                                   "rating_id" int,
                                   "name" varchar,
                                   PRIMARY KEY ("rating_id")
    );

ALTER TABLE "Friends" ADD FOREIGN KEY ("user_1") REFERENCES Users ("user_id");

ALTER TABLE "Friends" ADD FOREIGN KEY ("user_2") REFERENCES Users ("user_id");

ALTER TABLE "Likes" ADD FOREIGN KEY ("film_id") REFERENCES "Films" ("film_id");

ALTER TABLE "Likes" ADD FOREIGN KEY ("user_id") REFERENCES Users ("user_id");

ALTER TABLE "Films" ADD FOREIGN KEY ("genre_id") REFERENCES "Genre" ("genre_id");

ALTER TABLE "Films" ADD FOREIGN KEY ("rating_id") REFERENCES Mpa ("rating_id");
