package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by root on 16/02/18.
 */

public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;

    public Vector2 velocity;

    public Body b2body;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(1,0);
        b2body.setActive(false);        //stop enemi until mario see him

    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnhead();

    public void reverseVelocity(boolean x, boolean y){
        if(x) {
            velocity.x = -velocity.x;
        }
        if(y) {
            velocity.y = -velocity.y;
        }
    }//move the enemy


}//end class
