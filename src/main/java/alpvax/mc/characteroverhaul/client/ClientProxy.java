package alpvax.mc.characteroverhaul.client;

import alpvax.mc.characteroverhaul.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy {

  public static KeyBinding prone = new KeyBinding("key.prone", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, "key.categories.movement");

  public ClientProxy() {
    ClientRegistry.registerKeyBinding(prone);
  }
}
