import javax.swing.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;


public class GUI extends JFrame {
    Trainer trainer = new Trainer("MLdata.csv");
    private HashMap<String,String> selections = new HashMap<String,String>();
    
    public GUI(){
        this.setTitle("My Vanga");
        this.setSize(700,600);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        Dimension mainPanelD = new Dimension(this.getWidth(),this.getHeight()); 
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        mainPanel.setSize(mainPanelD);

        Dimension displayPanelD = new Dimension(this.getWidth(),(int)(this.getHeight() * 2/3));
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        displayPanel.setPreferredSize(displayPanelD);
        displayPanel.setSize(displayPanelD);
        mainPanel.add(new JScrollPane(displayPanel));

        Dimension linePanelD = new Dimension((int)displayPanelD.getWidth(),30);

        //add radio buttons
        trainer.getClassifierValues().forEach((column,values) -> {
            if (!column.startsWith(trainer.getPredictClassifer())){

                JPanel linePanel = new JPanel();
                linePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
                linePanel.setPreferredSize(linePanelD);
                linePanel.setName(column);
                // linePanel.addMouseListener(new MouseListener(){
                //     @Override
                //     public void
                // });

                JLabel colLabel = new JLabel(column);
                colLabel.setPreferredSize(new Dimension(300,(int)linePanelD.getHeight()));
                colLabel.setName(column);
                linePanel.add(colLabel);

                ButtonGroup group = new ButtonGroup();
                values.forEach((value) -> {
                    JRadioButton radioButton = new JRadioButton(value);
                    radioButton.setPreferredSize(new Dimension(200,(int)linePanelD.getHeight()));
                    radioButton.setName(value);
                    radioButton.setSelected(true);
                    
                    radioButton.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e){
                            selections.put(linePanel.getName(),radioButton.getText());
                        }
                    });
                    group.add(radioButton);
                    linePanel.add(radioButton);
                });
                for (Enumeration<AbstractButton> btns = group.getElements();btns.hasMoreElements();){
                    AbstractButton radioButton = btns.nextElement();
                    if (radioButton.isSelected()){
                        selections.put(linePanel.getName(),radioButton.getText());
                    }
                }
            
                
                displayPanel.add(linePanel);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(),(int)(this.getHeight() * 1/3)));
        mainPanel.add(buttonPanel);

        Dimension buttonDimension = new Dimension(150,40);
        JButton addButton = new JButton("Add new record");
        addButton.setPreferredSize(buttonDimension);
        addButton.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
                Entity entity = new Entity(selections);
                trainer.addEntity(entity);
            }
        });
        buttonPanel.add(addButton);

        JButton predictButton = new JButton("Predict");
        predictButton.setPreferredSize(buttonDimension);
        predictButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                List<String> cols = convertSelections();
                String target = trainer.getClassifierList().get(trainer.getClassifierList().size()-1);

                cols.forEach((str) -> {
                    System.out.println(str);
                });

                // find the value of target column
                int targetValue = Table.findTargetCol(cols, target);
                System.out.println(cols.get(targetValue));
            }
        });
        buttonPanel.add(predictButton);

        this.add(mainPanel);
        
    }

    public List<String> convertSelections(){
        Classifier cl = new Classifier();
        return cl.seperateCols(Table.colFormat, this.selections);
    }


    
}
