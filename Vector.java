//UI
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
*Simple graphics editor. You can draw rectangles, circles and polygons then select and edit them.
*@author Maciej Stosio
*@version 1.0
*/
public class Vector{
    /**
    *Lunch the main window.
    *@param args String[]
    */
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                new Window();
            }
        });
    }
}
