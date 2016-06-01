package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

public class NetworkTools {
    /// This function supports itemstacks with more then 64 items.
    public static ItemStack readItemStack(ByteBuf dataIn) {
        PacketBuffer buf = new PacketBuffer(dataIn);
        try {
            NBTTagCompound nbt = buf.readNBTTagCompoundFromBuffer();
            ItemStack stack = ItemStack.loadItemStackFromNBT(nbt);
            stack.stackSize = buf.readInt();
            return stack;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /// This function supports itemstacks with more then 64 items.
    public static void writeItemStack(ByteBuf dataOut, ItemStack itemStack) {
        PacketBuffer buf = new PacketBuffer(dataOut);
        NBTTagCompound nbt = new NBTTagCompound();
        itemStack.writeToNBT(nbt);
        try {
            buf.writeNBTTagCompoundToBuffer(nbt);
            buf.writeInt(itemStack.stackSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readString(ByteBuf dataIn) {
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

    public static void writeString(ByteBuf dataOut, String str) {
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

    public static String readStringUTF8(ByteBuf dataIn) {
        int s = dataIn.readInt();
        if (s == -1) {
            return null;
        }
        if (s == 0) {
            return "";
        }
        byte[] dst = new byte[s];
        dataIn.readBytes(dst);
        return new String(dst, Charset.forName("UTF-8"));
    }

    public static void writeStringUTF8(ByteBuf dataOut, String str) {
        if (str == null) {
            dataOut.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
        dataOut.writeInt(bytes.length);
        if (bytes.length > 0) {
            dataOut.writeBytes(bytes);
        }
    }

    public static BlockPos readPos(ByteBuf dataIn) {
        return new BlockPos(dataIn.readInt(), dataIn.readInt(), dataIn.readInt());
    }

    public static void writePos(ByteBuf dataOut, BlockPos pos) {
        dataOut.writeInt(pos.getX());
        dataOut.writeInt(pos.getY());
        dataOut.writeInt(pos.getZ());
    }

    public static <T extends Enum> void writeEnum(ByteBuf buf, T value, T nullValue) {
        if (value == null) {
            buf.writeInt(nullValue.ordinal());
        } else {
            buf.writeInt(value.ordinal());
        }
    }

    public static <T extends Enum> T readEnum(ByteBuf buf, T[] values) {
        return values[buf.readInt()];
    }

    public static void writeEnumCollection(ByteBuf buf, Collection<? extends Enum> collection) {
        buf.writeInt(collection.size());
        for (Enum type : collection) {
            buf.writeInt(type.ordinal());
        }
    }

    public static <T extends Enum> void readEnumCollection(ByteBuf buf, Collection<T> collection, T[] values) {
        collection.clear();
        int size = buf.readInt();
        for (int i = 0 ; i < size ; i++) {
            collection.add(values[buf.readInt()]);
        }
    }

    public static void writeFloat(ByteBuf buf, Float f) {
        if (f != null) {
            buf.writeBoolean(true);
            buf.writeFloat(f);
        } else {
            buf.writeBoolean(false);
        }
    }

    public static Float readFloat(ByteBuf buf) {
        if (buf.readBoolean()) {
            return buf.readFloat();
        } else {
            return null;
        }
    }
}
