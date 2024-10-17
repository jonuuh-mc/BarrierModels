package io.jonuuh.barriermodels.mixins;

import io.jonuuh.barriermodels.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    // I really hope this function being redirected isn't used for more than barrier particles; I know it's just a horrible deobfuscated name but still
    // unless intellij's "find in files" & "find usages" are broken it's only used here where it's redirected
    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"))
    private void barrierModels$clobberBarrierParticles(WorldClient worldClient, int posX, int posY, int posZ)
    {
        if (Settings.getInstance().doParticles())
        {
            worldClient.doVoidFogParticles(posX, posY, posZ);
        }
    }
}
