package com.yz.snake;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;
import javax.microedition.midlet.MIDlet;


public class SnakeMIDlet extends MIDlet implements CommandListener {
	
	private SnakeBody snakeBody;

	private Command exitCmd = new Command("退出", Command.EXIT, 3);

	private Command levelCmd = new Command("设置难度", Command.SCREEN, 2);

	public Command startCmd = new Command("继续", Command.SCREEN, 1);

	public Command restartCmd = new Command("重新开始", Command.SCREEN, 1);

	private Command cancelCmd = new Command("取消", Command.CANCEL, 1);

	private Command OKCmd = new Command("确定", Command.OK, 1);

	private Thread snakeThread;

	
	public SnakeMIDlet() {
		snakeBody = new SnakeBody(this);
		snakeBody.addCommand(exitCmd);
		snakeBody.addCommand(levelCmd);
		snakeBody.addCommand(startCmd);
		snakeBody.setCommandListener(this);
	}
	protected void startApp() {
		Display.getDisplay(this).setCurrent(snakeBody);

		try {
			snakeThread = new Thread(snakeBody);
			snakeBody.gameDestroyed=false;
			snakeThread.start();
		} catch (Error e) {
			destroyApp(false);
			notifyDestroyed();
		}
	}

	protected void destroyApp(boolean unconditional) {
	}

	protected void pauseApp() {
	}

	public void commandAction(Command c, Displayable d) {
		if (c == restartCmd) {
			snakeBody.restart();//重新开始游戏
		} else if (c == levelCmd) {
			/**
			 * 创建设置难度界面
			 */
			Item[] levelItem = { new Gauge("难度级别：", true, 9, snakeBody.getLevel()) };
			Form f = new Form("设置难度级别", levelItem);
			f.addCommand(cancelCmd);
			f.addCommand(OKCmd);
			f.setCommandListener(this);
			Display.getDisplay(this).setCurrent(f);
		} else if (c == exitCmd) {
			notifyDestroyed();
		} else if (c == startCmd) {
			if (snakeBody.gameOver) {
				snakeBody.removeCommand(startCmd);
				snakeBody.addCommand(restartCmd);
				snakeBody.restart();
			} else {
				snakeBody.restart2();
			}
		} else if (c == OKCmd) {
			Form f = (Form) d;
			Gauge g = (Gauge) f.get(0);
			snakeBody.setLevel(g.getValue());
			Display.getDisplay(this).setCurrent(snakeBody);
			snakeBody.restart();
		} else if (c == cancelCmd) {
			Display.getDisplay(this).setCurrent(snakeBody);
		}
	}
}
