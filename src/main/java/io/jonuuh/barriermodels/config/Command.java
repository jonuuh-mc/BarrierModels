package io.jonuuh.barriermodels.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class Command extends CommandBase
{
    private static Settings settingsInstance;

    public Command()
    {
        settingsInstance = Settings.getInstance();
    }

    @Override
    public String getCommandName()
    {
        return "barriers";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayerSP))
        {
            return;
        }

        if (args.length != 1 || (!args[0].equals("render") && !args[0].equals("connect") && !args[0].equals("particle")))
        {
            return;
        }

        String t = EnumChatFormatting.GREEN + "true";
        String f = EnumChatFormatting.RED + "false";
        String msg;

        switch (args[0])
        {
            case "render":
                settingsInstance.setDoRendering(!settingsInstance.doRendering());
                msg = "Barrier rendering: " + (settingsInstance.doRendering() ? t : f);
                Minecraft.getMinecraft().refreshResources();
                break;
            case "connect":
                settingsInstance.setDoConnecting(!settingsInstance.doConnecting());
                msg = "Barrier connected textures: " + (settingsInstance.doConnecting() ? t : f);
                Minecraft.getMinecraft().refreshResources();
                break;
            case "particle":
                settingsInstance.setDoParticles(!settingsInstance.doParticles());
                msg = "Barrier held item particles: " + (settingsInstance.doParticles() ? t : f);
                break;
            default:
                msg = "";
//            case "spawn":
//                spawnBlocks();
//                return;
//            case "id":
//                MovingObjectPosition movingObjectPosition = Minecraft.getMinecraft().objectMouseOver;
//
//                if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
//                {
//                    BlockPos blockpos = movingObjectPosition.getBlockPos();
//                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockpos).getBlock();
//                    Material material = block.getMaterial();
//
//                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(blockpos.toString() + " " + getMaterialName(material)));
//                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.valueOf(block.isTranslucent())));
//                }
//                return;
        }

        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
        settingsInstance.save();
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "render", "connect", "particle");
        }
        return null;
    }

//    public void spawnBlocks()
//    {
//        BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition().north();
//
//        for (int i = 0; i < Block.blockRegistry.getKeys().size(); i++)
//        {
//            setBlock(pos.north(i).west(), Block.getBlockById(166));
//            setBlock(pos.north(i), Block.getBlockById(i));
//        }
//    }
//
//    public void setBlock(BlockPos pos, Block block)
//    {
//        Minecraft.getMinecraft().theWorld.setBlockState(pos, block.getDefaultState(), 6);
//    }
//
//    public String getMaterialName(Material materialIn)
//    {
//        if (materialIn == Material.air){return "air";}
//        else if (materialIn == Material.grass){return "grass";}
//        else if (materialIn == Material.ground){return "ground";}
//        else if (materialIn == Material.wood){return "wood";}
//        else if (materialIn == Material.rock){return "rock";}
//        else if (materialIn == Material.iron){return "iron";}
//        else if (materialIn == Material.anvil){return "anvil";}
//        else if (materialIn == Material.water){return "water";}
//        else if (materialIn == Material.lava){return "lava";}
//        else if (materialIn == Material.leaves){return "leaves";}
//        else if (materialIn == Material.plants){return "plants";}
//        else if (materialIn == Material.vine){return "vine";}
//        else if (materialIn == Material.sponge){return "sponge";}
//        else if (materialIn == Material.cloth){return "cloth";}
//        else if (materialIn == Material.fire){return "fire";}
//        else if (materialIn == Material.sand){return "sand";}
//        else if (materialIn == Material.circuits){return "circuits";}
//        else if (materialIn == Material.carpet){return "carpet";}
//        else if (materialIn == Material.glass){return "glass";}
//        else if (materialIn == Material.redstoneLight){return "redstoneLight";}
//        else if (materialIn == Material.tnt){return "tnt";}
//        else if (materialIn == Material.coral){return "coral";}
//        else if (materialIn == Material.ice){return "ice";}
//        else if (materialIn == Material.packedIce){return "packedIce";}
//        else if (materialIn == Material.snow){return "snow";}
//        else if (materialIn == Material.craftedSnow){return "craftedSnow";}
//        else if (materialIn == Material.cactus){return "cactus";}
//        else if (materialIn == Material.clay){return "clay";}
//        else if (materialIn == Material.gourd){return "gourd";}
//        else if (materialIn == Material.dragonEgg){return "dragonEgg";}
//        else if (materialIn == Material.portal){return "portal";}
//        else if (materialIn == Material.cake){return "cake";}
//        else if (materialIn == Material.web){return "web";}
//        else if (materialIn == Material.piston){return "piston";}
//        else if (materialIn == Material.barrier){return "barrier";}
//        else {return "NULL";}
//    }

