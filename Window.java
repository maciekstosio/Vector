//UI
import javax.swing.*;
import javax.swing.colorchooser.*;
import java.awt.*;

//Events
import javax.swing.KeyStroke;
import java.awt.event.*;

//Files
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

//Other
import java.lang.*;
import java.util.ArrayList;


/**
*Enum class defining mode types.
*/
enum Mode{
    RECTANGLE, CIRCLE, POLYGON, EDIT;
}

/**
*Class handling window behavior.
*@author Maciej Stosio
*@version 1.0
*/
public class Window extends JFrame implements ActionListener{
    /** Selected global mode */
    public static Mode mode;
    /** Global color */
    public static Color rgb=Color.BLACK;
    /** Cancvas */
    public static JPanel canvas;

    private JFrame infoWindow;
    private JPanel toolbox;
    private JButton info,rectangle,circle,color,polygon,edit,save,clear,open,export;

    /**
    *Lunch main window.
    */
    public Window(){
        super("Vector");
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
        try{
            BufferedImage image = ImageIO.read(new File("assets/edit.png"));
            ImageIcon icon = new ImageIcon(image);
            Image editImage = icon.getImage();
            editImage = editImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH ) ;
            edit = new JButton(new ImageIcon(editImage));
            edit.addActionListener(this);
            left.add(edit);

            image = ImageIO.read(new File("assets/rectangle.png"));
            icon = new ImageIcon(image);
            Image rectangleImage = icon.getImage();
            rectangleImage = rectangleImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH );
            rectangle = new JButton(new ImageIcon(rectangleImage));
            rectangle.addActionListener(this);
            left.add(rectangle);

