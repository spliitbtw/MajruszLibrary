package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnPreDamaged {
	public static final Predicate< Data > DIRECT_DAMAGE = data->data.source.getDirectEntity() == data.attacker;
	public static final Predicate< Data > DEALT_ANY_DAMAGE = data->data.event.getAmount() > 0.0f;

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
		public static void onPreDamaged( LivingAttackEvent event ) {
			Data data = new Data( event );
			if( !willBeCancelled( data ) ) {
				ContextBase.accept( CONTEXTS, data );
			}
		}

		private static boolean willBeCancelled( Data data ) {
			boolean isInvulnerable = data.target.isInvulnerableTo( data.source );
			boolean isClientSide = data.level == null;
			boolean isDeadOrDying = data.target.isDeadOrDying();
			boolean isFireResistant = data.source.isFire() && data.target.hasEffect( MobEffects.FIRE_RESISTANCE );
			boolean hasInvulnerabilityTicks = data.target.invulnerableTime > 10.0f && dealtLessDamage( data );

			return isInvulnerable || isClientSide || isDeadOrDying || isFireResistant || hasInvulnerabilityTicks;
		}

		private static boolean dealtLessDamage( Data data ) {
			CombatEntry entry = data.target.getCombatTracker().getLastEntry();

			return entry != null && entry.getDamage() >= data.event.getAmount();
		}
	}

	public static class Data extends ContextData.Event< LivingAttackEvent > {
		public final DamageSource source;
		@Nullable public final LivingEntity attacker;
		public final LivingEntity target;

		public Data( LivingAttackEvent event ) {
			super( event.getEntity(), event );
			this.source = event.getSource();
			this.attacker = Utility.castIfPossible( LivingEntity.class, source.getEntity() );
			this.target = event.getEntity();
		}
	}
}