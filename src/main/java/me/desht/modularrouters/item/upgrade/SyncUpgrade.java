package me.desht.modularrouters.item.upgrade;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.block.tile.TileEntityItemRouter;
import me.desht.modularrouters.client.util.TintColor;
import me.desht.modularrouters.config.ConfigHandler;
import me.desht.modularrouters.core.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class SyncUpgrade extends ItemUpgrade {
    private static final String NBT_TUNING = "Tuning";

    public SyncUpgrade(Properties props) {
        super(props);
    }

    @Override
    public void addExtraInformation(ItemStack itemstack, List<ITextComponent> list) {
        list.add(new TranslationTextComponent("itemText.sync.tuning", getTunedValue(itemstack)));
    }

    @Override
    public void onCompiled(ItemStack stack, TileEntityItemRouter router) {
        router.setTunedSyncValue(getTunedValue(stack));
    }

    public static int getTunedValue(ItemStack stack) {
        if (!(stack.getItem() instanceof SyncUpgrade) || !stack.hasTag()) return 0;
        CompoundNBT tag = stack.getChildTag(ModularRouters.MODID);
        return tag == null ? 0 : tag.getInt(NBT_TUNING);
    }

    public static void setTunedValue(ItemStack stack, int newValue) {
        if (stack.getItem() instanceof SyncUpgrade) {
            stack.getOrCreateChildTag(ModularRouters.MODID).putInt(NBT_TUNING, newValue);
        }
    }

    @Override
    public TintColor getItemTint() {
        return new TintColor(255, 255, 192);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote && !player.isSneaking()) {
            ModularRouters.proxy.openSyncGui(stack, hand);
        } else if (player.isSneaking()) {
            if (!world.isRemote) {
                setTunedValue(stack, world.rand.nextInt(ConfigHandler.ROUTER.baseTickRate.get()));
            } else {
                player.sendStatusMessage(new TranslationTextComponent("itemText.sync.tuning", getTunedValue(stack)), true);
                player.playSound(ModSounds.SUCCESS, 1.0f, 1.5f);
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
}
