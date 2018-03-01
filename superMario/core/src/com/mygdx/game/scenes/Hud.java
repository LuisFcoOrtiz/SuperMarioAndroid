package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.GameOverScreen;

/**
 * Created by root on 15/02/18.
 */

public class Hud implements Disposable {
    public Stage stage;
    public Viewport viewport;

    private Integer worldTimer;
    private float timeCount;    //set the time coutdown
    private static Integer score;

    Label countDownLabel, timeLabel, levelLabel, worldLabel, marioLabel;
    static  Label scoreLabel;


    public Hud(SpriteBatch sb) {
        worldTimer = 80;       //timer countdown
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countDownLabel= new Label( String.format("%03d",worldTimer), new Label.LabelStyle( new BitmapFont(), Color.WHITE ) );
        scoreLabel = new Label( String.format("%06d",score), new Label.LabelStyle( new BitmapFont(), Color.WHITE ) );
        timeLabel = new Label( String.format("Tiempo",worldTimer), new Label.LabelStyle( new BitmapFont(), Color.WHITE ) );
        levelLabel = new Label( String.format("1-1",worldTimer), new Label.LabelStyle( new BitmapFont(), Color.WHITE ) );
        worldLabel = new Label( String.format("Mundo",worldTimer), new Label.LabelStyle( new BitmapFont(), Color.WHITE ) );
        marioLabel = new Label( String.format("MARIO",worldTimer), new Label.LabelStyle( new BitmapFont(), Color.WHITE ) );

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);
    }//end constructor


    /*public void update(float dt) {
        timeCount += dt;
        if  (timeCount >= 1) {
            worldTimer--;   //cutdown
            if(worldTimer < 50) {
                countDownLabel.setText(String.format("%03d",worldTimer));
                countDownLabel.setColor(Color.RED);
                if(worldTimer<=0) {
                    Music music = MyGdxGame.manager.get("mario_music.ogg", Music.class);
                    music.pause();
                    MyGdxGame.manager.get("loose.wav", Sound.class).play();
                    try {
                        Thread.sleep(4000);
                        Gdx.app.exit();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                countDownLabel.setText(String.format("%03d",worldTimer));
            }
            timeCount = 0;
        }
    }//check the time*/
    public Integer update(float dt) {
        timeCount += dt;
        if  (timeCount >= 1) {
            worldTimer--;   //cutdown
            if(worldTimer < 25) {
                countDownLabel.setText(String.format("%03d",worldTimer));
                countDownLabel.setColor(Color.RED);
                /*if(worldTimer<=0) {
                    Music music = MyGdxGame.manager.get("mario_music.ogg", Music.class);
                    music.pause();
                    MyGdxGame.manager.get("loose.wav", Sound.class).play();
                    try {
                        Thread.sleep(4000);
                        Gdx.app.exit();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }*/
            } else {
                countDownLabel.setText(String.format("%03d",worldTimer));
            }
            timeCount = 0;
        }
        return worldTimer;
    }//check the time

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d",score));
    }//update the score

    public static void restScore(int value) {
        score -= value;
        scoreLabel.setText(String.format("%06d",score));
    }

    public static Integer getScore() {
        return score;
    }//get the score

    public Integer getTime() {
        return worldTimer;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}//end hud
