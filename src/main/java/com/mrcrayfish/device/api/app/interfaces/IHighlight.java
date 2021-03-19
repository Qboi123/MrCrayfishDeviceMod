package com.mrcrayfish.device.api.app.interfaces;

import net.minecraft.util.text.TextFormatting;

/**
 * @author MrCrayfish
 */
public interface IHighlight
{
    TextFormatting[] getKeywordFormatting(String text);
}
