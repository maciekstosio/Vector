/**
*Prosty program do edycji grafiki.
*@author Maciej Stosio
*/

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

public class Paint{
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                new Window();
            }
        });
    }
}

class Window extends JFrame implements ActionListener{
    JPanel toolbox;
    JPanel canvas;
    JButton info,rectangle,circle,polygon;
    public static Mode mode;
    public Window(){
        super("Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,400);
        setLayout(new BorderLayout());

        createToolbox();
        createCanvas();

        setVisible(true);
    }

    private void createToolbox(){
        JPanel left,right;
        toolbox = new JPanel(new GridLayout(1,2));
        add(toolbox,BorderLayout.NORTH);
        left = new JPanel(new FlowLayout(FlowLayout.LEADING));
        right = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        toolbox.add(left);
        toolbox.add(right);

        rectangle = new JButton("R");
        rectangle.addActionListener(this);
        left.add(rectangle);

        circle = new JButton("C");
        circle.addActionListener(this);
        left.add(circle);

        polygon = new JButton("P");
        polygon.addActionListener(this);
        left.add(polygon);

        info = new JButton("Informacje");
        right.add(info);
    }

    private void createCanvas(){
        canvas = new CanvasPanel();
        add(canvas,BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        circle.setForeground(Color.BLACK);
        polygon.setForeground(Color.BLACK);
        rectangle.setForeground(Color.BLACK);

        if(source==rectangle){
            if(Window.mode!=Mode.RECTANGLE){
                rectangle.setForeground(Color.BLUE);
                Window.mode = Mode.RECTANGLE;
            }else{
                rectangle.setForeground(Color.BLACK);
                Window.mode = null;
            }
        }else if(source==circle){
            if(Window.mode!=Mode.CIRCLE){
                circle.setForeground(Color.BLUE);
                Window.mode = Mode.CIRCLE;
            }else{
                circle.setForeground(Color.BLACK);
                Window.mode = null;
            }
        }else if(source==polygon){
            if(Window.mode!=Mode.POLYGON){
                polygon.setForeground(Color.BLUE);
                Window.mode = Mode.POLYGON;
            }else{
                polygon.setForeground(Color.BLACK);
                Window.mode = null;
            }
        }
    }
}

class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener{
    private Graphics2D g2d;
    private int x, y;
    private ArrayList<Shape> shapes = new ArrayList<Shape>();
    private ArrayList<Point> points= new ArrayList<Point>();

    CanvasPanel(){
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Shape preview;
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.BLACK);

        for(Shape item: shapes) item.draw(g2d);

        if(Window.mode==Mode.RECTANGLE && points.size()>0){
            preview = new Rectangle(points);
            preview.preview(g2d,x,y);
        }else if(Window.mode==Mode.CIRCLE && points.size()>0){
            preview = new Circle(points);
            preview.preview(g2d,x,y);
        }else if(Window.mode==Mode.POLYGON){
            preview = new Polygon(points);
            preview.preview(g2d,x,y);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(Window.mode==Mode.RECTANGLE || Window.mode==Mode.CIRCLE){
            x= (int)e.getX();
            y= (int)e.getY();
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(Window.mode==Mode.POLYGON){
            x= (int)e.getX();
            y= (int)e.getY();
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(Window.mode==Mode.POLYGON){
            points.add(new Point((int)e.getX(), (int)e.getY()));
            if(e.getClickCount() == 2 && !e.isConsumed()) {
                e.consume();
                shapes.add(new Polygon(points));
                points.clear();
            }
            x= (int)e.getX();
            y= (int)e.getY();
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(Window.mode==Mode.RECTANGLE || Window.mode==Mode.CIRCLE){
            points.add(new Point((int)e.getX(), (int)e.getY()));
            x= (int)e.getX();
            y= (int)e.getY();
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(Window.mode==Mode.RECTANGLE){
            points.add(new Point((int)e.getX(), (int)e.getY()));
            shapes.add(new Rectangle(points));
            points.clear();
        }else if(Window.mode==Mode.CIRCLE){
            points.add(new Point((int)e.getX(), (int)e.getY()));
            shapes.add(new Circle(points));
            points.clear();
        }
        repaint();
    }
}

enum Mode{
    RECTANGLE, CIRCLE, POLYGON;
}

abstract class Shape{
    ArrayList<Point> points;

    public Shape(ArrayList<Point> p){
        points=new ArrayList<Point>(p);
    }

    public void add(int x, int y){
        points.add(new Point(x,y));
    }
    //
    public abstract void draw(Graphics2D g2d);
    public abstract void preview(Graphics2D g2d, int x, int y);
}

class Rectangle extends Shape{
    public Rectangle(ArrayList<Point> p){
        super(p);
    }

    public void draw(Graphics2D g2d){
        g2d.setColor(Color.BLACK);
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
        g2d.setColor(Color.BLACK);
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

        g2d.setColor(Color.BLACK);
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
