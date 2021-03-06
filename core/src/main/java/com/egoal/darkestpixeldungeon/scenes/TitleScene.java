/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.egoal.darkestpixeldungeon.scenes;

import android.opengl.GLES20;

import com.egoal.darkestpixeldungeon.Assets;
import com.egoal.darkestpixeldungeon.DarkestPixelDungeon;
import com.egoal.darkestpixeldungeon.Dungeon;
import com.egoal.darkestpixeldungeon.effects.BannerSprites;
import com.egoal.darkestpixeldungeon.effects.Fireball;
import com.egoal.darkestpixeldungeon.messages.Messages;
import com.egoal.darkestpixeldungeon.ui.Archs;
import com.egoal.darkestpixeldungeon.ui.ChangesButton;
import com.egoal.darkestpixeldungeon.ui.ExitButton;
import com.egoal.darkestpixeldungeon.windows.WndSettings;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

import javax.microedition.khronos.opengles.GL10;

public class TitleScene extends PixelScene {

  @Override
  public void create() {

    super.create();

    Music.INSTANCE.play(Assets.THEME, true);
    Music.INSTANCE.volume(DarkestPixelDungeon.musicVol() / 10f);

    uiCamera.visible = false;

    int w = Camera.main.width;
    int h = Camera.main.height;

    Archs archs = new Archs();
    archs.setSize(w, h);
    add(archs);

    // modified as darkest pixel dungeon
    Image title = BannerSprites.get(BannerSprites.Type.DPD_PIXEL_DUNGEON);
    add(title);

    float topRegion = Math.max(115f, h * 0.45f);

    title.x = (w - title.width()) / 2f;
    if (DarkestPixelDungeon.landscape())
      title.y = (topRegion - title.height()) / 2f;
    else
      title.y = 8 + (topRegion - title.height() - 16) / 2f;

    align(title);

    // torches beside the title
    placeTorch(title.x + 22, title.y + 55);
    placeTorch(title.x + title.width - 22, title.y + 55);

    Image signs = new Image(BannerSprites.get(BannerSprites.Type
            .DPD_PIXEL_DUNGEON_SIGNS)) {
      private float time = 0;

      @Override
      public void update() {
        super.update();
        am = (float) Math.sin(-(time += Game.elapsed));
      }

      @Override
      public void draw() {
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
        super.draw();
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
      }
    };
    signs.x = title.x + (title.width() - signs.width()) / 2f;
    signs.y = title.y;
    add(signs);

    // main buttons
    DashboardItem[] btnsMain = new DashboardItem[]{
            new DashboardItem(Messages.get(this, "play"), 0) {
              @Override
              protected void onClick() {
                DarkestPixelDungeon.switchNoFade(StartScene.class);

              }
            },
            new DashboardItem(Messages.get(this, "rankings"), 2) {
              @Override
              protected void onClick() {
                DarkestPixelDungeon.switchNoFade(RankingsScene.class);
              }
            },
            new DashboardItem(Messages.get(this, "badges"), 3) {
              @Override
              protected void onClick() {
                DarkestPixelDungeon.switchNoFade(BadgesScene.class);
              }
            },
            new DashboardItem(Messages.get(this, "about"), 1) {
              @Override
              protected void onClick() {
                DarkestPixelDungeon.switchNoFade(AboutScene.class);
              }
            },
            new DashboardItem(Messages.get(this, "guide"), 4) {
              @Override
              protected void onClick() {
                DarkestPixelDungeon.switchNoFade(GuideScene.class);
              }
            },
            new DashboardItem(Messages.get(this, "settings"), 5) {
              @Override
              protected void onClick() {
                parent.add(new WndSettings());
              }
            },
    };
    // align main buttons
    {
      final float btnHeight = btnsMain[0].height();
      final float btnWidth = btnsMain[0].width();
      final float btnGap = 1;
      final int btnRows = 4;
      for (int i = 0; i < btnsMain.length; ++i) {
        add(btnsMain[i]);

        int col = i / btnRows;
        int row = i % btnRows;
        btnsMain[i].setPos((w / 2 - btnWidth) / 2 + w / 2 * col, topRegion +
                2 + (btnHeight + btnGap) * row);
      }
    }

    // version & changes
    // add sdp version
    BitmapText sdpVersion = new BitmapText("v 0.4.2b", pixelFont);
    sdpVersion.measure();
    sdpVersion.hardlight(0x888888);
    sdpVersion.x = w - sdpVersion.width();
    sdpVersion.y = h - sdpVersion.height();
    add(sdpVersion);

    BitmapText version = new BitmapText("version " + Game.version + "",
            pixelFont);
    version.measure();
    version.hardlight(0xCCCCCC);
    version.x = w - version.width();
    version.y = h - sdpVersion.height() - version.height();
    add(version);

    ChangesButton changes = new ChangesButton();
    changes.setPos(w - changes.width() -1, h - sdpVersion.height() -
            version.height() - changes.height() -1);
    add(changes);

    changes.setBlink(
            DarkestPixelDungeon.version() != DarkestPixelDungeon.versionCode ||
                    !DarkestPixelDungeon.changeListChecked()
    );

    // exit
    ExitButton btnExit = new ExitButton();
    btnExit.setPos(w - btnExit.width(), 0);
    add(btnExit);

    fadeIn();
  }

  private void placeTorch(float x, float y) {
    Fireball fb = new Fireball();
    fb.setPos(x, y);
    add(fb);
  }

  private static class DashboardItem extends Button {

    public static final float BTN_WIDTH = 48;
    public static final float BTN_HEIGHT = 24;

    private static final int IMAGE_SIZE = 16;
    private static final int FONT_SIZE = 8;

    private Image image;
    private RenderedText label;

    public DashboardItem(String text, int index) {
      super();

      image.frame(image.texture.uvRect(index * IMAGE_SIZE, 0, (index + 1) *
              IMAGE_SIZE, IMAGE_SIZE));
      this.label.text(text);

      setSize(BTN_WIDTH, BTN_HEIGHT);
    }

    @Override
    protected void createChildren() {
      super.createChildren();

      image = new Image(Assets.DASHBOARD);
      add(image);

      label = renderText(FONT_SIZE);
      add(label);
    }

    @Override
    protected void layout() {
      super.layout();

      image.x = x + (width - image.width()) / 2 - FONT_SIZE;
      image.y = y;
      align(image);

      label.x = image.x + image.width + 2;
      label.y = y + 4;
      align(label);
    }

    @Override
    protected void onTouchDown() {
      image.brightness(1.5f);
      Sample.INSTANCE.play(Assets.SND_CLICK, 1, 1, 0.8f);
    }

    @Override
    protected void onTouchUp() {
      image.resetColor();
    }
  }
}
