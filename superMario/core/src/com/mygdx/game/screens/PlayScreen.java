package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.sprites.Enemy;
import com.mygdx.game.sprites.Goomba;
import com.mygdx.game.sprites.Item;
import com.mygdx.game.sprites.ItemDef;
import com.mygdx.game.sprites.Mario;
import com.mygdx.game.sprites.Mushroom;
import com.mygdx.game.tools.B2WorldCreator;
import com.mygdx.game.tools.WorldContactListener;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;

import sun.rmi.runtime.Log;

/**
 * Created by root on 15/02/18.
 */

public class PlayScreen implements Screen{

    private MyGdxGame game;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    //
    private Hud hud;
    //map
    private TmxMapLoader mapLoader;
    private TiledMap map;   //map itself
    private OrthogonalTiledMapRenderer renderer;
    //box 2D variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;     //world creator
    //sprites
    private Mario player;       //Mario

    //marios DEAD
    public boolean isDead = false;

    private TextureAtlas atlas;
    //music
    private Music music;
    //items
    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;    //CHANGE TO LINKED

    //CHECK THE sensors
    boolean available = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

    public PlayScreen(MyGdxGame myGame) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack"); //get the mario item

        this.game=myGame;                               //set the game
        gamecam = new OrthographicCamera();
        //game camera possition (BEFORE WAS  MyGdxGame.V_WIDTH / MyGdxGame.PPM) each one
        gameport = new FitViewport(MyGdxGame.V_WIDTH /130,MyGdxGame.V_HEIGHT / 130  ,gamecam);    //gamecamera
        hud = new Hud(game.batch);  //start the hud
        //map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");    //set the map
        renderer = new OrthogonalTiledMapRenderer(map,1 / MyGdxGame.PPM);
        //camera possition in map
        gamecam.position.set(gameport.getScreenWidth() /2, gameport.getScreenHeight()/2,0 );
        //
        world = new World(new Vector2(0,-10),true);    //gravitty
        b2dr = new Box2DDebugRenderer();
        //player MARIO
        player = new Mario( this);
        //create the world
        creator = new B2WorldCreator(this);
        //world listener for iterations
        world.setContactListener(new WorldContactListener());
        //music
        music = MyGdxGame.manager.get("mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();   //starts the music
        //items
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();  //was priorityQueue | LinkedBlockingDeque

    }//end constructor

    //items
    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if(!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class) {
                items.add(new Mushroom(this,idef.position.x, idef.position.y));
            }
        }
    }//show the items
    //items

    public TextureAtlas getAtlas() {
        return atlas;
    }//get the atlas

    @Override
    public void show() {

    }

    //TO UPDATE
    public void handleInput(float dt) { //acelerometer get here
        float accelX = Gdx.input.getAccelerometerX();
        float accelY = Gdx.input.getAccelerometerY();
        //float accelZ = Gdx.input.getAccelerometerZ();
        //control the ation handle
        if(accelY > 1 ) {//right
            player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
            gamecam.position.x += 10 * dt; //velocity |was 100 before
        } else if( accelX <= 3  ) {
            player.b2body.applyLinearImpulse(new Vector2(0,0.5f), player.b2body.getWorldCenter(),true);
        }else if (Gdx.input.isTouched()) {//second up
            player.b2body.applyLinearImpulse(new Vector2(0,1f), player.b2body.getWorldCenter(),true);
        }else if (accelY < -1) {//left
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
        }
        /*if (Gdx.input.isTouched()) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
            //Gdx.input.vibrate(2000);
            gamecam.position.x += 10 * dt; //velocity |was 100 before
        }//hacia adelante
        if (Gdx.input.justTouched()) {
            player.b2body.applyLinearImpulse(new Vector2(0,3f), player.b2body.getWorldCenter(),true);
        }//salta

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(),true);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x<=2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
        }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x>= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
        }*/
    }//end handle

    public void update(float dt){
        handleInput(dt);
        //item
        handleSpawningItems();
        //how many time update per second
        //velocity and positionItineration
        world.step(1/60f,6,2);

        player.update(dt);                          //update the sprite of mario
        for (Enemy enemy : creator.getGoombas()) {  //update goombas sprites
            enemy.update(dt);
            //activate goomba when mario is close
            if(enemy.getX() < player.getX() + 224 / MyGdxGame.PPM ) {
                enemy.b2body.setActive(true);
            }
        }
        //show items
        for(Item item : items) {
            item.update(dt);
        }

        //hud.update(dt);     //update the timer countdown
        if (hud.update(dt)==0) {
            isDead=true;
        }
        //gamecamp for mario
        gamecam.position.x=player.b2body.getPosition().x;
        gamecam.update();
        //render only the camera that you can see
        renderer.setView(gamecam);
    }//end update
    //
    @Override
    public void render(float delta) {
        //update
        update(delta);

        Gdx.gl.glClearColor(0, 0, 4, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render the game map
        renderer.render();

        ///render 2dbox
        b2dr.render(world,gamecam.combined);

        //main camera
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);                    //drawing Mario
        for (Enemy enemy : creator.getGoombas()) {  //drawing goombas sprites
            enemy.draw(game.batch);
        }
        //items
        for(Item item : items) {
            item.draw(game.batch);
        }//end items
        game.batch.end();

        //camera to our batch
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        ///The way that you can win or loose
        if(isDead) {
            //sendToDB();
            pre_post("fecha","PERDISTE",Hud.getScore()+"",""+hud.update(delta));
            game.setScreen(new GameOverScreen(game,"HAS PERDIDO!", "loose.wav"));
            dispose();
        }else if (Hud.getScore()>4000) {//score limit
            //sendToDB();
            pre_post("fecha","GANASTE",Hud.getScore()+"",""+hud.update(delta));
            game.setScreen(new GameOverScreen(game,"HAS GANADO!", "win.wav"));
            dispose();
        }
        //check if mario is dead

    }//end of render

    @Override
    public void resize(int width, int height) {
        gameport.update(width,height);
    }

    @Override
    public void pause() {

    }
    //for the enemy
    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }
    //
    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }//end dispose

    /*public void sendToDB() {
        //final String username = "root";

        //final String password = "usuario";
        Gdx.app.log("SENDING","DB sending data");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("fecha", "hola");
        parameters.put("nombre","nombre aqui");
        parameters.put("puntuacion","puntos");
        parameters.put("tiempo","tiempo aqui");
        final Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.setHeader("Upgrade", "HTTP/1.1, HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11");
        httpRequest.setUrl("http://192.168.1.207/query/newMensaje.php");
        httpRequest.setContent(HttpParametersUtils.convertHttpParameters(parameters));
        httpRequest.setTimeOut(6000);
        Gdx.app.log("SENDED","DB sending data");

    }*/
    //MYSQL sended

    private static void pre_post(String  fecha , String nombre , String puntuacion, String tiempo ) {

        String url = "http://192.168.1.119/query/newMensaje.php";   //urlcan change
        String urlParameters = "";

        try {
            urlParameters = "fecha=" + URLEncoder.encode(fecha, "UTF-8") +
                    "&nombre=" + URLEncoder.encode(nombre, "UTF-8") +
                    "&puntuacion=" + URLEncoder.encode(puntuacion, "UTF-8")+
                    "&tiempo=" + URLEncoder.encode(tiempo, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Gdx.app.log("ERROR",e.getMessage());
            e.printStackTrace();
        }


        String post = executePost( url , urlParameters ) ;

        Gdx.app.log("Sended","CONGRATS");


    }

    private static String executePost(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return "** error **";

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

}//end class