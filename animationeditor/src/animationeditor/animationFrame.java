/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animationeditor;
import java.awt.Color;
/**
 *
 * @author space
 */
//This will be used for temporary save purpose.
public class animationFrame {
    private Color[][] colorArrayTest;
    private boolean[][] selectedBox;
    private final int delay;
    //Constructor for when animationFrame first created.
    public animationFrame(Color[][] frameColorArray, boolean[][] frameSelected, int frameDelay){
        colorArrayTest = new Color[10][10];
        selectedBox = new boolean[10][10];
          for(int row = 0; row < selectedBox.length; row++){
                for(int col = 0; col < selectedBox[row].length; col++){
                    selectedBox[row][col] = frameSelected[row][col];
                    colorArrayTest[row][col] = frameColorArray[row][col
                            ];
            }
        }
        
        delay = frameDelay;
    }
    public void setColorArray(Color[][] newcolorArray){
        this.colorArrayTest = newcolorArray;
    }
    public void setFrameSelected(boolean[][] newFrameSelected){
        this.selectedBox = newFrameSelected;
    }
    public Color[][] getColorArray(){
        return this.colorArrayTest;
    }
    public boolean[][] getSelected(){
        return this.selectedBox;
    }
    public int getDelay(){
        return this.delay;
    }
    
    
 }
