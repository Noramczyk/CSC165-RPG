package a3;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.awt.geom.*;
import com.jogamp.common.nio.Buffers;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import ray.rage.*;
import ray.rage.util.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;
import ray.audio.*;
import com.jogamp.openal.ALFactory;
import static ray.rage.scene.SkeletalEntity.EndType.*;

import ray.rage.asset.texture.*;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.shader.GpuShaderProgram.Type;
import net.java.games.input.Event;
import ray.rage.util.BufferUtil;
import ray.rage.scene.Camera.Listener;

import ray.input.action.AbstractInputAction;
import net.java.games.input.Event;
import ray.rage.rendersystem.states.*;
import ray.input.*;
import ray.input.action.*;
import ray.input.action.Action;
import ray.input.InputManager;
import ray.input.GenericInputManager;

import ray.rage.asset.texture.*;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.shader.GpuShaderProgram.Type;
import net.java.games.input.Event;
import ray.rage.util.BufferUtil;
import ray.rage.scene.Camera.Listener;

import ray.physics.PhysicsEngine;
import ray.physics.PhysicsObject;
import ray.physics.PhysicsEngineFactory; 
import ray.rml.Angle;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;

import java.io.IOException;
import ray.networking.IGameConnection.ProtocolType;
import ray.networking.client.GameConnectionClient;
import ray.networking.server.IClientInfo;

import java.rmi.UnknownHostException;        //Remote HOST
import ray.rml.Vector3f;
import ray.rage.math.Transform;

import myGameEngine.*;

import java.util.Scanner;

public class MyGame extends VariableFrameRateGame {


    float xP, yP, zP, projPos, localX, localY, localZ, nX, nY, nZ,
                                         upperThres, lowerThres, fX, fY, fZ, spearX, spearY, spearZ, sX, sY, sZ;


    float xEP, zEP, yEP, xNP, zNP, xMP, zMP, xEN, zEN, xNN, zNN, xMN, zMN, xGN, zGN, xNPC, zNPC, x4G, z4G,
                                                                                x5G, z5G, x6G, z6G, x7G, z7G, x8G, z8G;       // Collision vars
     
    float zCalc, colCalc;                 // Distance vars for collision

    float mass = 1.0f, origX = -22.0f, origZ = -70.0f, avNodeX = -16.0f, avNodeY = 0.5f, avNodeZ = -25.0f;

    float up[] = {0,1,0};
    double[] temptf, temptf2;

    int counter = 0, modNe = 0, modPe = 0, numLives = 3;

    GL4RenderSystem rs;
    float elapsTime = 0.0f;
    String elapsTimeSecStr, elapsTimeMinStr, counter1Str, counter2Str, dispStr, timeRemainingStr, 
                                                                                elapsTimeStr, counterStr, playOne, playLife;
    int elapsTimeSec, counter1, counter2, playerOne, lives, x, y;
    int timeRemaining = 180, timeRemainingSec = 0;
    Random rand = new Random();

    private InputManager im;
    public SceneManager sm;
    private Camera3Pcontroller orbitController, orbitControllerP2;
    private Action moveForwardAction, moveBackwardAction, turnLeftAction, turnRightAction;
    private SkeletalEntity playerSE;
    private PhysicsEngine physicsEng, physicsEng2;
    private PhysicsObject playerPhys, goblin2Phys, goblin1Phys, goblin3Phys, goblin4Phys, goblin5Phys,
                                                    goblin6Phys, goblin7Phys, goblin8Phys, goblinNpcPhys, gndPlaneP, wallPhys;
    private float  lastThinkUpdateTime = 0;
    private float  lastTickUpdateTime = 0;

    private boolean isMoving = false;

    private SceneNode playerN, spearN, spearP, groundNode, gndNode, axisN, arrowN,
                                goblin2N, goblin1N, goblin3N, goblin4N, goblin5N, goblin6N, goblin7N, goblin8N, goblinNPC, tessE;


    private final static String GROUND_E = "Ground";
    private final static String GROUND_N = "GroundNode";

    private String serverAddress;                               // Networking
    private int serverPort;
    private ProtocolType serverProtocol;
    private ProtocolClient protClient;
    private NodeControllerTwo nc;

     private UUID id;

    boolean isClientConnected;                                          

    IAudioManager audioMgr;
    Sound playerWalkSound, gobinSound;
    Camera camera;
    boolean visited1 = false, visited2 = false, visited3 = false, idle = true;
    boolean running = true;
    boolean runningObj = false;

    boolean flagPe = true, flagPm = true, flagPn = true, flagLife = true, flagNe = true, flagNm = true, flagNn = true, 
                                                            flag4G = true, flag5G = true, flag6G = true, flag7G = true, flag8G = true;

    private static final String SKYBOX_NAME = "SkyBox";
    private boolean skyBoxVisible = true;

