package alpvax.characteroverhaul.api.client.gui;

import java.util.List;

import com.google.common.collect.ImmutableList;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.core.CharacterOverhaul;
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
		for(ICharacterCreationPageHandler handler : CharacterOverhaul.proxy.getPageHandlers())
		{
			b.addAll(handler.getPages(character));
		}
		pages = b.build();
	}
}
