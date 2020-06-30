package com.alant7.game3d.engine.framework;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class GameWindow {

	public static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static GameWindow Instance;

	public GameWindow() {
		Instance = this;
	}

	Runnable WhenReady = null;
	public GameWindow WhenReady(Runnable r) {
		this.WhenReady = r;
		return this;
	}

	public void Create() {

		JFrame F = new JFrame();

		F.setUndecorated(true);
		F.setSize(ScreenSize);

		Graphics Graphics = new Graphics();
		F.add(Graphics);

		F.setVisible(true);
		F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
