package com.yz.snake;

import javax.microedition.lcdui.Graphics;

public class SnakeFood {
    private int cellX;
    private int cellY;
    private int x;
    private int y;
    private long arenaSize;
    
    public SnakeFood(SnakeBody pit) {
        arenaSize = SnakeBody.CellWidth * SnakeBody.CellHeight;
        regenerate();
    }
  
    public boolean isAt(int cx, int cy) {
        return ((this.cellX == cx) && (this.cellY == cy));
    }

    public int getX() {
        return cellX;
    }

    public int getY() {
        return cellY;
    }

  
    public void regenerate() {
        int loc = (int)(System.currentTimeMillis() % arenaSize);
        cellY = (int)(loc / SnakeBody.CellWidth);
        cellX = (int)(loc % SnakeBody.CellHeight);
        y = cellY * SnakeBody.CELL_SIZE; 
        x = cellX * SnakeBody.CELL_SIZE;
                                       

        if (!SnakeBody.isInBounds(cellX, cellY)) {
            regenerate();
        }
    }
    /**
     *ªÊ÷∆Ã∞≥‘…ﬂ ≥ŒÔ 
     */
    
    public void paint(Graphics g) {
        g.setColor(139,101,8);
        g.fillRect(x + 1, y + 1, SnakeBody.CELL_SIZE - 2, SnakeBody.CELL_SIZE - 2);
        g.setColor(139,101,8);
        g.drawRect(x, y, SnakeBody.CELL_SIZE - 1, SnakeBody.CELL_SIZE - 1);
    }
}
