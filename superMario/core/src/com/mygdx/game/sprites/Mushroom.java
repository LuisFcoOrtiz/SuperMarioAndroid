package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by root on 17/02/18.
 */

public class Mushroom extends Item {

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        //show the mushroom
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.5f,0);    //movement of mushroom and velocity
    }//end constructor

    @Override
    public void definerItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());       //position initial for MUSHROM
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MyGdxGame.PPM);      //shape of mario

        fdef.filter.categoryBits = MyGdxGame.ITEM_BIT;  //where is the sprite
        //item collide with
        fdef.filter.maskBits = MyGdxGame.MARIO_BIT | MyGdxGame.OBJECT_BIT
                | MyGdxGame.GROUND_BIT | MyGdxGame.COIN_BIT | MyGdxGame.BRICK_BIT;

        fdef.shape=shape;
        body.createFixture(fdef).setUserData(this);


    }//define the mushrom

    @Override
    public void use(Mario mario) {
        destroy();          //when you take it is destroyed
        try {
            mario.grow();       //growing mario
        } catch(NullPointerException ex) {
            MyGdxGame.manager.get("powerup.wav", Sound.class).play();  //play sound
            Gdx.app.log("ERROR!!","MARIO IS NULL");
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        body.setLinearVelocity(velocity);
        //move the mushroom
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
