package myGame;

import java.awt.*;
import java.awt.event.*;
import java.nio.*;					// For FloatBuffer & IntBuffer
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
//import ray.input.InputManager.INPUT_ACT
import ray.rml.Vector3f;
import ray.rage.math.Transform;


public class Camera3Pcontroller 
{ 
	//Mygame game = new MyGame();

	private Camera camera; //the camera being controlled
	private SceneNode cameraN; //the node the camera is attached to
	private SceneNode target; //the target the camera looks at

    private float cameraAzimuth; //rotation of camera around Y axis

	private float cameraElevation; //elevation of camera above target
	private float radias; //distance between camera and target
	private Vector3 targetPos; //target’s position in the world
	private Vector3 worldUpVec;
	//private String name = "gpName";

	//private InputManager im;

	//float cameraAzimuth; //rotation of camera around Y axis

	private Action orbitAction;


	public Camera3Pcontroller(Camera cam, SceneNode camN,SceneNode targ,String controllerName, InputManager im)
	{ 
		//Game game = new MyGame();

		//System.out.println("Cam 3P");
		//System.out.println("gpName: " + controllerName);
		//System.out.println(" ");

		camera = cam;
		cameraN = camN;
		target = targ;
		//controllerName = name;
		cameraAzimuth = 225.0f; // start from BEHIND and ABOVE the target
		cameraElevation = 30.0f; // elevation is in degrees
		radias = 2.0f;
		worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);

		setupInput(im, controllerName);
		
		updateCameraPosition();
	}


	 public void updateCameraPosition()
    { 
       	double theta = Math.toRadians(cameraAzimuth); // rot around target
		double phi = Math.toRadians(cameraElevation); // altitude angle
		double x = radias * Math.cos(phi) * Math.sin(theta);
		double y = radias * Math.sin(phi);
		double z = radias * Math.cos(phi) * Math.cos(theta);

		cameraN.setLocalPosition(Vector3f.createFrom((float)x, (float)y, (float)z).add(target.getLocalPosition()));
		cameraN.lookAt(target, worldUpVec);



	}

	private void setupInput(InputManager im, String cn)
	{ 
		 //im = new GenericInputManager();
		 //cn = im.getFirstGamepadName();
		//String s = cn;

		orbitAction = new OrbitAroundAction(this);

		System.out.printf("Cam Input " + cn); 
		System.out.println(" ");   													//Button._3
		
		im.associateAction(cn,
		net.java.games.input.Component.Identifier.Axis.X,							//Axis.RX, 
		orbitAction,
		InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

	

	
	}


private class OrbitAroundAction extends AbstractInputAction
{ // Moves the camera around the target (changes camera azimuth).

	//private float cameraAzimuth;
	private MyGame game;
	private Camera3Pcontroller orbitController;
	//private Camera camera;
	//private SceneNode target; //the node the camera is attached to

	public OrbitAroundAction(Camera3Pcontroller c)
	{
		orbitController = c;
		//orbitController = c;
		System.out.println("Orbit Around: " + cameraAzimuth);
																				// net.java.games.input.Event evt evt
																				// Event e
	}
		
		public void performAction(float time, Event e)
		{ 

		
		float rotAmount = 0.2f;

		cameraAzimuth += rotAmount;

		System.out.println("camera Azimuth:" + cameraAzimuth);
		System.out.println("Orbit Around");
		//System.out.println("Orbit Around var:" + evt.getValue());

		
				
			if (e.getValue() < -0.2)
				{ rotAmount = -0.2f; }

			else
				{ 

				if (e.getValue() > 0.2)
					{ rotAmount = 0.2f; }

				  else
					{ rotAmount = 0.0f; }
				}

				System.out.println("Rotation:" + rotAmount);

				cameraAzimuth += rotAmount;
				cameraAzimuth = cameraAzimuth % 360;
				updateCameraPosition();

				/*if (e.getValue() > 0.1)
				{ 

					//cameraN.moveBackward(0.08f); 
					System.out.println("Rotation:" + rotAmount);
					
				}*/



		}


	}

}