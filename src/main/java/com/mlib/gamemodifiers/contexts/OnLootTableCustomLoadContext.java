package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnLootTableCustomLoadData;
import com.mlib.gamemodifiers.parameters.ContextParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnLootTableCustomLoadContext extends ContextBase< OnLootTableCustomLoadData > {
	static final List< OnLootTableCustomLoadContext > CONTEXTS = new ArrayList<>();

	public OnLootTableCustomLoadContext( Consumer< OnLootTableCustomLoadData > consumer, ContextParameters params ) {
		super( OnLootTableCustomLoadData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnLootTableCustomLoadContext( Consumer< OnLootTableCustomLoadData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	public static void broadcast( OnLootTableCustomLoadData data ) {
		ContextBase.accept( CONTEXTS, data );
	}
}
