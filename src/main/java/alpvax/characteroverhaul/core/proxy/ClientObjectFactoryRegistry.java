package alpvax.characteroverhaul.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientObjectFactoryRegistry
{
	private static FactoryMapHolder data = new FactoryMapHolder();

	private static class FactoryMapHolder
	{
		private Map<String, FactoryMap<?>> data = new HashMap<>();
		
		private <T> FactoryMap<T> get(Class<T> type)
		{
			@SuppressWarnings("unchecked")
			FactoryMap<T> map = (FactoryMap<T>)data.get(type.getName());
			if(map == null)
			{
				map = new FactoryMap<>(type);
				data.put(type.getName(), map);
			}
			return map;
		}
	};
	
	private static class FactoryMap<T>
	{
		private final Class<T> type;
		private Map<String, Function<ICharacter, T>> map = new HashMap<>();
		
		private FactoryMap(Class<T> type)
		{
			this.type = type;
		}

		private void put(String key, Function<ICharacter, T> value)
		{
			map.put(key, value);
		}
		
		private Function<ICharacter, T> get(String key)
		{
			return map.get(key);
		}
	}


	public static Ability createAbility(@Nonnull String className, @Nonnull ICharacter character)
	{
		Function<ICharacter, Ability> func = data.get(Ability.class).get(className);
		if(func != null)
		{
			return func.apply(character);
		}
		try
		{
			return castClass(className, Ability.class).getConstructor(ICharacter.class).newInstance(character);
		}
		catch(ClassNotFoundException e)
		{
			FMLLog.log(Level.ERROR, e, "Unable to find Ability class %s on Client, is it annotated with @SideOnly?", className);
		}
		catch(ClassCastException e)
		{
			FMLLog.log(Level.ERROR, e, "Class %s on Client does not inherit from Ability", className);
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			FMLLog.log(Level.ERROR, e, "Unable to instantiate Ability on Client, could not access constructor %1$s(ICharacter)", className);
		}
		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			FMLLog.log(Level.ERROR, e, "Error trying to instantiate Ability class %s on Client", className);
		}
		return null;
	}

	private static <T> Class<? extends T> castClass(@Nonnull String className, @Nonnull Class<T> superClass) throws ClassNotFoundException, ClassCastException
	{
		Class<?> clazz = Class.forName(className);
		if(!superClass.isAssignableFrom(clazz))
		{
			throw new ClassCastException("Cannot cast " + className + " to " + superClass.getName());
		}
		return clazz.asSubclass(superClass);
	}

	public static <T> T create(@Nonnull String className, @Nonnull Class<T> objectType, @Nonnull ICharacter character)
	{
		Function<ICharacter, T> func = data.get(objectType).get(className);
		if(func != null)
		{
			return func.apply(character);
		}
		try
		{
			return castClass(className, objectType).getConstructor(ICharacter.class).newInstance(character);
		}
		catch(ClassNotFoundException e)
		{
			FMLLog.log(Level.ERROR, e, "Unable to find {} class {} on Client, is it annotated with @SideOnly?", objectType.getSimpleName(), className);
		}
		catch(ClassCastException e)
		{
			FMLLog.log(Level.ERROR, e, "Class {} on Client does not inherit from {}", className, objectType.getSimpleName());
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			FMLLog.log(Level.ERROR, e, "Unable to instantiate {} on Client, could not access constructor {}(ICharacter)", objectType.getSimpleName(), className);
		}
		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			FMLLog.log(Level.ERROR, e, "Error trying to instantiate {} class %s on Client", objectType.getSimpleName(), className);
		}
		return null;
	}

	public static <T> void addFactory(@Nonnull Class<T> objectType, @Nonnull String className, @Nonnull Function<ICharacter, T> factory)
	{
		data.get(objectType).put(className, factory);
	}
}
