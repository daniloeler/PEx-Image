/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.featureselection;

/**
 *
 * @author Danilo M Eler
 */
public class PointsSelection {
 private float points[][];
 private String attributes[];
 private int attributeStatus[];
 
 PointsSelection(float points[][], String attributes[]){
    this.points = points;
    this.attributes = attributes;
    this.attributeStatus = new int[attributes.length];

    for(int i = 0; i < attributes.length; i++)
       attributeStatus[i] = 0;
    attributeStatus[attributes.length-1] = 1; //class attribute
 }

 private int searchAttribute(String attribute){
 	int i=0;
 	while (! attributes[i].equals(attribute)){
 		i++;
 	}
 	if (i<attributes.length)
 		return i;
    else return -1;
 }

 public void selectAttribute(String attribute){
   attributeStatus[ searchAttribute(attribute) ] = 1;
 }

 public void discardAttribute(String attribute){
   attributeStatus[ searchAttribute(attribute) ] = 0;
 }

 public String[] getSelectedAttributes(){
   String sAttributes[] = new String[numberOfSelectedAttributes()];
   int i, j;
   
   for (i = 0, j = 0; i < attributes.length; i++)
   	if (attributeStatus[i] == 1){   	
   		sAttributes[j] = attributes[i];
   		j++;
    }
   return sAttributes;
 }

 public String[] getDiscardedAttributes(){
   String dAttributes[] = new String[numberOfDiscardedAttributes()];
   int i,j;
   for (i=0,j=0; i<attributes.length; i++)
   	if (attributeStatus[i] == 0){   	
   		dAttributes[j] = attributes[i];
   		j++;
    }   	
   return dAttributes;
 }
 
 
 public String[] getAttributes()
 {
   String Attributes[] = new String[numberOfAttributes()];
   int i, j;
   for (i=0,j=0; i<attributes.length; i++)
   	if (attributeStatus[i] == 0){   	
   		Attributes[j] = attributes[i];
   		j++;
    }   	
   return Attributes;   
 }

 public float[][] getSelectedPoints(){
   int np = points.length; //number of points
   int ns = numberOfSelectedAttributes(); //number of selected attributes
   int na = attributes.length; //number of attributes
   float sPoints[][] = new float[np][ns];
   int i,j,k;
   for (i=0; i<np; i++){
     for (j=0, k=0; j<na; j++){
       if (attributeStatus[j] == 1){
          sPoints[i][k] = points[i][j];
          k++;
       } 
     }
   }    

   return sPoints;
 }

 public void savePointsToFile(String filename, String separator){

 }

 public void saveSelectedPointsToFile(String filename, String separator){
   float sPoints[][] = getSelectedPoints();

 }

 public int numberOfSelectedAttributes(){
  int sum = 0;
  for (int i=0; i<attributeStatus.length;i++)
  	if (attributeStatus[i] == 1)
  	  sum++;
  return sum;
 }
 
 public int numberOfDiscardedAttributes(){
  int sum = 0;
  for (int i=0; i<attributeStatus.length;i++)
  	if (attributeStatus[i] == 0)
  	  sum++;
  return sum;
 } 
 
 public int numberOfAttributes()
 {
   return attributeStatus.length;
 }

/*
public static void exibe(float m[][]){
	for (int i=0; i<m.length;i++){
		for (int j=0; j<m[i].length;j++){
			System.out.print(m[i][j] + " ");
		}
		System.out.println();
	}
		
}

public static void main(String args[]){
	float m[][] = new float[3][4];
	m[0][1] = 1.0f;
	m[1][1] = 2.0f;
	m[2][1] = 3.0f;
	m[1][3] = 1.0f;
	String at[] = new String[4];
	at[0] = "att1";
	at[1] = "att2";
	at[2] = "att3";
	at[3] = "class";
	PointsSelection p = new PointsSelection(m,at);
	for (int i=0; i<at.length; i++)
		System.out.print(at[i] + " ");
	System.out.println();	
	exibe(m);
	System.out.println(p.numberOfSelectedAttributes());
	p.discardAttribute("class");	
	System.out.println(p.numberOfSelectedAttributes());
	String atA[] = p.getSelectedAttributes();
	//p.selectAttribute("class");
	//System.out.println(p.numberOfSelectedAttributes());
	//String atA[] = p.getSelectedAttributes();
	for (int i=0; i<atA.length; i++)
		System.out.print(atA[i] + " ");
	System.out.println();	
	float mA[][] = p.getSelectedPoints();
	exibe(mA);	
	System.out.println();		
}*/
}
