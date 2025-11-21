package fr.elias.morecreeps.common.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class CREEPSGrowableEntity extends EntityLiving {

	public CREEPSGrowableEntity(World p_i1595_1_) {
		super(p_i1595_1_);
		// TODO Auto-generated constructor stub
	}
	
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte) 1));
	}
	
    public int getSize()
    {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

}
