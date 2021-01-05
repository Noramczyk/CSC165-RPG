package a3;


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

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;

import java.io.IOException;
import ray.networking.IGameConnection.ProtocolType;
import ray.networking.client.GameConnectionClient;
import ray.networking.server.IClientInfo;




public class ProtocolClient extends GameConnectionClient
{ 
private MyGame game;

private UUID id;
private int remPort;
private InetAddress remAddr;
private ProtocolType ptype;
//private Vector3 pos;

private float playX, playY, playZ;

//int remPort = 3500;

//private Vector<GhostAvatar> ghostAvatars;

		public ProtocolClient(InetAddress remAddr, int remPort, ProtocolType pType, MyGame game) throws IOException
		{ 
		super(remAddr, remPort, pType);
		this.game = game;
		this.id = UUID.randomUUID();
		//pos = Vector3f.createFrom(0.1f, 0.1f, 0.0f);

		System.out.println("remAddr: " + remAddr);
		System.out.println("remPort: " + remPort);
		System.out.println("Type: " + pType);

		//this.ghostAvatars = new Vector<GhostAvatar>();

		}

		@Override
		protected void processPacket(Object msg)
		{ 

		playX = game.xP;
		playY = game.yP;
		playZ = game.zP;

		String strMessage = (String)msg;
		String[] msgTokens = strMessage.split(",");

		System.out.println("Process Packets" + msg);

		//UUID clientID = UUID.fromString(msgTokens[1]);

			if(msgTokens.length > 0)
			{
				if(msgTokens[0].compareTo("join") == 0) // receive "join"
				{ // format: join, success or join, failure

					if(msgTokens[1].compareTo("success") == 0)
					{ 
						game.isClientConnected = true;

						System.out.println(" Success Game Connected");

						//sendCreateMessage(game.getPlayerPosition());
					}

					if(msgTokens[1].compareTo("failure") == 0)
					{ 

					game.isClientConnected = false;
					System.out.println("Failure");

					} 

				}
			


	
		}

		/*	if ((messageTokens[0].compareTo("dsfr") == 0 ) || (messageTokens[0].compareTo("create")==0))
			{ 
			// format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z
			UUID ghostID = UUID.fromString(messageTokens[1]);
			Vector3 ghostPosition = Vector3f.createFrom(Float.parseFloat(messageTokens[2]), 
						Float.parseFloat(messageTokens[3]),Float.parseFloat(messageTokens[4]));

				try
				{ 
					createGhostAvatar(ghostID, ghostPosition);
				} 
				catch (IOException e)
				{ 
					System.out.println("error creating ghost avatar");
				} 
			}*/

			/*if(messageTokens[0].compareTo("wsds") == 0) // rec. "create…"
			{ // etc….. }

				if(messageTokens[0].compareTo("wsds") == 0) // rec. "wants…"
				{ // etc….. }
					if(messageTokens[0].compareTo("move") == 0) // rec. "move..."
					{ // etc….. }
					} 
				}*/
		}

	public void sendJoinMessage() 
																	
	{ 																			// format: join, localId

		//System.out.println("Process Packets " + id);
		//System.out.println(isConnected());

		try
		{ 
			sendPacket(new String("join," + id.toString())); 			// join,
			System.out.println("Process Packets " + id.toString());
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		} 
	}

	public void sendByeMessage() 
																	
	{ 																			// format: join, localId

		//System.out.println("Process Packets " + id);
		//System.out.println(isConnected());

		try
		{ 

			sendPacket(new String("bye," + id.toString())); 			// join,
			System.out.println("Process Bye message: " + id.toString());

		} 

		catch (IOException e) 
		{ 
			e.printStackTrace();
		} 

	}

	public void sendCreateMessage()
	{ // format: (create, localId, x,y,z)

		try
		{ 

		String message = new String("create," + id.toString());
		message += "," + playX +"," + playY + "," + playZ;
		sendPacket(message);

		}
		catch (IOException e) 
		{ 
			e.printStackTrace();
		} 

	}




	/*public void sendMoveMessage(Vector3 pos)
	{
		
		try
			{ 
				String message = new String("Move," + id.toString());
				message += "," + pos.getLocalPosition().x() +
						"," + pos.getLocalPosition().y() + "," + pos.getLocalPosition().z();

				sendPacket(message);
			}

		catch (IOException e) 
		{ 
			e.printStackTrace();
		} 

	}*/


	




}