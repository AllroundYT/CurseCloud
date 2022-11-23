package dev.allround.cloud.util;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.log.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUtils {
    public static String readFile(Path path) {
        if (Files.exists(path)) {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path.toFile()))) {
                return new String(bufferedInputStream.readAllBytes());
            } catch (IOException e) {
                Cloud.getModule().getComponent(Logger.class).error(e);
                return "";
            }
        } else return "";
    }


    public static void copy(File source, File destination) throws IOException {
        if (!source.exists()) return;
        if (source.isDirectory()) {

            if (!destination.exists()) {
                destination.mkdirs();
            }

            if (source.list() == null) return;
            for (String file : source.list()) {
                copy(new File(source, file), new File(destination, file));
            }

        } else {
            if (!destination.exists()) {
                if (!destination.getParentFile().exists()) {
                    destination.getParentFile().mkdirs();
                }
                destination.createNewFile();
            }

            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination)); BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(source))) {
                byte[] bytes = bufferedInputStream.readAllBytes();
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.flush();
            }
        }
    }
    public static void setupDirectories(Path... paths) throws IOException {
        for (Path path : paths) {
            Files.createDirectories(path);
        }
    }

    public static void delete(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }

        file.delete();
    }

    public static <T> T readJsonFile(Path path, Class<T> clazz) {
        String jsonString = readFile(path);
        return new Gson().fromJson(jsonString, clazz);
    }

    public static void copyFromResources(String filePath, Path target) {
        try {
            if (!target.toFile().exists()) target.toFile().createNewFile();
            Files.copy(Objects.requireNonNull(Cloud.getModule().getClass().getClassLoader().getResourceAsStream(filePath)), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Cloud.getModule().getComponent(Logger.class).error(e);
        }
    }

    @SafeVarargs
    public static void replaceInFile(Path path, Pair<String, String>... pairs) {
        String fileContent = readFile(path);
        for (Pair<String, String> pair : pairs) {
            fileContent = fileContent.replace(pair.getFirst(), pair.getSecond());
        }
        clearFile(path);
        writeFile(path, fileContent.getBytes());
    }

    public static void clearFile(Path path) {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            Cloud.getModule().getComponent(Logger.class).error(e);
        }
    }

    public static void writeFile(Path path, byte[] data) {
        try {
            path.toFile().getParentFile().mkdirs();
            if (!path.toFile().exists()) path.toFile().createNewFile();
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path.toFile()))) {
                bufferedOutputStream.write(data);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            Cloud.getModule().getComponent(Logger.class).error(e);
        }
    }

    public static Path getFile(Path path) {

        try {
            if (!Files.exists(path.toAbsolutePath().getParent())) Files.createDirectories(path.toAbsolutePath().getParent());
            if (!Files.exists(path.toAbsolutePath())) Files.createFile(path.toAbsolutePath());
        } catch (IOException e) {
            Cloud.getModule().getCloudLogger().error(e);
        }

        return path.toAbsolutePath();
    }

    public static void writeJsonFile(Path path, Object o) {
        writeFile(path, new Gson().toJson(o).getBytes());
    }

    public static Path getDir(Path path) {
        try {
            if (!Files.exists(path.toAbsolutePath())) Files.createDirectories(path.toAbsolutePath());
        } catch (IOException e) {
            Cloud.getModule().getCloudLogger().error(e);
        }

        return path.toAbsolutePath();
    }
}
