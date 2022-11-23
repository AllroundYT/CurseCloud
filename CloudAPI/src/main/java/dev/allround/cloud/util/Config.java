package dev.allround.cloud.util;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.log.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

public class Config extends HashMap<String, String> {

    @SafeVarargs
    public Config(Pair<String, String>... pairs) {
        for (Pair<String, String> pair : pairs) {
            set(pair.getFirst(), pair.getSecond());
        }
    }

    public Config set(String key, String value) {
        put(key, value);
        return this;
    }

    public String get(String key) {
        return getOrDefault(key, "");
    }

    @Override
    public String getOrDefault(Object key, String defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    public void load(Path path) {
        try {
            if (!Files.exists(path.getParent())) Files.createDirectory(path.getParent());
            if (!Files.exists(path)) path.toFile().createNewFile();
        } catch (IOException e) {
            Cloud.getModule().getComponent(Logger.class).error(e);
        }
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            String jsonString = new String(bufferedInputStream.readAllBytes());
            if (jsonString.equalsIgnoreCase("")) return;
            JsonParser.parseString(jsonString).getAsJsonObject().entrySet().forEach(stringJsonElementEntry -> {
                set(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue().getAsString());
            });
        } catch (IOException e) {
            Cloud.getModule().getComponent(Logger.class).error(e);
        }
    }

    public void loadFromString(String jsonString) {
        Optional<Config> optionalConfig = Optional.ofNullable(new Gson().fromJson(jsonString, Config.class));
        if (optionalConfig.isEmpty()) return;
        optionalConfig.get().forEach(this::set);
    }


    public Config setDefault(String key, String defaultValue) {
        if (get(key).isEmpty()) set(key, defaultValue);
        return this;
    }

    public void save(Path path) {
        try {
            if (!Files.exists(path.getParent())) Files.createDirectory(path.getParent());
            if (!Files.exists(path)) path.toFile().createNewFile();
        } catch (IOException e) {
            Cloud.getModule().getComponent(Logger.class).error(e);
        }
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path.toFile()))) {
            bufferedOutputStream.write(new Gson().toJson(this).getBytes());
        } catch (IOException e) {
            Cloud.getModule().getComponent(Logger.class).error(e);
        }
    }
}