            image = ImageIO.read(new File("assets/circle.png"));
            icon = new ImageIcon(image);
            Image circleImage = icon.getImage();
            circleImage = circleImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH );
            circle = new JButton(new ImageIcon(circleImage));
            circle.addActionListener(this);
            left.add(circle);

            image = ImageIO.read(new File("assets/polygon.png"));
            icon = new ImageIcon(image);
            Image polygonImage = icon.getImage();
            polygonImage = polygonImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH );
            polygon = new JButton(new ImageIcon(polygonImage));
            polygon.addActionListener(this);
            left.add(polygon);

            color = new JButton();
            color.addActionListener(this);
            color.setBackground(rgb);
            color.setPreferredSize(new Dimension(20, 20));
            color.setBorder(BorderFactory.createEmptyBorder());
            color.setOpaque(true);
            left.add(color);

            image = ImageIO.read(new File("assets/clear.png"));
            icon = new ImageIcon(image);
            Image clearImage = icon.getImage();
            clearImage = clearImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH );
            clear = new JButton(new ImageIcon(clearImage));
            clear.addActionListener(this);
            right.add(clear);

            image = ImageIO.read(new File("assets/open.png"));
            icon = new ImageIcon(image);
            Image openImage = icon.getImage();
            openImage = openImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH );
            open = new JButton(new ImageIcon(openImage));
            open.addActionListener(this);
            right.add(open);

            image = ImageIO.read(new File("assets/save.png"));
            icon = new ImageIcon(image);
            Image saveImage = icon.getImage();
            saveImage = saveImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH );
            save = new JButton(new ImageIcon(saveImage));
            save.addActionListener(this);
            right.add(save);

            image = ImageIO.read(new File("assets/export.png"));
            icon = new ImageIcon(image);
            Image exportImage = icon.getImage();
            exportImage = exportImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH );
            export = new JButton(new ImageIcon(exportImage));
            export.addActionListener(this);
            right.add(export);

            image = ImageIO.read(new File("assets/info.png"));
            icon = new ImageIcon(image);
            Image infoImage = icon.getImage();
            infoImage = infoImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH );
            info = new JButton(new ImageIcon(infoImage));
            info.addActionListener(this);
            right.add(info);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createCanvas(){
        canvas = new CanvasPanel();
        add(canvas,BorderLayout.CENTER);
    }

    private void createInfoWindow(){
        infoWindow = new JFrame("Information");
        infoWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoWindow.setSize(300,175);
        infoWindow.setResizable(false);
        infoWindow.setLayout(new FlowLayout());

        infoWindow.add(new JLabel("<html><center style='margin-top: 30px;'>Vector - prosty program do edycji grafiki.<br/>Autor Maciej Stosio<br/>Icons from the Noun Project</center></html>"));
    }

    private void createColorPicker(){
        JColorChooser chooser = new JColorChooser();
        AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
        ActionListener setColor = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            rgb = chooser.getColor();
            color.setBackground(rgb);
          }
        };

        for (AbstractColorChooserPanel accp : panels) {
            if(!accp.getDisplayName().equals("RGB")) {
                chooser.removeChooserPanel(accp);
            }
        }

        JColorChooser.createDialog(null, "Choose a color", false, chooser, setColor , null).setVisible(true);
    }

    /**
    *Handle buttons' actions i.e change global mode, open info moda, save, open files, clear canvas.
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source != info && source != color){
            CanvasPanel.selected = null;
            CanvasPanel.points = new ArrayList<DoublePoint>();
            for(Shape s: CanvasPanel.shapes){
                s.unselect();
            }
            Window.canvas.repaint();
        }

        if(source==rectangle){
            if(Window.mode!=Mode.RECTANGLE){
                Window.mode = Mode.RECTANGLE;
            }else{
                Window.mode = null;
            }
        }else if(source==circle){
            if(Window.mode!=Mode.CIRCLE){
                Window.mode = Mode.CIRCLE;
            }else{
                Window.mode = null;
            }
        }else if(source==polygon){
            if(Window.mode!=Mode.POLYGON){
                Window.mode = Mode.POLYGON;
            }else{
                Window.mode = null;
            }
        }else if(source==edit){
            if(Window.mode!=Mode.EDIT){
                Window.mode = Mode.EDIT;
            }else{
                Window.mode = null;
                CanvasPanel.selected=null;
                for(Shape s: CanvasPanel.shapes){
                    s.unselect();
                }
                Window.canvas.repaint();
            }
        }else if(source==info){
            infoWindow.setVisible(true);
        }else if(source==color){
            createColorPicker();
        }else if(source==save){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Only .vector files", "vector"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                BufferedWriter bw;
                if(getFileExtension(file).equals("vector")){
                    try{
                        bw = new BufferedWriter(new FileWriter(file.toString()));

                        bw.write(getWidth() + "," + getHeight());
                        bw.newLine();
                        bw.write(CanvasPanel.shapes.size()+"");
                        bw.newLine();
                        for(Shape s: CanvasPanel.shapes){
                            Color color = s.getColor();
                            bw.write(s.getClass().getSimpleName());
                            bw.newLine();
                            bw.write(color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha());
                            bw.newLine();
                            ArrayList<DoublePoint> points = s.getPoints();
                            bw.write(points.size()+"");
                            bw.newLine();
                            for(DoublePoint p: points){
                                bw.write(p.getX()+","+p.getY());
                                bw.newLine();
                            }
                        }
                        bw.close();
                        JOptionPane.showMessageDialog(this,"File saved successfuly");
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this,"Unsupported filetype, use Vector's file (.vector) instead","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(source==open){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Only .vector files", "vector"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                BufferedReader br;
                if(getFileExtension(file).equals("vector")){
                    try{
                        String line;

                        br = new BufferedReader(new FileReader(file.toString()));
                        CanvasPanel.shapes = new ArrayList<Shape>();
                        CanvasPanel.selected = null;
                        CanvasPanel.points = new ArrayList<DoublePoint>();

                        if((line = br.readLine()) != null){
                            String[] parts = line.split(",");
                            int width = Integer.parseInt(parts[0]);
                            int height = Integer.parseInt(parts[1]);
                            setSize(width,height);
                            if((line = br.readLine()) != null){
                                int n = Integer.parseInt(line);
                                for(int i=0; i<n; i++){
                                    String shapeName = br.readLine();

                                    parts = br.readLine().split(",");
                                    int r= Integer.parseInt(parts[0]);
                                    int g = Integer.parseInt(parts[1]);
                                    int b = Integer.parseInt(parts[2]);
                                    int a = Integer.parseInt(parts[3]);

                                    int k = Integer.parseInt(br.readLine());

                                    ArrayList<DoublePoint> points = new ArrayList<DoublePoint>();
                                    for(int j=0; j<k; j++){
                                        parts = br.readLine().split(",");
                                        points.add(new DoublePoint(Double.parseDouble(parts[0]),Double.parseDouble(parts[01])));
                                    }

                                    Shape shape;
                                    switch(shapeName){
                                        case "Rectangle":
                                            shape = new Rectangle(points);
                                            break;
                                        case "Circle":
                                            shape = new Circle(points);
                                            break;
                                        default:
                                            shape = new Polygon(points);
                                            break;
                                    }
                                    shape.setColor(new Color(r,g,b,a));
                                    shape.init();

                                    CanvasPanel.shapes.add(shape);
                                }
                            }
                            Window.canvas.repaint();
                        }
                        br.close();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this,"Unsupported filetype, use Vector's file (.vector) instead","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(source==export){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();
                canvas.paint(g);
                try{
                    if(!ImageIO.write(image, "png", new File(fileChooser.getSelectedFile()+"/export"+System.currentTimeMillis()+".png"))){
                        JOptionPane.showMessageDialog(this,"Fail to export your canvas","Error",JOptionPane.ERROR_MESSAGE);
                    }
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(source==clear){
            CanvasPanel.shapes = new ArrayList<Shape>();
            CanvasPanel.selected = null;
            CanvasPanel.points = new ArrayList<DoublePoint>();
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        try{
            return name.substring(name.lastIndexOf(".") + 1);
        }catch (Exception e) {
            return "";
        }
        // String name = file.getName();
        // String[] namePieces = name.split(".");
        // System.out.println(name);
        // System.out.println(namePieces.length);
        // if(namePieces.length>0){
        //     return namePieces[namePieces.length-1];
        // }else{
        //     return "";
        // }
    }
}
