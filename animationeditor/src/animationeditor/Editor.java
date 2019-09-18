/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animationeditor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author space
 */
public class Editor extends javax.swing.JFrame { 
    //MENU OPTION
    private  JButton backButton = new JButton("Back Frame");
    private  JButton colorPicker = new JButton("Color");
    private  JButton nextFrame = new JButton("Frame");
    private  JButton save = new JButton("Save");
    private  JButton nextButton = new JButton("Next Frame");
    
    private GridLayout editorLayout = new GridLayout(11,15, 1,1);
    private boolean[][] selected = new boolean[10][10];
    private JButton[][] buttonGrid = new JButton[10][10];
    private Color[][] colorArray = new Color[10][10];
    private final ArrayList<animationFrame> tempSave;
    private ActionListener buttonListener;
    private int rVal = 50, gVal = 50, bVal = 50, frame = 0, indexedPage = 0;
    private animationFrame tempFrame;
    private final colorPicker pick = new colorPicker(this);
    /**
     * Creates new form Editor
     */
    public Editor() {
        this.tempSave = new ArrayList<animationFrame>();
        initComponents();
        this.setLayout(editorLayout);
        buttonListener = (ActionEvent evt) -> {
            JButton selectedBtn = (JButton)evt.getSource();
            for(int row = 0; row < buttonGrid.length; row++){
                for(int col = 0; col < buttonGrid[row].length; col++){
                    if(buttonGrid[row][col] == selectedBtn && !selected[row][col]){
                        System.out.printf("Button Selected: %d %d%n", row,col);
                        Color colour = new Color(rVal,gVal,bVal);
                        System.out.printf("%d %d %d%n",rVal, gVal, bVal);
                        buttonGrid[row][col].setBackground(colour);
                        colorArray[row][col] = colour;
                        selected[row][col] = true;
                    }else if(buttonGrid[row][col] == selectedBtn && selected[row][col]){
                        System.out.printf("Button Selected: %d %d%n", row,col);
                        buttonGrid[row][col].setBackground(Color.white);
                        colorArray[row][col] = Color.white;
                        selected[row][col] = false;
                    }
                    this.repaint();     
                }
            }
            if(this.colorPicker == selectedBtn){   
                pick.setVisible(true);
            }
            if(this.nextFrame == selectedBtn){
                this.nextFrameFunction();
                this.resetCanvas();
                this.repaint();
            }
            if(this.save == selectedBtn){
                try{
                    this.saveAnimation();
                }catch(IOException ex){
                    System.out.println(ex);
                }
            }
            if(this.backButton == selectedBtn){
                if(indexedPage == 0){
                    
                }else{
                    indexedPage--;
                    this.resetCanvas();
                    this.drawPage(indexedPage);
                    this.repaint();
                }
            }
            if(this.nextButton == selectedBtn){
                if(indexedPage > frame){
                    
                }else{
                    indexedPage++;
                    this.resetCanvas();
                    this.drawPage(indexedPage);
                    this.repaint();
                }
            }
        };
        setupCanvas();
    }
    //Class Functions to do actions
    public  void saveAnimation() throws IOException{
        PrintWriter out;
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this,"Would you like to save " + tempSave.size() +" frames?", "Warning", dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            String name = JOptionPane.showInputDialog(this, "Animation Filename");
            out = new PrintWriter(new BufferedWriter(new FileWriter("\\"+name+".txt", true)));
            out.println("<"+tempSave.size()+">");
            for(int frames = 0; frames < tempSave.size(); frames++){
                Color[][] tempColor = tempSave.get(frames).getColorArray();
                boolean[][] tempSelected = tempSave.get(frames).getSelected();
                out.println("["+Integer.toString(frames)+"]");
                for(int row = 0; row < tempColor.length; row++){
                    for(int col = 0; col < tempColor[row].length; col++){
                        if(tempSelected[row][col] == true){
                            out.print("(1,"+tempColor[row][col].getRed()+","+tempColor[row][col].getGreen()+","+tempColor[row][col].getBlue()+")");
                        }else{
                            out.print("(0,"+tempColor[row][col].getRed()+","+tempColor[row][col].getGreen()+","+tempColor[row][col].getBlue()+")");
                        }
                    }
                    out.println();
                }
                out.println("Delay: " + tempSave.get(frames).getDelay());
            }
            out.flush();
            out.close();
        }else{
            System.out.println("okie");
        }
    }
    public  void drawPage(int page){
           System.out.println("Page: "+ page);
           
           Color[][] tempColor = this.tempSave.get(page).getColorArray();
           boolean[][] tempSelected = this.tempSave.get(page).getSelected();
           
           for(int row = 0; row < colorArray.length; row++){
                for(int col = 0; col < colorArray[row].length; col++){
                    selected[row][col] = tempSelected[row][col];
                    //Color color = new Color(tempSave.get(0).getColorArray()[row][col]);
                    colorArray[row][col] = tempColor[row][col];
                    buttonGrid[row][col].setBackground(tempColor[row][col]);
                    //System.out.printf(" %d %d %d%n",color.getRed(), color.getGreen(),color.getBlue());
            }
        }
    }
    public  void nextFrameFunction(){
        int delay;
        this.frame++;
        this.indexedPage++;
        String delayString = JOptionPane.showInputDialog("How long do you want the delay for? (ms)");
        delay = Integer.parseInt(delayString);
        Color[][] tempColor = colorArray;
        boolean[][] tempSelected = selected;
        tempFrame = new animationFrame(colorArray, selected, delay);
        if(this.tempSave.add(tempFrame)){
            System.out.println("Uploaded");
        }else{
            System.out.println("Failed");
        }
    }  
    public void resetCanvas(){
        Color colourTest = new Color(255,255,255);
        for(int row = 0; row < buttonGrid.length; row++){
            for(int col = 0; col < buttonGrid[row].length; col++){
                this.selected[row][col] = false;
                this.colorArray[row][col] = colourTest;
                buttonGrid[row][col].setBackground(colourTest);
            }
        }
    }
    private void setupCanvas(){
           for(int row = 0; row < buttonGrid.length; row++){
                for(int col = 0; col < buttonGrid[row].length; col++){
                    selected[row][col] = false;
                    buttonGrid[row][col] = new JButton();
                    buttonGrid[row][col].addActionListener(buttonListener);
                    Color colour = new Color(255,255,255);
                    colorArray[row][col] = colour;
                    buttonGrid[row][col].setBackground(colour);
                    this.add(buttonGrid[row][col]);
            }
        }
        this.backButton.addActionListener(buttonListener);
        this.add(this.backButton);
        this.colorPicker.addActionListener(buttonListener);
        this.add(this.colorPicker); 
        this.nextFrame.addActionListener(buttonListener);
        this.add(this.nextFrame);
        this.save.addActionListener(buttonListener);
        this.add(this.save);
        this.nextButton.addActionListener(buttonListener);
        this.add(this.nextButton);
     
    }
    //Mutators.
    public void setRedValue(int newrVal){
        this.rVal = newrVal;
    }
    public void setGreenValue(int newgVal){
        this.gVal = newgVal;
    }
    public void setBlueValue(int newBVal){
        this.bVal = newBVal;
    }
    /**
      
    }
   
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    private static void createAndShowGui(){
      Editor mainPanel = new Editor();

      JFrame frame = new JFrame("ButtonGridEg");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true); 
    }
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new Editor().setVisible(true);
                //createAndShowGui();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
