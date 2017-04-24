/**
*Simple implementation of Point class using double coordinates.
*@author Maciej Stosio
*@version 1.0
*/
public class DoublePoint{
    private double x;
    private double y;

    /**
    *Constructor without arguments set the point to (0,0)
    */
    DoublePoint(){
        x=0;
        y=0;
    }

    /**
    *Constructor set the point to given (x,y) positon
    *@param x coordinate, double type
    *@param y coordinate, double type
    */
    DoublePoint(double x, double y){
        this.x=x;
        this.y=y;
    }

    /**
    *@return x coordinate
    */
    public double getX(){
        return x;
    }

    /**
    *@return y coordinate
    */
    public double getY(){
        return y;
    }

    /**
    *Change location of point
    *@param x coordinate, double type
    *@param y coordinate, double type
    */
    public void setLocation(double x, double y){
        this.x=x;
        this.y=y;
    }

    /**
    *Move point with vector [x,y]
    *@param dx x coordinate, double type
    *@param dy y coordinate, double type
    */
    public void translate(double dx, double dy){
        x+=dx;
        y+=dy;
    }
}
