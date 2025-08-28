package fr.elias.morecreeps.common.entity.netural;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;

public class CREEPSEntityRockMonster extends EntityMob {

    private static final Item dropItems[];
    protected double attackRange;
    private int angerLevel;
    public float modelsize;
    public String texture;

    public CREEPSEntityRockMonster(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/rockmonster.png";
        this.angerLevel = 0;
        this.attackRange = 16D;
        this.setSize(this.width * 3.25F, this.height * 3.25F);
        this.height = 4F;
        this.width = 3F;
        this.modelsize = 3F;
        this.getNavigator()
        .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new AIAttackEntity());
        this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.35D));
        this.tasks.addTask(5, new EntityAIWander(this, 0.35D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 16F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(60D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(5D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(this.getAttackTarget() != null ? 0.6D : 0.35D);
        super.onUpdate();

        if (this.motionY > 0.0D) {
            this.motionY -= 0.00033000000985339284D;
        }
        if (this.angerLevel > 0) {
            this.angerLevel--;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        double d = entity.posX - this.posX;
        double d1 = entity.posZ - this.posZ;
        float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
        this.motionX = (d / f1) * 0.5D * 0.30000000192092896D + this.motionX * 0.38000000098023223D;
        this.motionZ = (d1 / f1) * 0.5D * 0.17000000192092896D + this.motionZ * 0.38000000098023223D;
    }

    public class AIAttackEntity extends EntityAIBase {

        public CREEPSEntityRockMonster rockM = CREEPSEntityRockMonster.this;
        public int attackTime;

        public AIAttackEntity() {}

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.rockM.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && CREEPSEntityRockMonster.this.angerLevel > 0;
        }

        @Override
        public void updateTask() {
            try {
                --this.attackTime;
                EntityLivingBase entitylivingbase = this.rockM.getAttackTarget();
                double d0 = this.rockM.getDistanceSqToEntity(entitylivingbase);

                if (d0 < 4.0D) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 10;
                        entitylivingbase.motionX = CREEPSEntityRockMonster.this.motionX * 3D;
                        entitylivingbase.motionY = CREEPSEntityRockMonster.this.rand.nextFloat() * 2.533F;
                        entitylivingbase.motionZ = CREEPSEntityRockMonster.this.motionZ * 3D;
                        this.rockM.attackEntityAsMob(entitylivingbase);// or entitylivingbase.attackEntityFrom
                        // blablabla...
                    }

                    this.rockM.getMoveHelper()
                    .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
                } else if (d0 < 256.0D) {
                    // ATTACK ENTITY JUST CALLED HERE :D
                    this.rockM.attackEntity(entitylivingbase, (float) d0);
                    this.rockM.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
                } else {
                    this.rockM.getNavigator()
                    .clearPathEntity();
                    this.rockM.getMoveHelper()
                    .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.modelsize = nbttagcompound.getFloat("ModelSize");
    }

    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null)
            return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        int l = this.worldObj.getBlockLightOpacity(i, j, k);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);
        return i1 != Blocks.cobblestone && i1 != Blocks.log
                && i1 != Blocks.double_stone_slab
                && i1 != Blocks.stone_slab
                && i1 != Blocks.planks
                && i1 != Blocks.wool
                && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
                && this.worldObj.canBlockSeeTheSky(i, j, k)
                && this.rand.nextInt(15) == 0
                && l > 8;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.isEntityInvulnerable())
            return false;
        else {
            Entity entity = p_70097_1_.getEntity();

            if (entity instanceof EntityPlayer) {
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));

                for (int i = 0; i < list.size(); ++i) {
                    Entity entity1 = (Entity)list.get(i);

                    if (entity1 instanceof EntityPigZombie) {
                        CREEPSEntityRockMonster entityrockmonster = (CREEPSEntityRockMonster)entity1;
                        entityrockmonster.becomeAngryAt(entity);
                    }
                }

                this.becomeAngryAt(entity);
            }

            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
    }

    /**

    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        if (worldObj == null) {
            return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        }
        if (entity instanceof EntityPlayer) {
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(32D, 32D, 32D));
            for (Object o : list) {
                if (o instanceof Entity) {
                    Entity entity1 = (Entity) o;
                    if (entity1 instanceof CREEPSEntityRockMonster) {
                        CREEPSEntityRockMonster creepsentityrockmonster = (CREEPSEntityRockMonster) entity1;
                        creepsentityrockmonster.becomeAngryAt(entity);
                    }
                }
            }
            becomeAngryAt(entity);
        }
        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }
     **/

    private void becomeAngryAt(Entity entity) {
        this.setAttackTarget((EntityLivingBase) entity);
        this.angerLevel = 400 + this.rand.nextInt(400);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "morecreeps:rockmonster";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:rockmonsterhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:rockmonsterdeath";
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playLivingSound() {
        String s = this.getLivingSound();

        if (s != null) {
            this.worldObj.playSoundAtEntity(
                    this,
                    s,
                    this.getSoundVolume(),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (3F - this.modelsize));
        }
    }

    @Override
    public void onDeath(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();

        if ((entity instanceof EntityPlayer) && !((EntityPlayerMP) entity).func_147099_x()
                .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieverockmonster)) {
            this.worldObj.playSoundAtEntity(entity, "morecreeps:achievement", 1.0F, 1.0F);
            ((EntityPlayer) entity).addStat(MoreCreepsAndWeirdos.achieverockmonster, 1);
        }

        super.onDeath(damagesource);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    @Override
    public Item getDropItem() {
        return dropItems[this.rand.nextInt(dropItems.length)];
    }

    static {
        dropItems = (new Item[] { Item.getItemFromBlock(Blocks.cobblestone), Item.getItemFromBlock(Blocks.gravel),
                Item.getItemFromBlock(Blocks.cobblestone), Item.getItemFromBlock(Blocks.gravel),
                Item.getItemFromBlock(Blocks.iron_ore), Item.getItemFromBlock(Blocks.mossy_cobblestone) });
    }
}
