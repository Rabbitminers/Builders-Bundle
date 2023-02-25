package com.rabbitminers.buildersbundle.satchel;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import com.rabbitminers.buildersbundle.container.SatchelContainerMenu;
import com.rabbitminers.buildersbundle.container.SatchelInventory;
import com.rabbitminers.buildersbundle.networking.GrowItemStackPacket;
import com.rabbitminers.buildersbundle.networking.SaveCompoundTagPacket;
import com.rabbitminers.buildersbundle.registry.BuildersBundleNetwork;
import com.rabbitminers.buildersbundle.util.InventoryUtil;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
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
import java.util.List;
import java.util.Random;


public class SatchelItem extends Item {
    private static final int slotCount = 27;
    private static final String ITEM_ACCESSOR = "Items";
    private final ItemStack activeBlock;
    private final DyeColor colour;

    public SatchelItem(Properties properties) {
        super(properties);
        this.activeBlock = ItemStack.EMPTY;
        this.colour = DyeColor.BROWN;
    }


    public static EventResult onBlockPlaced(Level world, BlockPos pos, BlockState state, Entity placer) {
        if (world.isClientSide || !(placer instanceof Player player))
            return EventResult.pass();
        Inventory playerInventory = player.getInventory();

        int index = InventoryUtil.getFirstInventoryIndex(playerInventory, ArchitectsSatchel.EXAMPLE_ITEM.get());
        if (index == -1)
            return EventResult.pass();

        ItemStack bundle = playerInventory.getItem(index);
        SatchelInventory satchelInventory = getInventory(bundle);

        Item placedBlockItem = state.getBlock().asItem();

        int satchelItemIndex = InventoryUtil.getFirstInventoryIndex(satchelInventory, placedBlockItem);
        if (satchelItemIndex == -1)
            return EventResult.pass();
        ItemStack stackInSatchel = satchelInventory.getItem(satchelItemIndex);

        BuildersBundleNetwork.HANDLER
                .sendToServer(new GrowItemStackPacket(1, InteractionHand.MAIN_HAND));
        playerInventory.setChanged();

        stackInSatchel.shrink(1);
        SatchelItem.saveInventory(satchelInventory, bundle);

        return EventResult.pass();
    }
    public static void cycleSelectedBlock(Minecraft minecraft, boolean forwards) {
        Player player = minecraft.player;

        if (player == null)
            return;

        InteractionHand usedHand = InventoryUtil.getHandOfBundle(player,
                ArchitectsSatchel.EXAMPLE_ITEM.get());
        if (usedHand == null)
            return;

        ItemStack bundleItem = player.getItemInHand(usedHand);
        SatchelInventory bundleInventory = getInventory(bundleItem);

        int currentSlot = bundleInventory.getSelectedSlot();
        ItemStack currentItem = bundleInventory.getItem(currentSlot);

        int start, end, step;
        if (forwards) {
            start = currentSlot + 1;
            end = bundleInventory.getContainerSize() + currentSlot;
            step = 1;
        } else {
            start = currentSlot - 1;
            end = currentSlot - bundleInventory.getContainerSize();
            step = -1;
        }

        for (int i = start; i != end; i += step) {
            int index = (i + bundleInventory.getContainerSize()) % bundleInventory.getContainerSize();
            ItemStack itemStack = bundleInventory.getItem(index);
            if (itemStack.isEmpty()) {
                continue;
            }
            if (itemStack.getItem() != currentItem.getItem()) {
                currentSlot = index;
                break;
            }
        }

        bundleInventory.setSelectedSlotClient(currentSlot, usedHand, bundleItem);
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

        Direction face = context.getClickedFace();
        BlockPos setPosition = context.getClickedPos().relative(face);
        ItemStack satchel = player.getItemInHand(context.getHand());

        placeRandomBlockFromInventory(satchel, (ServerLevel) level, setPosition);

        return InteractionResult.CONSUME;
    }

    public boolean placeRandomBlockFromInventory(ItemStack satchelItem, ServerLevel level, BlockPos pos) {
        SatchelInventory inventory = getInventory(satchelItem);
        List<ItemStack> items = inventory.getAllItems();
        if (items.isEmpty())
            return false;
        Random random = new Random();
        int randomIndex = random.nextInt(items.size());
        ItemStack randomItem = items.get(randomIndex);
        if (!(randomItem.getItem() instanceof BlockItem blockItem))
            return false;
        BlockState state = blockItem.getBlock().defaultBlockState();
        randomItem.shrink(1);
        saveInventory(inventory, satchelItem);
        return level.setBlock(pos, state, 3);
    }


    public static EventResult cycleActiveBlock(Minecraft client, double ammount) {
        Player player = client.player;
        if (player == null) return EventResult.pass();

        InteractionHand usedHand;
        usedHand = player.getOffhandItem().getItem() == ArchitectsSatchel.EXAMPLE_ITEM.get()
                ? InteractionHand.OFF_HAND
                : player.getMainHandItem().getItem() == ArchitectsSatchel.EXAMPLE_ITEM.get()
                ? InteractionHand.MAIN_HAND : null;

        if (usedHand == null) return EventResult.pass();

        ItemStack bundleStack = player.getItemInHand(usedHand);
        SatchelInventory bundleInventory =
                SatchelItem.getInventory(bundleStack);

        ItemStack oldSelectedItem = bundleInventory.getSelectedItem();

        for(int i = bundleInventory.getSelectedSlot(); i < bundleInventory.getSelectedSlot(); i++) {
            ItemStack currentStack = bundleInventory.getItem(i);
            if (currentStack != ItemStack.EMPTY && currentStack.getItem() != oldSelectedItem.getItem()) {
                bundleInventory.setSelectedSlot(i);
                System.out.println("Set Slot As " + currentStack);
                break;
            }
        }

        return EventResult.interruptTrue();
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

    public static void saveInventory(Container inventory, ItemStack stack) {
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
