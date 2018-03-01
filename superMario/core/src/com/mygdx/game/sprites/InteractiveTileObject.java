package com.mygdx.game.sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by root on 15/02/18.
 */

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    protected PlayScreen screen;
    protected MapObject object;

    /*GENERIC CLASS FOR ITEMS*/
    public InteractiveTileObject(PlayScreen screen, MapObject object) {
        this.object=object;
        this.screen=screen;
        this.world=screen.getWorld();
        this.map=screen.getMap();
        this.bounds=((RectangleMapObject) object ).getRectangle();

        //create the items
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        //Rectangle rect = ( (RectangleMapObject) object ).getRectangle();

        bdef.type = BodyDef.BodyType.StaticBody;   //set gravitty for the body
        bdef.position.set( (bounds.getX()+bounds.getWidth() / 2) / MyGdxGame.PPM , (bounds.getY()+bounds.getHeight() /2) / MyGdxGame.PPM );

        body = world.createBody(bdef);
        shape.setAsBox(bounds.getWidth() / 2 / MyGdxGame.PPM, bounds.getHeight() / 2 / MyGdxGame.PPM);
        fdef.shape =shape;
        fixture = body.createFixture(fdef);


    }//end constructor

    public abstract void onHeadHit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }//method to interactuate with items

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * MyGdxGame.PPM / 16),
                (int)(body.getPosition().y * MyGdxGame.PPM / 16));
    }//get the position of the item who was hit

}//end class
