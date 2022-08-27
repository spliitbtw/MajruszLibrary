package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class OnBabySpawn {
	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final List< Context > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

		public Context( Consumer< Data > consumer, ContextParameters params ) {
			super( Data.class, consumer, params );
			ContextBase.addSorted( CONTEXTS, this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, new ContextParameters() );
		}

		@SubscribeEvent
		public static void onBreed( BabyEntitySpawnEvent event ) {
			ContextBase.accept( CONTEXTS, new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< BabyEntitySpawnEvent > {
		public final AgeableMob child;
		public final Mob parentA;
		public final Mob parentB;
		public final Player player;

		public Data( BabyEntitySpawnEvent event ) {
			super( event.getChild(), event );
			this.child = event.getChild();
			this.parentA = event.getParentA();
			this.parentB = event.getParentB();
			this.player = event.getCausedByPlayer();
		}
	}
}