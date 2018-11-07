package Main;
import java.awt.EventQueue; 
import javax.swing.JFrame; 

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial") 
public class Game extends JApplet { 
 
    /**
     * Launch the application.
     */ 
    public static void main(String[] args) { 
        EventQueue.invokeLater(new Runnable() { 
            public void run() { 
                try { 
                    JFrame frame = new JFrame(); 
                    JOptionPane.setRootFrame(frame); 
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                    frame.setContentPane(new GamePanel()); 
                    frame.pack(); 
                    frame.setVisible(true); 
                } catch (Exception e) { 
                    e.printStackTrace(); 
                } 
            } 
        }); 
    } 
 
    /**
     * Create the applet.
     */ 
    public Game() { 
        this.setContentPane(new GamePanel()); 
    } 
} 
