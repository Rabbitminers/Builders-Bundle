package com.rabbitminers.buildersbundle.networking;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class SaveCompoundTagPacket {
    private final CompoundTag nbt;
    private final InteractionHand hand;

    public SaveCompoundTagPacket(CompoundTag nbt, InteractionHand hand) {
        this.nbt = nbt;
        this.hand = hand;
    }

    public SaveCompoundTagPacket(FriendlyByteBuf buf) {
        this.nbt = buf.readNbt();
        this.hand = buf.readEnum(InteractionHand.class);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.nbt);
        buf.writeEnum(this.hand);
    }

    public static void handle(SaveCompoundTagPacket packet, Supplier<NetworkManager.PacketContext> ctx) {
        if (ctx.get().getEnvironment() == Env.SERVER) {
            ctx.get().queue(() -> {
                ServerPlayer sender = (ServerPlayer) ctx.get().getPlayer();
                ItemStack stack = sender.getItemInHand(packet.hand);
                stack.setTag(packet.nbt);
                sender.getInventory().setChanged();
            });
        }
    }
}
