package mcjty.theoneprobe.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.Collection;

public class NetworkTools {

    /// This function supports itemstacks with more then 64 items.
    public static ItemStack readItemStack(PacketBuffer buf) {
        ItemStack stack = buf.readItem();
        stack.setCount(buf.readInt());
        return stack;
    }

    /// This function supports itemstacks with more then 64 items.
    public static void writeItemStack(PacketBuffer buf, ItemStack itemStack) {
        buf.writeItemStack(itemStack, false);
        buf.writeInt(itemStack.getCount());
    }

    public static String readString(PacketBuffer dataIn) {
        int s = dataIn.readInt();
        if (s == -1) {
            return null;
        }
        if (s == 0) {
            return "";
        }
        byte[] dst = new byte[s];
        dataIn.readBytes(dst);
        return new String(dst);
    }

    public static void writeString(PacketBuffer dataOut, String str) {
        if (str == null) {
            dataOut.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes();
        dataOut.writeInt(bytes.length);
        if (bytes.length > 0) {
            dataOut.writeBytes(bytes);
        }
    }

    public static String readStringUTF8(PacketBuffer dataIn) {
        int s = dataIn.readInt();
        if (s == -1) {
            return null;
        }
        if (s == 0) {
            return "";
        }
        byte[] dst = new byte[s];
        dataIn.readBytes(dst);
        return new String(dst, java.nio.charset.StandardCharsets.UTF_8);
    }

    public static void writeStringUTF8(PacketBuffer dataOut, String str) {
        if (str == null) {
            dataOut.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        dataOut.writeInt(bytes.length);
        if (bytes.length > 0) {
            dataOut.writeBytes(bytes);
        }
    }

    public static <T extends Enum<T>> void writeEnumCollection(PacketBuffer buf, Collection<T> collection) {
        buf.writeVarInt(collection.size());
        for (T type : collection) {
            buf.writeEnum(type);
        }
    }

    public static <T extends Enum<T>> void readEnumCollection(PacketBuffer buf, Collection<T> collection, Class<T> enumClass) {
        collection.clear();
        int size = buf.readVarInt();
        for (int i = 0 ; i < size ; i++) {
            collection.add(buf.readEnum(enumClass));
        }
    }

    public static void writeFloat(PacketBuffer buf, Float f) {
        if (f != null) {
            buf.writeBoolean(true);
            buf.writeFloat(f);
        } else {
            buf.writeBoolean(false);
        }
    }

    public static Float readFloat(PacketBuffer buf) {
        if (buf.readBoolean()) {
            return buf.readFloat();
        } else {
            return null;
        }
    }
}
