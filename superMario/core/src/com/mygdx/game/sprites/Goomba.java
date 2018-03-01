package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.PlayScreen;

import sun.net.ftp.FtpDirEntry;

/**
 * Created by root on 16/02/18.
 */

public class Goomba extends Enemy {

    //animations
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i=0; i<2; i++) {
            //movement of goomba
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0,16,16));
        }
        walkAnimation = new Animation(0.4f,frames);
        stateTime = 0;
        setBounds(getX(),getY(),16 / MyGdxGame.PPM,16 / MyGdxGame.PPM);

        setToDestroy=false;
        destroyed = false;
    }//end constructor

    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);  //remove goomba
            destroyed=true;
            //item goomba destroyed
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32,0,16,16) );
            //destroy the body
            stateTime = 0;

        }else if (!destroyed) {//isnt destroyed
            b2body.setLinearVelocity(velocity);             //move goomba
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
            //looping the enemy animation
        }//check if goomba is destroyed

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX()-15,getY());       //position initial for goomba
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MyGdxGame.PPM);      //shape of mario
        //set the category to collide with
        fdef.filter.categoryBits = MyGdxGame.ENEMY_BIT;
        //what can mario hits
        fdef.filter.maskBits = MyGdxGame.GROUND_BIT | MyGdxGame.COIN_BIT | MyGdxGame.BRICK_BIT |
                    MyGdxGame.ENEMY_BIT | MyGdxGame.OBJECT_BIT | MyGdxGame.MARIO_BIT; //collect with enemies

        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);
        //head of goomba to hit him
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5,8).scl(1 / MyGdxGame.PPM);
        vertice[1] = new Vector2(5,8).scl(1 / MyGdxGame.PPM);
        vertice[2] = new Vector2(-3,3).scl(1 / MyGdxGame.PPM);
        vertice[3] = new Vector2(5,3).scl(1 / MyGdxGame.PPM);

        head.set(vertice);
        //shape of goomba
        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MyGdxGame.ENEMY_HEAD_BIT;    //enemy shape
        b2body.createFixture(fdef).setUserData(this);           //listener for the hit


    }//end define

    public void draw(Batch batch) {
        if(!destroyed || stateTime < 1) {
            super.draw(batch);  //delete gumba when is destroyed
        }
    }

    @Override
    public void hitOnhead() {
        setToDestroy = true;
    }


}//end class
