package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by root on 17/02/18.
 */

public abstract class Item extends Sprite{

    //variables
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y) {
        this.screen=screen;
        this.world=screen.getWorld();
        setPosition(x,y);
        setBounds(getX(), getY(), 16 / MyGdxGame.PPM, 16/MyGdxGame.PPM);
        definerItem();
        toDestroy =false;
        destroyed =false;
    }//constructor

    public abstract void definerItem();
    public abstract void use(Mario mario);

    public void update(float dt) {
        if(toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed=true;
        }   //destroy the  item
    }

    public void draw(Batch batch) {
        if(!destroyed){
            super.draw(batch);
        }//get item if not destroyed
    }

    public void destroy() {
        toDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y){
        if(x) {
            velocity.x = -velocity.x;
        }
        if(y) {
            velocity.y = -velocity.y;
        }
    }//reverseif touch something


}//end class
