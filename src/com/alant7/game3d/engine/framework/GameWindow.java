package com.alant7.game3d.engine.framework;

import com.alant7.game3d.engine.math.Geometry;
import com.alant7.game3d.engine.math.Vector3;
import com.alant7.game3d.engine.rendering.animation.Keyframe;
import com.alant7.game3d.engine.rendering.animation.ObjectAnimation;
import com.alant7.game3d.engine.rendering.ObjectModel;
import com.alant7.game3d.engine.world.GameObject;
import com.alant7.game3d.engine.world.World;

import javax.swing.JFrame;
import java.awt.*;

public class GameWindow {

	public static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static GameWindow Instance;

	private boolean Fullscreen = false, Maximised = false;

	private JFrame Frame;
	public Graphics Graphics;

	public GameWindow() {
		Instance = this;
	}

	Runnable WhenReady = null;
	public GameWindow WhenReady(Runnable r) {
		this.WhenReady = r;
		return this;
	}

	public void Create() {
		Frame = new JFrame();

		if (Fullscreen) {
			Frame.setUndecorated(true);
		}
		if (Maximised) {
			Frame.setExtendedState(Frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		}

		Frame.setSize(ScreenSize);

		Graphics = new Graphics();
		Frame.add(Graphics);

		Frame.setVisible(true);
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public GameWindow Fullscreen() {
		this.Fullscreen = true;
		return this;
	}

	public GameWindow Size (int W, int H) {
		ScreenSize.width = W;
		ScreenSize.height = H;
		return this;
	}

	public GameWindow Maximised() {
		this.Maximised = true;
		return this;
	}

	public JFrame GetFrame() {
		return this.Frame;
	}

	public static void main (String[] args) {

		new GameWindow().Fullscreen().Create();

		GameObject obj = new GameObject() {
			@Override
			public void Update() {}
		};
		obj.SetModel(new ObjectModel(
				Geometry.Cuboid(new Vector3(0, 0, 0), new Vector3(5, 5, 5), new Color[] {Color.WHITE})
		));

		obj.Animation = new ObjectAnimation(
				new Keyframe(new Vector3 (0, 0, 0), new Vector3 (0, Math.toRadians(-30), 0), 400)
		);

		World.Objects.add(obj);
	}

}
