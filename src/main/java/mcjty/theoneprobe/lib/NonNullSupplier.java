package mcjty.theoneprobe.lib;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface NonNullSupplier<T> {

    @Nonnull T get();
}