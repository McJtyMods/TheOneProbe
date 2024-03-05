package mcjty.theoneprobe.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class NetworkTools {

    /// This function supports itemstacks with more than 64 items.
    public static ItemStack readItemStack(FriendlyByteBuf buf) {
        ItemStack stack = buf.readItem();
        stack.setCount(buf.readInt());
        return stack;
    }

    /// This function supports itemstacks with more than 64 items.
    public static void writeItemStack(FriendlyByteBuf buf, ItemStack itemStack) {
        buf.writeItem(itemStack);
        buf.writeInt(itemStack.getCount());
    }

    public static <T extends Enum<T>> void writeEnumCollection(FriendlyByteBuf buf, Collection<T> collection) {
        buf.writeVarInt(collection.size());
        for (T type : collection) {
            buf.writeEnum(type);
        }
    }

    public static <T extends Enum<T>> void readEnumCollection(FriendlyByteBuf buf, Collection<T> collection, Class<T> enumClass) {
        collection.clear();
        int size = buf.readVarInt();
        for (int i = 0 ; i < size ; i++) {
            collection.add(buf.readEnum(enumClass));
        }
    }
}
