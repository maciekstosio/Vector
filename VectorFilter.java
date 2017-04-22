import javax.swing.*;
import javax.swing.colorchooser.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.lang.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter;

public class VectorFilter extends FileFilter{
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if(extension != null) {
            if(extension.equals(Utils.vector)) {
                return true;
            }else{
                return false;
            }
        }

        return false;
    }

    public String getDescription(){
        return new String("Only .vector files");
    }
}
