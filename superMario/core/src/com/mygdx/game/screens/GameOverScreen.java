package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;



/**
 * Created by root on 18/02/18.
 */

public class GameOverScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private Game game;

    public GameOverScreen(Game game, String whatHappend, String soundEffect) {
        //stop the music and loosing effect sound
        Music music = MyGdxGame.manager.get("mario_music.ogg", Music.class);
        music.pause();
        MyGdxGame.manager.get(soundEffect, Sound.class).play();
        //
        this.game=game;
        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage=new Stage(viewport, ((MyGdxGame) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label.LabelStyle font2 = new Label.LabelStyle(new BitmapFont(), Color.GOLD);

        //start the table screen at the center
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label luisDev = new Label("https://github.com/LuisFcoOrtiz",font2);
        Label gameOverLabel = new Label(whatHappend,font);
        Label playAgain = new Label("<-SALIR  |  VOLVER A JUGAR->",font);

        table.add(luisDev).expandX();
        table.row();
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgain).expandX().padTop(10f);

        stage.addActor(table);

    }//end constructor

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //acelerometer for Y
        float accelY = Gdx.input.getAccelerometerY();
        if(accelY > 3) {
            game.setScreen(new PlayScreen((MyGdxGame) game));
            dispose();
        } else if (accelY < -3) {
            Gdx.app.exit();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}//end class
