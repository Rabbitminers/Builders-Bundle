package com.rabbitminers.buildersbundle.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface RegSupplier<T> extends Supplier<T> {
    @Override
    T get();

    ResourceLocation getId();

    Holder<T> getHolder();
}
