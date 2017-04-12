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
