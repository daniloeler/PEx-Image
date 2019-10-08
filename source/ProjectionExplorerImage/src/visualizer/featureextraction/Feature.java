/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.featureextraction;

/**
 *
 * @author Danilo
 */
import ij.ImagePlus;
        
abstract public class Feature {
	/** O objeto � protected, mas poderia ser public tamb�m, isso porque vou extender em cada um dos Extratores */
	protected double featuresExtractor[];
	protected String featuresNames[];

	/** M�todo que ser� abstra�do em cada extrator implementado */
	abstract double[] extract(ImagePlus imp);

	/** Retorna um vetor com o nome das caracter�sticas referente a tal classe */
	public String[] getNames() {
		return featuresNames;
	}
}

