package com.mrcrayfish.device.jei;

import net.minecraft.item.DyeColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrCrayfish
 */
public class LaptopRecipeMaker
{
    public static List<LaptopRecipeWrapper> getLaptopRecipes()
    {
        List<LaptopRecipeWrapper> recipes = new ArrayList<LaptopRecipeWrapper>();
        for(DyeColor color : DyeColor.values())
        {
            recipes.add(new LaptopRecipeWrapper(color));
        }
        return recipes;
    }

    public LaptopRecipeMaker() {}
}
