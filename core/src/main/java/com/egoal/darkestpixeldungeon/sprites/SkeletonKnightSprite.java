package com.egoal.darkestpixeldungeon.sprites;

import com.egoal.darkestpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

/**
 * Created by 93942 on 5/13/2018.
 */

public class SkeletonKnightSprite extends MobSprite {

  public SkeletonKnightSprite() {
    super();

    texture(Assets.SKELETON_KNIGHT);

    TextureFilm frames = new TextureFilm(texture, 12, 15);

    idle = new Animation(12, true);
    idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3);

    run = new Animation(15, true);
    run.frames(frames, 4, 5, 6, 7, 8, 9);

    attack = new Animation(15, false);
    attack.frames(frames, 14, 15, 16);

    die = new Animation(12, false);
    die.frames(frames, 10, 11, 12, 13);

    play(idle);
  }

  @Override
  public void die() {
    super.die();
  }

  @Override
  public int blood() {
    return 0xFFcccccc;
  }
}
