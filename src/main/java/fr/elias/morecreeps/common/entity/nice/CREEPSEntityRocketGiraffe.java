package fr.elias.morecreeps.common.entity.nice;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.client.particles.CREEPSFxDirt;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityRocket;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import fr.elias.morecreeps.common.port.EnumParticleTypes;

public class CREEPSEntityRocketGiraffe extends EntityCreature {

    EntityPlayer entityplayer;
    World world;
    EntityPlayerMP playermp;
    private boolean foundplayer;
    private PathEntity pathToEntity;
    protected Entity playerToAttack;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    private float distance;
    public boolean used;
    public boolean tamed;
    public int basehealth;
    public int tamedfood;
    public int attempts;
    public double dist;
    public double prevdist;
    public int facetime;
    public String basetexture;
    public int rockettime;
    public int rocketcount;
    public int galloptime;
    public float modelsize;
    public double floatcycle;
    public int floatdir;
    public double floatmaxcycle;
    private Entity targetedEntity;
    public String name;
    public String texture;
    public double moveSpeed;
    public float health;
    static final String Names[] = { "Rory", "Stan", "Clarence", "FirePower", "Lightning", "Rocket Jockey",
            "Rocket Ralph", "Tim" };

    public CREEPSEntityRocketGiraffe(World world) {
        super(world);
        this.basetexture = "morecreeps:textures/entity/rocketgiraffe.png";
        this.texture = this.basetexture;
        this.moveSpeed = 0.65F;
        this.basehealth = this.rand.nextInt(50) + 30;
        this.health = this.basehealth;
        this.hasAttacked = false;
        this.foundplayer = false;
        this.setSize(1.5F, 4F);
        this.tamedfood = this.rand.nextInt(8) + 5;
        this.rockettime = this.rand.nextInt(10) + 5;
        this.tamed = false;
        this.name = "";
        this.modelsize = 1.0F;
        this.floatdir = 1;
        this.floatcycle = 0.0D;
        this.floatmaxcycle = 0.10499999672174454D;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
        this.tasks.addTask(5, new EntityAIWander(this, 0.25D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(70);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.65f);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("BaseHealth", this.basehealth);
        nbttagcompound.setInteger("Attempts", this.attempts);
        nbttagcompound.setBoolean("Tamed", this.tamed);
        nbttagcompound.setBoolean("FoundPlayer", this.foundplayer);
        nbttagcompound.setInteger("TamedFood", this.tamedfood);
        nbttagcompound.setString("BaseTexture", this.basetexture);
        nbttagcompound.setString("Name", this.name);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.basehealth = nbttagcompound.getInteger("BaseHealth");
        this.attempts = nbttagcompound.getInteger("Attempts");
        this.tamed = nbttagcompound.getBoolean("Tamed");
        this.foundplayer = nbttagcompound.getBoolean("FoundPlayer");
        this.tamedfood = nbttagcompound.getInteger("TamedFood");
        this.basetexture = nbttagcompound.getString("BaseTexture");
        this.name = nbttagcompound.getString("Name");
        this.texture = this.basetexture;
        this.modelsize = nbttagcompound.getFloat("ModelSize");
    }

    @Override
    protected void updateAITick() {
        if (this.modelsize > 1.0F) {
            this.ignoreFrustumCheck = true;
        }

        this.moveSpeed = 0.35F;

        if (this.riddenByEntity != null && (this.riddenByEntity instanceof EntityPlayer)) {
            this.moveForward = 0.0F;
            this.moveStrafing = 0.0F;
            this.moveSpeed = 1.95F;
            this.riddenByEntity.lastTickPosY = 0.0D;
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.prevRotationPitch = this.rotationPitch = 0.0F;
            EntityPlayer entityplayer = (EntityPlayer) this.riddenByEntity;
            float f = 1.0F;

            if (entityplayer.getAIMoveSpeed() > 0.01F && entityplayer.getAIMoveSpeed() < 10F) {
                f = entityplayer.getAIMoveSpeed();
            }

            this.moveStrafing = (float) ((entityplayer.moveStrafing / f) * this.moveSpeed * 1.95F);
            this.moveForward = (float) ((entityplayer.moveForward / f) * this.moveSpeed * 1.95F);

            if (this.onGround && (this.moveStrafing != 0.0F || this.moveForward != 0.0F)) {
                this.motionY += 0.16100040078163147D;
            }

            if (this.moveStrafing != 0.0F || this.moveForward != 0.0F) {
                if (this.floatdir > 0) {
                    this.floatcycle += 0.035999998450279236D;

                    if (this.floatcycle > this.floatmaxcycle) {
                        this.floatdir = this.floatdir * -1;
                        this.fallDistance += -1.5F;
                    }
                } else {
                    this.floatcycle -= 0.017999999225139618D;

                    if (this.floatcycle < -this.floatmaxcycle) {
                        this.floatdir = this.floatdir * -1;
                        this.fallDistance += -1.5F;
                    }
                }
            }

            if (this.moveStrafing == 0.0F && this.moveForward == 0.0F) {
                this.isJumping = false;
                this.galloptime = 0;
            }

            if (this.moveForward != 0.0F && this.galloptime++ > 10) {
                this.galloptime = 0;

                if (this.handleWaterMovement()) {
                    this.worldObj.playSoundAtEntity(this, "morecreeps:giraffesplash", this.getSoundVolume(), 1.0F);
                } else {
                    this.worldObj.playSoundAtEntity(this, "morecreeps:giraffegallop", this.getSoundVolume(), 1.0F);
                }
            }

            if (this.onGround && !this.isJumping) {
                this.isJumping = Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed();

                if (this.isJumping) {
                    this.motionY += 0.38999998569488525D;
                }
            }

            if (this.onGround && this.isJumping) {
                double d = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));

                if (d > 0.13D) {
                    double d1 = 0.13D / d;
                    this.motionX = this.motionX * d1;
                    this.motionZ = this.motionZ * d1;
                }

                this.motionX *= 5.9500000000000002D;
                this.motionZ *= 5.9500000000000002D;
            }

            return;
        } else {
            super.updateEntityActionState();
            return;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i) {
        Entity entity = damagesource.getEntity();

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
            if (this.riddenByEntity == entity || this.ridingEntity == entity)
                return true;

            if (entity != this && !(entity instanceof CREEPSEntityRocket)) {
                this.setAttackTarget((EntityLivingBase) entity);
            }

            return true;
        } else
            return false;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        this.setAttackTarget(null);

