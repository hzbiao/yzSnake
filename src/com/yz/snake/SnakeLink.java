package com.yz.snake;

public class SnakeLink {
    private int x;
    private int y;
    private int len;
    private byte dir;

    public SnakeLink(int startX, int startY, int length, byte direction) {
        x = startX;
        y = startY;
        dir = direction;
        len = length - 1;
    }

    public SnakeLink(int startX, int startY, byte direction) {
        this(startX, startY, 1, direction);
    }

    public void increaseLength() {
        len++;
    }

   
    public void decreaseLength() {
        len--;

        switch (dir) {
        case Snake.LEFT:
            x--;

            break;

        case Snake.RIGHT:
            x++;

            break;

        case Snake.UP:
            y--;

            break;

        case Snake.DOWN:
            y++;

            break;
        }
    }

    public int getLength() {
        return len + 1;
    }
   
    public int getX() {
        return x;
    }
   
    public int getY() {
        return y;
    }
   
    public int getEndX() {
        if (dir == Snake.LEFT) {
            return x - len;
        }

        if (dir == Snake.RIGHT) {
            return x + len;
        }
        return x;
    }
    
    public int getEndY() {
        if (dir == Snake.DOWN) {
            return y + len;
        }

        if (dir == Snake.UP) {
            return y - len;
        }

        return y;
    }

    public byte getDirection() {
        return dir;
    }

    public boolean contains(int x, int y) {
        switch (dir) {
        case Snake.LEFT:
            return ((y == this.y) && ((x <= this.x) && (x >= getEndX())));

        case Snake.RIGHT:
            return ((y == this.y) && ((x >= this.x) && (x <= getEndX())));

        case Snake.UP:
            return ((x == this.x) && ((y <= this.y) && (y >= getEndY())));

        case Snake.DOWN:
            return ((x == this.x) && ((y >= this.y) && (y <= getEndY())));
        }

        return false;
    }

    public String toString() {
        String dirString;

        switch (dir) {
        case Snake.LEFT:
            dirString = "Left";

            break;

        case Snake.RIGHT:
            dirString = "Right";

            break;

        case Snake.UP:
            dirString = "Up";

            break;

        case Snake.DOWN:
            dirString = "Down";

            break;

        default:
            dirString = "UNKNOWN -- " + dir;
        }

        return " pos == [" + x + "," + y + "]" + " - [" + getEndX() + "," + getEndY() + "]" +
        "   len == " + getLength() + "   dir == " + dirString;
    }
}
