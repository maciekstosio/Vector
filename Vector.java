/**
*Prosty program do edycji grafiki. Umożliwiający tworzenie prostokątów, elips oraz wielokątów, zaznaczanie ich i edycję.
*@author Maciej Stosio
*@version 1.0
*/

//UI
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
*Klasa startowa
*/
public class Vector{
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
