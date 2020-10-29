package myGame;

import java.awt.*;
import java.awt.event.*;
import java.nio.*;                  // For FloatBuffer & IntBuffer
import java.io.*;
import java.util.*;

import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;

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


public class MyGame extends VariableFrameRateGame
{

    // to minimize variable allocation in update()
    GL4RenderSystem rs;
    float elapsTime = 0.0f;
    String elapsTimeStr, counterStr, dispStr;
    int elapsTimeSec, counter = 0;

    private InputManager im;
    private Camera3Pcontroller orbitController;
 
    private Action moveFwdAct, moveBwdAct, orbitAction;
    private Camera camera;

    String gpName;

    public MyGame() {
        super();
        System.out.println("press T to render triangles");
        System.out.println("press L to render lines");
        System.out.println("press P to render points");
        System.out.println("press C to increment counter");
    }

    public static void main(String[] args) {
        Game game = new MyGame();
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
    protected void setupCameras(SceneManager sm, RenderWindow rw) 
    {

        SceneNode rootNode = sm.getRootSceneNode();
        camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
        rw.getViewport(0).setCamera(camera);

        SceneNode cameraN = rootNode.createChildSceneNode("MainCameraNode");
        
        //camera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
        //camera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
        //camera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
        
        //camera.setPo((Vector3f)Vector3f.createFrom(0.0f, 0.0f, 0.0f));

        //SceneNode cameraN = rootNode.createChildSceneNode(camera.getName() + "Node");
        cameraN.attachObject(camera);
        camera.setMode('n');
        camera.getFrustum().setFarClipDistance(1000.0f);                    // KEEP THIS

    }
    
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException 
    {
        //setupInputs();
        im = new GenericInputManager();

        ManualObject axis = makeAxis(eng, sm);                                               // Manual Object Axis
        axis.setPrimitive(Primitive.LINES);

        SceneNode axisN = sm.getRootSceneNode().createChildSceneNode("AxisNode");

        axisN.moveBackward(2.0f);                   // Set object back to VIEW!!!
        axisN.setLocalScale(0.75f, 0.75f, 0.75f);
        axisN.attachObject(axis);
     

        Entity dolphinE = sm.createEntity("dolphin", "dolphinHighPoly.obj");
        dolphinE.setPrimitive(Primitive.TRIANGLES);

        SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode("dolphinNode");
        //SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");


        dolphinN.moveBackward(5.0f);
        dolphinN.attachObject(dolphinE);
        //dolphinN.attachObject(camera);
    

        sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));
        
        Light plight = sm.createLight("testLamp1", Light.Type.POINT);
        plight.setAmbient(new Color(.3f, .3f, .3f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
        plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(5f);
        
        SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);

    

            TextureManager tm = eng.getTextureManager();
            Texture redTexture = tm.getAssetByPath("Dolphin_HighPolyUV.png");
            RenderSystem rs = sm.getRenderSystem();
            TextureState state = (TextureState)
            rs.createRenderState(RenderState.Type.TEXTURE);
            state.setTexture(redTexture);
            dolphinE.setRenderState(state);

            setupOrbitCamera(eng, sm);
            setupInputs(sm);
            dolphinN.yaw(Degreef.createFrom(45.0f));

    }

     protected void setupOrbitCamera(Engine eng, SceneManager sm)
    { 
        SceneNode dolphinN = sm.getSceneNode("dolphinNode");
        SceneNode cameraN = sm.getSceneNode("MainCameraNode");
        camera = sm.getCamera("MainCamera");

        //dolphinN.attachObject(camera);

        gpName = im.getFirstGamepadName();
        //System.out.print("gpName: " + im.getFirstGamepadName());
        //System.out.println("gpName: " + gpName);
        orbitController = new Camera3Pcontroller(camera, cameraN, dolphinN, gpName, im);
        //orbitController = new OrbitAroundAction();


    }

   
    protected void setupInputs(SceneManager sm)
    { 
        im = new GenericInputManager();
        String kbName = im.getKeyboardName();
        gpName = im.getFirstGamepadName();
       
       SceneNode dolphinN = getEngine().getSceneManager().getSceneNode("dolphinNode");

        //orbitAction = new OrbitAroundAction();
        moveFwdAct = new MoveForwardAction(dolphinN);
        moveBwdAct = new MoveBackwardAction(dolphinN);
        //orbitController = new Camera3Pcontroller(camera, cameraN, dolphinN, gpName, im);
        //orbitAction = new OrbitAroundAction(this);
        
        /*im.associateAction(gpName,
        net.java.games.input.Component.Identifier.Button._0,
        orbitAction,
        InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);*/

        //moveFwdAct = new MoveForwardAction(dolphinN);
        //moveBwdAct = new MoveBackwardAction(dolphinN);
        //orbitAct =  new OrbitAroundAction(camera);
        //orbitController = new Camera3Pcontroller(camera, cameraN, dolphinN, gpName, im);

        im.associateAction(gpName,
            net.java.games.input.Component.Identifier.Button._3,
            moveBwdAct, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);


    }

    @Override
    protected void update(Engine engine) {
        // build and set HUD
        rs = (GL4RenderSystem) engine.getRenderSystem();
        elapsTime += engine.getElapsedTimeMillis();
        elapsTimeSec = Math.round(elapsTime/1000.0f);
        elapsTimeStr = Integer.toString(elapsTimeSec);
        counterStr = Integer.toString(counter);
        dispStr = "Time = " + elapsTimeStr + "   Keyboard hits = " + counterStr;
        rs.setHUD(dispStr, 15, 15);

        im.update(elapsTime);

        orbitController.updateCameraPosition();
    }

 
   


  protected ManualObject makeAxis(Engine eng, SceneManager sm) throws IOException
    { 

    
    ManualObject pyr = sm.createManualObject("Axis");
    ManualObjectSection pyrSec =
    pyr.createManualSection("AxisSection");
    pyr.setGpuShaderProgram(sm.getRenderSystem().
    getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
 
                float[] vertices = new float[]
                {0.0f,  3.5f, 0.0f, 0.0f, -0.5f, 0.0f,
                 3.5f,  0.0f, 0.0f, 0.0f, -0.5f, 0.0f,
                 0.0f,  0.0f, -3.5f,0.0f, -0.5f, 0.0f};
                 //0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

                

        int[] indices = new int[] {0,1,2,3,4,5};

    
        FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);

        pyrSec.setVertexBuffer(vertBuf);
        pyrSec.setIndexBuffer(indexBuf);

        FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().
        createRenderState(RenderState.Type.FRONT_FACE);

        pyr.setDataSource(DataSource.INDEX_BUFFER);
        //pyr.setRenderState(texState);
        pyr.setRenderState(faceState);
        
        return pyr;

    }
   
   




}



