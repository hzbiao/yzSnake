package com.yz.snake;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class SnakeBody extends Canvas implements Runnable {
	static int CellWidth;

	static int CellHeight;

	private static final int START_POS = 3; //��ʼλ��

	private static final int CHAR_HEIGHT; //�߶�

	private static final int CHAR_WIDTH;//���

	private static final int SCORE_HEIGHT; //�����߶�

	private static final int DEFAULT_WAIT = 400;// Ĭ���ߵ��ٶ�

	static final byte MAX_LEVELS = 10;// ����Ѷȵȼ�

	public static final int CELL_SIZE = 9;// ���ӵĴ�С

	static {
		Font defaultFont = Font.getDefaultFont();  //Ĭ������
		CHAR_WIDTH = defaultFont.charWidth('A'); 
		CHAR_HEIGHT = defaultFont.getHeight(); 
		SCORE_HEIGHT = CHAR_HEIGHT * 2; 
	}

	private SnakeFood snakeFood;   

	private Snake snake;

	public boolean gameOver = false;

	public boolean gamePaused = false;

	private boolean forceRedraw = true;

	public boolean gameDestroyed = false;

	private int score = 0;

	private int level = 3;

	private int foodEaten = 0;

	private int width;

	private int height;

	private SnakeMIDlet snakeMIDlet;

	public SnakeBody(SnakeMIDlet wm) {
		this.snakeMIDlet = wm;
		width = getWidth();
		height = getHeight() - SCORE_HEIGHT;
		SnakeBody.CellWidth = (width - (START_POS * 2)) / SnakeBody.CELL_SIZE;
		SnakeBody.CellHeight = (height - (START_POS * 2)) / SnakeBody.CELL_SIZE;
		snake = new Snake(this);

		snakeFood = new SnakeFood(this);// ����ʳ��

		int x = snakeFood.getX();
		int y = snakeFood.getY();

		while (snake.contains(x, y)) {
			snakeFood.regenerate();
			x = snakeFood.getX();
			y = snakeFood.getY();
		}
	}

	/**
	 * ���õȼ�
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		if (this.level != level) {
			this.level = level;
			gameOver = true;
			gamePaused = false;
			foodEaten = 0;
			score = 0;
		}
	}

	public int getLevel() {
		return level;
	}

	public int getScore() {
		return score;
	}

	static boolean isInBounds(int x, int y) {
		if ((x < 0) || (x >= SnakeBody.CellWidth)) {
			return false;
		}

		if ((y < 0) || (y >= SnakeBody.CellHeight)) {
			return false;
		}

		return true;
	}

	/**
	 * ���¿�ʼ��Ϸ
	 */
	void restart() {
		gamePaused = false;
		snake.regenerate();
		snakeFood.regenerate();

		int x = snakeFood.getX();
		int y = snakeFood.getY();

		while (snake.contains(x, y)) {
			snakeFood.regenerate();
			x = snakeFood.getX();
			y = snakeFood.getY();
		}

		foodEaten = 0;
		score = 0;
		gameOver = false;

		this.addCommand(snakeMIDlet.startCmd);
		forceRedraw = true;

		synchronized (snake) {
			snake.notifyAll();
		}
	}

	/**
	 * ��ͣ��ʼ��Ϸ
	 */
	void restart2() {
		if (gamePaused) {
			gamePaused = false;
		}
		forceRedraw = true;

		synchronized (snake) {
			snake.notifyAll();
		}
	}

	public void keyPressed(int keyCode) {
		switch (getGameAction(keyCode)) {
		case Canvas.UP:
			snake.setDirection(Snake.UP);
			break;

		case Canvas.DOWN:
			snake.setDirection(Snake.DOWN);

			break;

		case Canvas.LEFT:
			snake.setDirection(Snake.LEFT);

			break;

		case Canvas.RIGHT:
			snake.setDirection(Snake.RIGHT);

			break;
		case Canvas.FIRE:
			if (gamePaused) {
				gamePaused = false;
				restart2();
			} else {
				gamePaused = true;
			}
			break;

		case 0:

			switch (keyCode) {
			case Canvas.KEY_NUM2:
				snake.setDirection(Snake.UP);

				break;

			case Canvas.KEY_NUM8:
				snake.setDirection(Snake.DOWN);

				break;

			case Canvas.KEY_NUM4:
				snake.setDirection(Snake.LEFT);

				break;

			case Canvas.KEY_NUM6:
				snake.setDirection(Snake.RIGHT);

				break;
				
			case Canvas.KEY_NUM5:
				if (gamePaused) {
					gamePaused = false;
					restart2();
				} else {
					gamePaused = true;
				}
				break;	
			}

			break;
		}
	}

	private void paintBodyContents(Graphics g) {
		try {
			snake.update(g);

			if (snakeFood.isAt(snake.getX(), snake.getY())) {
				snake.eat();
				score += level;

				foodEaten++;

				if (foodEaten > (level << 1)) {
					forceRedraw = true;
					foodEaten = 0;
					level++;

					if (level == MAX_LEVELS) {
						level--;
						gameOver = true;
					}
				}

				g.setColor(0 ,0 ,0); //���÷����ı�ʱ��ķ���������ɫ
				
				g.fillRect((width - (CHAR_WIDTH * 3)) - START_POS, height
						- START_POS, (CHAR_WIDTH * 3), CHAR_HEIGHT);
				g.setColor(255 ,250 ,250);
				g.drawString("" + score, width - (CHAR_WIDTH * 3) - START_POS,
						height - START_POS, Graphics.TOP | Graphics.LEFT);
				snakeFood.regenerate();

				int x = snakeFood.getX();
				int y = snakeFood.getY();

				while (snake.contains(x, y)) {
					snakeFood.regenerate();
					x = snakeFood.getX();
					y = snakeFood.getY();
				}
			}

			snakeFood.paint(g);
		} catch (Exception se) {
			gameOver = true;
		}
	}

	public void paint(Graphics g) {
		if (forceRedraw) {
			forceRedraw = false;
			//���Ʊ���ɫ  ��ɫ
			g.setColor(0,0,0);
			g.fillRect(0, 0, getWidth(), getHeight());
			//����snake���Χ�߿� ��ɫ
			g.setColor(255 ,250 ,250);
			g.drawRect(1, 1, (width - START_POS), (height - START_POS));
			//������ʾ	
			g.setColor(255 ,250 ,250);
			g.drawString("��ǰ�ȼ�: " + level, START_POS, height, Graphics.TOP
					| Graphics.LEFT);
			g.drawString("" + score, (width - (CHAR_WIDTH * 3)), height,
					Graphics.TOP | Graphics.LEFT);

			g.drawString("��Ϸ��ǰ����: ", (width - (CHAR_WIDTH * 4)), height,
					Graphics.TOP | Graphics.RIGHT);
			g.drawString("" + score, (width - (CHAR_WIDTH * 3)), height,
					Graphics.TOP | Graphics.LEFT);
			repaint();
			g.drawString("�õȼ���߷�: ", (width - (CHAR_WIDTH * 4)),
					(height + CHAR_HEIGHT), Graphics.TOP | Graphics.RIGHT);
			g.drawString("" + SnakeScore.getHighScore(level),
					(width - (CHAR_WIDTH * 3)), (height + CHAR_HEIGHT),
					Graphics.TOP | Graphics.LEFT);
			g.translate(START_POS, START_POS);
			g.setClip(0, 0, CellWidth * CELL_SIZE, CellHeight * CELL_SIZE);
			snake.paint(g);
			snakeFood.paint(g);
		} else {
			g.translate(START_POS, START_POS);
		}

		if (gamePaused) {
			Font pauseFont = g.getFont();
			int fontH = pauseFont.getHeight();
			int fontW = pauseFont.stringWidth("Paused");
			g.setColor(0,0,0);
			g.fillRect(((width - fontW) / 2) - 1, (height - fontH) / 2,
					fontW + 2, fontH);
			g.setColor(238,180,180);
			g.setFont(pauseFont);
			g.drawString("��	     ͣ", (width - fontW) / 2, (height - fontH) / 2,
					Graphics.TOP | Graphics.LEFT);
			this.addCommand(snakeMIDlet.startCmd);
		} else if (gameOver) {
			Font overFont = g.getFont();
			int fontH = overFont.getHeight();
			int fontW = overFont.stringWidth("Game Over");
			g.setColor(0,0,0);
			g.fillRect(((width - fontW) / 2) - 1, (height - fontH) / 2,
					fontW + 2, fontH);
			g.setColor(238,180,180);
			g.setFont(overFont);
			g.drawString("��Ϸ����", (width - fontW) / 2, (height - fontH) / 2,
					Graphics.TOP | Graphics.LEFT);
			this.removeCommand(snakeMIDlet.startCmd);
			this.addCommand(snakeMIDlet.restartCmd);
		} else {
			paintBodyContents(g);
		}

		g.translate(-START_POS, -START_POS);
	}

	protected void hideNotify() {
		super.hideNotify();
		forceRedraw = true;

		if (!gameOver) {
			gamePaused = true;
		}
	}

	public void run() {
		while (!gameDestroyed) {
			try {
				synchronized (snake) {
					if (gameOver) {
						if (SnakeScore.getHighScore(level) < score) {
							SnakeScore.setHighScore(level, score, "me");
						}
						repaint();
						snake.wait();
					} else if (gamePaused) {
						repaint();
						snake.wait();
					} else {
						snake.moveOnUpdate();
						repaint();
						snake.wait(DEFAULT_WAIT - (level * 40));
					}
				}
			} catch (Exception e) {
			}
		}
	}

}
