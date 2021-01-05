package myGameEngine;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import ray.rage.scene.controllers.*;
import static ray.rage.scene.SkeletalEntity.EndType.*;
import java.util.*;
import ray.input.action.AbstractInputAction;
import ray.rage.*;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import ray.input.*;
import ray.input.action.*;

import a3.*;

public class Camera3Pcontroller { 
    private Engine engine;
    private Camera camera;          //the camera being controlled  
    private SceneNode cameraN;      //the node the camera is attached to  
    private SkeletalEntity skelly;
    private SceneNode target;       //the target the camera looks at
    private int timeRemainingSec = 0;
    private int timeRemaining = 3;
    private float elapsTime = 0.0f;  
    private float cameraAzimuth;    //rotation of camera around Y axis  
    private float cameraElevation;  //elevation of camera above target  
    private float radius;           //distance between camera and target  
    private Vector3 targetPos;      //target’s position in the world  
    private Vector3 worldUpVec;
    private int playerNumber;  
    private SceneNode tessN;
    private boolean isMoving = false;
    private boolean isIdle = true;
    private MyGame game;
    private Node spearN;
     
    public Camera3Pcontroller(Camera cam, SceneNode camN, SkeletalEntity skel, SceneNode targ, SceneNode spear,
                                                                                        SceneNode tess, InputManager im, Engine e, int pNum)  { 
        engine = e;
        camera = cam;    
        cameraN = camN;  
        skelly = skel;  
        target = targ; 
        spearN = spear;                 // Pass spear instance
        tessN = tess;
        playerNumber = pNum;   
        cameraAzimuth = 0.0f;         // start from BEHIND and ABOVE the target    
        cameraElevation = 30.0f;        // elevation is in degrees    
        radius = 2.0f;    
        worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);    
        setupInput(im);    
        updateCameraPosition();
          
    } 
    // Updates camera position: computes azimuth, elevation, and distance    
    // relative to the target in spherical coordinates, then converts those  
    // to world Cartesian coordinates and setting the camera position  
    public void updateCameraPosition()   { 
        double theta = Math.toRadians(cameraAzimuth);       // rot around target    
        double phi = Math.toRadians(cameraElevation);       // altitude angle    
        double x = radius * Math.cos(phi) * Math.sin(theta);    
        double y = radius * Math.sin(phi);    
        double z = radius * Math.cos(phi) * Math.cos(theta);   
        cameraN.setLocalPosition(Vector3f.createFrom ((float)x, (float)y, (float)z).add(target.getWorldPosition()));    
        cameraN.lookAt(target, worldUpVec);

    }
    protected void updateVerticalPosition()
    {
        Tessellation tessE =((Tessellation)tessN.getAttachedObject("tessE"));
        Vector3 worldAvatarPosition = target.getWorldPosition();
        Vector3 localAvatarPosition = target.getLocalPosition();

        Vector3 newAvatarPosition = Vector3f.createFrom(localAvatarPosition.x(), tessE.getWorldHeight(worldAvatarPosition.x(),worldAvatarPosition.z())+ 0.3f, localAvatarPosition.z());

        target.setLocalPosition(newAvatarPosition);
    }
    private void setupInput(InputManager im)  
    { 
        Action moveForwardAction = new MoveForwardAction();
        Action moveBackwardAction = new MoveBackwardAction();
        Action moveControllerAction = new MoveControllerAction();
        Action turnControllerAction = new TurnControllerAction();
        Action orbitAUpAction = new OrbitAroundUpAction();
        Action orbitADownAction = new OrbitAroundDownAction();
        Action orbitALeftAction = new OrbitAroundLeftAction();
        Action orbitARightAction = new OrbitAroundRightAction();
        Action turnLeftAction = new TurnLeftAction();
        Action turnRightAction = new TurnRightAction();
        Action escape = new EscapeGame();

        Action rightAction = new RightAction();
        Action leftAction = new LeftAction();

        Action zoomInAction = new ZoomInAction();
        Action zoomOutAction = new ZoomOutAction();
        Action orbitATrigAction = new OrbitAroundTriggerAction();
        Action orbitAActionRY = new OrbitAroundActionRY();
        Action zoomAxisAction = new ZoomAxisAction();   
        Action throwAction = new ThrowAction();
        Action idleAction = new IdleAction();
        ArrayList<Controller> cs = im.getControllers();
        for(Controller contr : cs ) {
            if(playerNumber == 1) {
                if(contr.getType() == Controller.Type.MOUSE) {
                    im.associateAction(contr,net.java.games.input.Component.Identifier.Axis.Z,zoomAxisAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    
                }
                if(contr.getType() == Controller.Type.KEYBOARD){
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.UP, orbitAUpAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.DOWN, orbitADownAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.Q, throwAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.E, idleAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.ESCAPE, escape, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);


                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.A, leftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.D, rightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.LEFT, turnLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.RIGHT, turnRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);


                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.X, zoomInAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.Z, zoomOutAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.W, moveForwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Key.S, moveBackwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                }
                else if(contr.getType() == Controller.Type.GAMEPAD || contr.getType() == Controller.Type.STICK) {
                }
            }
            else if(playerNumber == 2) {
                if(contr.getType() == Controller.Type.KEYBOARD){
                }
                else if(contr.getType() == Controller.Type.GAMEPAD || contr.getType() == Controller.Type.STICK) {
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Axis.Y, moveControllerAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Axis.X, turnControllerAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr,net.java.games.input.Component.Identifier.Axis.Z,orbitATrigAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);//  similar input set up for OrbitRadiusAction, OrbitElevationAction
                    im.associateAction(contr,net.java.games.input.Component.Identifier.Axis.RY,orbitAActionRY,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Button._4, zoomInAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                    im.associateAction(contr, net.java.games.input.Component.Identifier.Button._5, zoomOutAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                }
            }
        }
    }
    private class TurnLeftAction extends AbstractInputAction  
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
            Matrix3 matRot = Matrix3f.createRotationFrom(Degreef.createFrom(100.0f * (engine.getElapsedTimeMillis()/1000)), worldUp);
            float rotAmount;
     
                rotAmount = 100.0f * (engine.getElapsedTimeMillis()/1000);

                cameraAzimuth += rotAmount;      
                cameraAzimuth = cameraAzimuth % 360;  
                updateVerticalPosition();
                updateCameraPosition();
        }
    }

     private class RightAction extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)
        {
            float fX = target.getLocalForwardAxis().x();
            float fY = target.getLocalForwardAxis().y();
            float fZ = target.getLocalForwardAxis().z();

            Angle rotAmtN;
            Angle rotAmtS;
                
                System.out.println(fX + " " + fY + " " + fZ);

                rotAmtN = Degreef.createFrom(-1.2f);
                rotAmtS = Degreef.createFrom(-1.2f);
                target.yaw(rotAmtN);
                spearN.yaw(rotAmtS);
        }
    }
     private class LeftAction extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)
        {
            Angle rotAmtN;
            Angle rotAmtS;
                

                rotAmtN = Degreef.createFrom(1.2f);
                rotAmtS = Degreef.createFrom(1.2f);
                target.yaw(rotAmtN);
                spearN.yaw(rotAmtS);
  
        }
    }

     private class EscapeGame extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)
        {
           System.out.println("Shutdown requested!");
           game.setState(Game.State.STOPPING);
  
        }
    }


    private class ThrowAction extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)
        {
            isMoving = false;
            skelly.stopAnimation();
            skelly.playAnimation("throw", 1.5f, NONE, 0);  
        }
    }
    private class IdleAction extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)
        {
            isMoving = false;
            skelly.stopAnimation();
            skelly.playAnimation("idle", 0.5f, LOOP, 0);  
        }
    }
    private class MoveForwardAction extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            if (evt.getValue() == 1) {
                //System.out.println(evt.getValue());
                target.moveLeft(-10.0f * (engine.getElapsedTimeMillis()/1000));
                updateVerticalPosition();
                if(isMoving == false) {
                    isMoving = true;
                    skelly.stopAnimation();
                    skelly.playAnimation("walk", 1.0f, LOOP, 0);  
                }
            }
        }
    }
    private class MoveBackwardAction extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            target.moveLeft(10.0f * (engine.getElapsedTimeMillis()/1000));
            updateVerticalPosition();
        }
    }
    private class MoveControllerAction extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)
        {
            if (evt.getValue() < -0.2)  {  
                target.moveBackward(-10.0f * (engine.getElapsedTimeMillis()/1000));
                updateVerticalPosition();
            }
            if (evt.getValue() > 0.2)   { 
                target.moveBackward(10.0f * (engine.getElapsedTimeMillis()/1000));
                updateVerticalPosition();
            }
        }
    }
    private class TurnControllerAction extends AbstractInputAction
    {
        public void performAction(float time, net.java.games.input.Event evt)
        {
            if (evt.getValue() < -0.2)  {  
                Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
                Matrix3 matRot = Matrix3f.createRotationFrom(Degreef.createFrom(100.0f * (engine.getElapsedTimeMillis()/1000)), worldUp);
                target.setLocalRotation(matRot.mult(target.getWorldRotation()));
                target.moveBackward(-0.001f * (engine.getElapsedTimeMillis()/1000));
                float rotAmount;
                rotAmount=100.0f * (engine.getElapsedTimeMillis()/1000);
                cameraAzimuth += rotAmount;      
                cameraAzimuth = cameraAzimuth % 360;  
                updateVerticalPosition();
                updateCameraPosition();
                
            }
            if (evt.getValue() > 0.2)   { 
                Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
                Matrix3 matRot = Matrix3f.createRotationFrom(Degreef.createFrom(-100.0f * (engine.getElapsedTimeMillis()/1000)), worldUp);
                target.setLocalRotation(matRot.mult(target.getWorldRotation()));
                target.moveBackward(-0.001f * (engine.getElapsedTimeMillis()/1000));
                float rotAmount;
                rotAmount=-100.0f * (engine.getElapsedTimeMillis()/1000);
                cameraAzimuth += rotAmount;      
                cameraAzimuth = cameraAzimuth % 360;  
                updateVerticalPosition();
                updateCameraPosition();
            }
        }
    }
    private class TurnRightAction extends AbstractInputAction  
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
            Matrix3 matRot = Matrix3f.createRotationFrom(Degreef.createFrom(-100.0f * (engine.getElapsedTimeMillis()/1000)), worldUp);
            float rotAmount;
            //Angle rotAmtN;
            Angle rotAmtS;
                

                //rotAmtN = Degreef.createFrom(-1.2f);
                //rotAmtS = Degreef.createFrom(1.2f);
                //target.yaw(rotAmtN);
                //spearN.yaw(rotAmtS);

            //target.setLocalRotation(matRot.mult(target.getWorldRotation()));
            //target.moveBackward(-0.001f * (engine.getElapsedTimeMillis()/1000));
            //float rotAmount;
            rotAmount=-100.0f * (engine.getElapsedTimeMillis()/1000);

            cameraAzimuth += rotAmount;      
            cameraAzimuth = cameraAzimuth % 360; 
            updateVerticalPosition(); 
            updateCameraPosition();
        }
    }
    private class ZoomInAction extends AbstractInputAction  
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            if(radius >= 1.0f) {
                float zoomAmount;
                zoomAmount = -1.0f * (engine.getElapsedTimeMillis()/1000);
                radius += zoomAmount;
                updateCameraPosition();
            }
        }
    }
    private class ZoomOutAction extends AbstractInputAction  
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            if(radius <= 3.0f) {
                float zoomAmount;
                zoomAmount = 1.0f * (engine.getElapsedTimeMillis()/1000);
                radius += zoomAmount;
                updateCameraPosition();
            }
        }
    }
    private class OrbitAroundUpAction extends AbstractInputAction  
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            if(cameraElevation < 89.0f) {
                //System.out.println(cameraElevation);
                float rotAmount;
                rotAmount=20.0f * (engine.getElapsedTimeMillis()/1000);
                cameraElevation += rotAmount;      
                cameraElevation = cameraElevation % 360;  
                updateCameraPosition();  
            }  
        }
    }
    private class OrbitAroundDownAction extends AbstractInputAction  
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            if(cameraElevation >= 0) {
                //System.out.println(cameraElevation);
                float rotAmount;
                rotAmount=-20.0f * (engine.getElapsedTimeMillis()/1000);
                cameraElevation += rotAmount;      
                cameraElevation = cameraElevation % 360;  
                updateCameraPosition(); 
            }   
        }
    }
    private class OrbitAroundLeftAction extends AbstractInputAction  
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            float rotAmount;
            rotAmount=-20.0f * (engine.getElapsedTimeMillis()/1000);
            cameraAzimuth += rotAmount;      
            cameraAzimuth = cameraAzimuth % 360;  
            updateCameraPosition();    
        }
    }
    private class OrbitAroundRightAction extends AbstractInputAction  
    {
        public void performAction(float time, net.java.games.input.Event evt)    
        {
            float rotAmount;
            rotAmount=20.0f* (engine.getElapsedTimeMillis()/1000);
            cameraAzimuth += rotAmount;      
            cameraAzimuth = cameraAzimuth % 360;  
            updateCameraPosition();    
        }
    }
    private class OrbitAroundTriggerAction extends AbstractInputAction  
    { 
        // Moves the camera around the target (changes camera azimuth).    
        public void performAction(float time, net.java.games.input.Event evt)    
        { 
            float rotAmount;      
            if (evt.getValue() < -0.2)  { 
                rotAmount=20.0f * (engine.getElapsedTimeMillis()/1000); 
            } else { 
                if (evt.getValue() > 0.2)    { 
                    rotAmount=-20.0f * (engine.getElapsedTimeMillis()/1000); 
                }  else  { 
                    rotAmount=0.0f * (engine.getElapsedTimeMillis()/1000); 
                }      
            }       
        cameraAzimuth += rotAmount;      
        cameraAzimuth = cameraAzimuth % 360;  
        updateCameraPosition();  
        } 
    }
    private class OrbitAroundActionRY extends AbstractInputAction  
    { 
        // Moves the camera around the target (changes camera azimuth).    
        public void performAction(float time, net.java.games.input.Event evt)    
        { 
            float rotAmount;      
            if (evt.getValue() < -0.2)  { 
                if(cameraElevation < 89.0f) {
                    //System.out.println(cameraElevation);
                    rotAmount=20.0f * (engine.getElapsedTimeMillis()/1000);
                    cameraElevation += rotAmount;      
                    cameraElevation = cameraElevation % 360;  
                    updateCameraPosition();  
                }  
            } else { 
                if (evt.getValue() > 0.2)    { 
                    if(cameraElevation >= 0) {
                        //System.out.println(cameraElevation);
                        rotAmount=-20.0f * (engine.getElapsedTimeMillis()/1000);
                        cameraElevation += rotAmount;      
                        cameraElevation = cameraElevation % 360;  
                        updateCameraPosition(); 
                    }    
                }  else  { 
                    rotAmount=0.0f; 
                }      
            }       
        } 
    } //  similar for OrbitRadiusAction, OrbitElevationAction
    private class ZoomAxisAction extends AbstractInputAction  
    { 
        // Moves the camera around the target (changes camera azimuth).    
        public void performAction(float time, net.java.games.input.Event evt)    
        { 
            float rotAmount;      
            if (evt.getValue() < -0.2)  { 
                if(radius <= 3.0f) {
                    float zoomAmount;
                    zoomAmount = 10.0f * (engine.getElapsedTimeMillis()/1000);
                    radius += zoomAmount;
                    updateCameraPosition();
                }
            } else { 
                if (evt.getValue() > 0.2)    { 
                    if(radius >= 1.0f) {
                        float zoomAmount;
                        zoomAmount = -10.0f * (engine.getElapsedTimeMillis()/1000);
                        radius += zoomAmount;
                        updateCameraPosition();
                    }    
                }  else  { 
                    rotAmount=0.0f * (engine.getElapsedTimeMillis()/1000); 
                }      
            }       
        } 
    } //  similar for OrbitRadiusAction, OrbitElevationAction
} 