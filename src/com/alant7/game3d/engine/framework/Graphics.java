package com.alant7.game3d.engine.framework;

import com.alant7.game3d.engine.event.KeyboardListener;
import com.alant7.game3d.engine.math.*;
import com.alant7.game3d.engine.math.object.Shape2;
import com.alant7.game3d.engine.rendering.animation.AnimationManager;
import com.alant7.game3d.engine.rendering.animation.ObjectAnimation;
import com.alant7.game3d.engine.userinterface.GraphicalUserInterface;
import com.alant7.game3d.engine.world.Camera;
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
import java.util.HashMap;

import static com.alant7.game3d.engine.world.Camera.*;

public class Graphics extends Canvas implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	// Sorted polygons
	public static ArrayList<Shape2> Sorted = new ArrayList<>();

	// Used for keeping mouse in center
	static Robot r;

	// Cache for already calculated vertices so that they are not calculated multiple times
	public static HashMap<Vector3, double[]> PROJECTED_VECTOR_CACHE = new HashMap<>();

	// FPS Counter and limiter
	public double FpsCounter = 0, FpsCap = 60;

	// Render distance and coordinates that are used to calculate render distance for each object
	public static double RENDER_DISTANCE = 30;
	public static double[] RENDER_DISTANCE_REF = Camera.ViewFrom;

	// Will hold the order that the polygons in the ArrayList alant7.game3d.engine.math.object.DPolygon should be drawn meaning alant7.game3d.engine.math.object.DPolygon.get(NewOrder[0]) gets drawn first
	int[] NewOrder;

	// Flags that are checked every frame before rendering
	public static boolean SHOULD_SORT_POLYGONS		 = true,
						  SHOULD_RECALCULATE_SHADOWS = true,
						  SHOULD_UPDATE_SHADOWS 	 = true;

	// Background color that is drawn before all objects
	public static Color BACKGROUND = Color.GRAY;
	
	public Graphics() {
		this.addKeyListener(this);
		setFocusable(true);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);

		new Thread(this::GameLoop).start();

	}

	// Variables used for debugging
	public static int VECTORS_CALCULATED, avgFrameTime = 0, avgRender = 0, avgCalc = 0, avgSort = 0, TotalRender = 0, TotalCalc = 0, TotalSort = 0;

	public void GameLoop() {

		// FPS COUNTER VARIABLES
		int FRAMES = 0, TotalFrameTime = 0;
		long LastUpdate = 0, MeasureStart = System.currentTimeMillis();

		// PREPARE CALCULATOR FOR CALCULATING
		Calculator.Prepare();

		// CALL FUNCTIONS WHEN ENGINE IS READY
		if (GameWindow.Instance.WhenReady != null) GameWindow.Instance.WhenReady.run();

		while (true) {

			long Start = System.currentTimeMillis();
			if (LastUpdate - MeasureStart >= 1000) {

				FpsCounter = FRAMES;
				avgFrameTime 	= TotalFrameTime / FRAMES;
				avgRender    	= TotalRender    / FRAMES;
				avgCalc			= TotalCalc 	 / FRAMES;
				avgSort			= TotalSort		 / FRAMES;
				TotalFrameTime 	= 0;
				TotalRender    	= 0;
				TotalCalc		= 0;
				TotalSort		= 0;
				FRAMES 			= 0;
				MeasureStart 	= Start;

			}

			NotifyListeners();
			Render();

			LastUpdate = System.currentTimeMillis();;
			TotalFrameTime += LastUpdate - Start;

			FRAMES++;

			try {
				Thread.sleep((int)(1000 / FpsCap - (LastUpdate - Start)));
			} catch (Exception e) {}
		}

	}

	void NotifyListeners() {

		for (int i = 0; i < KeyboardListener.LISTENERS.size(); i++)
			for (int j = 0; j < KeyboardListener.KEYS_DOWN.size(); j++)
				KeyboardListener.LISTENERS.get(i).KeyPressed(KeyboardListener.KEYS_DOWN.get(j));

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

		java.awt.Graphics2D g = (Graphics2D)bs.getDrawGraphics();

		//Clear screen and draw background color
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, (int) GameWindow.ScreenSize.getWidth(), (int) GameWindow.ScreenSize.getHeight());

		VECTORS_CALCULATED = 0;

		Sorted.clear();
		PROJECTED_VECTOR_CACHE.clear();

		long CalcStart = System.currentTimeMillis();

		for (int i = AnimationManager.ObjectAnimations.size() - 1; i >= 0; i--) {
			GameObject Object = AnimationManager.ObjectAnimations.get(i);
			Object.DefaultAnimationUpdate(new Vector3(), Object.Animation.GetCurrentRotation());

			if (Object.Animation.Completed) {
				if (!Object.Animation.Loop) AnimationManager.ObjectAnimations.remove(Object);
				else Object.Animation.Restart();
			}
		}

		for (int i = World.Objects.size() - 1; i >= 0; i--) {

			GameObject Object = World.Objects.get(i);
			if (Object.ShouldBeRemoved) {
				World.Objects.remove(i);
				continue;
			}

			Object.Update();
			Object.UpdateRenderModel();

			Sorted.addAll(Object.RenderModel.Polygons);

		}

		TotalCalc += System.currentTimeMillis() - CalcStart;

		g.setColor(new java.awt.Color(112, 177, 52));
		for (int i = 0; i < World.LightSources.size(); i++) {
			if (SHOULD_UPDATE_SHADOWS) {
				World.LightSources.get(i).UpdateShadows();
				SHOULD_UPDATE_SHADOWS = false;
			}
			for (int j = 0; j < World.LightSources.get(i).Shadows.size(); j++) {
				Shape2 Proj = World.LightSources.get(i).Shadows.get(j).Project();
				if (Proj.Visible()) g.fillPolygon(Proj.x, Proj.y, Proj.x.length);
			}
		}


		long SortStart = System.currentTimeMillis();
		setOrder();
		TotalSort += System.currentTimeMillis() - SortStart;

		long DrawStart = System.currentTimeMillis();
		for (int j = 0; j < Sorted.size(); j++) {

			if (!Sorted.get(NewOrder[j]).Visible()) continue;

			g.setPaint(Sorted.get(NewOrder[j]).p);
			g.fillPolygon(Sorted.get(NewOrder[j]).x, Sorted.get(NewOrder[j]).y, Sorted.get(NewOrder[j]).x.length);

			g.setColor(Color.BLACK);
			g.drawPolygon(Sorted.get(NewOrder[j]).x, Sorted.get(NewOrder[j]).y, Sorted.get(NewOrder[j]).x.length);

		}

		// Draw direction for X, Y, Z axis
		//drawDirectionShow(g);

		//FPS display
		g.setColor(Color.BLACK);
		g.drawString(String.format("Lights: " + World.LightSources.size() + " FPS: %s  FRAME_TIME: %sms  CALC_TIME: %sms  RENDER_TIME: %sms  SORT_TIME: %sms  CAM_ROT: (%s, %s)  CAM_POS: (%s, %s, %s)  VECTORS_CALC: %s", FpsCounter, avgFrameTime, avgCalc, avgRender, avgSort, Math.toDegrees(Camera.HorLook), VertLook, ViewFrom[0], ViewFrom[2], ViewFrom[1], VECTORS_CALCULATED), 40, 40);

		for (int i = 0; i < GraphicalUserInterface.GetComponents().size(); i++)
			GraphicalUserInterface.GetComponents().get(i).Render(g);

		TotalRender += System.currentTimeMillis() - DrawStart;

		bs.show();

	}

	// Reorder shapes so that they are drawn from furthest to closest.
	// Will be changed in the future ! O(n^2)
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
		
	public static void InvisibleMouse() {
		 Toolkit toolkit = Toolkit.getDefaultToolkit();
		 BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
		 Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0,0), "InvisibleCursor");        
		 GameWindow.Instance.Graphics.setCursor(invisibleCursor);
	}
	
	void drawDirectionShow(java.awt.Graphics g) {
		Vector2 p1x = new Vector2(), p2x = new Vector2(32, 0);
		Vector2 p1z = new Vector2(), p2z = new Vector2(32, 0);

		double[] xrot = Geometry.RotateAround(-HorLook - 3.14, new double[]{p2x.x, p2x.y}, new double[]{0, 0});
		double[] zrot = Geometry.RotateAround(-HorLook - 3.14 / 2, new double[]{p2z.x, p2z.y}, new double[]{0, 0});

		g.setColor(Color.RED);
		g.drawLine((int) (xrot[0] + GameWindow.ScreenSize.getWidth() / 2), (int) (xrot[1] + GameWindow.ScreenSize.getHeight() / 2), (int) (p1x.x + GameWindow.ScreenSize.getWidth() / 2), (int) (p1x.y + GameWindow.ScreenSize.getHeight() / 2));

		g.setColor(Color.GREEN);
		g.drawLine((int) (zrot[0] + GameWindow.ScreenSize.getWidth() / 2), (int) (zrot[1] + GameWindow.ScreenSize.getHeight() / 2), (int) (p1z.x + GameWindow.ScreenSize.getWidth() / 2), (int) (p1z.y + GameWindow.ScreenSize.getHeight() / 2));
	}
	
	public static void CenterMouse() {
		try {
			r = new Robot();
			r.mouseMove((int)GameWindow.ScreenSize.getWidth() / 2, (int)GameWindow.ScreenSize.getHeight() / 2);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void keyPressed(KeyEvent e) {

		if (!KeyboardListener.KEYS_DOWN.contains(e.getKeyCode())) {
			System.out.println("ADDED: " + e.getKeyCode());
			KeyboardListener.KEYS_DOWN.add(e.getKeyCode());
		}

		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);

	}

	public void keyReleased(KeyEvent e) {
		KeyboardListener.KEYS_DOWN.remove((Integer)e.getKeyCode());
		System.out.println("NEW SIZE: " + KeyboardListener.KEYS_DOWN.size());
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseDragged(MouseEvent arg0) {

	}

	// PREVIOUS MOUSE POSITION
	public static int MouseX = 0, MouseY;
	public void mouseMoved(MouseEvent arg0) {

		for (int i = 0; i < com.alant7.game3d.engine.event.MouseMotionListener.LISTENERS.size(); i++) {
			com.alant7.game3d.engine.event.MouseMotionListener.LISTENERS.get(i).MouseMoved(arg0.getX() - MouseX, arg0.getY() - MouseY);
		}

		MouseX = arg0.getX();
		MouseY = arg0.getY();
	}
	
	public void mouseClicked(MouseEvent arg0) {

		for (int i = 0; i < com.alant7.game3d.engine.event.MouseListener.LISTENERS.size(); i++) {
			if (arg0.getButton() == MouseEvent.BUTTON1)
				com.alant7.game3d.engine.event.MouseListener.LISTENERS.get(i).LeftClick(arg0.getX(), arg0.getY());
			else if (arg0.getButton() == MouseEvent.BUTTON2)
				com.alant7.game3d.engine.event.MouseListener.LISTENERS.get(i).RightClick(arg0.getX(), arg0.getY());
		}

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
		if(arg0.getUnitsToScroll() > 0) {
			if(zoom > MinZoom)
				zoom -= 25 * arg0.getUnitsToScroll();
		}
		else {
			if(zoom < MaxZoom)
				zoom -= 25 * arg0.getUnitsToScroll();
		}	
	}
}
