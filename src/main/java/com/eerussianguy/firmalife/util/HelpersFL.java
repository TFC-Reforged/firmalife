package com.eerussianguy.firmalife.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.eerussianguy.firmalife.ConfigFL;
import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.network.PacketSpawnVanillaParticle;
import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.TerraFirmaCraft;

public class HelpersFL
{
    public HelpersFL()
    {

    }

    public static boolean doesStackMatchTool(ItemStack stack, String toolClass)
    {
        Set<String> toolClasses = stack.getItem().getToolClasses(stack);
        return toolClasses.contains(toolClass);
    }

    public static void insertWhitelist()
    {
        ConfigManager.sync(TerraFirmaCraft.MOD_ID, Config.Type.INSTANCE);
        String[] additions = {"yeast_starter", "coconut_milk", "yak_milk", "zebu_milk", "goat_milk", "curdled_goat_milk", "curdled_yak_milk", "pina_colada"};
        if (ConfigFL.General.COMPAT.addToWoodenBucket)
        {
            Set<String> woodenBucketSet = new HashSet<>(Arrays.asList(ConfigTFC.General.MISC.woodenBucketWhitelist));
            for (String a : additions)
            {
                if (woodenBucketSet.add(a) && ConfigFL.General.COMPAT.logging)
                {
                    FirmaLife.logger.info("Added {} to TFC's wooden bucket fluid whitelist", a);
                }
            }
            ConfigTFC.General.MISC.woodenBucketWhitelist = woodenBucketSet.toArray(new String[] {});
        }
        if (ConfigFL.General.COMPAT.addToBarrel)
        {
            Set<String> barrelSet = new HashSet<>(Arrays.asList(ConfigTFC.Devices.BARREL.fluidWhitelist));
            for (String a : additions)
            {
                if (barrelSet.add(a) && ConfigFL.General.COMPAT.logging)
                {
                    FirmaLife.logger.info("Added {} to TFC's barrel fluid whitelist", a);
                }
            }
            ConfigTFC.Devices.BARREL.fluidWhitelist = barrelSet.toArray(new String[] {});
        }
    }

    public static void sendVanillaParticleToClient(EnumParticleTypes particle, World worldIn, double x, double y, double z, double speedX, double speedY, double speedZ)
    {
        final int range = 80;
        PacketSpawnVanillaParticle packet = new PacketSpawnVanillaParticle(particle, x, y, z, speedX, speedY, speedZ);
        NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), x, y, z, range);
        FirmaLife.getNetwork().sendToAllAround(packet, point);
    }
}
