package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by root on 15/02/18.
 */

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN= 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");

        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.COIN_BIT);

    }//end constructor

    @Override
    public void onHeadHit() {
        Gdx.app.log("COIN","Coin collision");
        if(getCell().getTile().getId() == BLANK_COIN) { //sounds effects and score

            MyGdxGame.manager.get("bump.wav", Sound.class).play();
        } else {
            if(object.getProperties().containsKey("mushroom")) {    //if the coin has mushroom
                //spawn the mushroom
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MyGdxGame.PPM),
                        Mushroom.class) );
                MyGdxGame.manager.get("powerup_spawn.wav", Sound.class).play();  //play sound
                Hud.addScore(1000);  //add score  if is mushroom
            } else {
                MyGdxGame.manager.get("coin.wav", Sound.class).play();  //play sound
                Hud.addScore(500);  //add score
            }
        }//check if hit the coin or was hitten
        getCell().setTile(tileSet.getTile(BLANK_COIN));

    }

}//end class
