package com.mlib.gamemodifiers;

import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.BooleanConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.entities.EntityHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

public abstract class Condition {
	public static final Comparator< Condition > COMPARATOR = ( left, right ) -> Mth.sign( left.priority.ordinal() - right.priority.ordinal() );
	public enum Priority {
		LOWEST, LOW, NORMAL, HIGH, HIGHEST
	}

	final Priority priority;

	public Condition( Priority priority ) {
		this.priority = priority;
	}

	public Condition() {
		this( Priority.NORMAL );
	}

	public abstract boolean check( GameModifier feature, com.mlib.gamemodifiers.Context.Data data );

	public void setup( ConfigGroup group ) {}

	public static class Excludable extends Condition {
		final BooleanConfig availability;

		public Excludable() {
			super( Priority.HIGHEST );
			this.availability = new BooleanConfig( "is_enabled", "Specifies whether this game modifier is enabled.", false, true );
		}

		@Override
		public void setup( ConfigGroup group ) {
			group.addConfig( this.availability );
		}

		@Override
		public boolean check( GameModifier gameModifier, com.mlib.gamemodifiers.Context.Data data ) {
			return this.availability.isEnabled();
		}
	}

	public static class Chance extends Condition {
		final DoubleConfig chance;

		public Chance( double defaultChance ) {
			super( Priority.HIGH );
			this.chance = new DoubleConfig( "chance", "Chance of this to happen.", false, defaultChance, 0.0, 1.0 );
		}

		@Override
		public void setup( ConfigGroup group ) {
			group.addConfig( this.chance );
		}

		@Override
		public boolean check( GameModifier gameModifier, com.mlib.gamemodifiers.Context.Data data ) {
			return Random.tryChance( this.chance.get() );
		}
	}

	public static class Context< DataType extends com.mlib.gamemodifiers.Context.Data > extends Condition {
		final Class< DataType > dataClass;
		final Predicate< DataType > predicate;

		public Context( Class< DataType > dataClass, Predicate< DataType > predicate ) {
			super( Priority.LOW );
			this.dataClass = dataClass;
			this.predicate = predicate;
		}

		@Override
		public boolean check( GameModifier gameModifier, com.mlib.gamemodifiers.Context.Data data ) {
			DataType contextData = Utility.castIfPossible( this.dataClass, data );
			assert contextData != null;

			return this.predicate.test( contextData );
		}
	}

	public static class IsLivingBeing extends Condition {
		@Override
		public boolean check( GameModifier feature, com.mlib.gamemodifiers.Context.Data data ) {
			return EntityHelper.isAnimal( data.entity ) || EntityHelper.isHuman( data.entity );
		}
	}

	public static class ArmorDependentChance extends Condition {
		@Override
		public boolean check( GameModifier feature, com.mlib.gamemodifiers.Context.Data data ) {
			return Random.tryChance( getChance( data.entity ) );
		}

		private double getChance( @Nullable LivingEntity entity ) {
			if( entity == null )
				return 1.0;

			MutableInt armorCount = new MutableInt( 0 );
			entity.getArmorSlots().forEach( itemStack->{
				if( !itemStack.isEmpty() )
					armorCount.add( 1 );
			} );
			return switch( armorCount.getValue() ) {
				default -> 1.0;
				case 1 -> 0.7;
				case 2 -> 0.49;
				case 3 -> 0.34;
				case 4 -> 0.24;
			};
		}
	}
}
