package alpvax.characteroverhaul.client.gui;

import java.util.ArrayList;
import java.util.List;

import alpvax.characteroverhaul.api.config.Config;
import alpvax.characteroverhaul.core.CharacterOverhaul;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class COConfigGui extends GuiConfig
{

	public COConfigGui(GuiScreen parentScreen)
	{
		super(parentScreen, getElements(), CharacterOverhaul.MOD_ID, false, false, I18n.format(CharacterOverhaul.MOD_ID + ".configgui.mainConfigTitle"));
	}

	private static List<IConfigElement> getElements()
	{
		if(Minecraft.getMinecraft().theWorld == null)
		{
			return (new ConfigElement(Config.configFile.getCategory(Configuration.CATEGORY_CLIENT))).getChildElements();
		}
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		/*TODO:list.add(new DummyCategoryElement("forgeCfg", "forge.configgui.ctgy.forgeGeneralConfig", GeneralEntry.class));
		list.add(new DummyCategoryElement("forgeClientCfg", "forge.configgui.ctgy.forgeClientConfig", ClientEntry.class));
		list.add(new DummyCategoryElement("forgeChunkLoadingCfg", "forge.configgui.ctgy.forgeChunkLoadingConfig", ChunkLoaderEntry.class));
		list.add(new DummyCategoryElement("forgeVersionCheckCfg", "forge.configgui.ctgy.VersionCheckConfig", VersionCheckEntry.class));*/
		return list;
	}

}
