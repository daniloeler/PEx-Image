/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx).
 *
 * How to cite this work:
 *  
@inproceedings{paulovich2007pex,
author = {Fernando V. Paulovich and Maria Cristina F. Oliveira and Rosane 
Minghim},
title = {The Projection Explorer: A Flexible Tool for Projection-based 
Multidimensional Visualization},
booktitle = {SIBGRAPI '07: Proceedings of the XX Brazilian Symposium on 
Computer Graphics and Image Processing (SIBGRAPI 2007)},
year = {2007},
isbn = {0-7695-2996-8},
pages = {27--34},
doi = {http://dx.doi.org/10.1109/SIBGRAPI.2007.39},
publisher = {IEEE Computer Society},
address = {Washington, DC, USA},
}
 *  
 * PEx is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Computer Graphics and Image
 * Processing Group (http://www.lcad.icmc.usp.br) at Instituto de Ciencias
 * Matematicas e de Computacao - ICMC - (http://www.icmc.usp.br) of 
 * Universidade de Sao Paulo, Sao Carlos/SP, Brazil. The initial developer 
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.util.SystemPropertiesManager;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Splash extends javax.swing.JFrame {

    static {
        splash = new Splash();
    }

    private Splash() {
        this.setVisible(false);
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);

        SplashPanel sp = new SplashPanel();
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(sp, BorderLayout.CENTER);
        this.setSize(sp.getWidth(), sp.getHeight());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = this.getSize();

        this.setLocation((screenSize.width - size.width) / 2,
                (screenSize.height - size.height) / 2);
    }

    public static Splash getInstance() {
        return splash;
    }

    public void start() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.setVisible(true);
    }

    public void stop() {
        this.setVisible(false);
        this.dispose();
    }

    public static class SplashPanel extends javax.swing.JPanel {

        public SplashPanel() {
            MediaTracker media = new MediaTracker(this);
            toolkit = Toolkit.getDefaultToolkit();
            image = toolkit.getImage(SplashPanel.class.getResource(imgName));

            if (image != null) {
                try {
                    media.addImage(image, 0);
                    media.waitForID(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Splash.class.getName()).log(Level.SEVERE,
                            null, ex);
                }
            }

            this.setPreferredSize(new java.awt.Dimension(image.getWidth(this),
                    image.getHeight(this)));
            this.setSize(image.getWidth(this), image.getHeight(this));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this.getBackground(), this);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g.setColor(java.awt.Color.BLACK);
            String version = SystemPropertiesManager.getInstance().getProperty("TOOL.VERSION");
            if (version == null || version.trim().length() < 1) {
                version= "**.**.**";
            }

            g.setFont(new Font("Verdana", Font.BOLD, 12));
            g.drawString("Version " + version, 315, 255);
        }

        private String imgName = "/images/splash.png";
        private Toolkit toolkit;
        private Image image;
    }

    public static void main(String args[]) {
        try {
            Splash.getInstance().start();
            Thread.sleep(2000);
            Splash.getInstance().stop();

        } catch (InterruptedException ex) {
            Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Splash splash;
}
