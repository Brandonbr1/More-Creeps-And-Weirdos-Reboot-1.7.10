package fr.elias.morecreeps.common.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityTameable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class TameableNamePacket implements IMessage {

  private int entityId;
  private String newName;

  public TameableNamePacket() {}

  public TameableNamePacket(int entityId, String newName) {
    this.entityId = entityId;
    this.newName = newName;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.entityId = buf.readInt();
    int nameLength = buf.readInt();
    if (nameLength > 0) {
      byte[] nameBytes = new byte[nameLength];
      buf.readBytes(nameBytes);
      this.newName = new String(nameBytes);
    } else {
      this.newName = "";
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.entityId);
    if (this.newName != null && this.newName.length() > 0) {
      byte[] nameBytes = this.newName.getBytes();
      buf.writeInt(nameBytes.length);
      buf.writeBytes(nameBytes);
    } else {
      buf.writeInt(0);
    }
  }

  public static class Handler implements IMessageHandler<TameableNamePacket, IMessage> {

    @Override
    public IMessage onMessage(final TameableNamePacket message, final MessageContext ctx) {
      Entity entity = ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityId);
      if (entity instanceof CREEPSEntityTameable) {
        // Only allow the owner to change the name
        CREEPSEntityTameable tameable = (CREEPSEntityTameable) entity;
        if (tameable.getIsTamed()
            && tameable.getOwnerUUID() != null
            && ctx.getServerHandler()
                .playerEntity
                .getUniqueID()
                .toString()
                .equals(tameable.getOwnerUUID())) {
          tameable.setTamedName(message.newName);
        }
      }
      return null;
    }
  }
}
