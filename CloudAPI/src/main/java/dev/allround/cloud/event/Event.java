package dev.allround.cloud.event;

public class Event {
    private final String name;
    private final Object[] data;

    public Event(String name, Object... data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Object[] getData() {
        return data;
    }
}
