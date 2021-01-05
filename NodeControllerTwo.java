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

import ray.rml.Vector3f;

import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;


public class NodeControllerTwo extends AbstractController
{
	private float scaleRate = .003f; 
	private float cycleTime = 5.0f; 
	private float totalTime = 0.0f;
	private float direction = 1.0f;


	@Override
	protected void updateImpl(float elapsedTimeMillis)
		{ 
			totalTime += elapsedTimeMillis;
			float scaleAmt = 0.08f;
			//float scaleAmtX = 1.0f + direction * scaleRate;

		if (totalTime > cycleTime)
			{ 
				direction = -direction;
				totalTime = 0.0f;
			}


			for (Node n : super.controlledNodesList)		// Node controller TWO			
				{ 
					Vector3 curScale = n.getLocalPosition();

				
					curScale = Vector3f.createFrom( curScale.x(), curScale.y() - scaleAmt,  curScale.z());

						
					n.setLocalPosition(curScale);
				}
		}
}