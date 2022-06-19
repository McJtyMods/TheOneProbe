package mcjty.theoneprobe.items;

// @todo temporary interface to use while enum configs are broken in Forge
public interface IEnumConfig<T extends Enum<T>> {
    T get();
    T getDefault();
}
