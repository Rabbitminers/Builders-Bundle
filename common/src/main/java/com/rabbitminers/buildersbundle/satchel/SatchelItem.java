package com.rabbitminers.buildersbundle.satchel;

import com.rabbitminers.buildersbundle.container.SatchelContainerMenu;
import com.rabbitminers.buildersbundle.container.SatchelInventory;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;


public class SatchelItem extends Item {
    private static final int slotCount = 27;
    private static final String ITEM_ACCESSOR = "Items";
    private final Block activeBlock;
    private final DyeColor colour;

    public SatchelItem(Properties properties) {
        super(properties);
        this.activeBlock = Blocks.AIR;
        this.colour = DyeColor.BROWN;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer
                && player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND)
            openGUI(serverPlayer, stack);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (level.isClientSide || player == null || player.isSpectator() || player.isShiftKeyDown())
            return InteractionResult.SUCCESS;

        if (activeBlock == null) {
            player.displayClientMessage(new TextComponent("No Item!"), false);
            return InteractionResult.FAIL;
        }

        Direction face = context.getClickedFace();
        BlockState state = activeBlock.defaultBlockState();

        level.setBlock(context.getClickedPos(), state, 3);

        return InteractionResult.CONSUME;
    }


    public static void openGUI(ServerPlayer player, ItemStack stack) {
        MenuConstructor provider = getServerMenuProvider(stack);
        MenuProvider namedProvider = new SimpleMenuProvider(provider, new TextComponent("Builder's Bag"));
        MenuRegistry.openExtendedMenu(player, namedProvider, buf -> {
            // TODO: Write items
        });
    }

    public static SatchelInventory getInventory(ItemStack stack) {
        return new SatchelInventory(stack, slotCount);
    }

    public void saveInventory(Container inventory, ItemStack backpackStack) {
        if (inventory instanceof SatchelInventory satchelInventory)
            satchelInventory.writeItemStack();
    }

    // @ExpectPlatform
    public static MenuConstructor getServerMenuProvider(ItemStack stack) {
        return (id, playerInventory, serverPlayer) -> new SatchelContainerMenu(id, playerInventory);
    }

    // @ExpectPlatform
    public static SatchelContainerMenu getClientMenu(int id, Inventory playerInventory, FriendlyByteBuf extra) {
        return new SatchelContainerMenu(id, playerInventory, extra);
    }


    @ExpectPlatform
    public static void openScreen(ServerPlayer player, MenuProvider item, InteractionHand hand) {
        throw new AssertionError();
    }

    public static int getSlotCount() {
        return slotCount;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        return super.overrideStackedOnOther(itemStack, slot, clickAction, player);
    }
}
