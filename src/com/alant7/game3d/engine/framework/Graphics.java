package com.alant7.game3d.engine.framework;

import com.alant7.game3d.engine.math.*;
import com.alant7.game3d.engine.math.object.Shape2;
import com.alant7.game3d.engine.rendering.ObjectModel;
import com.alant7.game3d.engine.world.GameObject;
import com.alant7.game3d.engine.world.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Graphics extends Canvas implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	
	//ArrayList of all the 3D polygons - each 3D polygon has a 2D 'alant7.game3d.engine.math.object.PolygonObject' inside called 'DrawablePolygon'
	public static ArrayList<Shape2> Sorted = new ArrayList<>();

	//Used for keeping mouse in center
	Robot r;

	public static double[] ViewFrom = new double[] { 10, 15, 5},
					ViewTo = new double[] {0, 0, 0},
					LightDir = new double[] {1, 1, 1};
	
	//The smaller the zoom the more zoomed out you are and visa versa, although altering too far from 1000 will make it look pretty weird
	public static double zoom = 1000, MinZoom = 500, MaxZoom = 2500, MouseX = 0, MouseY = 0, MovementSpeed = 0.5;
	
	//FPS is a bit primitive, you can set the MaxFPS as high as u want
	public double drawFPS = 0, MaxFPS = 60, SleepTime = 1000.0/MaxFPS, LastRefresh = 0, StartTime = System.currentTimeMillis(), LastFPSCheck = 0, Checks = 0;
	//VertLook goes from 0.999 to -0.999, minus being looking down and + looking up, HorLook takes any number and goes round in radians
	//aimSight changes the size of the center-cross. The lower HorRotSpeed or VertRotSpeed, the faster the camera will rotate in those directions
	public double VertLook = -0.4, HorLook = 3, aimSight = 4, HorRotSpeed = 900, VertRotSpeed = 2200, SunPos = 0;

	//will hold the order that the polygons in the ArrayList alant7.game3d.engine.math.object.DPolygon should be drawn meaning alant7.game3d.engine.math.object.DPolygon.get(NewOrder[0]) gets drawn first
	int[] NewOrder;

	public static boolean SHOULD_SORT_POLYGONS = true;
	boolean[] Keys = new boolean[4];
	
	long repaintTime = 0;
	
	public Graphics() {
		this.addKeyListener(this);
		setFocusable(true);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);

		invisibleMouse();

		new Thread(this::GameLoop).start();
	}

	int avgRender = 0;

	public void GameLoop() {

		int FRAMES = 0;
		long LastUpdate = 0, MeasureStart = System.currentTimeMillis();

		int RenderTotal = 0;

		Calculator.SetPrederterminedInfo();
		System.out.println(" READY. CALLING.. ");

		if (GameWindow.Instance.WhenReady != null) {
			GameWindow.Instance.WhenReady.run();
			System.out.println(" RAN ! ");
		} else System.out.println(" NULL ! ");

		GameObject obj = new GameObject() {
			@Override
			public void Update() {

			}
		};
		obj.SetModel(new ObjectModel(
				Geometry.Cuboid(new Vector3(0, 0, 0), new Vector3(5, 5, 5), new Color[] {Color.WHITE})
		));

		GameObject obj2 = new GameObject() {
			@Override
			public void Update() {

			}
		};
		obj2.SetModel(new ObjectModel(
				Geometry.Cuboid(new Vector3(0, 0, 0), new Vector3(5, 5, 5), new Color[] {Color.WHITE})
		));
		obj2.Position = new Vector3(8, 0, 0);

		World.Objects.add(obj);
		World.Objects.add(obj2);

		while (true) {

			long Start = System.currentTimeMillis();
			if (LastUpdate - MeasureStart >= 1000) {
				drawFPS = FRAMES;
				avgRender = RenderTotal / FRAMES;

				RenderTotal = 0;
				FRAMES = 0;

				MeasureStart = Start;
			}

			Render();

			long End = System.currentTimeMillis();
			LastUpdate = End;

			RenderTotal += End - Start;

			FRAMES++;

			try {
				Thread.sleep((int)(1000 / MaxFPS));
			} catch (Exception e) {}
		}

	}

	void Render() {

		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			try {
				createBufferStrategy(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		java.awt.Graphics g = bs.getDrawGraphics();
		//Clear screen and draw background color
		g.setColor(new Color(132, 200, 73));
		g.fillRect(0, 0, (int) GameWindow.ScreenSize.getWidth(), (int) GameWindow.ScreenSize.getHeight());

		CameraMovement();

		//Calculated all that is general for this camera position
		Calculator.SetPrederterminedInfo();

		if (SHOULD_SORT_POLYGONS) {

			Sorted.clear();

			for (int i = 0; i < World.Objects.size(); i++) {

				World.Objects.get(i).UpdateRenderModel();
				Sorted.addAll(World.Objects.get(i).RenderModel.Polygons);

			}

			setOrder();

			for (int j = 0; j < Sorted.size(); j++) {
				g.setColor(Sorted.get(NewOrder[j]).c);
				g.fillPolygon(Sorted.get(NewOrder[j]).Polygon);
			}

		}

		//draw the cross in the center of the screen
		drawDirectionShow(g);

		//FPS display
		g.setColor(Color.BLACK);
		g.drawString(String.format("FPS: %s    RENDER_TIME: %sms    CAM_ROT: (%s, %s)", drawFPS, avgRender, Math.toDegrees(HorLook), VertLook), 40, 40);
		bs.show();

	}

	void setOrder() {
		double[] k = new double[Sorted.size()];
		NewOrder = new int[Sorted.size()];

		for (int i = 0; i < Sorted.size(); i++) {
			k[i] = Sorted.get(i).Distance;
			NewOrder[i] = i;
		}

		double temp;
		int tempr;
		for (int a = 0; a < k.length - 1; a++)
			for (int b = 0; b < k.length - 1; b++)
				if (k[b] < k[b + 1]) {
					temp = k[b];
					tempr = NewOrder[b];
					NewOrder[b] = NewOrder[b + 1];
					k[b] = k[b + 1];

					NewOrder[b + 1] = tempr;
					k[b + 1] = temp;
				}
	}
		
	void invisibleMouse()
	{
		 Toolkit toolkit = Toolkit.getDefaultToolkit();
		 BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
		 Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0,0), "InvisibleCursor");        
		 setCursor(invisibleCursor);
	}
	
	void drawDirectionShow(java.awt.Graphics g) {
		Vector2 p1x = new Vector2(), p2x = new Vector2(32, 0);
		Vector2 p1z = new Vector2(), p2z = new Vector2(32, 0);

		double[] xrot = Geometry.RotateAround(new Vector2(-HorLook + 3.14, -HorLook + 3.14), new double[]{p2x.x, p2x.y}, new double[]{0, 0});
		double[] zrot = Geometry.RotateAround(new Vector2(-HorLook - 3.14 / 2, -HorLook - 3.14 / 2), new double[]{p2z.x, p2z.y}, new double[]{0, 0});

		g.setColor(Color.RED);
		g.drawLine((int) (xrot[0] + GameWindow.ScreenSize.getWidth() / 2), (int) (xrot[1] + GameWindow.ScreenSize.getHeight() / 2), (int) (p1x.x + GameWindow.ScreenSize.getWidth() / 2), (int) (p1x.y + GameWindow.ScreenSize.getHeight() / 2));

		g.setColor(Color.GREEN);
		g.drawLine((int) (zrot[0] + GameWindow.ScreenSize.getWidth() / 2), (int) (zrot[1] + GameWindow.ScreenSize.getHeight() / 2), (int) (p1z.x + GameWindow.ScreenSize.getWidth() / 2), (int) (p1z.y + GameWindow.ScreenSize.getHeight() / 2));
	}
	
	void CameraMovement()
	{
		Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
		double xMove = 0, yMove = 0, zMove = 0;
		Vector VerticalVector = new Vector(0, 0, 1);
		Vector SideViewVector = ViewVector.CrossProduct(VerticalVector);
		
		if(Keys[0])
		{
			xMove += ViewVector.x ;
			yMove += ViewVector.y ;
			zMove += ViewVector.z ;
		}

		if(Keys[2])
		{
			xMove -= ViewVector.x ;
			yMove -= ViewVector.y ;
			zMove -= ViewVector.z ;
		}
			
		if(Keys[1])
		{
			xMove += SideViewVector.x ;
			yMove += SideViewVector.y ;
			zMove += SideViewVector.z ;
		}

		if(Keys[3])
		{
			xMove -= SideViewVector.x ;
			yMove -= SideViewVector.y ;
			zMove -= SideViewVector.z ;
		}
		
		Vector MoveVector = new Vector(xMove, yMove, zMove);
		MoveTo(ViewFrom[0] + MoveVector.x * MovementSpeed, ViewFrom[1] + MoveVector.y * MovementSpeed, ViewFrom[2] + MoveVector.z * MovementSpeed);
	}

	void MoveTo(double x, double y, double z)
	{
		ViewFrom[0] = x;
		ViewFrom[1] = y;
		ViewFrom[2] = z;
		updateView();
	}

	void MouseMovement(double NewMouseX, double NewMouseY) {
			double difX = (NewMouseX - GameWindow.ScreenSize.getWidth()/2);
			double difY = (NewMouseY - GameWindow.ScreenSize.getHeight()/2);
			difY *= 6 - Math.abs(VertLook) * 5;
			VertLook -= difY  / VertRotSpeed;
			HorLook += difX / HorRotSpeed;
	
			if(VertLook>0.999)
				VertLook = 0.999;
	
			if(VertLook<-0.999)
				VertLook = -0.999;
			
			updateView();
	}
	
	void updateView()
	{
		double r = Math.sqrt(1 - (VertLook * VertLook));
		ViewTo[0] = ViewFrom[0] + r * Math.cos(HorLook);
		ViewTo[1] = ViewFrom[1] + r * Math.sin(HorLook);		
		ViewTo[2] = ViewFrom[2] + VertLook;
	}


	
	void CenterMouse() 
	{
			try {
				r = new Robot();
				r.mouseMove((int) GameWindow.ScreenSize.getWidth()/2, (int) GameWindow.ScreenSize.getHeight()/2);
			} catch (AWTException e) {
				e.printStackTrace();
			}
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W)
			Keys[0] = true;
		if(e.getKeyCode() == KeyEvent.VK_A)
			Keys[1] = true;
		if(e.getKeyCode() == KeyEvent.VK_S)
			Keys[2] = true;
		if(e.getKeyCode() == KeyEvent.VK_D)
			Keys[3] = true;

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			for (int i = 0; i < World.Objects.size(); i++)
				World.Objects.get(i).Rotate(World.Objects.get(i).Rotation.Add(new Vector2(0.5, 1)), new Vector2(3, 3));
		}

		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W)
			Keys[0] = false;
		if(e.getKeyCode() == KeyEvent.VK_A)
			Keys[1] = false;
		if(e.getKeyCode() == KeyEvent.VK_S)
			Keys[2] = false;
		if(e.getKeyCode() == KeyEvent.VK_D)
			Keys[3] = false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseDragged(MouseEvent arg0) {
		MouseMovement(arg0.getX(), arg0.getY());
		MouseX = arg0.getX();
		MouseY = arg0.getY();
		CenterMouse();
	}
	
	public void mouseMoved(MouseEvent arg0) {
		MouseMovement(arg0.getX(), arg0.getY());
		MouseX = arg0.getX();
		MouseY = arg0.getY();
		CenterMouse();
	}
	
	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if(arg0.getUnitsToScroll()>0)
		{
			if(zoom > MinZoom)
				zoom -= 25 * arg0.getUnitsToScroll();
		}
		else
		{
			if(zoom < MaxZoom)
				zoom -= 25 * arg0.getUnitsToScroll();
		}	
	}
}
