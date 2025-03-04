package net.detrovv.damnation;

import net.detrovv.damnation.block.ModBlocks;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Damnation implements ModInitializer
{
	public static final String MOD_ID = "damnation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		ModBlocks.registerModBlocks();
	}
}