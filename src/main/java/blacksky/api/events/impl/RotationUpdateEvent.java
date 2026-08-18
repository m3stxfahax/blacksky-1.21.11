package blacksky.api.events.impl;

import blacksky.api.events.Event;
import blacksky.api.events.types.EventPhase;

public final class RotationUpdateEvent implements Event {
    private final EventPhase phase;

    public RotationUpdateEvent(EventPhase phase) {
        this.phase = phase;
    }

    public EventPhase getPhase() {
        return phase;
    }

    public boolean isPre() {
        return phase == EventPhase.PRE;
    }

    public boolean isPost() {
        return phase == EventPhase.POST;
    }
}
