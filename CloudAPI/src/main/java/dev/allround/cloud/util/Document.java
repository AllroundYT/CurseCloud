package dev.allround.cloud.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("ALL")
public final class Document {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private JsonObject jsonObject = new JsonObject();

    public Document() {
        this(new JsonObject());
    }

    public Document(final @NotNull Path path) {
        this.read(path);
    }

    public Document(final @NotNull Reader reader) {
        this.read(reader);
    }

    public Document(final @NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Document(final @NotNull String json) {
        this.jsonObject = GSON.fromJson(json, JsonObject.class);
    }

    public Document(final @NotNull Object object) {
        this.setJsonObject(object);
    }

    public <T> T get(final @NotNull String key, final @NotNull Class<T> clazz) {
        return GSON.fromJson(this.jsonObject.get(key), clazz);
    }

    public <T> T get(final @NotNull String key, final @NotNull Type type) {
        return GSON.fromJson(this.jsonObject.get(key), type);
    }

    public Document addIfNotExists(String key, boolean state) {
        if (!has(key)) set(key, state);
        return this;
    }

    public Document addIfNotExists(String key, int state) {
        if (!has(key)) set(key, state);
        return this;
    }

    public Document addIfNotExists(String key, String state) {
        if (!has(key)) set(key, state);
        return this;
    }

    public <T> T get(final @NotNull Class<T> clazz) {
        return GSON.fromJson(this.jsonObject, clazz);
    }

    public <T> T get(final @NotNull Type type) {
        return GSON.fromJson(this.jsonObject, type);
    }

    public @NotNull Document set(final @NotNull String key, final @NotNull Object object) {
        this.jsonObject.add(key, GSON.toJsonTree(object, object.getClass()));
        return this;
    }

    public @NotNull Document read(final @NotNull Path path) {
        if (!Files.exists(path)) return this;
        try (final BufferedReader fileReader = Files.newBufferedReader(path)) {
            this.jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean has(String key) {
        return this.jsonObject.has(key);
    }

    public @NotNull Document read(final @NotNull Reader reader) {
        this.jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        return this;
    }


    public @NotNull Document write(final @NotNull Path path) {
        try (final BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW)) {
            writer.write(GSON.toJson(this.jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public @NotNull JsonObject getJsonObject() {
        return this.jsonObject;
    }

    public @NotNull Document setJsonObject(final @NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public @NotNull Document setJsonObject(final @NotNull Object object) {
        this.jsonObject = GSON.toJsonTree(object).getAsJsonObject();
        return this;
    }

    @Override
    public String toString() {
        return this.jsonObject.toString();
    }
}
