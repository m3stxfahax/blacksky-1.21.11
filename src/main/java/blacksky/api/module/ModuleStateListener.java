package blacksky.api.module;

@FunctionalInterface
public interface ModuleStateListener {
    void onChanged(Module module);
}
