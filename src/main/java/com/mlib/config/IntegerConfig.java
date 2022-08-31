package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class IntegerConfig extends NumberConfig< Integer > {
	public IntegerConfig( String name, String comment, boolean worldRestartRequired, int defaultValue, int min, int max ) {
		super( name, comment, worldRestartRequired, defaultValue, min, max );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.defineInRange( this.name, this.defaultValue, this.min, this.max );
	}
}

