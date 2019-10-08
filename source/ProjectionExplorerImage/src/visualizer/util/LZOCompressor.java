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
 * Contributor(s): Roberto Pinho <robertopinho@yahoo.com.br>, 
 *                 Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
package visualizer.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
//import java.io.UnsupportedEncodingException;

public class LZOCompressor
{
   
   public interface CLibrary extends Library
   {
      CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "./lib/DLLProject" : "c"), CLibrary.class);

      long Compress(String Text, long Size);
   }

   public long compressUsingLZO(String dest)
 {
/*
      byte[] utf8 = null;
      int byteCount = 0;
      try {
         utf8 = dest.getBytes("UTF-8");
         byteCount = utf8.length;
      } catch (UnsupportedEncodingException ex) {
         ex.printStackTrace();
      }
*/
      int Length = dest.length();
      int Size = dest.codePointCount(0, Length);
      long Result = CLibrary.INSTANCE.Compress(dest, (long) Size);

      if (Result < 0) {
         Result = Result * (-1);
      }
      return Result;
   }
}
