import javax.swing.*;
import javax.swing.colorchooser.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.lang.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

enum Mode{
    RECTANGLE, CIRCLE, POLYGON;
}

class Window extends JFrame implements ActionListener{
    public static Mode mode;
    public static Color rgb=Color.BLACK;

    private JFrame infoWindow;
    private JPanel toolbox;
    private JPanel canvas;
    private JButton info,rectangle,circle,color,polygon;

    public Window(){
        super("Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700,400);
        setLayout(new BorderLayout());

        createToolbox();
        createCanvas();
        createInfoWindow();

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

        circle = new JButton("O");
        circle.addActionListener(this);
        left.add(circle);

        polygon = new JButton("P");
        polygon.addActionListener(this);
        left.add(polygon);

        color = new JButton();
        color.addActionListener(this);
        color.setBackground(rgb);
        color.setPreferredSize(new Dimension(20, 20));
        color.setBorder(BorderFactory.createEmptyBorder());
        color.setOpaque(true);
        left.add(color);

        info = new JButton("Informacje");
        info.addActionListener(this);
        right.add(info);
    }

    private void createCanvas(){
        canvas = new CanvasPanel();
        add(canvas,BorderLayout.CENTER);
    }

    private void createInfoWindow(){
        infoWindow = new JFrame("Informacje");
        infoWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoWindow.setSize(300,150);
        infoWindow.setResizable(false);
        infoWindow.setLayout(new FlowLayout());

        infoWindow.add(new JLabel("<html><center style='margin-top: 30px;'>Prosty program do edycji grafiki.<br/>&copy; Maciej Stosio</center></html>"));
    }

    private Color createColorPicker(){
        Color c=Color.BLACK;
        JColorChooser chooser = new JColorChooser();
        // JPanel chooserColor = new JPanel();
        // JLabel color = new JLabel(" DUPA ");
        // chooserColor.setLayout(new GridLayout(1,1));
        // chooserColor.add(color);
        // color.setBackground(Color.BLUE);
        // color.setOpaque(true);
        // chooser.setPreviewPanel(chooserColor);
        // info.setForeground(Color.BLUE);
        AbstractColorChooserPanel[] panels = chooser.getChooserPanels();

        ActionListener setColor = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            c = chooser.getColor();
          }
        };

        for (AbstractColorChooserPanel accp : panels) {
            if(!accp.getDisplayName().equals("RGB")) {
                chooser.removeChooserPanel(accp);
            }
        }

        JColorChooser.createDialog(null, "Dialog Title", false, chooser, setColor , null).setVisible(true);
        return c;
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
        }else if(source==info){
            infoWindow.setVisible(true);
        }else if(source==color){
            rgb = createColorPicker();
            color.setBackground(rgb);
            createColorPicker();
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
