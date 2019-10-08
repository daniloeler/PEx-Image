/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.graph;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Image;

/**
 *
 * @author wilson
 */
public class GroupRepresentative {
    
    private Image representacao;
    private int posX, posY;
    private boolean bordered;
    private boolean transparent;
    
    public GroupRepresentative(int posX, int posY, Image representacao) {
        this.posX = posX;
        this.posY = posY;
        this.representacao = representacao;
        transparent = bordered = false;
    }
    
    public boolean isBordered() {
        return bordered;
    }
    
    public void setBordered(boolean bordered) {
        this.bordered = bordered;
    }

    public Image getRepresentacao() {
        return representacao;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }
    
    public void draw(java.awt.Graphics2D g2) {        
        
        if( isTransparent() )
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        setTransparent(false);
        
        g2.drawImage(representacao, posX, posY, null);
                
        if( isBordered() ) {
            int w = representacao.getWidth(null);
            int h = representacao.getHeight(null);
            g2.setStroke(new BasicStroke(2.0f));
            g2.setColor(new Color(0, 149, 65));
            g2.drawRect(((int) posX) -2, ((int) posY)-2 , w + 3, h + 3);
        }
    }
}
