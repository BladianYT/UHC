package me.bladian.uhc.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public enum BorderSize
{
    B3500(3500),
    B3000(3000),
    B2500(2500),
    B2000(2000),
    B1500(1500),
    B1000(1000),
    B500(500),
    B250(250),
    B100(100),
    B50(50),
    B25(25);

    private int size;

    public int getSize()
    {
        return size;
    }

    BorderSize(int size)
    {
        this.size = size;
    }

    public static BorderSize getBorderByNumber(int number, int starting)
    {
        List<BorderSize> borderSizes = new ArrayList<>();
        borderSizes.addAll(Arrays.asList(BorderSize.values()));
        int border = number+starting;
        return borderSizes.get(border);
    }
}
