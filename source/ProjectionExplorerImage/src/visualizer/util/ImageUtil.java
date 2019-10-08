/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer Image (PEx-Image).
 *
 * How to cite this work:
 *
 *
 * PEx-Image is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx-Image is distributed in the hope that it will be useful, but WITHOUT 
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
 * with PEx-Image. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */


/*
 * ImageUtil.java
 *
 * Created on 13 de Dezembro de 2007, 10:17
 */

package visualizer.util;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.ColorConvertOp;


/**
 *
 * @author Danilo Medeiros Eler
 *
 */

public class ImageUtil {
    
    public static void convertDirToGray(String directory){
        File dir = new File(directory);
        String[] files = dir.list();
        if (files.length > 0){
            for(int i=0; i<files.length;i++){
                File f = new File(directory + "\\" + files[i]);
                try{
                    BufferedImage img = ImageIO.read(f);
                    if (img.getType() != BufferedImage.TYPE_BYTE_GRAY ){
                        img = RGBToGray(img);
                        String type = files[i].substring( files[i].lastIndexOf('.')+1, files[i].length() );
                        ImageIO.write(img, type , new File(directory + "\\" +files[i]));
                    }
                } catch ( java.io.IOException ie){
                    ie.printStackTrace();
                }
            }
        }
    }
    
    public static BufferedImage RGBToGray(BufferedImage input){
        ColorSpace graySpace = ColorSpace.getInstance (ColorSpace.CS_GRAY);
        ColorConvertOp grayConvert = new ColorConvertOp (graySpace, null);
        BufferedImage output = new BufferedImage (input.getWidth(), 
        input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        return grayConvert.filter (input, output);       
    }
    
    public static BufferedImage myRGBToGray(BufferedImage input){
        ColorSpace graySpace = ColorSpace.getInstance (ColorSpace.CS_GRAY);
        ColorConvertOp grayConvert = new ColorConvertOp (graySpace, null);
        BufferedImage output = new BufferedImage (input.getWidth(), 
        input.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        return grayConvert.filter (input, output);   
    }
    
    public static BufferedImage readImage(String filename){
        try{
            return ImageIO.read( new File( filename ) );
        }catch(java.io.IOException ioe){
            ioe.printStackTrace();
            return null;
        }
    }
    
    public static int[][] getGrayValues(BufferedImage image){
        int[][] gray = new int[image.getHeight()][image.getWidth()];
        
        for (int y=0; y<image.getHeight(); y++)
            for (int x=0; x<image.getWidth(); x++){
                Color c = new Color( image.getRGB( x, y ) );
                gray[y][x] = c.getRed();
            }    
        
        return gray;
    }
    
    public static int numberOfPixels(String filename){
        BufferedImage im = readImage(filename);
        return im.getWidth() * im.getHeight();
    }
    
    public static float getSobelAt(int [][] m, int x, int y){        
        float gx, gy;
        gx = (m[y][x-1] * -2) + (m[y-1][x-1] * -1) + (m[y+1][x-1] * -1) +
             (m[y][x+1] * 2) + (m[y-1][x+1] * 1) + (m[y+1][x+1] * 1);

        gy = (m[y+1][x] * -2) + (m[y+1][x-1] * -1) + (m[y+1][x+1] * -1) +
             (m[y-1][x] * 2) + (m[y-1][x-1] * 1) + (m[y-1][x+1] * 1);

        return (float) Math.sqrt( gx*gx + gy*gy );
    }
    
    public static void writeImage(BufferedImage image, String type, String filename){
        try{
            ImageIO.write(image, type, new File(filename));
        }catch(java.io.IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    
    
}
