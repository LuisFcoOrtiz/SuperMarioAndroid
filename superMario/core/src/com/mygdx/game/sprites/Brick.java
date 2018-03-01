package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.screens.PlayScreen;


/**
 * Created by root on 15/02/18.
 */

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, MapObject object) {

        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("BRICK","BRICK collision");
        setCategoryFilter(MyGdxGame.DESTROYED_BIT); //destroy the brick
        getCell().setTile(null);    //drop the brick hitten
        Hud.addScore(200);      //add 200 points
        //music
        MyGdxGame.manager.get("breakblock.wav", Sound.class).play();    //start the sound

    }//mario hit here
}//end Brick
