//UI and Graphics
import javax.swing.*;
import javax.swing.colorchooser.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

//Other
import java.lang.*;
import java.util.ArrayList;

class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{
    private Graphics2D g2d;
    public static Shape selected;
    private int x, y;
    public static ArrayList<Shape> shapes = new ArrayList<Shape>();
    public static ArrayList<DoublePoint> points= new ArrayList<DoublePoint>();

    CanvasPanel(){
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyBindings();
    }

    private void addKeyBindings(){
        //BACKSPACE
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "delete");
        getActionMap().put("delete", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<CanvasPanel.shapes.size();i++){
                    if(CanvasPanel.selected==CanvasPanel.shapes.get(i)){
                        CanvasPanel.selected=null;
                        CanvasPanel.shapes.remove(i);
                        Window.canvas.repaint();
                        break;
                    }
                }
            }
        });

        //SHIFT
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SHIFT"), "shift press");
        getActionMap().put("shift press", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println("shift press");
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released SHIFT"), "shift release");
        getActionMap().put("shift release", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("shift release");
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Shape preview;
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

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
    public void mouseMoved(MouseEvent e) {
        if(Window.mode==Mode.POLYGON){
            x= e.getX();
            y= e.getY();
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(Window.mode==Mode.POLYGON){
            Shape newShape;
            points.add(new DoublePoint(e.getX(), e.getY()));
            if(e.getClickCount() == 2 && !e.isConsumed()) {
                e.consume();
                newShape = new Polygon(points);
                newShape.setColor(Window.rgb);
                if(newShape.valid()){
                    shapes.add(newShape);
                }
                points.clear();
            }
            x=e.getX();
            y=e.getY();
        }else if(Window.mode==Mode.EDIT){
            if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()){
                if(selected!=null){
                    JColorChooser chooser = new JColorChooser();
                    AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
                    ActionListener setColor = new ActionListener() {
                      public void actionPerformed(ActionEvent actionEvent) {
                        selected.setColor(chooser.getColor());
                        Window.canvas.repaint();
                      }
                    };

                    for (AbstractColorChooserPanel accp : panels) {
                        if(!accp.getDisplayName().equals("RGB")) {
                            chooser.removeChooserPanel(accp);
                        }
                    }

                    JColorChooser.createDialog(null, "Wybierz kolor", false, chooser, setColor , null).setVisible(true);

                }
            }else{
                for(int i = shapes.size()-1; i>=0; i--){
                    if(shapes.get(i).include(e.getX(),e.getY())){
                        if(shapes.get(i).isSelected()){
                            shapes.get(i).unselect();
                            selected=null;
                        }else{
                            shapes.get(i).select();
                            selected = shapes.get(i);
                        }
                        for(int k = i-1; k>=0; k--){
                            shapes.get(k).unselect();
                        }
                        break;
                    }else{
                        shapes.get(i).unselect();
                    }
                }
            }
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
            points.add(new DoublePoint(e.getX(), e.getY()));
            x= e.getX();
            y= e.getY();
        }else if(Window.mode==Mode.EDIT){
            x= e.getX();
            y= e.getY();
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(Window.mode==Mode.RECTANGLE || Window.mode==Mode.CIRCLE){
            x= e.getX();
            y= e.getY();
        }else if(Window.mode==Mode.EDIT){
            if(selected!=null){
                selected.move(e.getX()-x,e.getY()-y);
                x=e.getX();
                y=e.getY();
            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Shape newShape;
        if(Window.mode==Mode.RECTANGLE){
            points.add(new DoublePoint(e.getX(), e.getY()));
            newShape = new Rectangle(points);
            newShape.setColor(Window.rgb);
            if(newShape.valid()){
                newShape.init();
                shapes.add(newShape);
            }
            points.clear();
        }else if(Window.mode==Mode.CIRCLE){
            points.add(new DoublePoint(e.getX(), e.getY()));
            newShape = new Circle(points);
            newShape.setColor(Window.rgb);
            if(newShape.valid()){
                newShape.init();
                shapes.add(newShape);
            }
            points.clear();
        }
        repaint();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
       String message;
       int notches = e.getWheelRotation();
       if(selected!=null){
           selected.resize(notches);
       }
       repaint();
    }
}
