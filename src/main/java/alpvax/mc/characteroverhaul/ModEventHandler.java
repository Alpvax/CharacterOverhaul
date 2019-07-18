package alpvax.mc.characteroverhaul;

import alpvax.mc.characteroverhaul.config.ConfigHandler;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = CharacterOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

  private static final Logger LOGGER = LogManager.getLogger(CharacterOverhaul.MODID + " Mod Event Subscriber");
  private static final List<Block> blocksToRegisterItems = Lists.newArrayList();

  @SubscribeEvent
  public static void onRegisterBlocks(final RegistryEvent.Register<Block> event) {
    // Register all your blocks inside this registerAll call
    /*event.getRegistry().registerAll(
        // This block has the ROCK material, meaning it needs at least a wooden pickaxe to break it. It is very similar to Iron Ore
        setup(new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)), "example_ore"),
        // This block has the IRON material, meaning it needs at least a stone pickaxe to break it. It is very similar to the Iron Block
        setup(new Block(Block.Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL)), "example_block"),
        // This block has the ROCK material, meaning it needs at least a wooden pickaxe to break it. It is very similar to Furnace
        setup(new MiniModelBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13)), "mini_model")
    );
    LOGGER.debug("Registered Blocks");
     */
  }

  @SubscribeEvent
  public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
    final ModConfig config = event.getConfig();
    // Rebake the configs when they change
    if (config.getSpec() == ConfigHandler.CLIENT_SPEC) {
      ConfigHandler.updateClient(config);
    } else if (config.getSpec() == ConfigHandler.SERVER_SPEC) {
      ConfigHandler.updateServer(config);
    }
  }

  @SubscribeEvent
  public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
    final IForgeRegistry<Item> registry = event.getRegistry();
    /*registry.registerAll(
        setup(new Item(new Item.Properties()), "example_item")
    );

     */

    for (final Block block : blocksToRegisterItems) {

      final ResourceLocation blockRegistryName = block.getRegistryName();
      Preconditions.checkNotNull(blockRegistryName, "Registry Name of Block \"" + block + "\" is null! This is not allowed!");

      /*// Check that the blocks is from our mod, if not, continue to the next block
      if (!blockRegistryName.getNamespace().equals(ExampleMod.MODID)) {
        continue;
      }*/

      // If you have blocks that don't have a corresponding ItemBlock, uncomment this code and create an Interface - or even better an Annotation - called NoAutomaticItemBlock with no methods and implement it on your blocks that shouldn't have ItemBlocks
//			if (!(block instanceof NoAutomaticItemBlock)) {
//				continue;
//			}

      // Make the properties, and make it so that the item will be on our ItemGroup (CreativeTab)
      final Item.Properties properties = new Item.Properties();//.group(ModItemGroups.MOD_ITEM_GROUP);
      // Create the new BlockItem with the block and it's properties
      final BlockItem blockItem = new BlockItem(block, properties);
      // Setup the new BlockItem with the block's registry name and register it
      registry.register(setup(blockItem, blockRegistryName));
    }
    LOGGER.debug("Registered Items");
  }
  /*@SubscribeEvent
  public static void onRegisterTE(@Nonnull final RegistryEvent.Register<TileEntityType<?>> event) {
    // Register your TileEntities here if you have them
    event.getRegistry().registerAll(
        // We don't have a datafixer for our TileEntity, so we pass null into build
        setup(TileEntityType.Builder.create(MiniModelTileEntity::new, ModBlocks.MINI_MODEL).build(null), "mini_model")
    );
    LOGGER.debug("Registered TileEntitys");
  }*/

  @Nonnull
  private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final String name) {
    Preconditions.checkNotNull(name, "Name to assign to entry cannot be null!");
    return setup(entry, new ResourceLocation(CharacterOverhaul.MODID, name));
  }

  @Nonnull
  private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final ResourceLocation registryName) {
    Preconditions.checkNotNull(entry, "Entry cannot be null!");
    Preconditions.checkNotNull(registryName, "Registry name to assign to entry cannot be null!");
    entry.setRegistryName(registryName);
    if (entry instanceof Block) {
      blocksToRegisterItems.add((Block)entry);
    }
    return entry;
  }

}