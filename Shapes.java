import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.lang.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

abstract class Shape{
    ArrayList<Point> points;

    public Shape(ArrayList<Point> p){
        points=new ArrayList<Point>(p);
    }

    public void add(int x, int y){
        points.add(new Point(x,y));
    }

    public abstract void draw(Graphics2D g2d);
    public abstract void preview(Graphics2D g2d, int x, int y);
}

class Rectangle extends Shape{
    public Rectangle(ArrayList<Point> p){
        super(p);
    }

    public void draw(Graphics2D g2d){
        g2d.setColor(Window.rgb);
        g2d.fillRect((int) Math.min(points.get(0).getX(),points.get(1).getX()),
                     (int) Math.min(points.get(0).getY(),points.get(1).getY()),
                     (int) Math.abs(points.get(1).getX()-points.get(0).getX()),
                     (int) Math.abs(points.get(1).getY()-points.get(0).getY()));
    }

    public void preview(Graphics2D g2d, int x, int y){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);

        add(x,y);

        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.drawRect((int) Math.min(points.get(0).getX(),points.get(1).getX()),
                     (int) Math.min(points.get(0).getY(),points.get(1).getY()),
                     (int) Math.abs(points.get(1).getX()-points.get(0).getX()),
                     (int) Math.abs(points.get(1).getY()-points.get(0).getY()));
    }
}

class Circle extends Shape{
    public Circle(ArrayList<Point> p){
        super(p);
    }

    public void draw(Graphics2D g2d){
        g2d.setColor(Window.rgb);
        g2d.fillOval((int) Math.min(points.get(0).getX(),points.get(1).getX()),
                     (int) Math.min(points.get(0).getY(),points.get(1).getY()),
                     (int) Math.abs(points.get(1).getX()-points.get(0).getX()),
                     (int) Math.abs(points.get(1).getY()-points.get(0).getY()));
    }

    public void preview(Graphics2D g2d, int x, int y){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);

        add(x,y);

        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.drawOval((int) Math.min(points.get(0).getX(),points.get(1).getX()),
                     (int) Math.min(points.get(0).getY(),points.get(1).getY()),
                     (int) Math.abs(points.get(1).getX()-points.get(0).getX()),
                     (int) Math.abs(points.get(1).getY()-points.get(0).getY()));
    }
}

class Polygon extends Shape{
    public Polygon(ArrayList<Point> p){
        super(p);
    }

    public void draw(Graphics2D g2d){
        GeneralPath path = new GeneralPath();

        path.moveTo(points.get(0).getX(),points.get(0).getY());
        for(int i=0; i<points.size(); i++) path.lineTo(points.get(i).getX(),points.get(i).getY());
        path.lineTo(points.get(0).getX(),points.get(0).getY());
        path.closePath();

        g2d.setColor(Window.rgb);
        g2d.fill(path);
    }

    public void preview(Graphics2D g2d, int x, int y){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);
        GeneralPath path = new GeneralPath();

        add(x,y);

        path.moveTo(points.get(0).getX(),points.get(0).getY());
        for(int i=0; i<points.size(); i++) path.lineTo(points.get(i).getX(),points.get(i).getY());

        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.draw(path);
    }
}