//    public void test()
//    {
//        BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition().up(3).north(2).west(2);
//
//        setLabel(pos, 1, -3, 1, "0");
//        createCenters(pos);
//
//        pos = pos.north(6);
//        for (int i = 0; i < 6; i++)
//        {
//            setLabel(pos.west(i * 4), 1, -3, 1, String.valueOf(i));
//            createCenters(pos.west(i * 4));
//        }
//
//        pos = pos.north(6);
//        for (int i = 0; i < 6; i++)
//        {
//            for (int j = 0; j < 5; j++)
//            {
//                setLabel(pos.west(j * 4), 1, -3, 1, String.valueOf(j + (5 * i)));
//                createCenters(pos.west(j * 4));
//            }
//            pos = pos.north(4);
//        }
//
//    }
//
//    public void createCenters(BlockPos pos)
//    {
////        setBlock(pos, 0, 0, 0);
////        setBlock(pos, 1, 0, 0);
////        setBlock(pos, 2, 0, 0);
////
////        setBlock(pos, 0, 0, 1);
//        setBlock(pos, 1, 0, 1);
////        setBlock(pos, 2, 0, 1);
////
////        setBlock(pos, 0, 0, 2);
////        setBlock(pos, 1, 0, 2);
////        setBlock(pos, 2, 0, 2);
//
//
////        setBlock(pos, 0, 1, 0);
//        setBlock(pos, 1, 1, 0);
////        setBlock(pos, 2, 1, 0);
//
//        setBlock(pos, 0, 1, 1);
//        setBlock(pos, 1, 1, 1);
//        setBlock(pos, 2, 1, 1);
//
////        setBlock(pos, 0, 1, 2);
//        setBlock(pos, 1, 1, 2);
////        setBlock(pos, 2, 1, 2);
//
//
////        setBlock(pos, 0, 2, 0);
////        setBlock(pos, 1, 2, 0);
////        setBlock(pos, 2, 2, 0);
////
////        setBlock(pos, 0, 2, 1);
//        setBlock(pos, 1, 2, 1);
////        setBlock(pos, 2, 2, 1);
////
////        setBlock(pos, 0, 2, 2);
////        setBlock(pos, 1, 2, 2);
////        setBlock(pos, 2, 2, 2);
//    }
//
//    public void setLabel(BlockPos pos, int x, int y, int z, String label)
//    {
//        String position = (pos.getX() + x) + " " + (pos.getY() + y) + " " + (pos.getZ() + z);
//        Minecraft.getMinecraft().thePlayer.sendChatMessage("/setblock " + position + " standing_sign 0 replace {Text2:\"" + label + "\"}");
//    }
//
//    public void setBlock(BlockPos pos, int x, int y, int z)
//    {
//        String position = (pos.getX() + x) + " " + (pos.getY() + y) + " " + (pos.getZ() + z);
//        Minecraft.getMinecraft().thePlayer.sendChatMessage("/setblock " + position + " minecraft:barrier");
//    }
}