/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.view.tools;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Danilo M Eler
 */
public class ViewPanelMove extends JPanel{

    public ViewPanelMove(int w, int h){
        this.width = w;
        this.height = h;
    }

@Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

        if (this.imageBuffer == null) {
            this.imageBuffer = new BufferedImage(this.width,
                                    this.height, BufferedImage.TYPE_INT_RGB);

            java.awt.Graphics2D g2Buffer = this.imageBuffer.createGraphics();
            g2Buffer.setColor(Color.WHITE);
            g2Buffer.fillRect(0, 0, this.width-1, this.height-1);

            g2Buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2Buffer.dispose();
        }

        if (this.imageBuffer != null) {
            g2.drawImage(this.imageBuffer, 0, 0, null);
            //g2.setColor(java.awt.Color.BLUE);
            //g2.drawRect(10, 10, 15, 15);
        }
    }

    public BufferedImage getImageBuffer(){
        return this.imageBuffer;
    }

    private BufferedImage imageBuffer;
    private int width;
    private int height;
}
