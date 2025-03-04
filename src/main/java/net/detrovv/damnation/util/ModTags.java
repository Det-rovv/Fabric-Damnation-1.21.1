package net.detrovv.damnation.util;

import net.detrovv.damnation.Damnation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags
{
    public static class Enchantments
    {
        public static final TagKey<Enchantment> ANCIENT_CURSES =
                createTag("ancient_curses");

        private static TagKey<Enchantment> createTag(String name)
        {
            return TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Damnation.MOD_ID, name));
        }
    }
}
