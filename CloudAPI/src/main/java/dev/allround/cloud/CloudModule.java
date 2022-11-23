package dev.allround.cloud;
import dev.allround.cloud.log.Logger;
import dev.allround.cloud.util.Initializeable;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public interface CloudModule extends Initializeable, Startable, Stopable {

    List<Object> getComponents();

    ModuleInfo getModuleInfo();

    default void registerComponent(Object o) {
        getComponents().add(o);
    }

    @SuppressWarnings("unchecked")
    default <T> T getComponent(Class<T> clazz) {
        Optional<T> optional = (Optional<T>) getComponents().stream().filter(component -> clazz.isAssignableFrom(component.getClass())).findFirst();
        try {
            return optional.orElseThrow(() -> new Exception("No component found of type " + clazz.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default Logger getCloudLogger() {
        return getComponent(Logger.class);
    }

    default ExecutorService getCachedThreadPool() {
        return getComponent(ExecutorService.class);
    }


    default ScheduledExecutorService getScheduledExecutorService() {
        return getComponent(ScheduledExecutorService.class);
    }
}
