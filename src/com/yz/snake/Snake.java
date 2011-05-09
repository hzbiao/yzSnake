package com.yz.snake;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

public class Snake {
	
	public static final byte DOWN = 2;
	public static final byte LEFT = 4;
	public static final byte RIGHT = 6;
	public static final byte UP = 8;
	private static final int INIT_X = 3;
	private static final int INIT_Y = 8;
	private static final int INIT_LEN = 8;
	private static final byte INIT_DIR = RIGHT;

	private byte currentDirection;

	private Vector worm = new Vector(5, 2);
	private boolean needUpdate;
	private boolean moveOnNextUpdate;
	private boolean hasEaten;

	public Snake(SnakeBody pit) {
		regenerate();
	}

	public void regenerate() {
		synchronized (worm) {
			worm.removeAllElements();
			worm.addElement(new SnakeLink(INIT_X, INIT_Y, INIT_LEN, INIT_DIR));

			currentDirection = INIT_DIR;
			needUpdate = false;
			hasEaten = false;
			moveOnNextUpdate = false;
		}
	}

	public void setDirection(byte direction) {
		synchronized (worm) {
			if ((direction != currentDirection) && !needUpdate) {
				SnakeLink sl = (SnakeLink) worm.lastElement();
				int x = sl.getEndX();
				int y = sl.getEndY();
				switch (direction) {
				case UP:
					if (currentDirection != DOWN) {
						y--;
						needUpdate = true;
					}
					break;
				case DOWN:
					if (currentDirection != UP) {
						y++;
						needUpdate = true;
					}
					break;
				case LEFT:
					if (currentDirection != RIGHT) {
						x--;
						needUpdate = true;
					}
					break;
				case RIGHT:
					if (currentDirection != LEFT) {
						x++;
						needUpdate = true;
					}
					break;
				}
				if (needUpdate == true) {
					worm.addElement(new SnakeLink(x, y, 0, direction));
					currentDirection = direction;
				}
			}
		}
	}

	public void moveOnUpdate() {
		synchronized (worm) {
			moveOnNextUpdate = true;
		}
	}

	public void update(Graphics g) throws Exception {
		SnakeLink head;
		SnakeLink sl;
		int headX;
		int headY;

		if (!moveOnNextUpdate) {
			return;
		}

		synchronized (worm) {
			head = (SnakeLink) worm.lastElement();
			head.increaseLength();

			if (!hasEaten) {
				SnakeLink tail;
				tail = (SnakeLink) worm.firstElement();

				int tailX = tail.getX();
				int tailY = tail.getY();

				tail.decreaseLength();

				if (tail.getLength() == 0) {
					worm.removeElement(tail);
				}

				g.setColor(0,0,0);  //绘制snake的轨迹
				drawLink(g, tailX, tailY, tailX, tailY, 1);
			} else {
				hasEaten = false;
			}

			needUpdate = false;

			if (!SnakeBody.isInBounds(head.getEndX(), head.getEndY())) {
				throw new Exception("撞墙了！！！");
			}

			headX = (byte) head.getEndX();
			headY = (byte) head.getEndY();

			g.setColor(255,165,0); //贪吃蛇的颜色
			drawLink(g, headX, headY, headX, headY, 1);

			for (int i = 0; i < (worm.size() - 1); i++) {
				sl = (SnakeLink) worm.elementAt(i);

				if (sl.contains(headX, headY)) {
					throw new Exception("你咬到自己了！！");
				}
			}
		}
	}

	public void drawLink(Graphics g, int x1, int y1, int x2, int y2, int len) {
		len *= SnakeBody.CELL_SIZE;

		if (x1 == x2) {
			x1 *= SnakeBody.CELL_SIZE;

			if (y2 < y1) {
				y1 = y2 * SnakeBody.CELL_SIZE;
			} else {
				y1 *= SnakeBody.CELL_SIZE;
			}

			g.fillRect(x1, y1, SnakeBody.CELL_SIZE, len);
		} else {
			y1 *= SnakeBody.CELL_SIZE;

			if (x2 < x1) {
				x1 = x2 * SnakeBody.CELL_SIZE;
			} else {
				x1 *= SnakeBody.CELL_SIZE;
			}

			g.fillRect(x1, y1, len, SnakeBody.CELL_SIZE);
		}
	}

	//绘制初始贪吃蛇的颜色
	public void paint(Graphics g) {
		SnakeLink sl;
		int x1;
		int x2;
		int y1;
		int y2;
		int len;
		g.setColor(255,165,0);
		for (int i = 0; i < worm.size(); i++) {
			sl = (SnakeLink) worm.elementAt(i);
			x1 = sl.getX();
			x2 = sl.getEndX();
			y1 = sl.getY();
			y2 = sl.getEndY();
			len = sl.getLength();
			drawLink(g, x1, y1, x2, y2, len);
		}
	}
	public void eat() {
		hasEaten = true;
	}
	public int getX() {
		synchronized (worm) {
			return ((SnakeLink) worm.lastElement()).getEndX();
		}
	}
	public int getY() {
		synchronized (worm) {
			return ((SnakeLink) worm.lastElement()).getEndY();
		}
	}
	public boolean contains(int x, int y) {
		SnakeLink sl;
		synchronized (worm) {
			for (int i = 0; i < worm.size(); i++) {
				sl = (SnakeLink) worm.elementAt(i);
				if (sl.contains(x, y)) {
					return true;
				}
			}
		}
		return false;
	}
}
