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

public class Utils{
    public final static String vector = "vector";

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
