package alpvax.mc.characteroverhaul;

import alpvax.mc.characteroverhaul.character.ICharacter;
import alpvax.mc.characteroverhaul.character.LivingEntityCharacter;
import alpvax.mc.characteroverhaul.character.capability.CharacterCapability;
import alpvax.mc.characteroverhaul.character.race.RaceManager;
import alpvax.mc.characteroverhaul.command.CharacterCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = CharacterOverhaul.MODID)
public class ForgeEventHandler {
  @SubscribeEvent
  public static void onServerStart(FMLServerStartingEvent event) {
    new CharacterCommand(event.getCommandDispatcher());
  }
  @SubscribeEvent
  public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
    event.getServer().getResourceManager().addReloadListener(new RaceManager());
  }

  @SuppressWarnings("unchecked")
  @SubscribeEvent
  public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof PlayerEntity) {
      event.addCapability(new ResourceLocation(CharacterOverhaul.MODID, "charactercapability"), new ICapabilitySerializable<CompoundNBT>() {
        private ICharacter character = new LivingEntityCharacter((LivingEntity)event.getObject());
        @Override
        public CompoundNBT serializeNBT() {
          if (character instanceof INBTSerializable) {
            return ((INBTSerializable<CompoundNBT>)character).serializeNBT();
          }
          return (CompoundNBT) CharacterCapability.CHARACTER_CAPABILITY.writeNBT(character, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
          if (character instanceof INBTSerializable) {
            ((INBTSerializable<CompoundNBT>)character).deserializeNBT(nbt);
          }
          CharacterCapability.CHARACTER_CAPABILITY.readNBT(character, null, nbt);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
          return CharacterCapability.CHARACTER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> character));
        }
      });
    }
  }
}
