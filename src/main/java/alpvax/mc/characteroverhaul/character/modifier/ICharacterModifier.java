package alpvax.mc.characteroverhaul.character.modifier;

import alpvax.mc.characteroverhaul.character.ICharacter;

public interface ICharacterModifier {
  void apply(ICharacter character);

  void remove(ICharacter character);
}
