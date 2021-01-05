package a3;

import java.io.*;
import java.util.*;
import net.java.games.input.Event;


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




public class SendCloseConnectionPacketAction extends AbstractInputAction

{

	private ProtocolClient protClient;
	private MyGame game;
	//private boolean isClientConnected;

	public SendCloseConnectionPacketAction(MyGame g, ProtocolClient p)
	{
		game = g;
		protClient = p;
	}



 // for leaving the game... need to attach to an input device
@Override
	public void performAction(float time, Event evt)
	{ 
		//if(protClient != null && game.isClientConnected == true)
		//{
			protClient.sendByeMessage();
			System.out.println("Shutdown requested!");
			game.setState(Game.State.STOPPING);

			//protClient.sendByeMessage();
			
			//System.out.println("Shutdown requested!");
			//game.setState(Game.State.STOPPING);
		
		//}
			
	
	} 


}