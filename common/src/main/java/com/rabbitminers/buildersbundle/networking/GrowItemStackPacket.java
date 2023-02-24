package com.rabbitminers.buildersbundle.networking;

import com.rabbitminers.buildersbundle.registry.BuildersBundleNetwork;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.utils.Env;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class GrowItemStackPacket {
    private final int amount;
    private final InteractionHand hand; // ItemStack doesn't hold its inventory

    public GrowItemStackPacket(int amount, InteractionHand hand) {
        this.amount = amount;
        this.hand = hand;
    }

    public GrowItemStackPacket(FriendlyByteBuf buf) {
        this.amount = buf.readInt();
        this.hand = buf.readEnum(InteractionHand.class);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.amount);
        buf.writeEnum(this.hand);
    }

    public static void handle(GrowItemStackPacket packet, Supplier<NetworkManager.PacketContext> ctx) {
        if (ctx.get().getEnvironment() == Env.SERVER) {
            ctx.get().queue(() -> {
                ServerPlayer sender = (ServerPlayer) ctx.get().getPlayer();
                ItemStack stack = sender.getItemInHand(packet.hand);
                stack.grow(packet.amount);
            });
        }
    }
}
