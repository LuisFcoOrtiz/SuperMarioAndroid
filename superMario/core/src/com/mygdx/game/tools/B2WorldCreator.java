package com.mygdx.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.sprites.Brick;
import com.mygdx.game.sprites.Coin;
import com.mygdx.game.sprites.Goomba;

/**
 * Created by root on 15/02/18.
 */

public class B2WorldCreator {

    private Array<Goomba> goombas;      //Enemies array

    public B2WorldCreator(PlayScreen screen) {
        ///screen given
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //*CREATION OF TEXTURES*//
        //creation of the bodies => 2
        for(MapObject object:map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ( (RectangleMapObject) object ).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;   //set gravitty for the body
            bdef.position.set( (rect.getX()+rect.getWidth() / 2) / MyGdxGame.PPM , (rect.getY()+rect.getHeight() /2) / MyGdxGame.PPM );

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fdef.shape =shape;
            //enemy
            fdef.filter.categoryBits = MyGdxGame.OBJECT_BIT;    //an enemy hit
            //end enemy
            body.createFixture(fdef);
        }
        //create pipes => 3
        for(MapObject object:map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ( (RectangleMapObject) object ).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;   //set gravitty for the body
            bdef.position.set( (rect.getX()+rect.getWidth() / 2) / MyGdxGame.PPM , (rect.getY()+rect.getHeight() /2) / MyGdxGame.PPM );

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fdef.shape =shape;
            body.createFixture(fdef);
        }
        //create brick => 5
        for(MapObject object:map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            //Rectangle rect = ( (RectangleMapObject) object ).getRectangle();

            new Brick(screen, object);  //brick creator
        }
        //create coins => 4
        for(MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            //Rectangle rect = ( (RectangleMapObject) object ).getRectangle();

            new Coin(screen, object);   //coin creator
        }
        //create goombas enemies => 6
        goombas = new Array<Goomba>();
        for(MapObject object:map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ( (RectangleMapObject) object ).getRectangle();
            //add gombas to their possitions
            goombas.add(new Goomba(screen, rect.getX() / MyGdxGame.PPM, rect.getY() / MyGdxGame.PPM));

        }

    }//end constructor

    public Array<Goomba> getGoombas() {
        return goombas;
    }//get the goombas


}//end class
