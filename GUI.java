import javax.swing.*;

import java.awt.Dimension;

import java.awt.BorderLayout;



public class GUI extends JFrame {
    
    
    public GUI(){
        this.setTitle("My Vanga");
        this.setSize(740,600);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        // INITIALIZE MAIN PANEL
        Dimension mainPanelD = new Dimension(this.getWidth(),this.getHeight()); 
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setSize(mainPanelD);
        this.add(mainPanel);
        
        //  INITIALIZE VERTICAL MENU BAR
        // create menu panel
        Menu menu = new Menu(mainPanel);
        mainPanel.add(menu,BorderLayout.WEST);


        

    }


    
}
