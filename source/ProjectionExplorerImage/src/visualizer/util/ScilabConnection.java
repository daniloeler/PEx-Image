/*
 * ScilabConnection.java
 *
 * Created on 13 de Novembro de 2007, 15:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package visualizer.util;

import javasci.*;
/**
 *
 * @author Danilo Medeiros Eler
 */
public class ScilabConnection {
    
    /** Creates a new instance of ScilabConnection */
    /** Creates a new instance of ScilabConnection */
    public ScilabConnection() {
    }    
    
    public ScilabConnection(String codeDir, String dataDir, String arqOut, String arqNames) {
        this.codeDir = codeDir;
        this.dataDir = dataDir;
        this.arqOut = arqOut;
        this.arqNames = arqNames;        
    }    
    
    public void execute(){
        try{
        System.out.println("exec " + codeDir + "\\go.sci;");
        Scilab.Exec("exec " + codeDir + "\\go.sci;");
        System.out.println("go(''" + codeDir + "'', ''" + dataDir + "'', ''" + arqOut + "'', ''" + arqNames+"'');");
        Scilab.Exec("go(''" + codeDir + "'', ''" + dataDir + "'', ''" + arqOut + "'', ''" + arqNames+"'');");
        //System.out.println("Extraiu tudo");
        } catch (Throwable e) {
            System.out.println("Scilab Code Error");
            e.printStackTrace();            
        }
    }
    
    public static boolean isScilabInstalled() {
        try {
            Scilab.Exec("clear;");
        } catch (Throwable e) {
            return false;
        }
        
        return true;
    }

    public String getCodeDir() {
        return codeDir;
    }

    public void setCodeDir(String codeDir) {
        this.codeDir = codeDir;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getArqOut() {
        return arqOut;
    }

    public void setArqOut(String arqOut) {
        this.arqOut = arqOut;
    }

    public String getArqNames() {
        return arqNames;
    }

    public void setArqNames(String arqNames) {
        this.arqNames = arqNames;
    }
    
    private String codeDir;
    private String dataDir;
    private String arqOut;
    private String arqNames;
    
}
