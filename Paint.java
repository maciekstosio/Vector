/**
*Prosty program do edycji grafiki. Umożliwiający tworzenie prostokątów, elips oraz wielokątów, zaznaczanie ich i edycję.
*@author Maciej Stosio
*@version 1.0
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

/**
*Klasa startowa
*/
public class Paint{
    /**
    *Metoda uruchamiajaca program
    *@param args Array of string
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
