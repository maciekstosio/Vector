/**
*Shape clases that inherit after Shape abstract Class.
*@author Maciej Stosio
*@version 1.0
*/

//Graphics
import java.awt.*;
import java.awt.geom.*;

//Other
import java.util.ArrayList;


class Rectangle extends Shape{
    public double width,height,x,y;

    public Rectangle(ArrayList<DoublePoint> p){
        super(p);
    }

    public void init(){
        x=Math.min(points.get(0).getX(),points.get(1).getX());
        y=Math.min(points.get(0).getY(),points.get(1).getY());
        width=Math.abs(points.get(1).getX()-points.get(0).getX());
        height=Math.abs(points.get(1).getY()-points.get(0).getY());
    }

    public void draw(Graphics2D g2d){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);
        g2d.setColor(color);
        if(selected){
            g2d.setColor(new Color(255,0,255,128));
        }
        g2d.fillRect((int) Math.floor(x),(int) Math.floor(y), (int) Math.floor(width), (int) Math.floor(height));
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

    public void move(int deltaX, int deltaY){
        x+=deltaX;
        y+=deltaY;
        for(DoublePoint p: points){
            p.translate(deltaX,deltaY);
        }
    }

    public void resize(int notch){
        double x = width/height;
        width*=1.0+zoom*notch;
        height*=1.0+zoom*notch;
        if(width<1 || height<1){
            width=x;
            height=1;
        }
        points.get(1).setLocation(Math.min(points.get(0).getX(),points.get(1).getX())+width,Math.min(points.get(0).getY(),points.get(1).getY())+height);
    }

    public boolean valid(){
        return  Math.abs(points.get(1).getX()-points.get(0).getX())>0 && Math.abs(points.get(1).getY()-points.get(0).getY())>0;
    }

    public boolean include(int currentX, int currentY){
        return x<currentX && currentX<x+width && y<currentY && currentY<y+height;
    }
}

class Circle extends Shape{
    private double x,y,dx,dy;

    public Circle(ArrayList<DoublePoint> p){
        super(p);
    }

    public void init(){
        x= Math.min(points.get(0).getX(),points.get(1).getX());
        y= Math.min(points.get(0).getY(),points.get(1).getY());
        dx= Math.abs(points.get(1).getX()-points.get(0).getX())/2;
        dy= Math.abs(points.get(1).getY()-points.get(0).getY())/2;
    }

    public void draw(Graphics2D g2d){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);
        g2d.setColor(color);
        if(selected){
            g2d.setColor(new Color(255,0,255,128));
        }
        g2d.fillOval((int) Math.floor(x),(int) Math.floor(y),(int) Math.floor(dx*2),(int) Math.floor(dy*2));
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

    public void move(int deltaX, int deltaY){
        x+=deltaX;
        y+=deltaY;
        for(DoublePoint p: points){
            p.translate(deltaX,deltaY);
        }
    }

    public void resize(int notch){
        double x = dx/dy;
        dx *= 1.0+notch*zoom;
        dy *= 1.0+notch*zoom;
        if(dy<1 || dx<1){
            dx=x;
            dy=1;
        }
        points.get(1).setLocation(Math.min(points.get(0).getX(),points.get(1).getX())+dx,Math.min(points.get(0).getY(),points.get(1).getY())+dy);
    }

    public boolean include(int currentX, int currentY){
        return (Math.pow(currentX-(x+dx),2)/(dx*dx) + Math.pow(currentY-(y+dy),2)/(dy*dy)) < 1;
    }

    public boolean valid(){
        return (Math.abs(points.get(1).getX()-points.get(0).getX())/2)>0 && (Math.abs(points.get(1).getY()-points.get(0).getY())/2)>0;
    }
}

class Polygon extends Shape{
    public Polygon(ArrayList<DoublePoint> p){
        super(p);
    }

    public void init(){}

    public void draw(Graphics2D g2d){
        GeneralPath path = new GeneralPath();

        path.moveTo((int)points.get(0).getX(),(int)points.get(0).getY());
        for(int i=0; i<points.size(); i++) path.lineTo((int)points.get(i).getX(),(int)points.get(i).getY());
        path.closePath();

        g2d.setColor(color);
        if(selected){
            g2d.setColor(new Color(255,0,255,128));
        }
        g2d.fill(path);
    }

    public void preview(Graphics2D g2d, int currentX, int currentY){
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0);
        GeneralPath path = new GeneralPath();

        add(currentX,currentY);
        path.moveTo((int)points.get(0).getX(),(int)points.get(0).getY());
        for(int i=0; i<points.size(); i++) path.lineTo((int)points.get(i).getX(),(int)points.get(i).getY());

        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.draw(path);
    }

    public void resize(int notch){
        double x = points.get(0).getX();
        double y = points.get(0).getY();
        // double x,y;
        DoublePoint vector;
        for(int i=1;i<points.size();i++){
            vector = new DoublePoint();
            vector.setLocation(points.get(i).getX()-x,points.get(i).getY()-y);
            x = points.get(i).getX();
            y = points.get(i).getY();
            points.get(i).setLocation(points.get(i-1).getX()+vector.getX()*(1.0+notch*zoom),
                                      points.get(i-1).getY()+vector.getY()*(1.0+notch*zoom));
        }
    }

    public void move(int deltaX, int deltaY){
        for(DoublePoint p: points){
            p.translate(deltaX,deltaY);
        }
    }

    public boolean include(int currentX, int currentY){
        double x1,x2,y1,y2,
        count=0;
        for(int i=1;i<points.size(); i++){
            x1=points.get(i-1).getX();
            x2=points.get(i).getX();
            y1=points.get(i-1).getY();
            y2=points.get(i).getY();
            if(y2-y1!=0){
                if(Math.min(y2,y1)<currentY && currentY<Math.max(y2,y1)){
                    if(currentX<((currentY-y1)*(x2-x1)/(y2-y1))+x1){
                        count++;
                    }
                }
            }
        }
        x1=points.get(points.size()-1).getX();
        x2=points.get(0).getX();
        y1=points.get(points.size()-1).getY();
        y2=points.get(0).getY();
        if(y2-y1!=0){
            if(Math.min(y2,y1)<currentY && currentY<Math.max(y2,y1)){
                if(currentX<((currentY-y1)*(x2-x1)/(y2-y1))+x1){
                    count++;
                }
            }
        }
        return count%2==1;
    }

    public boolean valid(){
        boolean px=false,py=false;
        if(points.size()<3) return false;
        DoublePoint start = points.get(0);
        for(int i=1; i<points.size();i++){
            if(points.get(i).getX()!=start.getX()) px=true;
            if(points.get(i).getY()!=start.getY()) py=true;
            if(px&&py) return true;
        }
        return px&&py;
    }
}
