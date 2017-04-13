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
    boolean selected=false;
    Color color=Color.BLACK;

    public Shape(ArrayList<Point> p){
        points=new ArrayList<Point>(p);
    }

    public void add(int x, int y){
        points.add(new Point(x,y));
    }

    public boolean isSelected(){
        return selected;
    }

    public void select(){
        selected = true;
    }

    public void unselect(){
        selected = false;
    }

    public void setColor(Color c){
        color=c;
    }

    public abstract boolean valid();
    public abstract boolean include(int x, int y);
    public abstract void draw(Graphics2D g2d);
    public abstract void preview(Graphics2D g2d, int x, int y);
}

class Rectangle extends Shape{
    public int width,height,x,y;

    public Rectangle(ArrayList<Point> p){
        super(p);
    }

    public void draw(Graphics2D g2d){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);
        g2d.setColor(color);
        if(selected){
            g2d.setColor(Color.MAGENTA);
        }
        x=(int) Math.min(points.get(0).getX(),points.get(1).getX());
        y=(int) Math.min(points.get(0).getY(),points.get(1).getY());
        width=(int) Math.abs(points.get(1).getX()-points.get(0).getX());
        height=(int) Math.abs(points.get(1).getY()-points.get(0).getY());
        g2d.fillRect(x,y,width,height);
    }

    public void preview(Graphics2D g2d, int currentX, int currentY){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);

        add(currentX,currentY);

        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.drawRect((int) Math.min(points.get(0).getX(),points.get(1).getX()),
                     (int) Math.min(points.get(0).getY(),points.get(1).getY()),
                     (int) Math.abs(points.get(1).getX()-points.get(0).getX()),
                     (int) Math.abs(points.get(1).getY()-points.get(0).getY()));
    }

    public boolean valid(){
        return  Math.abs(points.get(1).getX()-points.get(0).getX())>0 && Math.abs(points.get(1).getY()-points.get(0).getY())>0;
    }

    public boolean include(int currentX, int currentY){
        return x<currentX && currentX<x+width && y<currentY && currentY<y+height;
    }
}

class Circle extends Shape{
    private int x,y,dx,dy;

    public Circle(ArrayList<Point> p){
        super(p);
    }

    public void draw(Graphics2D g2d){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);
        g2d.setColor(color);
        if(selected){
            g2d.setColor(Color.MAGENTA);
        }
        x=(int) Math.min(points.get(0).getX(),points.get(1).getX());
        y=(int) Math.min(points.get(0).getY(),points.get(1).getY());
        dx=(int) Math.abs(points.get(1).getX()-points.get(0).getX())/2;
        dy=(int) Math.abs(points.get(1).getY()-points.get(0).getY())/2;
        g2d.fillOval(x,y,dx*2,dy*2);
    }

    public void preview(Graphics2D g2d, int currentX, int currentY){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);

        add(currentX,currentY);

        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.drawOval((int) Math.min(points.get(0).getX(),points.get(1).getX()),
                     (int) Math.min(points.get(0).getY(),points.get(1).getY()),
                     (int) Math.abs(points.get(1).getX()-points.get(0).getX()),
                     (int) Math.abs(points.get(1).getY()-points.get(0).getY()));
    }

    public boolean include(int currentX, int currentY){
        return (Math.pow(currentX-(x+dx),2)/(dx*dx) + Math.pow(currentY-(y+dy),2)/(dy*dy)) < 1;
    }

    public boolean valid(){
        return (Math.abs(points.get(1).getX()-points.get(0).getX())/2)>0 && (Math.abs(points.get(1).getY()-points.get(0).getY())/2)>0;
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

        g2d.setColor(color);
        g2d.fill(path);
    }

    public void preview(Graphics2D g2d, int currentX, int currentY){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);
        GeneralPath path = new GeneralPath();

        add(currentX,currentY);

        path.moveTo(points.get(0).getX(),points.get(0).getY());
        for(int i=0; i<points.size(); i++) path.lineTo(points.get(i).getX(),points.get(i).getY());

        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.draw(path);
    }

    public boolean include(int x, int y){
        //TODO
        return false;
    }

    public boolean valid(){
        boolean px=false,py=false;
        if(points.size()<3) return false;
        Point start = points.get(0);
        for(int i; i<points.size();i++){
            if(points.get(i).getX()!=start.getX()) px=true;
            if(points.get(i).getY()!=start.getY()) py=true;
            if(px&&py) return true;
        }
        return px&&py;
    }
}
