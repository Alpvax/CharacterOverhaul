package alpvax.mc.characteroverhaul.character.capability;

import alpvax.mc.characteroverhaul.character.ICharacter;
import alpvax.mc.characteroverhaul.character.race.RaceManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CharacterCapability {
  @CapabilityInject(ICharacter.class)
  public static Capability<ICharacter> CHARACTER_CAPABILITY = null;

  public static void register() {
    CapabilityManager.INSTANCE.register(
        ICharacter.class,
        new Capability.IStorage<ICharacter>() {
          @Override
          public INBT writeNBT(Capability<ICharacter> capability, ICharacter instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("skills", instance.getSkills().serializeNBT());
            nbt.put("race", RaceManager.toNBT(instance.getRace()));
            return nbt;
          }

          @Override
          public void readNBT(Capability<ICharacter> capability, ICharacter instance, Direction side, INBT nbt) {
            instance.getSkills().deserializeNBT(((CompoundNBT) nbt).getCompound("skills"));
            instance.setRace(RaceManager.deserialize(((CompoundNBT) nbt).getCompound("race")));
          }
        },
        () -> null
    );
  }
}
