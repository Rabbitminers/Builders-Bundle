package com.rabbitminers.buildersbundle.satchel;

import com.mojang.datafixers.util.Pair;
import com.rabbitminers.buildersbundle.container.SatchelContainerMenu;
import com.rabbitminers.buildersbundle.container.SatchelInventory;
import com.rabbitminers.buildersbundle.networking.GrowItemStackPacket;
import com.rabbitminers.buildersbundle.registry.BuildersBundleItems;
import com.rabbitminers.buildersbundle.registry.BuildersBundleNetwork;
import com.rabbitminers.buildersbundle.satchel.modes.IPlacementMode;
import com.rabbitminers.buildersbundle.util.InventoryUtil;
import dev.architectury.event.EventResult;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Environment(EnvType.CLIENT)
    public static EventResult onBlockPlaced(Level world, BlockPos pos, BlockState state, Entity placer) {
        if (world.isClientSide || !(placer instanceof Player player) || player.isCreative())
            return EventResult.pass();

        Item placedItem = state.getBlock().asItem();
        Pair<ItemStack, Integer> found = InventoryUtil
                .findItemInBundles(player.getInventory(), placedItem);
        ItemStack bundleStack = found.getFirst();
        int index = found.getSecond();

        if (bundleStack.isEmpty() || index == -1)
            return EventResult.pass();

        SatchelInventory inventory = getInventory(bundleStack);
        ItemStack foundStack = inventory.getItem(index);

        InteractionHand usedHand = InventoryUtil.getHandOfItem(player, placedItem);
        if (usedHand == null)
            return EventResult.pass();

        foundStack.shrink(1);
        saveInventory(inventory, bundleStack);

        BuildersBundleNetwork.HANDLER
                .sendToServer(new GrowItemStackPacket(1, usedHand));
        player.getInventory().setChanged();

        return EventResult.pass();
    }


    @Environment(EnvType.CLIENT)
    public static void cycleSelectedBlock(Minecraft minecraft, boolean forwards) {
        Player player = minecraft.player;

        if (player == null)
            return;

        InteractionHand usedHand = InventoryUtil.getHandOfItem(player,
                BuildersBundleItems.BUILDERS_BUNDLE.get());
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
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (level.isClientSide || player == null || player.isSpectator() || player.isShiftKeyDown())
            return InteractionResult.SUCCESS;

        Direction face = context.getClickedFace();
        BlockPos setPosition = context.getClickedPos().relative(face);
        ItemStack satchel = player.getItemInHand(context.getHand());

        boolean didPlace = placeBlockFromInventory(satchel, (ServerLevel) level, setPosition,
                new BlockPlaceContext(context));
        return didPlace ? InteractionResult.CONSUME : InteractionResult.FAIL;
    }

    public boolean placeBlockFromInventory(ItemStack satchelItem, ServerLevel level, BlockPos pos,
                                                 BlockPlaceContext context) {
        SatchelInventory inventory = getInventory(satchelItem);
        if (inventory.isEmpty())
            return false;
        PlacementMode mode = getPlacementMode(satchelItem);
        ItemStack itemToPlace = mode.getStackForPlacement(inventory);
        if (!(itemToPlace.getItem() instanceof BlockItem blockItem))
            return false;
        BlockState state = blockItem.getBlock().getStateForPlacement(context);
        if (state == null || !state.canSurvive(level, pos))
            return false;
        itemToPlace.shrink(1);
        saveInventory(inventory, satchelItem);

        if (blockItem.getBlock() instanceof DoorBlock doorBlock) {
            BlockPos topPos = pos.above();
            BlockState topState = level.getBlockState(topPos);
            if (!topState.getMaterial().isReplaceable())
                return false;
            boolean couldSetBottom = level.setBlock(pos, state, 3);
            boolean couldSetTop = level.setBlock(topPos, doorBlock
                    .defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3);
            return couldSetTop || couldSetBottom;
        } else if (blockItem.getBlock() instanceof DoublePlantBlock tallFlower) {
            BlockPos topPos = pos.above();
            BlockState topState = level.getBlockState(topPos);
            if (!topState.getMaterial().isReplaceable())
                return false;
            boolean couldSetBottom = level.setBlock(pos, state, 3);
            boolean couldSetTop = level.setBlock(topPos, tallFlower
                    .defaultBlockState().setValue(TallFlowerBlock.HALF, DoubleBlockHalf.UPPER), 3);
            return couldSetTop || couldSetBottom;
        } else {
            return level.setBlock(pos, state, 3);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        PlacementMode mode = getPlacementMode(itemStack);

        components.add(new TranslatableComponent("buildersbundle.tooltips.selected")
                .append(mode.toString()).withStyle(ChatFormatting.GRAY));

        components.add(new TranslatableComponent("buildersbundle.tooltips.slot")
                .withStyle(ChatFormatting.GRAY));

        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    public static void openGUI(ServerPlayer player, ItemStack stack) {
        MenuConstructor provider = getServerMenuProvider(stack);
        MenuProvider namedProvider = new SimpleMenuProvider(provider, new TranslatableComponent("item.buildersbundle.builders_bundle"));
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

    public static PlacementMode getPlacementMode(ItemStack stack) {
        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
        return getPlacementMode(nbt);
    }

    public static PlacementMode getPlacementMode(CompoundTag nbt) {
        if (nbt == null || !nbt.contains("PlacementMode"))
            return PlacementMode.NONE;
        return PlacementMode.values()[nbt.getInt("PlacementMode")];
    }

    public static void cyclePlacementMode(ItemStack stack) {
        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
        PlacementMode newMode = getPlacementMode(nbt).cycleMode();
        if (nbt == null) return;
        nbt.putInt("PlacementMode", newMode.ordinal());
        stack.setTag(nbt);
    }


    public static MenuConstructor getServerMenuProvider(ItemStack stack) {
        return (id, playerInventory, serverPlayer) -> new SatchelContainerMenu(id, playerInventory);
    }


    public static int getSlotCount() {
        return slotCount;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        return super.overrideStackedOnOther(itemStack, slot, clickAction, player);
    }
}
