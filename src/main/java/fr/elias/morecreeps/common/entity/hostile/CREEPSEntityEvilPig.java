package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityEvilPig extends EntityMob {
  /** Public so other projectiles (shrink ray) can adjust it like before. */
  public float modelsize = 1.0F;

  public CREEPSEntityEvilPig(World world) {
    super(world);
    this.setSize(0.9F, 0.9F);

    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false));
    this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(5, new EntityAILookIdle(this));

    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.28D);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
  }

  private static float wrapTo180(float a) {
    while (a <= -180F) a += 360F;
    while (a > 180F) a -= 360F;
    return a;
  }
  private static float approachAngle(float current, float target, float maxStep) {
    float diff = wrapTo180(target - current);
    if (diff >  maxStep) diff =  maxStep;
    if (diff < -maxStep) diff = -maxStep;
    return current + diff;
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    float desiredPitch = 0.0F;
    EntityLivingBase tgt = this.getAttackTarget();
    if (tgt != null && this.riddenByEntity == null && this.canEntityBeSeen(tgt)) {
      double dy = tgt.posY + tgt.getEyeHeight() - (this.posY + this.getEyeHeight());
      double dx = tgt.posX - this.posX;
      double dz = tgt.posZ - this.posZ;
      double horiz = MathHelper.sqrt_double(dx * dx + dz * dz);
      float look = (float)(-Math.atan2(dy, horiz) * 180.0D / Math.PI);
      desiredPitch = MathHelper.clamp_float(look, -12.0F, 12.0F);
    }

    float next = approachAngle(this.rotationPitch, desiredPitch, 4.0F);
    this.rotationPitch     = next;
    this.prevRotationPitch = next;  // removing interpolation jitter
  }

  @Override
  public boolean attackEntityAsMob(Entity entity) {
    return super.attackEntityAsMob(entity);
  }

  @Override protected String getLivingSound() { return "mob.pig.say"; }
  @Override protected String getHurtSound()   { return "mob.pig.say"; }
  @Override protected String getDeathSound()  { return "mob.pig.death"; }
}
