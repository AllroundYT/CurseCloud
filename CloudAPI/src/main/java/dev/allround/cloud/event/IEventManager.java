package dev.allround.cloud.event;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.util.Initializeable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class IEventManager implements Initializeable {
    protected final ArrayList<EventListener> eventListeners;

    public IEventManager() {
        this.eventListeners = new ArrayList<>();
    }

    public ArrayList<EventListener> getEventListeners() {
        return eventListeners;
    }

    public void callEvent(Event event) {
        Packet packet = new Packet(PacketType.EVENT, new Gson().toJson(event));
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(packet);
        handleEvent(event);
    }

    public void registerListener(EventListener... listeners) {
        this.eventListeners.addAll(List.of(listeners));
    }

    public void handleEvent(Event event) {
        Cloud.getModule().getCloudLogger().debug("[EventManager] Event called ->  Event: " + event.getName());


        for (EventListener listener : getEventListeners()) {

            Class<EventListener> c = (Class<EventListener>) listener.getClass();
            Method[] methods = c.getMethods();
            for (Method method : methods) {
                try {
                    EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                    if (!event.getClass().isAssignableFrom(method.getParameterTypes()[0])) continue;
                    method.invoke(listener, event);
                    Cloud.getModule().getCloudLogger().debug("[EventManager] Event Listener triggered ->  Class: " + c.getName() + " Method: " + method.getName());

                } catch (Exception e) {
                    Cloud.getModule().getCloudLogger().error(e);
                }
            }

        }

    } //only called when an event packet got received or called from callEvent

}
