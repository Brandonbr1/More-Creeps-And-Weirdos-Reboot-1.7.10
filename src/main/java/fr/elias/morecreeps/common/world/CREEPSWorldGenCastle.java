package fr.elias.morecreeps.common.world;

import java.util.Map;
import java.util.Random;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.CREEPSEntityCastleKing;
import net.minecraft.block.Block;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class CREEPSWorldGenCastle extends WorldGenerator
{
    public static Map stringToClassMapping = EntityList.stringToClassMapping;
    public static Random rand = new Random();
    public int floor;
    private int topFloor;
    private int castleHeight;
    private boolean alternate;
    public boolean chunky;

    public CREEPSWorldGenCastle()
    {
    }

    
    
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        boolean flag = false;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        castleHeight = 5;
        alternate = true;
        boolean flag1 = false;
        char c = 300;
        int k1 = (i + random.nextInt(8)) - random.nextInt(8);
        int l1 = 200;
        int i2 = (k + random.nextInt(8)) - random.nextInt(8);
        l = k1;
        i1 = l1;
        j1 = i2;
        int j2 = 0;
        chunky = false;
        
        //TODO get the closest player (hope it will not overload the server...) -elias
        EntityPlayer player = world.getClosestPlayer(i, j, k, 64D); //get player far from 64 blocks

        if (player == null)
        {
            return false;
        }

        if (Math.abs((double)k1 - player.posX) < 10D || Math.abs((double)i2 - player.posZ) < 10D)
        {
            return false;
        }

        int k2 = 0;
        int l2 = 0;
        boolean flag2 = false;

        while (k2 < 3)
        {
            k2 = 0;
            Block i3 = world.getBlock(l - 4, i1 - l2, j1 - 4);
            Block k3 = world.getBlock(l + 30, i1 - l2, j1 - 4);
            Block i4 = world.getBlock(l - 4, i1 - l2, j1 + 30);
            Block k4 = world.getBlock(l + 30, i1 - l2, j1 + 30);

            if (i3 != Blocks.air && i3 != Blocks.leaves && i3 != Blocks.water && i3 != Blocks.flowing_water)
            {
                k2++;
            }

            if (k3 != Blocks.air && k3 != Blocks.leaves && k3 != Blocks.water && k3 != Blocks.flowing_water)
            {
                k2++;
            }

            if (i4 != Blocks.air && i4 != Blocks.leaves && i4 != Blocks.water && i4 != Blocks.flowing_water)
            {
                k2++;
            }

            if (k4 != Blocks.air && k4 != Blocks.leaves && k4 != Blocks.water && k4 != Blocks.flowing_water)
            {
                k2++;
            }

            if (i3 == Blocks.water || i3 == Blocks.flowing_water)
            {
                flag2 = true;
            }

            if (k3 == Blocks.water || k3 == Blocks.flowing_water)
            {
                flag2 = true;
            }

            if (i4 == Blocks.water || i4 == Blocks.flowing_water)
            {
                flag2 = true;
            }

            if (k4 == Blocks.water || k4 == Blocks.flowing_water)
            {
                flag2 = true;
            }

            l2++;
        }

        i1 = l1 = (i1 - l2) + 2;

        if (world.blockExists(k1 - 4, l1, i2 - 4) && world.blockExists(k1 + 30, l1, i2 - 4) && world.blockExists(k1 + 30, l1, i2 + 30) && world.blockExists(k1 - 4, l1, i2 + 30))
        {
            chunky = true;
        }

        if (!chunky)
        {
            return false;
        }

        int j3 = 0;
        label0:

        do
        {
            if (j3 >= castleHeight * 7 + 7)
            {
                break;
            }

            for (int l3 = -12 + j3; l3 < 38; l3 += 2)
            {
                for (int j4 = -12 + j3; j4 < 38; j4 += 2)
                {
                    Block l4 = world.getBlock(i + l3, i1 + j3, k + j4);
                    //TODO check if "2" blocks is a good idea
                    if (l4 != Blocks.air && l4 != Blocks.leaves && l4 != Blocks.leaves2 && l4 != Blocks.log && l4 != Blocks.log2)
                    {
                        j2++;
                    }

                    if (j2 > c)
                    {
                        break label0;
                    }
                }
            }

            j3 += 2;
        }
        while (true);

        if (j2 > c)
        {
            flag2 = true;
        }

        if (flag2)
        {
            return false;
        }

        j3 = 0;
        boolean flag3 = false;
        boolean flag4 = false;
        int i5 = i1 - 6;
        int j5 = random.nextInt(3);
        floor = 1;
        topFloor = 0;

        for (int k5 = -1; k5 < castleHeight * 7 + 7; k5++)
        {
            for (int k6 = -12; k6 < 38; k6++)
            {
                for (int i8 = -12; i8 < 38; i8++)
                {
                    world.setBlockToAir(l + k6, i1 + k5, j1 + i8);
                }
            }
        }

        for (int l5 = 2; l5 < 5; l5++)
        {
            for (int l6 = -12; l6 < 38; l6++)
            {
                for (int j8 = -12; j8 < 38; j8++)
                {
                    if (world.getBlock(l + l6, i1 - l5, j1 + j8) == Blocks.air)
                    {
                        world.setBlock(l + l6, i1 - l5, j1 + j8, Blocks.stone);
                    }
                }
            }
        }

        for (int i6 = -10; i6 < 36; i6++)
        {
            for (int i7 = -10; i7 < 36; i7++)
            {
                world.setBlock(l + i6, i1 - 1, j1 + i7, Blocks.water);
            }
        }

        label1:

        for (int j6 = -7; j6 < castleHeight * 7 - 7; j6 += 7)
        {
            for (int j7 = -4; j7 < 30; j7++)
            {
                for (int k8 = -4; k8 < 30; k8++)
                {
                    if ((world.getBlock(l + j7, i1 + 6 + j6, j1 + k8)) == Blocks.air || world.getBlock(l + j7, i1 + 6 + j6, j1 + k8) == Blocks.water && rand.nextInt(50) != 0)
                    {
                        world.setBlock(l + j7, i1 + 6 + j6, j1 + k8, Blocks.double_stone_slab);
                    }

                    if (world.getBlock(l + j7, i1 + 7 + j6, j1 + k8) != Blocks.air || random.nextInt(25) != 0 || j6 >= (castleHeight - 1) * 7 - 7)
                    {
                        continue;
                    }

                    world.setBlock(l + j7, i1 + 7 + j6, j1 + k8, Blocks.web);

                    if (rand.nextInt(10) == 0)
                    {
                        world.setBlock(l + j7, i1 + 8 + j6, j1 + k8, Blocks.web);
                    }
                }
            }

            int k7 = 0;

            do
            {
                if (k7 >= 2)
                {
                    break;
                }

                int l8 = rand.nextInt(20) - 10;
                int l12 = rand.nextInt(20) - 10;

                if (world.getBlock(l + 10 + l8, i1 + 7 + j6, j1 + 5 + l12) == Blocks.air)
                {
                    world.setBlock(l + 10 + l8, i1 + 7 + j6, j1 + 5 + l12, Blocks.mob_spawner);
                    TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getTileEntity(l + 10 + l8, i1 + 7 + j6, j1 + 5 + l12);
                    tileentitymobspawner.func_145881_a().setEntityName(populateSpawner(random));
                    k7++;
                }
            }
            while (true);

            if (j6 != (castleHeight - 1) * 7 - 7)
            {
                continue;
            }

            k7 = 0;

            do
            {
                int i9;
                int i13;

                do
                {
                    if (k7 >= 2)
                    {
                        continue label1;
                    }

                    i9 = rand.nextInt(20) - 10;
                    i13 = rand.nextInt(20) - 10;
                }
                while (world.getBlock(l + 10 + i9, i1 + 7 + j6, j1 + 5 + i13) != Blocks.air);

                world.setBlock(l + 10 + i9, i1 + 7 + j6, j1 + 5 + i13, Blocks.mob_spawner);
                TileEntityMobSpawner tileentitymobspawner1 = (TileEntityMobSpawner)world.getTileEntity(l + 10 + i9, i1 + 7 + j6, j1 + 5 + i13);
                tileentitymobspawner1.func_145881_a().setEntityName("CastleGuard");
                k7++;
            }
            while (true);
        }

        ItemStack itemstack = new ItemStack(MoreCreepsAndWeirdos.earthgem, 1);
        int l7 = random.nextInt(castleHeight) + 1;
        buildTower(world, random, k1, l1, i2, l, i1, j1, true, itemstack, l7);

        for (int j9 = 5; j9 < 20; j9++)
        {
            for (int j13 = 0; j13 < 10; j13++)
            {
                for (int i16 = -2; i16 < castleHeight * 7 - 6; i16++)
                {
                    world.setBlock(l + j9, i1 + i16, j1 - 5, Blocks.cobblestone);
                }
            }
        }

        for (int k9 = 4; k9 < 21; k9++)
        {
            if (alternate)
            {
                world.setBlock(l + k9, i1 + 29, j1 - 5, Blocks.cobblestone);
            }

            alternate = !alternate;
            world.setBlock(l + k9, i1 + 20, j1 - 6, Blocks.cobblestone);
            world.setBlock(l + k9, i1 + 16, j1 - 6, Blocks.cobblestone);

            if (alternate)
            {
                world.setBlock(l + k9, i1 + 10, j1 - 6, Blocks.cobblestone);
            }

            world.setBlock(l + k9, i1 + 9, j1 - 6, Blocks.cobblestone);
        }

        itemstack = new ItemStack(MoreCreepsAndWeirdos.mininggem, 1);
        l7 = random.nextInt(castleHeight) + 1;
        buildTower(world, random, k1, l1, i2, l + 25, i1, j1, false, itemstack, l7);

        for (int l9 = 5; l9 < 20; l9++)
        {
            for (int k13 = 0; k13 < 10; k13++)
            {
                for (int j16 = -2; j16 < castleHeight * 7 - 6; j16++)
                {
                    world.setBlock(l + l9, i1 + j16, j1 + 29, Blocks.cobblestone);
                }
            }
        }

        for (int i10 = 4; i10 < 21; i10++)
        {
            if (alternate)
            {
                world.setBlock(l + i10, i1 + 29, j1 + 29, Blocks.cobblestone);
            }

            alternate = !alternate;
            world.setBlock(l + i10, i1 + 20, j1 + 30, Blocks.cobblestone);
            world.setBlock(l + i10, i1 + 16, j1 + 30, Blocks.cobblestone);

            if (alternate)
            {
                world.setBlock(l + i10, i1 + 10, j1 + 30, Blocks.cobblestone);
            }

            world.setBlock(l + i10, i1 + 9, j1 + 30, Blocks.cobblestone);
        }

        itemstack = new ItemStack(MoreCreepsAndWeirdos.skygem, 1);
        l7 = random.nextInt(castleHeight) + 1;
        buildTower(world, random, k1, l1, i2, l + 25, i1, j1 + 25, false, itemstack, l7);

        for (int j10 = 5; j10 < 20; j10++)
        {
            for (int l13 = 0; l13 < 10; l13++)
            {
                for (int k16 = -2; k16 < castleHeight * 7 - 6; k16++)
                {
                    world.setBlock(l - 5, i1 + k16, j1 + j10, Blocks.cobblestone);
                }
            }
        }

        for (int k10 = 4; k10 < 21; k10++)
        {
            if (alternate)
            {
                world.setBlock(l - 5, i1 + 29, j1 + k10, Blocks.cobblestone);
            }

            alternate = !alternate;
            world.setBlock(l - 6, i1 + 20, j1 + k10, Blocks.cobblestone);
            world.setBlock(l - 6, i1 + 16, j1 + k10, Blocks.cobblestone);

            if (alternate)
            {
                world.setBlock(l - 6, i1 + 10, j1 + k10, Blocks.cobblestone);
            }

            world.setBlock(l - 6, i1 + 9, j1 + k10, Blocks.cobblestone);
        }

        itemstack = new ItemStack(MoreCreepsAndWeirdos.healinggem, 1);
        l7 = random.nextInt(castleHeight) + 1;
        buildTower(world, random, k1, l1, i2, l, i1, j1 + 25, true, itemstack, l7);

        for (int l10 = 5; l10 < 20; l10++)
        {
            for (int i14 = 0; i14 < 10; i14++)
            {
                for (int l16 = -2; l16 < castleHeight * 7 - 6; l16++)
                {
                    world.setBlock(l + 29, i1 + l16, j1 + l10, Blocks.cobblestone);
                }
            }
        }

        for (int i11 = 4; i11 < 21; i11++)
        {
            if (alternate)
            {
                world.setBlock(l + 29, i1 + 29, j1 + i11, Blocks.cobblestone);
            }

            alternate = !alternate;
            world.setBlock(l + 30, i1 + 20, j1 + i11, Blocks.cobblestone);
            world.setBlock(l + 30, i1 + 16, j1 + i11, Blocks.cobblestone);

            if (alternate)
            {
                world.setBlock(l + 30, i1 + 10, j1 + i11, Blocks.cobblestone);
            }

            world.setBlock(l + 30, i1 + 9, j1 + i11, Blocks.cobblestone);
        }

        for (int j11 = 0; j11 < (castleHeight - 1) * 7 - 6; j11 += 7)
        {
            for (int j14 = 6; j14 < 20; j14 += 3)
            {
                world.setBlock(l + j14, i1 + 4 + j11, j1 - 4, Blocks.torch);
                world.setBlock(l + j14, i1 + 4 + j11, j1 + 28, Blocks.torch);

                if (j14 > 6 && j14 < 17)
                {
                    world.setBlock(l - 4, i1 + 4 + j11, j1 + j14, Blocks.torch);
                    world.setBlock(l + 28, i1 + 4 + j11, j1 + j14, Blocks.torch);
                }
            }
        }

        for (int k11 = 11; k11 < 15; k11++)
        {
            for (int k14 = 0; k14 < 4; k14++)
            {
                world.setBlockToAir(l + k11, i1 + k14, j1 - 5);
                world.setBlockToAir(l + k11, i1 + k14, j1 + 29);
            }
        }

        for (int l11 = 11; l11 < 15; l11++)
        {
            for (int l14 = 0; l14 < 6; l14++)
            {
                for (int i17 = 0; i17 < 4; i17++)
                {
                    world.setBlock(l + l11, i1 - 1, j1 - 6 - l14, Blocks.planks);
                    world.setBlock(l + l11, i1 - 1, j1 + 30 + l14, Blocks.planks);
                }
            }
        }

        world.setBlock(l + 9, i1 + 5, j1 + 30, Blocks.torch);
        world.setBlock(l + 16, i1 + 5, j1 + 30, Blocks.torch);
        world.setBlock(l + 9, i1 + 5, j1 - 6, Blocks.torch);
        world.setBlock(l + 16, i1 + 5, j1 - 6, Blocks.torch);

        for (int i12 = 0; i12 < castleHeight * 7; i12 += 7)
        {
            for (int i15 = 0; i15 < 2; i15++)
            {
                world.setBlock(l + 17, i1 + i12, j1 + -1 + i15, Blocks.stone_stairs);
                world.setBlock(l + 5, i1 + i12, j1 + -1 + i15, Blocks.stone_stairs);
                world.setBlock(l + 17, i1 + i12, j1 + 24 + i15, Blocks.stone_stairs);
                world.setBlock(l + 5, i1 + i12, j1 + 24 + i15, Blocks.stone_stairs);
                world.setBlock(l + 19, i1 + i12, j1 + -1 + i15, Blocks.stone_stairs);
                world.setBlock(l + 7, i1 + i12, j1 + -1 + i15, Blocks.stone_stairs);
                world.setBlock(l + 19, i1 + i12, j1 + 24 + i15, Blocks.stone_stairs);
                world.setBlock(l + 7, i1 + i12, j1 + 24 + i15, Blocks.stone_stairs);
                //old method: world.setBlockMetadataWithNotify(x, y, z, block id, meta id (if needed, used for wool color etc.));
                //world.setBlockMetadataWithNotify(l + 19, i1 + i12, j1 + -1 + i15, 1);
                world.setBlock(l + 19, i1 + i12, j1 + -1 + i15, Blocks.stone);
                //world.setBlockMetadataWithNotify(l + 7, i1 + i12, j1 + -1 + i15, 1);
                world.setBlock(l + 7, i1 + i12, j1 + -1 + i15, Blocks.stone);
                //world.setBlockMetadataWithNotify(l + 19, i1 + i12, j1 + 24 + i15, 1);
                world.setBlock(l + 19, i1 + i12, j1 + 24 + i15, Blocks.stone);
                //world.setBlockMetadataWithNotify(l + 7, i1 + i12, j1 + 24 + i15, 1);
                world.setBlock(l + 7, i1 + i12, j1 + 24 + i15, Blocks.stone);
                //fixed :)
                world.setBlock(l + 19, i1 + i12, j1 + -1 + i15, Blocks.stone_stairs);
                world.setBlock(l + 7, i1 + i12, j1 + -1 + i15, Blocks.stone_stairs);
                world.setBlock(l + 19, i1 + i12, j1 + 24 + i15, Blocks.stone_stairs);
                world.setBlock(l + 7, i1 + i12, j1 + 24 + i15, Blocks.stone_stairs);
                //TODO check if it works
            }
        }

        for (int j12 = 0; j12 < castleHeight * 7; j12 += 7)
        {
            for (int j15 = 0; j15 < 7; j15++)
            {
                for (int j17 = 0; j17 < 9; j17++)
                {
                    world.setBlock(l + 9 + j15, i1 + j12, (j1 + 16) - j17, Blocks.stone_slab);
                }
            }
        }

        for (int k12 = 0; k12 < castleHeight * 7; k12 += 7)
        {
            for (int k15 = 0; k15 < 9; k15 += 3)
            {
                world.setBlock(l + 9 + k15, i1 + k12, j1 + 16, Blocks.double_stone_slab);
                world.setBlock(l + 9 + k15, i1 + k12 + 1, j1 + 16, Blocks.double_stone_slab);
                world.setBlock(l + 9 + k15, i1 + k12 + 2, j1 + 16, Blocks.torch);
                world.setBlock(l + 9 + k15, i1 + k12, j1 + 8, Blocks.double_stone_slab);
                world.setBlock(l + 9 + k15, i1 + k12 + 1, j1 + 8, Blocks.double_stone_slab);
                world.setBlock(l + 9 + k15, i1 + k12 + 2, j1 + 8, Blocks.torch);
            }
        }

        l7 = random.nextInt(castleHeight) + 1;
        itemstack = new ItemStack(MoreCreepsAndWeirdos.firegem, 1);
        boolean flag5 = false;

        for (int l15 = 0; l15 < castleHeight * 7; l15 += 7)
        {
            world.setBlock(l + 9 + 3, i1 + l15, (j1 + 16) - 3, Blocks.double_stone_slab);
            world.setBlock(l + 9 + 3, i1 + l15, (j1 + 16) - 4, Blocks.double_stone_slab);
            world.setBlock(l + 9 + 3, i1 + l15 + 1, (j1 + 16) - 4, Blocks.chest);
            world.setBlock(l + 9 + 3, i1 + l15 + 1, (j1 + 16) - 3, Blocks.chest);
            TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(l + 9 + 3, i1 + l15 + 1, (j1 + 16) - 3);

            for (int k17 = 0; k17 < random.nextInt(20); k17++)
            {
                ItemStack itemstack1 = populateChest(floor, random);

                if (itemstack1 != null)
                {
                    tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack1);
                }

                if (!flag5 && l7 == l15 / 7)
                {
                    tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack);
                    flag5 = true;
                }
            }
        }

        CREEPSEntityCastleKing creepsentitycastleking = new CREEPSEntityCastleKing(world);
        creepsentitycastleking.setLocationAndAngles(l + 9 + 6, i1 + castleHeight * 7, (j1 + 16) - 4, world.rand.nextFloat() * 360F, 0.0F);
        creepsentitycastleking.setPosition(l + 9 + 6, i1 + castleHeight * 7, (j1 + 16) - 4);
        world.spawnEntityInWorld(creepsentitycastleking);
        return true;
    }

    public void buildTower(World world, Random random, int i, int j, int k, int l, int i1, int j1, boolean flag, ItemStack itemstack, int k1)
    {
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        int l1 = i1 - 13;
        int i2 = random.nextInt(3);
        boolean flag4 = false;
        floor = 1;
        topFloor = 0;

        for (int j2 = 0; j2 < castleHeight + 1; j2++)
        {
            l1 += 7;

            if (j2 == castleHeight)
            {
                topFloor = 1;
            }

            for (int k2 = 0; k2 < 7; k2++)
            {
                if (l1 == i1 - 6 && k2 < 4)
                {
                    k2 = 4;
                }

                for (int j3 = -7; j3 < 7; j3++)
                {
                    for (int l3 = -7; l3 < 7; l3++)
                    {
                        int k4 = j3 + l;
                        int i5 = k2 + l1;
                        int j5 = l3 + j1;

                        if (l3 == -7)
                        {
                            if (j3 > -5 && j3 < 4)
                            {
                                world.setBlock(k4, i5, j5, Blocks.cobblestone);
                            }

                            continue;
                        }

                        if (l3 == -6 || l3 == -5)
                        {
                            if (j3 == -5 || j3 == 4)
                            {
                                world.setBlock(k4, i5, j5, Blocks.cobblestone);
                                continue;
                            }

                            if (l3 == -6)
                            {
                                if (j3 == (k2 + 1) % 7 - 3)
                                {
                                    world.setBlock(k4, i5, j5, Blocks.stone_stairs);

                                    if (k2 == 5)
                                    {
                                        world.setBlock(k4 - 7, i5, j5, Blocks.double_stone_slab);
                                    }

                                    if (k2 == 6 && topFloor == 1)
                                    {
                                        world.setBlock(k4, i5, j5, Blocks.cobblestone);
                                    }

                                    continue;
                                }

                                if (j3 < 4 && j3 > -5)
                                {
                                    world.setBlockToAir(k4, i5, j5);
                                }

                                continue;
                            }

                            if (l3 != -5 || j3 <= -5 || j3 >= 5)
                            {
                                continue;
                            }

                            if (k2 != 0 && k2 != 6 || j3 != -4 && j3 != 3)
                            {
                                if (k2 == 5 && (j3 == 3 || j3 == -4))
                                {
                                    world.setBlock(k4, i5, j5, Blocks.double_stone_slab);
                                }
                                else
                                {
                                    world.setBlock(k4, i5, j5, Blocks.cobblestone);
                                }
                            }
                            else
                            {
                                world.setBlockToAir(k4, i5, j5);
                            }

                            continue;
                        }

                        if (l3 == -4 || l3 == -3 || l3 == 2 || l3 == 3)
                        {
                            if (j3 == -6 || j3 == 5)
                            {
                                world.setBlock(k4, i5, j5, Blocks.cobblestone);
                                continue;
                            }

                            if (j3 <= -6 || j3 >= 5)
                            {
                                continue;
                            }

                            if (k2 == 5)
                            {
                                world.setBlock(k4, i5, j5, Blocks.double_stone_slab);
                                continue;
                            }

                            if (world.getBlock(k4, i5, j5) != Blocks.chest)
                            {
                                world.setBlockToAir(k4, i5, j5);
                            }

                            continue;
                        }

                        if (l3 > -3 && l3 < 2)
                        {
                            if (j3 == -7 || j3 == 6)
                            {
                                if (k2 < 0 || k2 > 3 || j3 != -7 && j3 != 6 || l3 != -1 && l3 != 0)
                                {
                                    world.setBlock(k4, i5, j5, Blocks.cobblestone);
                                    continue;
                                }

                                if (j3 == -7 && flag || j3 == 6 && !flag)
                                {
                                    world.setBlock(k4, i5, j5, Blocks.glass);
                                }

                                continue;
                            }

                            if (j3 <= -7 || j3 >= 6)
                            {
                                continue;
                            }

                            if (k2 == 5)
                            {
                                world.setBlock(k4, i5, j5, Blocks.double_stone_slab);
                            }
                            else
                            {
                                world.setBlockToAir(k4, i5, j5);
                            }

                            continue;
                        }

                        if (l3 == 4)
                        {
                            if (j3 == -5 || j3 == 4)
                            {
                                world.setBlock(k4, i5, j5, Blocks.cobblestone);
                                continue;
                            }

                            if (j3 <= -5 || j3 >= 4)
                            {
                                continue;
                            }

                            if (k2 == 5)
                            {
                                world.setBlock(k4, i5, j5, Blocks.double_stone_slab);
                            }
                            else
                            {
                                world.setBlockToAir(k4, i5, j5);
                            }

                            continue;
                        }

                        if (l3 == 5)
                        {
                            if (j3 == -4 || j3 == -3 || j3 == 2 || j3 == 3)
                            {
                                world.setBlock(k4, i5, j5, Blocks.cobblestone);
                                continue;
                            }

                            if (j3 <= -3 || j3 >= 2)
                            {
                                continue;
                            }

                            if (k2 == 5)
                            {
                                world.setBlock(k4, i5, j5, Blocks.double_stone_slab);
                            }
                            else
                            {
                                world.setBlock(k4, i5, j5, Blocks.cobblestone);
                            }

                            continue;
                        }

                        if (l3 != 6 || j3 <= -3 || j3 >= 2)
                        {
                            continue;
                        }

                        if (k2 < 0 || k2 > 3 || j3 != -1 && j3 != 0)
                        {
                            world.setBlock(k4, i5, j5, Blocks.cobblestone);
                        }
                        else
                        {
                            world.setBlock(k4, i5, j5, Blocks.cobblestone);
                        }
                    }
                }
            }

            if (floor == 2)
            {
                world.setBlock(l + 3, l1, j1 - 5, Blocks.cobblestone);
                world.setBlock(l + 3, l1 - 1, j1 - 5, Blocks.cobblestone);
            }

            if (topFloor == 1)
            {
                double d = l;
                double d1 = l1 + 6;
                double d2 = (double)j1 + 0.5D;
            }
            else
            {
                if (rand.nextInt(5) == 0)
                {
                    world.setBlock(l + 2, l1 + 6, j1 + 2, Blocks.mob_spawner);
                    TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getTileEntity(l + 2, l1 + 6, j1 + 2);
                    tileentitymobspawner.func_145881_a().setEntityName(populateSpawner(random));
                }

                world.setBlock(l - 3, l1 + 6, j1 + 2, Blocks.mob_spawner);
                TileEntityMobSpawner tileentitymobspawner1 = (TileEntityMobSpawner)world.getTileEntity(l - 3, l1 + 6, j1 + 2);
                tileentitymobspawner1.func_145881_a().setEntityName(populateSpawner(random));
            }

            if (topFloor != 1)
            {
                world.setBlock(l, l1 + 6, j1 - 3, Blocks.double_stone_slab);
                world.setBlock(l - 1, l1 + 6, j1 - 3, Blocks.double_stone_slab);
            }

            if (l1 + 56 >= 120 && floor == 1)
            {
                floor = 2;
            }

            if (topFloor != 1)
            {
                for (int l2 = 0; l2 < 2; l2++)
                {
                    world.setBlock(l - l2, l1 + 7, j1 - 3, Blocks.chest);
                    TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(l - l2, l1 + 7, j1 - 3);

                    for (int i4 = 0; i4 < 1 + l2 + i2; i4++)
                    {
                        ItemStack itemstack1 = populateChest(floor, random);

                        if (itemstack1 != null)
                        {
                            tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack1);
                        }
                    }

                    if (k1 == floor && !flag4)
                    {
                        tileentitychest.setInventorySlotContents(0, itemstack);
                        flag4 = true;
                    }
                }
            }

            world.setBlock(l + 3, l1, j1 - 6, Blocks.torch);
            world.setBlock(l - 4, l1, j1 - 6, Blocks.torch);
            world.setBlock(l + 1, l1, j1 - 4, Blocks.torch);
            world.setBlock(l - 2, l1, j1 - 4, Blocks.torch);

            for (int i3 = 0; i3 < (floor * 4 + i2) - 8 && topFloor != 1; i3++)
            {
                int k3 = 5 - random.nextInt(12);
                int j4 = l1 + 5;
                int l4 = 5 - random.nextInt(10);

                if (l4 < -2 && k3 < 4 && k3 > -5 && k3 != 1 && k3 != -2)
                {
                    continue;
                }

                k3 += l;
                l4 += j1;

                if (world.getBlock(k3, j4, l4) == Blocks.double_stone_slab && world.getBlock(k3, j4 + 1, l4) != Blocks.mob_spawner)
                {
                    world.setBlockToAir(k3, j4, l4);
                }
            }

            floor++;
        }
    }

    private ItemStack populateChest(int i, Random random)
    {
        int j = random.nextInt(8 * i);

        if (j > 40)
        {
            j = 40;
        }

        switch (j)
        {
            case 1:
                return new ItemStack(Items.wheat, random.nextInt(12) + 3);

            case 2:
                return new ItemStack(Items.reeds, random.nextInt(6) + 6);

            case 3:
                return new ItemStack(Items.cookie, random.nextInt(6) + 6);

            case 4:
                return new ItemStack(Items.arrow, random.nextInt(30) + 10);

            case 5:
                return new ItemStack(MoreCreepsAndWeirdos.money, random.nextInt(4) + 1);

            case 6:
                return new ItemStack(MoreCreepsAndWeirdos.evilegg, random.nextInt(4) + 1);

            case 7:
                return new ItemStack(Items.leather, 1);

            case 8:
                return new ItemStack(Items.paper, 1);

            case 9:
                return new ItemStack(Items.apple, 1);

            case 10:
                return new ItemStack(Items.wooden_axe, 1);

            case 11:
                return new ItemStack(Items.bow, 1);

            case 12:
                return new ItemStack(MoreCreepsAndWeirdos.bandaid, random.nextInt(15) + 1);

            case 13:
                return new ItemStack(MoreCreepsAndWeirdos.blorpcola, random.nextInt(10) + 5);

            case 14:
                return new ItemStack(Items.sign, 1);

            case 15:
                return new ItemStack(Items.wheat, random.nextInt(10) + 5);

            case 16:
                return new ItemStack(Items.bread, 1);

            case 17:
                return new ItemStack(Items.iron_pickaxe, 1);

            case 18:
                return new ItemStack(Items.iron_axe, 1);

            case 19:
                return new ItemStack(Items.bucket, 1);

            case 20:
                return new ItemStack(Items.iron_shovel, 1);

            case 21:
                return new ItemStack(MoreCreepsAndWeirdos.evilegg, random.nextInt(15) + 1);

            case 22:
                return new ItemStack(MoreCreepsAndWeirdos.goodonut, random.nextInt(15) + 1);

            case 23:
                return new ItemStack(MoreCreepsAndWeirdos.money, random.nextInt(10) + 1);

            case 24:
                return new ItemStack(Items.water_bucket, 1);

            case 25:
                return new ItemStack(MoreCreepsAndWeirdos.frisbee, 1);

            case 26:
                return new ItemStack(Items.cake, 1);

            case 27:
                return new ItemStack(MoreCreepsAndWeirdos.money, random.nextInt(10) + 5);

            case 28:
                return new ItemStack(Items.milk_bucket, 1);

            case 29:
                return new ItemStack(MoreCreepsAndWeirdos.lolly, random.nextInt(4) + 1);

            case 30:
                return new ItemStack(MoreCreepsAndWeirdos.money, random.nextInt(24) + 1);

            case 32:
                return new ItemStack(Items.diamond, 1);

            case 33:
                return new ItemStack(Items.golden_helmet, 1);

            case 34:
                return new ItemStack(Items.diamond_helmet, 1);

            case 35:
                return new ItemStack(Items.golden_boots, 1);

            case 36:
                return new ItemStack(MoreCreepsAndWeirdos.shrinkray, 1);

            case 37:
                return new ItemStack(MoreCreepsAndWeirdos.horseheadgem, 1);

            case 38:
                return new ItemStack(Items.diamond, 1);

            case 39:
                return new ItemStack(Items.golden_apple, 1);

            case 40:
                return new ItemStack(MoreCreepsAndWeirdos.money, random.nextInt(49) + 1);

            case 31:
            default:
                return null;
        }
    }

    public static boolean mobExists(String s)
    {
        return stringToClassMapping.get(s) != null;
    }

    private String populateSpawner2(Random random)
    {
        return "";
    }

    private String populateSpawner(Random random)
    {
        int i = floor;
        int j = rand.nextInt(10);

        if (topFloor == 1)
        {
            return "CastleGuard";
        }

        if (i == 0)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "Skeleton";
            }
        }

        if (i == 1)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "CastleGuard";
            }
        }

        if (i == 2)
        {
            if (mobExists("Mummy") && j > 5)
            {
                return "Mummy";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "CastleGuard";
            }
        }

        if (i == 3)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "Skeleton";
            }
        }

        if (i == 4)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "Spider";
            }
            else
            {
                return "Skeleton";
            }
        }

        if (i == 5)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "CastleGuard";
            }
        }
        else
        {
            return "CastleGuard";
        }
    }

    
}

