package me.bladian.uhc.combat;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityTypes;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by BladianYT
 */
@SuppressWarnings("unchecked")
public class MobUtil
{

    private static MobUtil instance;

    public static MobUtil getInstance()
    {
        if (instance == null)
        {
            instance = new MobUtil();
        }
        return instance;
    }

    private static Object getPrivateField(String fieldName, Class<EntityTypes> oclass)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
    {
        Field field = oclass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(null);
    }

    public void registerEntityEdit(Class<CombatSkeleton> entityClass, @SuppressWarnings("SameParameterValue") int entityId)
    {
        try
        {
            ((Map) getPrivateField("c", EntityTypes.class)).put("CombatZombie", entityClass);
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        try
        {
            ((Map) getPrivateField("d", EntityTypes.class)).put(entityClass, "CombatZombie");
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        try
        {
            ((Map) getPrivateField("f", EntityTypes.class)).put(entityClass, entityId);
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        try
        {
            ((Map) getPrivateField("g", EntityTypes.class)).put("CombatZombie", entityId);
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    public void registerEntity(Class<PlaceHolder> entityClass, @SuppressWarnings("SameParameterValue") int entityId)
    {
        try
        {
            ((Map) getPrivateField("c", EntityTypes.class)).put("PlaceHolder", entityClass);
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        try
        {
            ((Map) getPrivateField("d", EntityTypes.class)).put(entityClass, "PlaceHolder");
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        try
        {
            ((Map) getPrivateField("f", EntityTypes.class)).put(entityClass, entityId);
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        try
        {
            ((Map) getPrivateField("g", EntityTypes.class)).put("CombatZombie", entityId);
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    public static void spawnEntity(Entity entity, Location loc)
    {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
    }
}
