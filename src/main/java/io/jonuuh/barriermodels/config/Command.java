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
//    public void spawnStates()
//    {
//        BlockPos initPos = Minecraft.getMinecraft().thePlayer.getPosition().north();
//        BlockPos pos = initPos;
//        Block barrier = Block.getBlockById(166);
//        int lastConnections = 0;
//
//        for (Map<String, Boolean> state : BarrierModels.states)
//        {
//            int connections = Booleans.countTrue(Booleans.toArray(state.values()));
//
//            if (connections > lastConnections)
//            {
//                pos = initPos.north(connections * 5);
//                lastConnections = connections;
//            }
//
//            setBlock(pos, barrier);
//
//            if (state.get("down"))
//            {
//                setBlock(pos.down(), barrier);
//            }
//            if (state.get("east"))
//            {
//                setBlock(pos.east(), barrier);
//            }
//            if (state.get("north"))
//            {
//                setBlock(pos.north(), barrier);
//            }
//            if (state.get("south"))
//            {
//                setBlock(pos.south(), barrier);
//            }
//            if (state.get("up"))
//            {
//                setBlock(pos.up(), barrier);
//            }
//            if (state.get("west"))
//            {
//                setBlock(pos.west(), barrier);
//            }
//
//            pos = pos.east(5);;
//        }
//    }
//
//    public void setBlock(BlockPos pos, Block block)
//    {
//        String position = pos.getX() + " " + pos.getY() + " " + pos.getZ();
//        Minecraft.getMinecraft().thePlayer.sendChatMessage("/setblock " + position + " minecraft:barrier");
//    }
//    public void setBlock(BlockPos pos, Block block)
//    {
//        Minecraft.getMinecraft().theWorld.setBlockState(pos, block.getDefaultState(), 2);
//    }
}