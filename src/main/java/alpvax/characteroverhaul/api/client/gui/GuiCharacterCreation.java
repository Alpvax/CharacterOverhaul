package alpvax.characteroverhaul.api.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCharacterCreation extends GuiScreen
{
	private List<ICharacterCreationPage> pages = new ArrayList<>();
	//TODO:Create Creation GUI
}
