package me.desht.modularrouters.proxy;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.block.tile.TileEntityItemRouter;
import me.desht.modularrouters.container.ItemRouterContainer;
import me.desht.modularrouters.container.ModuleContainer;
import me.desht.modularrouters.gui.GuiItemRouter;
import me.desht.modularrouters.gui.GuiModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ModularRouters.GUI_MODULE_HELD_MAIN) {
            return new ModuleContainer(player, player.getHeldItem(EnumHand.MAIN_HAND));
        } else if (ID == ModularRouters.GUI_MODULE_HELD_OFF) {
            return new ModuleContainer(player, player.getHeldItem(EnumHand.OFF_HAND));
        } else if (ID == ModularRouters.GUI_MODULE_INSTALLED) {
            // player wants to configure a module already installed in an item router
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileEntityItemRouter) {
                TileEntityItemRouter router = (TileEntityItemRouter) te;
                int slotIndex = router.getConfigSlot(player);
                if (slotIndex >= 0) {
                    router.clearConfigSlot(player);
                    ItemStack installedModuleStack = ((TileEntityItemRouter) te).getModules().getStackInSlot(slotIndex);
                    return installedModuleStack == null ? null : new ModuleContainer(player, installedModuleStack, router);
                }
            }
        } else if (ID == ModularRouters.GUI_ROUTER) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            return tileEntity instanceof TileEntityItemRouter ? new ItemRouterContainer(player.inventory, (TileEntityItemRouter) tileEntity) : null;
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ModularRouters.GUI_MODULE_HELD_MAIN) {
            return new GuiModule(new ModuleContainer(player, player.getHeldItem(EnumHand.MAIN_HAND)), EnumHand.MAIN_HAND);
        } else if (ID == ModularRouters.GUI_MODULE_HELD_OFF) {
            return new GuiModule(new ModuleContainer(player, player.getHeldItem(EnumHand.OFF_HAND)), EnumHand.OFF_HAND);
        } else if (ID == ModularRouters.GUI_MODULE_INSTALLED) {
            // player wants to configure a module already installed in an item router
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileEntityItemRouter) {
                TileEntityItemRouter router = (TileEntityItemRouter) te;
                int slotIndex = router.getConfigSlot(player);
                if (slotIndex >= 0) {
                    router.clearConfigSlot(player);
                    ItemStack installedModuleStack = ((TileEntityItemRouter) te).getModules().getStackInSlot(slotIndex);
                    return new GuiModule(new ModuleContainer(player, installedModuleStack), router.getPos(), slotIndex, null);
                }
            }
        } else if (ID == ModularRouters.GUI_ROUTER) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            return tileEntity instanceof TileEntityItemRouter ? new GuiItemRouter(player.inventory, (TileEntityItemRouter) tileEntity) : null;
        }
        return null;
    }
}