    public MyGame(String serverAddr, int sPort, String serverProtocol) 
    {
        super();
        this.serverAddress = serverAddr;
        this.serverPort = sPort;
        this.serverProtocol = ProtocolType.UDP;


        System.out.println("Welcome to my Game");


    }
    public static void main(String[] args) 
    {
        Game game = new MyGame(args[0], Integer.parseInt(args[1]), args[2]);
        // 192.168.1.7 3505 UDP
    
            String serverAdd = args[0];
            String ssPort = args[1];   
            String serverProtocols = args[2];

            System.out.println("Enter IP Address, Port Number, Server Protocol(UDP)");
            System.out.println(serverAdd + " " + ssPort + " " + serverProtocols);


        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
        }
        
    }
    @Override
    protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
        rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
    }
    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {
        SceneNode rootNode = sm.getRootSceneNode();
        Camera camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
        rw.getViewport(0).setCamera(camera);

    }
    protected void setupOrbitCamera(Engine eng, SceneManager sm)  { 
        SkeletalEntity playerSE = (SkeletalEntity)eng.getSceneManager().getEntity("playerAv");
        SceneNode playerN = sm.getSceneNode("playerNode1");   
        SceneNode cameraN = sm.getSceneNode("MainCameraNode");
        SceneNode tessN = sm.getSceneNode("tessNode");
        Camera camera = sm.getCamera("MainCamera");  


        orbitController = new Camera3Pcontroller(camera, cameraN, playerSE, playerN, spearN, tessN, im, eng,1);

    } 


    private void executeScript(ScriptEngine engine, String scriptFileName)  {       
        try    { 
            FileReader fileReader = new FileReader(scriptFileName);
            engine.eval(fileReader);    //execute the script statements in the file      
            fileReader.close();    
        }    catch (FileNotFoundException e1)    { 
            System.out.println(scriptFileName + " not found " + e1); 
        }    catch (IOException e2)     { 
            System.out.println("IO problem with " + scriptFileName + e2); 
        }    catch (ScriptException e3)      { 
            System.out.println("ScriptException in " + scriptFileName + e3); 
        }    catch (NullPointerException e4)    { 
            System.out.println ("Null ptr exception in " + scriptFileName + e4); 
        }  
    } 
    public void initAudio(SceneManager sm) {
        AudioResource resource1, resource2;
        audioMgr = AudioManagerFactory.createAudioManager("ray.audio.joal.JOALAudioManager");
        if(!audioMgr.initialize()) {
            System.out.println("Audio Manager failed to initialize!");
            return;
        }
        //resource1 = audioMgr.createAudioResource("Player.wav", AudioResourceType.AUDIO_SAMPLE);
        resource2 = audioMgr.createAudioResource("Gobin.wav", AudioResourceType.AUDIO_SAMPLE);
        //playerWalkSound = new Sound(resource1, SoundType.SOUND_EFFECT, 100, true);
        gobinSound = new Sound(resource2, SoundType.SOUND_EFFECT,100, true);

        //playerWalkSound.initialize(audioMgr);
        gobinSound.initialize(audioMgr);
        gobinSound.setMaxDistance(10.0f);
        gobinSound.setMinDistance(0.5f);
        gobinSound.setRollOff(5.0f);

        SceneNode gobinN = sm.getSceneNode("planetNode");
        gobinSound.setLocation(gobinN.getWorldPosition());
        //System.out.println(gobinSound.getLocation());
        setEarParameters(sm);
        gobinSound.play();
    }  
    public void setEarParameters(SceneManager sm) {
        SceneNode cameraN = sm.getSceneNode("MainCameraNode");
        Vector3 avDir = cameraN.getWorldForwardAxis();

        audioMgr.getEar().setLocation(cameraN.getWorldPosition());
        //System.out.println(audioMgr.getEar().getLocation());
        audioMgr.getEar().setOrientation(avDir,Vector3f.createFrom(0,1,0));
    }
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
        im = new GenericInputManager();

        xP = -16.0f;
        yP = 0.5f;
        zP = -25.0f;

        sX = -18.0f;
        sY =  3.0f;
        sZ = -40.0f;


        ScriptEngineManager factory = new ScriptEngineManager();
        java.util.List<ScriptEngineFactory> list = factory.getEngineFactories();
        ScriptEngine jsEngine = factory.getEngineByName("js");
        File scriptFile1 = new File("./MyGameEngine/InitParams.js");
        this.executeScript(jsEngine,scriptFile1.toString());
    
        RenderSystem rs =sm.getRenderSystem();
        Configuration conf = eng.getConfiguration();
        TextureManager tm = eng.getTextureManager();
        //TextureState state = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        TextureState building_1 = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);

        tm.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path"));

        Texture front = tm.getAssetByPath("Front.png");        
        Texture back = tm.getAssetByPath("Back.png");        
        Texture left = tm.getAssetByPath("Left.png");       
        Texture right = tm.getAssetByPath("Right.png");       
        Texture top = tm.getAssetByPath("Top.png");        
        Texture bottom = tm.getAssetByPath("Bottom.png");

        tm.setBaseDirectoryPath(conf.valueOf("assets.textures.path"));

        AffineTransform xform = new AffineTransform();
        xform.translate(0,front.getImage().getHeight());
        xform.scale(1d,-1d);



        front.transform(xform);        
        back.transform(xform);        
        left.transform(xform);        
        right.transform(xform);        
        top.transform(xform);        
        bottom.transform(xform);

        SkyBox sb = sm.createSkyBox(SKYBOX_NAME);        
        sb.setTexture(front, SkyBox.Face.FRONT);        
        sb.setTexture(back, SkyBox.Face.BACK);        
        sb.setTexture(left, SkyBox.Face.LEFT);        
        sb.setTexture(right, SkyBox.Face.RIGHT);        
        sb.setTexture(top, SkyBox.Face.TOP);        
        sb.setTexture(bottom, SkyBox.Face.BOTTOM);        
        sm.setActiveSkyBox(sb);

        Tessellation tessE = sm.createTessellation("tessE",2);

        tessE.setSubdivisions(256f);

        SceneNode tessN =sm.getRootSceneNode().createChildSceneNode("tessNode");
        tessN.attachObject(tessE);

        tessN.scale(250,1000,250);
        tessE.setHeightMap(this.getEngine(), "Heightmap-1.jpg");
        tessE.setTexture(this.getEngine(),"Map2.jpg");

        sm.getAmbientLight().setIntensity(new Color(.5f,.5f,.5f));                                      // MOVEMENT

        Light plight = sm.createLight("testLamp1", Light.Type.POINT);
        plight.setAmbient(new Color(.3f, .3f, .3f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
        plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(4f);

        Light plightD = sm.createLight("testLamp2", Light.Type.POINT);                                  // Spear Light
        plightD.setAmbient(new Color(0.5f, 0.5f, 0.5f));
        plightD.setDiffuse(new Color(0.1f, 0.7f, 0.1f));
        plightD.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plightD.setRange(3f);

        Light plightNPC = sm.createLight("testLampNPC", Light.Type.POINT);                          // NPC Light
        plightNPC.setAmbient(new Color(0.01f, 0.01f, 0.01f));
        plightNPC.setDiffuse(new Color(0.9f, 0.1f, 0.1f));
        plightNPC.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plightNPC.setRange(2f);

        SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);

      /*  SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plightD);

        SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plightNPC); */
        //plightNode.moveUp(7.5f);




        
        Camera camera = getEngine().getSceneManager().getCamera("MainCamera");

        SceneNode playerRoot = sm.getRootSceneNode().createChildSceneNode("myPlayerRoot");          // *** Root of Player heirarchy ****

        playerSE = sm.createSkeletalEntity("playerAv","basic_player.rkm","basic_player.rks");
        //playerN = sm.getRootSceneNode().createChildSceneNode("playerNode"+1);  
        playerN = playerRoot.createChildSceneNode("playerNode"+1);                                 // ???? ("playerNode"+1) Animation

            playerN.setLocalPosition(xP, yP, zP);
            playerN.moveUp(0.5f);
            playerN.attachObject(playerSE);



        SceneNode rootNode = sm.getRootSceneNode();


        Entity spearE = sm.createEntity("myNeptune", "Spear#2.obj");                       //  Planet #3
        spearE.setPrimitive(Primitive.TRIANGLES);
        //spearN = sm.getRootSceneNode().createChildSceneNode("N1");
        spearN = playerRoot.createChildSceneNode("N1"); 

               
            spearN.attachObject(spearE);
            spearN.attachObject(plightD);
            spearN.setLocalPosition(sX, sY, sZ);               // X Y Z
            spearN.setLocalScale(0.8f, 0.5f, 0.8f);
            //spearN.pitch(Degreef.createFrom(270.0f));
            spearN.yaw(Degreef.createFrom(90.0f));

        Entity goblin2E = sm.createEntity("myGob2", "Gobin.obj");                            
        goblin2E.setPrimitive(Primitive.TRIANGLES);
        goblin2N = sm.getRootSceneNode().createChildSceneNode("myGoblin2Node");
       
            goblin2N.attachObject(goblin2E);
            goblin2N.setLocalPosition(-10.0f, -1.5f, -8.0f);                      // Next to player              
            goblin2N.setLocalScale(1f, 1f, 1f);
            goblin2N.moveUp(0.5f);

        Entity goblin1E = sm.createEntity("myGob1", "Gobin.obj");                           
        goblin1E.setPrimitive(Primitive.TRIANGLES);
        goblin1N = sm.getRootSceneNode().createChildSceneNode("myGoblin1Node");

            goblin1N.attachObject(goblin1E);
            goblin1N.setLocalPosition(17.0f, -1.5f, -35.0f);                     // Right
            goblin1N.setLocalScale(1f, 1f, 1f);

        Entity goblin3E = sm.createEntity("myGob3", "Gobin.obj");                           
        goblin3E.setPrimitive(Primitive.TRIANGLES);
        goblin3N = sm.getRootSceneNode().createChildSceneNode("myGoblin3Node");

            goblin3N.attachObject(goblin3E);
            goblin3N.setLocalPosition(-49.0f, -1.5f, -53.0f);                    // Left
            goblin3N.setLocalScale(1f, 1f, 1f); 

        Entity goblin4E = sm.createEntity("myGob4", "Gobin.obj");                           
        goblin4E.setPrimitive(Primitive.TRIANGLES);
        goblin4N = sm.getRootSceneNode().createChildSceneNode("myGoblin4Node");

            goblin4N.attachObject(goblin4E);
            goblin4N.setLocalPosition(-17.0f, -1.5f, -95.0f);                    // Left
            goblin4N.setLocalScale(1f, 1f, 1f); 

        Entity goblin5E = sm.createEntity("myGob5", "Gobin.obj");                           
        goblin5E.setPrimitive(Primitive.TRIANGLES);
        goblin5N = sm.getRootSceneNode().createChildSceneNode("myGoblin5Node");

            goblin5N.attachObject(goblin5E);
            goblin5N.setLocalPosition(-112.0f, -1.5f, -96.0f);                    // Left
            goblin5N.setLocalScale(1f, 1f, 1f); 

        Entity goblin6E = sm.createEntity("myGob6", "Gobin.obj");                           
        goblin6E.setPrimitive(Primitive.TRIANGLES);
        goblin6N = sm.getRootSceneNode().createChildSceneNode("myGoblin6Node");

            goblin6N.attachObject(goblin6E);
            goblin6N.setLocalPosition(-111.0f, -1.5f, -5.0f);                    // Left
            goblin6N.setLocalScale(1f, 1f, 1f); 

        Entity goblin7E = sm.createEntity("myGob7", "Gobin.obj");                           
        goblin7E.setPrimitive(Primitive.TRIANGLES);
        goblin7N = sm.getRootSceneNode().createChildSceneNode("myGoblin7Node");

            goblin7N.attachObject(goblin7E);
            goblin7N.setLocalPosition(-104.0f, -1.5f, 31.0f);                    // Left
            goblin7N.setLocalScale(1f, 1f, 1f); 

        Entity goblin8E = sm.createEntity("myGob8", "Gobin.obj");                           
        goblin8E.setPrimitive(Primitive.TRIANGLES);
        goblin8N = sm.getRootSceneNode().createChildSceneNode("myGoblin8Node");

            goblin8N.attachObject(goblin8E);
            goblin8N.setLocalPosition(-114.0f, -1.5f, -23.0f);                    // Left
            goblin8N.setLocalScale(1f, 1f, 1f); 


        Entity goblinNPCE = sm.createEntity("myGobNPC", "Gobin.obj");                           
        goblinNPCE.setPrimitive(Primitive.TRIANGLES);
        goblinNPC = sm.getRootSceneNode().createChildSceneNode("myGoblinNpcNode");

            goblinNPC.attachObject(goblinNPCE);
            goblinNPC.attachObject(plightNPC);
            goblinNPC.setLocalPosition(-18.0f, 0.01f, -70.0f);
            goblinNPC.setLocalScale(1f, 1f, 1f); 


        Entity groundEntity = sm.createEntity(GROUND_E, "cube.obj");

            gndNode = rootNode.createChildSceneNode(GROUND_N);
            gndNode.attachObject(groundEntity);
            gndNode.setLocalPosition(0f, -4.0f, -2f); 
            //gndNode.setLocalScale(300,0.01f,300);




        
        SceneNode playerCamera = sm.getRootSceneNode().createChildSceneNode(camera.getName() + "Node");
        playerCamera.attachObject(camera);
        camera.setMode('n');


        Entity planetE = sm.createEntity("planet", "Gobin.obj");
        planetE.setPrimitive(Primitive.TRIANGLES);

        SceneNode planet1N = sm.getRootSceneNode().createChildSceneNode(planetE.getName() + "Node");
        planet1N.attachObject(planetE);
        planet1N.yaw(Degreef.createFrom(((Double)(jsEngine.get("houseRot"))).floatValue()));

        Texture houseT = tm.getAssetByPath("Goblin.jpg");
        building_1.setTexture(houseT);
        planetE.setRenderState(building_1);

        Entity planet2E = sm.createEntity("planet2", "earth.obj");
        planet2E.setPrimitive(Primitive.TRIANGLES);


            Texture playerT = tm.getAssetByPath("Basic_Player.jpg");

            Texture neptuneTexture = tm.getAssetByPath("Spear#2.png");
            Texture moonTexture = tm.getAssetByPath("Goblin.jpg");
            Texture earthTexture = tm.getAssetByPath("Goblin.jpg");
            Texture goblin3Texture = tm.getAssetByPath("Goblin.jpg");
            Texture goblin4Texture = tm.getAssetByPath("Goblin.jpg");
            Texture goblin5Texture = tm.getAssetByPath("Goblin.jpg");
            Texture goblin6Texture = tm.getAssetByPath("Goblin.jpg");
            Texture goblin7Texture = tm.getAssetByPath("Goblin.jpg");
            Texture goblin8Texture = tm.getAssetByPath("Goblin.jpg");
       
            Texture goblinNpcTexture = tm.getAssetByPath("Goblin.jpg");

            //RenderSystem rs = sm.getRenderSystem();

            TextureState stateE = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);

            TextureState state = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);                 // rs && THIS
            TextureState stateN = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            TextureState stateM = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            TextureState stateG = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            TextureState stateG3 = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            TextureState stateG4 = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            TextureState stateG5 = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            TextureState stateG6 = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            TextureState stateG7 = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            TextureState stateG8 = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        
            TextureState stateNPC = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
 
            stateE.setTexture(earthTexture);
            state.setTexture(playerT);
            stateN.setTexture(neptuneTexture);
            stateM.setTexture(moonTexture);
            stateG3.setTexture(goblin3Texture);
            stateG4.setTexture(goblin4Texture);
            stateG5.setTexture(goblin5Texture);
            stateG6.setTexture(goblin6Texture);
            stateG7.setTexture(goblin7Texture);
            stateG8.setTexture(goblin8Texture);
            stateNPC.setTexture(goblinNpcTexture);
         
           
            goblin1E.setRenderState(stateE);
            spearE.setRenderState(stateN);
            goblin2E.setRenderState(stateM);
            goblin3E.setRenderState(stateG3);
            goblin4E.setRenderState(stateG4);
            goblin5E.setRenderState(stateG5);
            goblin6E.setRenderState(stateG6);
            goblin7E.setRenderState(stateG7);
            goblin8E.setRenderState(stateG8);
            goblinNPCE.setRenderState(stateNPC);
            playerSE.setRenderState(state);
          
            RotationController rc = new RotationController(Vector3f.createUnitVectorY(), .02f);

            rc.addNode(goblin2N);
            rc.addNode(goblin1N);
            rc.addNode(goblin3N);
            rc.addNode(goblin4N);
            rc.addNode(goblin5N);
            rc.addNode(goblin6N);
            rc.addNode(goblin7N);
            rc.addNode(goblin8N);
        
            sm.addController(rc);

            setupOrbitCamera(eng, sm);

            playerN.yaw(Degreef.createFrom(180.0f));
            //playerN.rotate(Degreef.createFrom(90.0f));


            playerN.translate(0.0f, 3.0f,0.0f);
            playerN.scale(0.1f,0.1f, 0.1f);
            initAudio(sm);
            playerSE.loadAnimation("idle","basic_player_idle.rka");
            playerSE.loadAnimation("walk","basic_player_walk.rka");
            playerSE.loadAnimation("throw","basic_player_throw.rka");
            playerSE.stopAnimation();
            playerSE.playAnimation("idle", 0.3f, LOOP, 0);



            initPhysicsSystem();
            createRagePhysicsWorld(); 
            setupNetworking();
            //running = true;
            //detect = new DetectCollision(playerN, goblin1N, goblin2N, goblin3N, goblinNPC, spearN, sm);
            //detect = new DetectCollision();
            //detectCollision();

    }

    @Override
    protected void update(Engine engine) 
    {
        long currentTime = System.nanoTime();
       
        float elapsedThinkMilliSecs = (currentTime-lastThinkUpdateTime)/(1000000.0f);
        float elapsedTickMilliSecs = (currentTime-lastTickUpdateTime)/(1000000.0f);

        NodeControllerTwo tc = new NodeControllerTwo(); 
        //long modTime = scriptFile1.lastModified();
        long fileLastModifiedTime = 0;
        Matrix3 matRot;
       

        SceneManager sm = engine.getSceneManager();
        ScriptEngineManager factory = new ScriptEngineManager();
        java.util.List<ScriptEngineFactory> list = factory.getEngineFactories();
        ScriptEngine jsEngine = factory.getEngineByName("js");
   
        Viewport tVp = engine.getRenderSystem().getRenderWindow().getViewport(0);
        SceneNode house = engine.getSceneManager().getSceneNode("planetNode");

        //int oneAmt = detect.playerOneCount();                                  // Passed variable from DetectCollision
        //int life = detect.playerLives();

        int oneAmt = modPe;
        int life = numLives;

        playerOne = oneAmt;
        lives = life;

        playOne = Integer.toString(playerOne);
        playLife = Integer.toString(lives);

 

            rs = (GL4RenderSystem) engine.getRenderSystem();
            elapsTime += engine.getElapsedTimeMillis();
            elapsTimeSec = Math.round(elapsTime/1000.0f);
            elapsTimeSecStr = Integer.toString(elapsTimeSec%60);
            elapsTimeMinStr = Integer.toString(elapsTimeSec/60);
            counter1Str = Integer.toString(counter1);
            counter2Str = Integer.toString(counter2);


            dispStr = "Goblins Killed = " + playOne + "     Lives = " + playLife;
            rs.setHUD(dispStr, 15, 15);

            if(life == 0)
            {
                dispStr = " GAME OVER ";
                rs.setHUD(dispStr, 400, 350);
                setState(Game.State.STOPPING);
            }

            else if(oneAmt == 5)
            {
                dispStr = " Player Wins ";
                rs.setHUD(dispStr, 400, 350);
                setState(Game.State.STOPPING);
            }


                im.update(elapsTime);
                detectCollision(sm);
              
                attachObj();

                orbitController.updateCameraPosition();
                gobinSound.setLocation(house.getWorldPosition());
                setEarParameters(sm);
                SkeletalEntity playerSE = (SkeletalEntity)engine.getSceneManager().getEntity("playerAv");
                playerSE.update();
                playerN.update();


                float time = engine.getElapsedTimeMillis();

                if (running)
                    { 
                       Matrix4 mat;
                        physicsEng.update(time);

                        for (SceneNode s : engine.getSceneManager().getSceneNodes())
                        { 
                            if (s.getPhysicsObject() != null)
                            { 
                                mat = Matrix4f.createFrom(toFloatArray(
                                s.getPhysicsObject().getTransform()));
                                s.setLocalPosition(mat.value(0,3),mat.value(1,3),
                                mat.value(2,3));
                            }
                        }
                    }


                        if (elapsedTickMilliSecs >= 50.0f)     // Tick
                            { 
                                lastTickUpdateTime = currentTime;
                                tick();
                           
                            }

                        if (elapsedThinkMilliSecs >= 100.0f)     // Think
                            { 
                                lastThinkUpdateTime = currentTime;
                                think();
                            }


   /*     if((elapsTimeSec%60) < 10) {
            dispStr = "Player: 1  Time = " + elapsTimeMinStr + ":" + "0"+ elapsTimeSecStr + " Score: " + counter1Str;
            rs.setHUD(dispStr, tVp.getActualLeft(), tVp.getActualBottom());
        }
        else{
            dispStr = "Player: 1  Time = " + elapsTimeMinStr + ":" + elapsTimeSecStr + " Score: " + counter1Str;
            rs.setHUD(dispStr, tVp.getActualLeft(), tVp.getActualBottom());
        }

   
        if(modTime > fileLastModifiedTime)
        {
            fileLastModifiedTime = modTime;
            this.executeScript(jsEngine,scriptFile1.toString());
            house.setLocalPosition(-((Double)(jsEngine.get("housePosX"))).floatValue(),-((Double)(jsEngine.get("housePosY"))).floatValue(),-((Double)(jsEngine.get("housePosZ"))).floatValue());
            //house.yaw(Degreef.createFrom(((Double)(jsEngine.get("houseRot"))).floatValue()));
        }*/

    

        /*************/
      

    }

    public void attachObj()
    {

    localX = playerN.getLocalPosition().x();
    localY = playerN.getLocalPosition().y();
    localZ = playerN.getLocalPosition().z();

    nX = spearN.getLocalPosition().x();
    nY = spearN.getLocalPosition().y();
    nZ = spearN.getLocalPosition().z();

    upperThres = nZ + 1.5f;
    lowerThres = nZ - 1.5f;

    //running = true;

        if((localZ <= upperThres) && (localZ >= lowerThres))
        {
          
            spearN.setLocalPosition(localX , localY, localZ);
            spearN.moveUp(0.3f);
       
        }

    }

    private void initPhysicsSystem()
    { 

        String engine = "ray.physics.JBullet.JBulletPhysicsEngine";
        float[] gravity = {0, -3f, 0};

        physicsEng = PhysicsEngineFactory.createPhysicsEngine(engine);
        physicsEng.initSystem();
        physicsEng.setGravity(gravity);

    }

    private void createRagePhysicsWorld()
    { 
    //float mass = 1.0f;
    //float up[] = {0,1,0};
    //double[] temptf;

     //running = true;

        temptf = toDoubleArray(spearN.getLocalTransform().toFloatArray());

            playerPhys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            playerPhys.setBounciness(0.001f);
            playerPhys.setFriction(90000.5f);
            //spearN.setLocalPosition(22.0f, 2.5f, -70.0f);
            spearN.scale(0.01f, 0.03f, 0.01f);
            spearN.setPhysicsObject(playerPhys);
            //moonN.setPhysicsObject(dolphinPhys);

             //x = playerPhys.getUID();

        temptf = toDoubleArray(gndNode.getLocalTransform().toFloatArray());

            gndPlaneP = physicsEng.addStaticPlaneObject(physicsEng.nextUID(), temptf, up, 2.0f);
            gndNode.setLocalPosition(0.01f, -4.0f, -2);
            gndNode.setPhysicsObject(gndPlaneP);    


        temptf = toDoubleArray(goblin2N.getLocalTransform().toFloatArray());

            goblin2Phys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            //goblin2Phys.setFriction(90000.5f);
            goblin2N.setLocalPosition(-10.0f, -1.5f, -8.0f);
            goblin2N.scale(1f, 1f, 1f);
            goblin2N.setPhysicsObject(goblin2Phys);
            //moonN.setFriction(10.0f);

        temptf = toDoubleArray(goblin1N.getLocalTransform().toFloatArray());

            goblin1Phys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            //goblin1Phys.setFriction(90000.5f);
            goblin1N.setLocalPosition(17.0f, -1.5f, -35.0f);
            goblin1N.scale(1f, 1f, 1f);
            goblin1N.setPhysicsObject(goblin1Phys);

        temptf = toDoubleArray(goblin3N.getLocalTransform().toFloatArray());

            goblin3Phys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            //goblin3Phys.setFriction(90000.5f);
            goblin3N.setLocalPosition(-49.0f, -1.5f, -53.0f);
            goblin3N.scale(1f, 1f, 1f);
            goblin3N.setPhysicsObject(goblin3Phys);

        temptf = toDoubleArray(goblin4N.getLocalTransform().toFloatArray());

            goblin4Phys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            goblin4Phys.setFriction(90000.5f);
            goblin4N.setLocalPosition(-17.0f, -1.5f, -95.0f);
            goblin4N.scale(1f, 1f, 1f);
            goblin4N.setPhysicsObject(goblin4Phys);

        temptf = toDoubleArray(goblin5N.getLocalTransform().toFloatArray());

            goblin5Phys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            goblin5Phys.setFriction(90000.5f);
            goblin5N.setLocalPosition(-112.0f, -1.5f, -96.0f);
            goblin5N.scale(1f, 1f, 1f);
            goblin5N.setPhysicsObject(goblin5Phys);

        temptf = toDoubleArray(goblin6N.getLocalTransform().toFloatArray());

            goblin6Phys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            goblin6Phys.setFriction(90000.5f);
            goblin6N.setLocalPosition(-111.0f, -1.5f, -5.0f);
            goblin6N.scale(1f, 1f, 1f);
            goblin6N.setPhysicsObject(goblin6Phys);

        temptf = toDoubleArray(goblin7N.getLocalTransform().toFloatArray());

            goblin7Phys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            goblin7Phys.setFriction(90000.5f);
            goblin7N.setLocalPosition(-112.0f, -1.5f, -96.0f);
            goblin7N.scale(1f, 1f, 1f);
            goblin7N.setPhysicsObject(goblin7Phys);

        temptf = toDoubleArray(goblin8N.getLocalTransform().toFloatArray());

            goblin8Phys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
            goblin8Phys.setFriction(90000.5f);
            goblin8N.setLocalPosition(-114.0f, -1.5f, -23.0f);
            goblin8N.scale(1f, 1f, 1f);
            goblin8N.setPhysicsObject(goblin8Phys);



    }

      public void keyPressed(KeyEvent e)
    { 
        float forceX, forceZ;

        switch (e.getKeyCode())
        { 
            
            case KeyEvent.VK_SPACE:
           
                
            float xEN, zEN;

            running = true;


            localX = playerN.getLocalPosition().x();
            localY = playerN.getLocalPosition().y();
            localZ = playerN.getLocalPosition().z();

            xEN = goblin1N.getLocalPosition().x();
            zEN = goblin1N.getLocalPosition().z();

            fX = playerN.getWorldRightAxis().x();
            fY = playerN.getWorldRightAxis().y();
            fZ = playerN.getWorldRightAxis().z();

            spearX = spearN.getLocalPosition().x();
            spearY = spearN.getLocalPosition().y();
            spearZ = spearN.getLocalPosition().z();

      
                forceX = 100;                                               // SCRIPTING CHANGE !!! 
                forceZ = 100;
                //forceZ = 0;
                //forceZ = 0;

                forceX = fX * 500;
                forceZ = fZ * 500;

                playerPhys.applyForce(forceX, 0, forceZ, localX, localY, localZ);
                isMoving = false;
                playerSE.stopAnimation();
                playerSE.playAnimation("idle", 0.5f, NONE, 0);  
                            
                System.out.println(fX + " " + fY + " " + fZ);

            break;
        


            case KeyEvent.VK_ENTER:

                 x = playerPhys.getUID();
                 y = gndPlaneP.getUID();
              
                physicsEng.removeObject(x);
                running = false;

              

            break;

            case KeyEvent.VK_SHIFT:

            temptf = toDoubleArray(spearN.getLocalTransform().toFloatArray());

                playerPhys = physicsEng.addSphereObject(physicsEng.nextUID(),mass, temptf, 2.0f);
                playerPhys.setBounciness(0.05f);
                spearN.setPhysicsObject(playerPhys);
                running = false;
           
            break;

    

           case KeyEvent.VK_P:


            localX = playerN.getLocalPosition().x();
            localY = playerN.getLocalPosition().y();
            localZ = playerN.getLocalPosition().z();

            xEN = goblin1N.getLocalPosition().x();
            zEN = goblin1N.getLocalPosition().z();


            fX = playerN.getLocalForwardAxis().x();
            fY = playerN.getLocalForwardAxis().y();
            fZ = playerN.getLocalForwardAxis().z();

            spearX = spearN.getLocalPosition().x();
            spearY = spearN.getLocalPosition().y();
            spearZ = spearN.getLocalPosition().z();


                System.out.println(localX + " " + localY + " " + localZ);
                System.out.println("   ");
                //System.out.println(fX + " " + fY + " " + fZ);

            break;


            case KeyEvent.VK_R:

            spearX = spearN.getLocalPosition().x();
            spearY = spearN.getLocalPosition().y();
            spearZ = spearN.getLocalPosition().z();

                spearN.setLocalPosition(-18.0f, 1.0f, -40.0f);
           
                break;


          
         }

        super.keyPressed(e);

    }

    private float[] toFloatArray(double[] arr)
    { 
        if (arr == null) return null;
        int n = arr.length;
        float[] ret = new float[n];

        for (int i = 0; i < n; i++)
        { ret[i] = (float)arr[i];
        }

        return ret;
    }


    private double[] toDoubleArray(float[] arr)
    { 
        if (arr == null) return null;
        int n = arr.length;
        double[] ret = new double[n];

        for (int i = 0; i < n; i++)
        { 
            ret[i] = (double)arr[i];
        }

        return ret;
    }

    private void setupNetworking()
    { 
        isClientConnected = false;

            try
            { 
                protClient = new ProtocolClient(InetAddress.
                getByName(serverAddress), serverPort, serverProtocol, this);
            } 
            catch (UnknownHostException e) 
            { 
                e.printStackTrace();

            } 
            catch (IOException e) 
            { 
                e.printStackTrace();
            }

            if (protClient == null)
            { 
                System.out.println("missing protocol host"); }
            else
            { 

            protClient.sendJoinMessage();
            protClient.sendCreateMessage();

            } 

    }

     protected void processNetworking(float elapsTime)
    { 
  
        if (protClient != null)
        protClient.processPackets();

    }   
    
    
    public void detectCollision(SceneManager sm) 
    {
                
    float xP, yP, zP, xN, yN, zN, xS, yS, zS;
   
            nc = new NodeControllerTwo(); 
            sm.addController(nc);


                xN = playerN.getLocalPosition().x();
                yN = playerN.getLocalPosition().y();
                zN = playerN.getLocalPosition().z();

                xEN = goblin1N.getLocalPosition().x();
                zEN = goblin1N.getLocalPosition().z();

                xS = spearN.getLocalPosition().x();
                yS = spearN.getLocalPosition().y();
                zS = spearN.getLocalPosition().z();

                xMN = goblin2N.getLocalPosition().x();
                zMN = goblin2N.getLocalPosition().z();

                xGN = goblin3N.getLocalPosition().x();
                zGN = goblin3N.getLocalPosition().z();

                x4G = goblin4N.getLocalPosition().x();
                z4G = goblin4N.getLocalPosition().z();

                x5G = goblin5N.getLocalPosition().x();
                z5G = goblin5N.getLocalPosition().z();

                x6G = goblin6N.getLocalPosition().x();
                z6G = goblin6N.getLocalPosition().z();

                x7G = goblin7N.getLocalPosition().x();
                z7G = goblin7N.getLocalPosition().z();

                x8G = goblin8N.getLocalPosition().x();
                z8G = goblin8N.getLocalPosition().z();

                xNPC = goblinNPC.getLocalPosition().x();
                zNPC = goblinNPC.getLocalPosition().z();



                    if((xEN > 17.1) && (flagPe == true))        // Goblin#1
                        {

                                nc.addNode(goblin1N);                
                                modPe = modPe + 1;
                                System.out.println("Goblin #1 collision detected ...");
                                flagPe = false;
                        }
        


                    if((xGN < -49.1f) && (flagNn == true))         // Goblin#3
                      {

                              nc.addNode(goblin3N);
                              modPe = modPe + 1;
                              System.out.println("Goblin #3 collision detected ...");
                              flagNn = false;

                      }


                    if((zMN > -7.9f) && (flagPm == true))        // Goblin#2 next to origin of player
                        {

                                                 
                              nc.addNode(goblin2N);
                              modPe = modPe + 1;
                              System.out.println("Goblin #2 collision detected ...");
                              flagPm = false;

                        }

                    if((z4G < -95.1f) && (flag4G == true))        
                        {

                                                 
                              nc.addNode(goblin4N);
                              modPe = modPe + 1;
                              System.out.println("Goblin #4 collision detected ...");
                              flag4G = false;

                        }

                    if((z5G < -96.0f) && (flag5G == true))        
                        {

                                                 
                              nc.addNode(goblin5N);
                              modPe = modPe + 1;
                              System.out.println("Goblin #5 collision detected ...");
                              flag5G = false;

                        }

                     if((z6G > -5.0f) && (flag6G == true))        
                        {

                                                 
                              nc.addNode(goblin6N);
                              modPe = modPe + 1;
                              System.out.println("Goblin #6 collision detected ...");
                              flag6G = false;

                        }

                      if((z7G > 31.0f) && (flag7G == true))       
                        {

                                                 
                              nc.addNode(goblin7N);
                              modPe = modPe + 1;
                              System.out.println("Goblin #7 collision detected ...");
                              flag7G = false;

                        }

                     if((x8G < -114.0f) && (flag8G == true))        
                        {

                                                 
                              nc.addNode(goblin8N);
                              modPe = modPe + 1;
                              System.out.println("Goblin #8 collision detected ...");
                              flag8G = false;

                        }

                     //colCalc = zP - zNPC;

                   if((zN < (zNPC + 0.1f)) && (zN > (zNPC - 0.1f)) && (xN < (xNPC + 0.1f)) && 
                                                                        (xN > (xNPC - 0.1f)) && (flagLife == true))
                      {

                              //nc.addNode(avN);
                              playerN.setLocalPosition(avNodeX, avNodeY, avNodeZ);
                              goblinNPC.setLocalPosition(-18.0f, 0.5f, -70.0f);
                              numLives = numLives - 1;
                              System.out.println("Player collision detected ...");
                              flagLife = false;

                      } 

                      flagLife = true;

  
        }

     public void tick()                           
                {                                           // Update Position NPC & avN
                  
                    xP = playerN.getLocalPosition().x();
                    yP = playerN.getLocalPosition().y();
                    zP = playerN.getLocalPosition().z();

                    xNPC = goblinNPC.getLocalPosition().x();
                    zNPC = goblinNPC.getLocalPosition().z();

                    //System.out.println("Player location (tick): " + xP + " " + yP + " " + zP);

                }


       public void think()
                {

                float nX, nZ, aX, aZ;
                float negTrav = -0.1f, posTrav = 0.1f;

               
                    xNPC = goblinNPC.getLocalPosition().x();
                    zNPC = goblinNPC.getLocalPosition().z();


                    zCalc = zP + xNPC;
                    

                    if (xP < xNPC)
                    {
                      nX = xNPC + (negTrav);                              // Modify for script values posTrav & negTrav
                    }
                    else
                    {
                      nX = xNPC + (posTrav);
                    }
                        
                        if(zP < zNPC)
                        {
                          nZ = zNPC + (negTrav);
                        }
                        else
                        {
                          nZ = zNPC  + (posTrav);
                        }


                    goblinNPC.setLocalPosition(nX, 0.01f, nZ); 


             
                }
              


}