        if (!(entity instanceof EntityPlayer)) {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            this.motionX = (d / f1) * 0.40000000000000002D * 0.10000000192092896D + this.motionX * 0.18000000098023225D;
            this.motionZ = (d1 / f1) * 0.40000000000000002D * 0.14000000192092896D + this.motionZ * 0.18000000098023225D;

            if (f < 2D - (2D - this.modelsize) && entity.getBoundingBox().maxY > this.getBoundingBox().minY
                    && entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                this.attackTime = 10;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
            }

            super.attackEntityAsMob(entity);
        }
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity == null)
            return;

        double d = Math.cos((this.rotationYaw * Math.PI) / 180D) * 0.20000000000000001D;
        double d1 = Math.sin((this.rotationYaw * Math.PI) / 180D) * 0.20000000000000001D;
        float f = 3.35F - (1.0F - this.modelsize) * 2.0F;

        if (this.modelsize > 1.0F) {
            f *= 1.1F;
        }

        this.riddenByEntity.setPosition(this.posX + d, (this.posY + f) - this.floatcycle, this.posZ + d1);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        this.used = false;

        if (this.tamed && entityplayer.isSneaking())
        {
            entityplayer.openGui(MoreCreepsAndWeirdos.INSTANCE, 5, this.worldObj, (int) this.posX, (int) this.posY, (int) this.posZ);
        }

        if (itemstack == null && this.tamed) {
            if (entityplayer.riddenByEntity == null && this.modelsize > 0.5F) {
                this.rotationYaw = entityplayer.rotationYaw;
                this.rotationPitch = entityplayer.rotationPitch;
                entityplayer.fallDistance = -5F;
                entityplayer.mountEntity(this);

            } else if (this.modelsize < 0.5F && this.tamed) {
                MoreCreepsAndWeirdos.proxy.addChatMessage("Your Rocket Giraffe is too small to ride!");
            } else {
                MoreCreepsAndWeirdos.proxy.addChatMessage("Unmount all creatures before riding your Rocket Giraffe.");
            }
        }

        if (itemstack != null && this.riddenByEntity == null && itemstack.getItem() == Items.cookie) {
            this.used = true;
            this.tamedfood--;
            String s = "";

            if (this.tamedfood > 1)
            {
                s = "s";
            }

            if (this.tamedfood > 0 && !this.worldObj.isRemote) {
                MoreCreepsAndWeirdos.proxy.addChatMessage(
                        (new StringBuilder()).append("You need \2476")
                        .append(String.valueOf(this.tamedfood))
                        .append(" cookie")
                        .append(String.valueOf(s))
                        .append(" \247fto tame this Rocket Giraffe.")
                        .toString());
            }

            if (itemstack.stackSize - 1 == 0) {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
            } else {
                itemstack.stackSize--;
            }

            this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:giraffechew",
                    this.getSoundVolume(),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

            if (this.worldObj.isRemote)
            {
                for (int i = 0; i < 35; i++) {
                    double d2 = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
                    double d4 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
                    CREEPSFxDirt creepsfxdirt = new CREEPSFxDirt(
                            this.worldObj,
                            this.posX + d2 * 0.40000000596046448D,
                            this.posY + 4.5D,
                            this.posZ + d4 * 0.40000000596046448D,
                            Item.getItemById(12));
                    creepsfxdirt.renderDistanceWeight = 6D;
                    // TODO: FIX PARTICLES CRASH
                    //  creepsfxdirt.setParticleTextureIndex(12);
                    Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxdirt);
                }
            }

            if (this.tamedfood < 1) {

                /**
                if (this.world.isRemote) {
                    if (!Minecraft.getMinecraft().thePlayer.getStatFileWriter()
                            .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieverocketgiraffe)) {
                        this.confetti();
                        this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
                        entityplayer.addStat(MoreCreepsAndWeirdos.achieverocketgiraffe, 1);
                    }

                }
                 **/

                /**if (!this.world.isRemote) {
                    if (!this.playermp.func_147099_x()
                            .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieverocketgiraffe)) {
                        this.confetti();
                        this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
                        this.playermp.addStat(MoreCreepsAndWeirdos.achieverocketgiraffe, 1);
                    }
                }
                 **/


                this.smoke();
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:giraffetamed",
                        1.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                this.tamed = true;

                if (this.name.length() < 1) {
                    this.name = Names[this.rand.nextInt(Names.length)];
                }

                MoreCreepsAndWeirdos.proxy.addChatMessage("");
                MoreCreepsAndWeirdos.proxy.addChatMessage(
                        (new StringBuilder()).append("\2476")
                        .append(String.valueOf(this.name))
                        .append(" \247fhas been tamed!")
                        .toString());
                this.health = this.basehealth;
                this.setHealth(this.health);
                this.basetexture = "morecreeps:textures/entity/rocketgiraffetamed.png";
                this.texture = this.basetexture;
            }
        }

        String s1 = "";

        if (this.tamedfood > 1) {
            s1 = "s";
        }

        if (!this.used && !this.tamed && !this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy.addChatMessage(
                    (new StringBuilder()).append("You need \2476")
                    .append(String.valueOf(this.tamedfood))
                    .append(" cookie")
                    .append(String.valueOf(s1))
                    .append(" \247fto tame this Rocket Giraffe.")
                    .toString());
        }

        if (itemstack != null && this.riddenByEntity != null
                && (this.riddenByEntity instanceof EntityLiving)
                && itemstack.getItem() == MoreCreepsAndWeirdos.rocket)
        {
            if (itemstack.stackSize - 1 == 0) {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
            } else
            {
                itemstack.stackSize--;
            }

            double d1 = -MathHelper.sin((entityplayer.rotationYaw * (float) Math.PI) / 180F);
            double d3 = MathHelper.cos((entityplayer.rotationYaw * (float) Math.PI) / 180F);
            double d5 = 0.0D;
            double d6 = 0.0D;
            double d7 = 0.012999999999999999D;
            double d8 = 4D;
            this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:rocketfire",
                    1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            CREEPSEntityRocket creepsentityrocket = new CREEPSEntityRocket(this.worldObj, entityplayer, 0.0F);

            if (creepsentityrocket != null) {
                this.worldObj.spawnEntityInWorld(creepsentityrocket);
            }
        }

        return true;
    }

    /**
     * Returns the Y Offset of this entity.
     */
    @Override
    public double getYOffset() {
        if (this.ridingEntity instanceof EntityPlayer)
            return this.getYOffset() - 1.1F;
        else
            return this.getYOffset();
    }

    private void smoke() {
        if (this.worldObj.isRemote)
        {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 10; j++) {
                    double d = this.rand.nextGaussian() * 0.059999999999999998D;
                    double d1 = this.rand.nextGaussian() * 0.059999999999999998D;
                    double d2 = this.rand.nextGaussian() * 0.059999999999999998D;
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.SMOKE_LARGE,
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height + i,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                }
            }
        }
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
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (1.0F - this.modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        if (this.rand.nextInt(10) == 0)
            return "morecreeps:giraffe";
        else
            return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:giraffehurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:giraffedead";
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null)
            return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.getBoundingBox().minY);
        int k = MathHelper.floor_double(this.posZ);
        if (this.worldObj == null)
            return false;
        int l = this.worldObj.getBlockLightOpacity(i, j, k);
        if (this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0 && this.worldObj.checkBlockCollision(this.getBoundingBox())
                && this.worldObj.canBlockSeeTheSky(i, j, k)
                && this.rand.nextInt(15) == 0
                && l > 8) {
            Block i1 = this.worldObj.getBlock(i, j - 1, k);

            return i1 != null && i1 != Blocks.snow
                    && i1 != Blocks.cobblestone
                    && i1 != Blocks.planks
                    && i1 != Blocks.wool;
        }
        return false;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn() {
        return !this.tamed;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource) {
        this.smoke();

        if (this.rand.nextInt(10) == 0) {
            this.dropItem(MoreCreepsAndWeirdos.rocket, this.rand.nextInt(5) + 1);
        }

        super.onDeath(damagesource);
    }

    public void confetti() {
        double d = -MathHelper.sin(((this.entityplayer).rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos(((this.entityplayer).rotationYaw * (float) Math.PI) / 180F);
        CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.world);
        if (creepsentitytrophy != null) {
            creepsentitytrophy.setLocationAndAngles(
                    (this.entityplayer).posX + d * 3D,
                    (this.entityplayer).posY - 2D,
                    (this.entityplayer).posZ + d1 * 3D,
                    (this.entityplayer).rotationYaw,
                    0.0F);
            this.world.spawnEntityInWorld(creepsentitytrophy);
        }

    }
}
