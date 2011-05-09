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

	private Command exitCmd = new Command("�˳�", Command.EXIT, 3);

	private Command levelCmd = new Command("�����Ѷ�", Command.SCREEN, 2);

	public Command startCmd = new Command("����", Command.SCREEN, 1);

	public Command restartCmd = new Command("���¿�ʼ", Command.SCREEN, 1);

	private Command cancelCmd = new Command("ȡ��", Command.CANCEL, 1);

	private Command OKCmd = new Command("ȷ��", Command.OK, 1);

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
			snakeBody.restart();//���¿�ʼ��Ϸ
		} else if (c == levelCmd) {
			/**
			 * ���������ѶȽ���
			 */
			Item[] levelItem = { new Gauge("�Ѷȼ���", true, 9, snakeBody.getLevel()) };
			Form f = new Form("�����Ѷȼ���", levelItem);
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
