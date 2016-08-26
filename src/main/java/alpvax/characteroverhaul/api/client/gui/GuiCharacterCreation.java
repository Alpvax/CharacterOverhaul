package alpvax.characteroverhaul.api.client.gui;

import java.util.List;

import com.google.common.collect.ImmutableList;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.CharacterModifierFactory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCharacterCreation extends GuiScreen
{
	private final List<ICharacterCreationPage> pages;
	private final ICharacter character;
	//TODO:Create Creation GUI

	public GuiCharacterCreation(ICharacter character)
	{
		this.character = character;
		ImmutableList.Builder<ICharacterCreationPage> b = ImmutableList.<ICharacterCreationPage>builder();
		for(CharacterModifierFactory<?> factory : CharacterModifierFactory.REGISTRY.getValues())
		{
			List<ICharacterCreationPage> list = factory.getPagesForGUI();
			if(list != null && factory.isValidForCharacter(character))
			{
				b.addAll(list);
			}
		}
		pages = b.build();
	}
}
