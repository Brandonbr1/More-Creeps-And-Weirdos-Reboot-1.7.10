package fr.elias.morecreeps.common.packets;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class SyncPlayerDataPacket implements IMessage
{

    public SyncPlayerDataPacket()
    {

    }

    public int currentFine;
    //  public SyncPlayerDataPacket(CREEPSExtendedPlayerProps props)
    //  {

    //  }

    @Override
    public void fromBytes(ByteBuf buf)
    {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<SyncPlayerDataPacket, IMessage>
    {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(SyncPlayerDataPacket message, MessageContext ctx)
        {

            return null;
        }
    }

}
