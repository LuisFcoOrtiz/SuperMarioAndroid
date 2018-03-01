package com.mygdx.game.sprites;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by root on 17/02/18.
 */

public class ItemDef  {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position=position;
        this.type =type;
    }//end ocnstructor

}
