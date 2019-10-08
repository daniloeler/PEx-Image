package visualizer.featureselection;

import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.IOException;

public class SalienceSelectorThread extends Thread 
{
   public SalienceSelectorThread(String Path){ super(Path); }
   public String ReturnString;
   private Process Processo;
   
   public void run() 
   {
      try 
      {
         Processo = Runtime.getRuntime().exec(getName());
         Scanner input = new Scanner(new BufferedInputStream(Processo.getInputStream()));
         
         while(input.hasNextLine())
         {
            FeatureSelectionView.setTextArea(input.nextLine() + "\n");
         }
         FeatureSelectionView.setTextArea("\nProcesso Anulado");
      }
      catch(IOException ex) 
      {
         ex.printStackTrace();
      }
   }
   
   public void StopProcess()
   {
      Processo.destroy();
      //System.out.println("Processo Destruido");
   }
   
   public String getString()
   {
      return ReturnString;
   }
}
