package me.desht.modularrouters.integration.top;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import me.desht.modularrouters.client.render.RenderHelper;
import me.desht.modularrouters.item.module.ItemModule;
import me.desht.modularrouters.util.ModuleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import org.apache.commons.lang3.Validate;

public class ElementModule implements IElement {
    private static final String ARROWS = " ▼▲◀▶▣▤";

    private final ItemStack stack;
    private final ItemModule.RelativeDirection dir;

    public ElementModule(ItemStack stack) {
        Validate.isTrue(stack.getItem() instanceof ItemModule, "provided item stack is not an ItemModule!");
        this.stack = stack;
        this.dir = ModuleHelper.getDirectionFromNBT(stack);
    }

    public ElementModule(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        this.stack = pb.readItemStack();
        this.dir = ItemModule.RelativeDirection.values()[pb.readByte()];
    }

    @Override
    public void render(int x, int y) {
        String dirStr = String.valueOf(ARROWS.charAt(dir.ordinal()));
        RenderHelper.renderItemStack(Minecraft.getInstance(), stack, x + (getWidth() - 18) / 2, y + (getHeight() - 18) / 2, dirStr);
    }

    @Override
    public int getWidth() {
        return 20;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeItemStack(stack);
        pb.writeByte(dir.ordinal());
    }

    @Override
    public int getID() {
        return TOPCompatibility.ELEMENT_MODULE_ITEM;
    }

}
