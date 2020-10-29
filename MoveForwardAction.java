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


public class MoveForwardAction extends AbstractInputAction
{

private Node avN;
private Camera camera;

int dx, dy, dz, ax, ay, az;


public MoveForwardAction(Node n)
	{
	 	avN = n;
	}

	public void performAction(float time, Event e)
	{ 

		Angle rotAmt = Degreef.createFrom(10.0f);
        avN.yaw(rotAmt);

	}
}