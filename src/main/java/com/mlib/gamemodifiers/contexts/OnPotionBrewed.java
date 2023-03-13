package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPotionBrewed {
	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onPotionBrewed( PlayerBrewedPotionEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ILevelData {
		public final PlayerBrewedPotionEvent event;
		public final Player player;
		public final ItemStack itemStack;

		public Data( PlayerBrewedPotionEvent event ) {
			this.event = event;
			this.player = event.getEntity();
			this.itemStack = event.getStack();
		}

		@Override
		public Level getLevel() {
			return null;
		}
	}
}
