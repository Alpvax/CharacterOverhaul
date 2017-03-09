package alpvax.characteroverhaul.client;

import alpvax.characteroverhaul.api.config.CharacterConfig;
import alpvax.characteroverhaul.network.AbilityTriggerPacket;
import alpvax.characteroverhaul.network.CharacterNetwork;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(value = Side.CLIENT)
public class KeybindHandler
{
	private static KeyBinding[] abilityKeybinds = new KeyBinding[9];

	static
	{
		//ClientRegistry.registerKeyBinding(new KeyBinding);
		for(int i = 0; i < abilityKeybinds.length; i++)
		{
			//Default to numpad 1-9
			ClientRegistry.registerKeyBinding(abilityKeybinds[i] = new KeyBinding(I18n.format("key.character.ability" + i), KeyConflictContext.IN_GAME, 79 - 4 * i / 3 + i % 3, "key.categories.character.abilities"));
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void keyInput(KeyInputEvent event)
	{
		for(int i = 0; i < CharacterConfig.numAbilities; i++)
		{
			if(abilityKeybinds[i].isPressed())
			{
				CharacterNetwork.sendToServer(new AbilityTriggerPacket(i));
			}
		}
	}
}
