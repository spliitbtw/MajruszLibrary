package com.mlib.time;

import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnServerTick;
import com.mlib.gamemodifiers.parameters.Priority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Anim {
	static final List< IAnimation > PENDING_ANIMS = Collections.synchronizedList( new ArrayList<>() );
	static final List< IAnimation > ANIMS = Collections.synchronizedList( new ArrayList<>() );

	public static void delay( Consumer< Delay > callback, int ticks ) {
		setup( new Delay( callback, ticks ) );
	}

	public static void nextTick( Runnable callback ) {
		delay( delay->callback.run(), 1 );
	}

	private static < Type extends IAnimation > Type setup( Type anim ) {
		PENDING_ANIMS.add( anim );
		anim.onStart();

		return anim;
	}

	@AutoInstance
	public static class Updater extends GameModifier {
		public Updater() {
			new OnServerTick.Context( this::updateAnims )
				.priority( Priority.HIGHEST )
				.addCondition( TimeHelper::isEndPhase );
		}

		private void updateAnims( OnServerTick.Data data ) {
			synchronized( PENDING_ANIMS ) {
				ANIMS.addAll( PENDING_ANIMS );
				PENDING_ANIMS.clear();
			}
			for( Iterator< IAnimation > iterator = ANIMS.iterator(); iterator.hasNext(); ) {
				IAnimation anim = iterator.next();
				anim.onTick();
				if( anim.isFinished() ) {
					anim.onEnd();
					iterator.remove();
				}
			}
		}
	}
}
