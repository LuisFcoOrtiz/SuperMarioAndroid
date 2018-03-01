package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.sprites.Enemy;
import com.mygdx.game.sprites.InteractiveTileObject;
import com.mygdx.game.sprites.Item;
import com.mygdx.game.sprites.Mario;

/**
 * Created by root on 15/02/18.
 */

public class WorldContactListener implements ContactListener {
    //check all contacts in the world
    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("CONTACT","BEGIN CONTACT");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;
            //head with something
            Gdx.app.log("CONTACT","HEAD CONTACT");


            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                Gdx.input.vibrate(250); //vibrate head hit
                //hit on the head
                ((InteractiveTileObject) object.getUserData()).onHeadHit();

            }//check interactive items object
        }//mario collipsion

        switch (cDef) {//check all collides
            case MyGdxGame.ENEMY_HEAD_BIT | MyGdxGame.MARIO_BIT: //Mario destroy an enemy
                if (fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_HEAD_BIT) {
                    ((Enemy)fixA.getUserData()).hitOnhead();
                } else {
                    ((Enemy)fixB.getUserData()).hitOnhead();
                }
                break;
            case MyGdxGame.ENEMY_BIT | MyGdxGame.OBJECT_BIT: //enemies collide with object
                if (fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT) {
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                } else {
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                }
                break;
            case MyGdxGame.MARIO_BIT | MyGdxGame.ENEMY_BIT: //EVENT WHEN MARIO TOUCH AN ENEMY
                Gdx.input.vibrate(500); //vibration when you are dead
                Hud.restScore(1000);
                Gdx.app.log("EVENT","YOU ARE DEAD");
                break;
            case MyGdxGame.ENEMY_BIT | MyGdxGame.ENEMY_BIT: //Enemies collide themselves
                ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case MyGdxGame.ITEM_BIT | MyGdxGame.OBJECT_BIT: //Item collide with object
                if (fixA.getFilterData().categoryBits == MyGdxGame.ITEM_BIT) {
                    ((Item)fixA.getUserData()).reverseVelocity(true,false);
                } else {
                    ((Item)fixB.getUserData()).reverseVelocity(true,false);
                }
                break;
            case MyGdxGame.ITEM_BIT | MyGdxGame.MARIO_BIT: //Item collide with Mario
                if (fixA.getFilterData().categoryBits == MyGdxGame.ITEM_BIT) {
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData()); //get the mario
                } else {
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
                }
                break;

        }//check enemies interactuations

    } //end begin the connection

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("CONTACT","END CONTACT");
        //Gdx.input.cancelVibrate();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}//end class
