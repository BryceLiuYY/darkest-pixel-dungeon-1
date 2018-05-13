package com.egoal.darkestpixeldungeon.items;

import com.egoal.darkestpixeldungeon.actors.hero.Hero;
import com.egoal.darkestpixeldungeon.messages.Messages;
import com.egoal.darkestpixeldungeon.sprites.ItemSpriteSheet;
import com.egoal.darkestpixeldungeon.utils.GLog;

import java.util.ArrayList;

/**
 * Created by 93942 on 5/13/2018.
 */

public class Humanity extends Item{
	private static final String AC_CONSUME	=	"CONSUME";
	
	private static final float TIME_TO_CONSUME	=	1f;
	
	{
		image	=	ItemSpriteSheet.DPD_HUMANITY;
		defaultAction	=	AC_CONSUME;
		
		stackable	=	true;
	}
	
	public Humanity(){
		super();
		identify();
	}
	
	@Override
	public ArrayList<String> actions(Hero hero){
		ArrayList<String> actions	=	super.actions(hero);
		actions.add(AC_CONSUME);
		
		return actions;
	}
	
	@Override
	public void execute(final Hero hero, String action){
		super.execute(hero, action);
		
		if(action==AC_CONSUME){
			//todo: add effects
			//0. recover sanity
			hero.SAN	-=	hero.SAN_MAX*0.7;
			if(hero.SAN<0)
				hero.SAN	=	0;
			
			//1. recover hp
			hero.HP	+=	hero.HT*0.3;
			if(hero.HP>hero.HT)
				hero.HP	=	hero.HT;

			curUser.spend(TIME_TO_CONSUME);
			curUser.busy();
			
			GLog.i(Messages.get(this, "used"));
		}
	}
	
}
