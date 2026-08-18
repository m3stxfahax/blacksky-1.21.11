package blacksky.api.events.listener;

import blacksky.api.events.Event;

@FunctionalInterface
public interface EventListener<T extends Event> {
    void invoke(T event) throws Exception;
}
