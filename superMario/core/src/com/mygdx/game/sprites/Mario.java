package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by root on 15/02/18.
 */

public class Mario extends Sprite {
    public World world;
    public Body b2body;
    //sprites for mario
    public TextureRegion marioStand;
    //animations
    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING};
    public State currentState;
    public State previousState;
    private Animation marioRun;
    private TextureRegion marioJump;
    //growing mario animations
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;

    public float stateTimer;
    public boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    public boolean marioIsDead;


    public Mario(PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));        //get the little mario sprite

        this.world=screen.getWorld();
        //animate mario
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        //run animation
        for(int i=1; i<4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 10,16,16 ) );
        }
        marioRun = new Animation(0.1f, frames);

        frames.clear();
        for(int i=1; i<4; i++) {    //big mario run animation
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 10,16,32 ) );
        }
        bigMarioRun= new Animation(0.1f, frames);

        frames.clear();

        //get animation frames to grow mario
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32));
        growMario = new Animation(0.2f, frames);

        //jump animation
        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80,0,16,16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80,0,16,32);

        //get the sprite
        marioStand = new TextureRegion(getTexture(),0,10,16,16);
        //get the sprite bigmario
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32);


        //define mario
        defineMario();
        setBounds(0,0,16 / MyGdxGame.PPM,16 / MyGdxGame.PPM);
        setRegion(marioStand);  //associate sprite to Mario
        Gdx.app.log("MARIOOO","MARIO HAS BEEN CREATED");
    }//end constructor

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //set animation
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        //get animation keyframe depending on the marios state
        switch (currentState) {
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;   //doesnt grow mario
                }
                break;
            case JUMPING:
                //region = (TextureRegion) marioJump.getKeyFrame(stateTimer); //could by problem
                region = marioIsBig ? bigMarioJump : (TextureRegion) marioJump; //could by problem
                break;
            case RUNNING:
                region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer,true) : (TextureRegion) marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }//check the state

        if((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()) {
            //going to the left
            region.flip(true,false);
            runningRight = false;
        }else if ((b2body.getLinearVelocity().x >0 || runningRight) && region.isFlipX()) {
            //going to the right
            region.flip(true,false);
            runningRight=true;
        }//check the direcction

        stateTimer=currentState==previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }//get the exactly frame

    public State getState() {

        if(runGrowAnimation) {
            return State.GROWING;       //mario si growing
        }else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING) ) {
            //is jumping
            return State.JUMPING;
        }else if (b2body.getLinearVelocity().y < 0) {
            //is falling
            return State.FALLING;
        } else if(b2body.getLinearVelocity().x != 0) {
            //is runnign
            return State.RUNNING;
        } else {
            //doesnt doing anything
            return State.STANDING;
        }
    }//for animations

    public void grow() {
        runGrowAnimation =true;
        marioIsBig = true;
        setBounds(getX(),getY(),getWidth(),getHeight()*2);
        MyGdxGame.manager.get("powerup.wav", Sound.class).play();  //play sound
    }//grow mario

    public boolean isDead(){
        return marioIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MyGdxGame.PPM,32 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MyGdxGame.PPM);      //shape of mario
        //set the category to collide with
        fdef.filter.categoryBits = MyGdxGame.MARIO_BIT;
        //what can mario collide
        fdef.filter.maskBits = MyGdxGame.GROUND_BIT | MyGdxGame.COIN_BIT | MyGdxGame.BRICK_BIT |
        MyGdxGame.ENEMY_BIT | MyGdxGame.OBJECT_BIT
                | MyGdxGame.ENEMY_HEAD_BIT | MyGdxGame.ITEM_BIT;


        fdef.shape=shape;
        b2body.createFixture(fdef);

        //when mario knock something with his head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MyGdxGame.PPM, 6 / MyGdxGame.PPM), new Vector2(-2 / MyGdxGame.PPM, 6 / MyGdxGame.PPM) );
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head"); //head connection

    }//define mario body

}//end mario
