package me.bladian.uhc.combat;

import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.World;

/**
 * Created by BladianYT
 */
public class PlaceHolder extends EntityHorse
{


    //See if I can do this better eventually

    public PlaceHolder(World world) {
        super(world);
        this.setInvisible(true);
    }


    @Override
    public void makeSound(String s, float f, float f1)
    {
    }

    @Override
    public void e()
    {
    }
}
