package fr.elias.morecreeps.common.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe;
import io.netty.buffer.ByteBuf;

public class MountInputPacket implements IMessage {

    private int entityId;
    private float forward;
    private float strafe;
    private boolean jump;

    public MountInputPacket() {}

    public MountInputPacket(int entityId, float forward, float strafe, boolean jump) {
        this.entityId = entityId;
        this.forward = forward;
        this.strafe = strafe;
        this.jump = jump;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeFloat(this.forward);
        buf.writeFloat(this.strafe);
        buf.writeBoolean(this.jump);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.forward = buf.readFloat();
        this.strafe = buf.readFloat();
        this.jump = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<MountInputPacket, IMessage> {

        @Override
        public IMessage onMessage(final MountInputPacket message, final MessageContext ctx) {
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            WorldServer world = (WorldServer) player.worldObj;
            Entity e = world.getEntityByID(message.entityId);
            if (e instanceof CREEPSEntityRocketGiraffe && e.riddenByEntity == player) {
                CREEPSEntityRocketGiraffe g = (CREEPSEntityRocketGiraffe) e;
                g.moveForward = message.forward;
                g.moveStrafing = message.strafe;
                if (message.jump) {
                    g.doMountJump();
                }
            }
            return null;
        }
    }
}
