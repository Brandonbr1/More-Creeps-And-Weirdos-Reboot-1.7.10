package fr.elias.morecreeps.common.entity.proj;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityGuineaPig;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHotdog;
import fr.elias.morecreeps.common.port.EnumParticleTypes;

public class CREEPSEntityTombstone extends EntityAnimal {

    public int interest;
    private boolean primed;
    public boolean tamed;
    public int basehealth;
    public boolean used;
    public boolean grab;
    public List<?> piglist;
    public int pigstack;
    public int level;
    public float totaldamage;
    public int alt;
    public boolean hotelbuilt;
    public int wanderstate;
    public int speedboost;
    public int totalexperience;
    public float baseSpeed;
    public int attackStrength;
    public float modelsize;
    public boolean heavenbuilt;
    public boolean angrydog;
    public int firenum;
    public int firepower;
    public int healtimer;
    public EntityLiving owner;
    public float dogsize;
    public String name;
    public int skillattack;
    public int skilldefend;
    public int skillhealing;
    public int skillspeed;
    public String deathtype;
    public String basetexture;
    public String texture;

    public CREEPSEntityTombstone(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/tombstone.png";
        this.basetexture = "";
        this.interest = 0;
        this.primed = false;
        this.tamed = false;
        this.basehealth = 0;
        this.used = false;
        this.grab = false;
        this.pigstack = 0;
        this.level = 0;
        this.totaldamage = 0.0F;
        this.hotelbuilt = false;
        this.wanderstate = 0;
        this.speedboost = 0;
        this.totalexperience = 0;
        this.baseSpeed = 0.0F;
        this.modelsize = 1.0F;
        this.heavenbuilt = false;
        this.angrydog = false;
        this.firenum = 0;
        this.firepower = 0;
        this.healtimer = 0;
        this.owner = null;
        this.attackStrength = 0;
        this.dogsize = 0.0F;
        this.name = "";
        this.skillattack = 0;
        this.skilldefend = 0;
        this.skillhealing = 0;
        this.skillspeed = 0;
        this.deathtype = "";
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(100D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.0D);
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    @Override
    public EntityAnimal createChild(EntityAgeable entityanimal) {
        return new CREEPSEntityTombstone(this.worldObj);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        this.used = false;

        if (itemstack == null) {
            entityplayer.addChatMessage(
                    new ChatComponentText("Use a \2474LifeGem\247f on this tombstone to bring your pet back to life!"));
            return false;
        }

        if (itemstack != null && itemstack.getItem() != MoreCreepsAndWeirdos.lifegem) {
            entityplayer.addChatMessage(
                    new ChatComponentText("Use a \2474LifeGem\247f on this tombstone to bring your pet back to life!"));
            return false;
        }

        if (itemstack != null && itemstack.getItem() == MoreCreepsAndWeirdos.lifegem) {
            itemstack.stackSize--;
            entityplayer.swingItem();

            if (itemstack.stackSize < 1) {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                itemstack.stackSize = 0;
            }

            this.smoke();

            if (this.deathtype.equals("GuineaPig")) {
                CREEPSEntityGuineaPig creepsentityguineapig = new CREEPSEntityGuineaPig(this.worldObj);
                creepsentityguineapig.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                creepsentityguineapig.interest = this.interest;
                creepsentityguineapig.tamed = this.tamed;
                creepsentityguineapig.name = this.name;
                creepsentityguineapig.basehealth = this.basehealth;
                creepsentityguineapig.level = this.level;
                creepsentityguineapig.basetexture = this.basetexture;
                creepsentityguineapig.totaldamage = this.totaldamage;
                creepsentityguineapig.hotelbuilt = this.hotelbuilt;
                creepsentityguineapig.attackStrength = this.attackStrength;
                creepsentityguineapig.wanderstate = this.wanderstate;
                creepsentityguineapig.speedboost = this.speedboost;
                creepsentityguineapig.totalexperience = this.totalexperience;
                creepsentityguineapig.baseSpeed = this.baseSpeed;
                creepsentityguineapig.health = 5;
                creepsentityguineapig.modelsize = this.modelsize;
                creepsentityguineapig.skillattack = this.skillattack;
                creepsentityguineapig.skilldefend = this.skilldefend;
                creepsentityguineapig.skillhealing = this.skillhealing;
                creepsentityguineapig.skillspeed = this.skillspeed;
                creepsentityguineapig.texture = this.basetexture;

                if (this.wanderstate == 1) {
                    creepsentityguineapig.moveSpeed = 0.0F;
                } else {
                    creepsentityguineapig.moveSpeed = this.speedboost <= 0 ? this.baseSpeed : this.baseSpeed + 0.5F;
                }

                this.worldObj.spawnEntityInWorld(creepsentityguineapig);
                this.setDead();
            }

            if (this.deathtype.equals("Hotdog")) {
                CREEPSEntityHotdog creepsentityhotdog = new CREEPSEntityHotdog(this.worldObj);
                creepsentityhotdog.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                creepsentityhotdog.interest = this.interest;
                creepsentityhotdog.tamed = this.tamed;
                creepsentityhotdog.name = this.name;
                creepsentityhotdog.basehealth = this.basehealth;
                creepsentityhotdog.level = this.level;
                creepsentityhotdog.basetexture = this.basetexture;
                creepsentityhotdog.totaldamage = this.totaldamage;
                creepsentityhotdog.heavenbuilt = this.heavenbuilt;
                creepsentityhotdog.attackStrength = this.attackStrength;
                creepsentityhotdog.wanderstate = this.wanderstate;
                creepsentityhotdog.speedboost = this.speedboost;
                creepsentityhotdog.totalexperience = this.totalexperience;
                creepsentityhotdog.baseSpeed = this.baseSpeed;
                creepsentityhotdog.skillattack = this.skillattack;
                creepsentityhotdog.skilldefend = this.skilldefend;
                creepsentityhotdog.skillhealing = this.skillhealing;
                creepsentityhotdog.skillspeed = this.skillspeed;
                creepsentityhotdog.firepower = this.firepower;
                creepsentityhotdog.dogsize = this.dogsize;
                creepsentityhotdog.health = 5;
                creepsentityhotdog.texture = this.basetexture;

                if (this.wanderstate == 1) {
                    creepsentityhotdog.moveSpeed = 0.0F;
                } else {
                    creepsentityhotdog.moveSpeed = this.speedboost <= 0 ? this.baseSpeed : this.baseSpeed + 0.5F;
                }

                if (!this.worldObj.isRemote) {
                    this.worldObj.spawnEntityInWorld(creepsentityhotdog);
                }

                this.setDead();
            }
        }

        return true;
    }

    private void smoke() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 30; j++) {
                double d = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d4 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(
                        EnumParticleTypes.HEART,
                        (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        this.posY + 0.5D + this.rand.nextFloat() * this.height,
                        (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        d,
                        d2,
                        d4);
            }

            for (int k = 0; k < 4; k++) {
                for (int l = 0; l < 10; l++) {
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d3 = this.rand.nextGaussian() * 0.02D;
                    double d5 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height + k,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d1,
                            d3,
                            d5);
                }
            }
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {}

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        if (this.rand.nextInt(10) == 0)
            return "morecreeps:tombstone";
        else
            return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return null;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setString("DeathType", this.deathtype);
        nbttagcompound.setInteger("Interest", this.interest);
        nbttagcompound.setBoolean("Tamed", this.tamed);
        nbttagcompound.setString("Name", this.name);
        nbttagcompound.setInteger("BaseHealth", this.basehealth);
        nbttagcompound.setInteger("Level", this.level);
        nbttagcompound.setString("BaseTexture", this.basetexture);
        nbttagcompound.setFloat("TotalDamage", this.totaldamage);
        nbttagcompound.setBoolean("heavenbuilt", this.heavenbuilt);
        nbttagcompound.setBoolean("hotelbuilt", this.hotelbuilt);
        nbttagcompound.setInteger("AttackStrength", this.attackStrength);
        nbttagcompound.setInteger("WanderState", this.wanderstate);
        nbttagcompound.setInteger("SpeedBoost", this.speedboost);
        nbttagcompound.setInteger("TotalExperience", this.totalexperience);
        nbttagcompound.setFloat("BaseSpeed", this.baseSpeed);
        nbttagcompound.setInteger("SkillAttack", this.skillattack);
        nbttagcompound.setInteger("SkillDefense", this.skilldefend);
        nbttagcompound.setInteger("SkillHealing", this.skillhealing);
        nbttagcompound.setInteger("SkillSpeed", this.skillspeed);
        nbttagcompound.setInteger("FirePower", this.firepower);
        nbttagcompound.setFloat("DogSize", this.dogsize);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.deathtype = nbttagcompound.getString("DeathType");
        this.interest = nbttagcompound.getInteger("Interest");
        this.tamed = nbttagcompound.getBoolean("Tamed");
        this.name = nbttagcompound.getString("Name");
        this.basetexture = nbttagcompound.getString("BaseTexture");
        this.basehealth = nbttagcompound.getInteger("BaseHealth");
        this.level = nbttagcompound.getInteger("Level");
        this.totaldamage = nbttagcompound.getFloat("TotalDamage");
        this.heavenbuilt = nbttagcompound.getBoolean("heavenbuilt");
        this.hotelbuilt = nbttagcompound.getBoolean("hotelbuilt");
        this.attackStrength = nbttagcompound.getInteger("AttackStrength");
        this.wanderstate = nbttagcompound.getInteger("WanderState");
        this.speedboost = nbttagcompound.getInteger("SpeedBoost");
        this.totalexperience = nbttagcompound.getInteger("TotalExperience");
        this.baseSpeed = nbttagcompound.getFloat("BaseSpeed");
        this.skillattack = nbttagcompound.getInteger("SkillAttack");
        this.skilldefend = nbttagcompound.getInteger("SkillDefense");
        this.skillhealing = nbttagcompound.getInteger("SkillHealing");
        this.skillspeed = nbttagcompound.getInteger("SkillSpeed");
        this.firepower = nbttagcompound.getInteger("FirePower");
        this.dogsize = nbttagcompound.getFloat("DogSize");
        this.modelsize = nbttagcompound.getFloat("ModelSize");
    }

    public void onDeath(Entity entity) {}

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn() {
        return false;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }
}
