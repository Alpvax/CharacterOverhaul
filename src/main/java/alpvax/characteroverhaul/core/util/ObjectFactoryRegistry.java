package alpvax.characteroverhaul.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import alpvax.characteroverhaul.api.CharacterOverhaul;
import alpvax.characteroverhaul.api.character.ICharacter;

public class ObjectFactoryRegistry
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
				map = new FactoryMap<>();//(type);
				data.put(type.getName(), map);
			}
			return map;
		}
	};
	
	private static class FactoryMap<T>
	{
		private Map<String, Function<ICharacter, T>> map = new HashMap<>();
		
		/*private FactoryMap(Class<T> type)
		{
			this.type = type;
		}*/

		private void put(String key, Function<ICharacter, T> value)
		{
			map.put(key, value);
		}
		
		private Function<ICharacter, T> get(String key)
		{
			return map.get(key);
		}
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
		if(func == null)
		{
			try
			{
				Constructor<? extends T> ctor = castClass(className, objectType).getConstructor(ICharacter.class);
				func = (c) -> {
					try
					{
						return ctor.newInstance(c);
					}
					catch(InstantiationException
							| IllegalAccessException
							| IllegalArgumentException
							| InvocationTargetException e)
					{
						CharacterOverhaul.log(Level.ERROR, e, "Error trying to instantiate %s class %s on Client", objectType.getSimpleName(), className);
					}
					return null;
				};
				data.get(objectType).put(className, func);
			}
			catch(ClassNotFoundException e)
			{
				CharacterOverhaul.log(Level.ERROR, e, "Unable to find %s class %s on Client, is it annotated with @SideOnly?", objectType.getSimpleName(), className);
			}
			catch(ClassCastException e)
			{
				CharacterOverhaul.log(Level.ERROR, e, "Class %s on Client does not inherit from %s", className, objectType.getSimpleName());
			}
			catch(NoSuchMethodException | SecurityException e)
			{
				CharacterOverhaul.log(Level.ERROR, e, "Unable to instantiate %s on Client, could not access constructor %s(ICharacter)", objectType.getSimpleName(), className);
			}
		}
		return func.apply(character);
	}

	public static <T> void addFactory(@Nonnull Class<T> objectType, @Nonnull String className, @Nonnull Function<ICharacter, T> factory)
	{
		data.get(objectType).put(className, factory);
	}
}
