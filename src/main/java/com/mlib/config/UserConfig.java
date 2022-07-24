package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public abstract class UserConfig {
	protected final String name;
	protected final String comment;

	public UserConfig( String name, String comment ) {
		this.name = name;
		this.comment = comment;
	}

	public String getName() {
		return this.name;
	}

	public String getComment() {
		return this.comment;
	}

	abstract public void build( ForgeConfigSpec.Builder builder );
}
