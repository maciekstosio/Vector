//Graphics
import java.awt.*;
import java.awt.geom.*;

//Other
import java.util.ArrayList;

/**
*Abstract class shape and subclasses defining shapes.
*@author Maciej Stosio
*@version 1.0
*/
public abstract class Shape{
    ArrayList<DoublePoint> points;
    boolean selected=false;
    Color color=Color.BLACK;
    double zoom = 0.05;

    /**
    *Create shape without any points.
    */
    public Shape(){
        points=new ArrayList<DoublePoint>();
    }

    /**
    *Create new shape based on given points.
    *@param p list of DoublePoint's
    */
    public Shape(ArrayList<DoublePoint> p){
        points=new ArrayList<DoublePoint>(p);
    }

    /**
    *Create shape without any points.
    *@param x double number of x coordinate
    *@param y double number of y coordinate
    */
    public void add(double x, double y){
        points.add(new DoublePoint(x,y));
    }

    /**
    *Return wheter shape is selected or not.
    *@return boolean
    */
    public boolean isSelected(){
        return selected;
    }
    /** Select the shape. */
    public void select(){
        selected = true;
    }

    /** Unselect the shape. */
    public void unselect(){
        selected = false;
    }

    /**
    *Set color of shape.
    *@param c instance of Color class
    */
    public void setColor(Color c){
        color=c;
    }

    /**
    *Get color of shape.
    *@return instance of Color class
    */
    public Color getColor(){
        return color;
    }

    /**
    *Get all points in shape
    *@return list of DoublePoint
    */
    public ArrayList<DoublePoint> getPoints(){
        return points;
    }

    /**
    *Check if shape is valid i.e. height can't be 0.
    *@return True if it's valid, false if it's not
    */
    public abstract boolean valid();

    /**
    *Check if given point is inside the shape.
    *@param x x coordinate as integer
    *@param y y coordinate as integer
    *@return True if it's inside, false if it's not
    */
    public abstract boolean include(int x, int y);

    /**
    *Init shape - generate information like width, height, radius.
    */
    public abstract void init();

    /**
    *Scal shape of n unit's
    *@param n unit's as integer
    */
    public abstract void resize(int n);

    /**
    *Translate shape of vector [x,y].
    *@param deltaX x vector value as integer
    *@param deltaY y vector value as integer
    */
    public abstract void move(int deltaX, int deltaY);

    /**
    *Draw shape on given canvas
    *@param g2d Graphics2D context
    */
    public abstract void draw(Graphics2D g2d);

    /**
    *Preview shape based on given points and another, temporary point.
    *@param g2d Graphics2D context
    *@param x x coordinate of temporary point as integer
    *@param y y coordinate of temporary point as integer
    */
    public abstract void preview(Graphics2D g2d, int x, int y);
}
