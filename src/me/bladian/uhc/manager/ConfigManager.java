package me.bladian.uhc.manager;

/**
 * Created by BladianYT
 */
public class ConfigManager
{

    private boolean nether = false;
    private boolean speed1 = false;
    private boolean speed2 = false;
    private boolean strength1 = false;
    private boolean strength2 = false;

    private boolean enderpearlDamage = false;

    private boolean horseHealing = false;

    private boolean godApples = false;

    private boolean shears = true;


    public boolean isNether()
    {
        return nether;
    }

    public void setNether(boolean nether)
    {
        this.nether = nether;
    }

    public boolean isSpeed1()
    {
        return speed1;
    }

    public void setSpeed1(boolean speed1)
    {
        this.speed1 = speed1;
    }

    public boolean isSpeed2()
    {
        return speed2;
    }

    public void setSpeed2(boolean speed2)
    {
        this.speed2 = speed2;
    }

    public boolean isStrength1()
    {
        return strength1;
    }

    public void setStrength1(boolean strength1)
    {
        this.strength1 = strength1;
    }

    public boolean isStrength2()
    {
        return strength2;
    }

    public void setStrength2(boolean strength2)
    {
        this.strength2 = strength2;
    }

    public boolean isEnderpearlDamage()
    {
        return enderpearlDamage;
    }

    public void setEnderpearlDamage(boolean enderpearlDamage)
    {
        this.enderpearlDamage = enderpearlDamage;
    }

    public boolean isHorseHealing()
    {
        return horseHealing;
    }

    public void setHorseHealing(boolean horseHealing)
    {
        this.horseHealing = horseHealing;
    }

    public boolean isGodApples()
    {
        return godApples;
    }

    public void setGodApples(boolean godApples)
    {
        this.godApples = godApples;
    }

    public boolean isShears()
    {
        return shears;
    }

    public void setShears(boolean shears)
    {
        this.shears = shears;
    }

    private int spectatorRadius = 500;

    public int getSpectatorRadius()
    {
        return spectatorRadius;
    }

    public void setSpectatorRadius(int spectatorRadius)
    {
        this.spectatorRadius = spectatorRadius;
    }
}
