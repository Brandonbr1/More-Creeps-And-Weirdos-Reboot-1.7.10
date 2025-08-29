package fr.elias.morecreeps.common.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

public class SetGiraffeNamePacket implements IMessage {
  private int entityId;
  private String name;

  public SetGiraffeNamePacket() {}

  public SetGiraffeNamePacket(int entityId, String name) {
    this.entityId = entityId;
    this.name = name;
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.entityId);
    ByteBufUtils.writeUTF8String(buf, this.name == null ? "" : this.name);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.entityId = buf.readInt();
    this.name = ByteBufUtils.readUTF8String(buf);
  }

  public static class Handler implements IMessageHandler<SetGiraffeNamePacket, IMessage> {
    @Override
    public IMessage onMessage(final SetGiraffeNamePacket message, final MessageContext ctx) {
      final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
      final WorldServer world = (WorldServer) player.worldObj;

      Entity e = world.getEntityByID(message.entityId);
      if (e instanceof CREEPSEntityRocketGiraffe) {
        CREEPSEntityRocketGiraffe g = (CREEPSEntityRocketGiraffe) e;
        g.name = message.name != null ? message.name : "";
      }
      return null;
    }
  }
}
