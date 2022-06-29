package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

public class OnItemFishedData extends ContextData {
	public final ItemFishedEvent event;
	public final Player player;
	public final NonNullList< ItemStack > drops;

	public OnItemFishedData( ItemFishedEvent event ) {
		super( event.getEntityLiving() );
		this.event = event;
		this.player = event.getPlayer();
		this.drops = event.getDrops();
	}
}