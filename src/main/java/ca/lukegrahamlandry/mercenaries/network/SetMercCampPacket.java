package ca.lukegrahamlandry.mercenaries.network;

import ca.lukegrahamlandry.mercenaries.entity.MercenaryEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// server -> client
public class SetMercCampPacket {
    private final int entityId;

    public SetMercCampPacket(int entityId) {
        this.entityId = entityId;
    }

    public SetMercCampPacket(PacketBuffer buf) {
        this(buf.readInt());
    }

    public static void toBytes(SetMercCampPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
    }

    public static void handle(SetMercCampPacket msg, Supplier<NetworkEvent.Context> context) {
        PlayerEntity player = context.get().getSender();
        context.get().enqueueWork(() -> {
            Entity entity = player.level.getEntity(msg.entityId);
            if (entity instanceof MercenaryEntity) {
                MercenaryEntity merc = (MercenaryEntity) entity;
                merc.setCamp(merc.blockPosition());
                merc.campDimension = merc.level.dimension();
            }
        });
        context.get().setPacketHandled(true);
    }
}

