/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.projection.representative;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import visualizer.matrix.Matrix;
import visualizer.view.color.ColorScale;
import visualizer.view.color.RainbowScale;

/**
 *
 * @author wilson
 */
public class BoxplotRepresentative implements RepresentativeGenerator {
    private List<BoxplotDataGenerator> boxplotDataGenerator;
    private final int NUM_COLS = 2;
    private AnalysisType aType;
    
    public BoxplotRepresentative(List<BoxplotDataGenerator> boxplotDataGenerator, AnalysisType aType) {
        this.boxplotDataGenerator = boxplotDataGenerator;
        this.aType = aType;
    }
    
    private BoxAndWhiskerCategoryDataset createDataSet(BoxplotDataGenerator generator,
            Matrix selected, Matrix projection) {        
        try {
            DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();    

            float[] vGlobal = null;
            float[] vLocal = null;
          
            if( aType.equals(AnalysisType.BOTH) ) {                
                vGlobal = generator.generateGlobalAnalysis(selected, projection);
                vLocal = generator.generateLocalAnalysis(selected);            
                addValues(vLocal, generator.toString(), "Local Analysis", dataset);                
                addValues(vGlobal, generator.toString(), "Global Analysis", dataset);                
            } else if( aType.equals(AnalysisType.LOCAL) ) {
                vLocal = generator.generateLocalAnalysis(selected);            
                addValues(vLocal, "", generator.toString(), dataset);  
            } else if( aType.equals(AnalysisType.GLOBAL) ) {
                vGlobal = generator.generateGlobalAnalysis(selected, projection);
                addValues(vGlobal, "", generator.toString(), dataset);            
            }
            
            return dataset;
        } catch( IOException e ) {
            throw new RuntimeException(e);
        }        
    } 

    private void addValues(float[] v, String rowKey, String colKey, DefaultBoxAndWhiskerCategoryDataset dataset) {
        List<Double> list =  new ArrayList<>();
        for( int j = 0; j < v.length; ++j )
            list.add((double)v[j]);
        dataset.add(list, rowKey, colKey);
    }
    
    public JPanel getSmallMultiples(Matrix selected, Matrix projection) {
        
        int n = boxplotDataGenerator.size() % 2 == 0 ? boxplotDataGenerator.size() : boxplotDataGenerator.size()+1;
        
        SmallMultiplesPanel smallMultiples = new SmallMultiplesPanel(n/NUM_COLS, NUM_COLS);
        ColorScale scn = new RainbowScale();        
        
        for( int i = 0; i < boxplotDataGenerator.size(); ++i ) {
            BoxAndWhiskerCategoryDataset dataset = createDataSet(boxplotDataGenerator.get(i), selected, projection);
            CategoryAxis xAxis = new CategoryAxis("");
            NumberAxis yAxis = new NumberAxis("");
            BoxAndWhiskerRenderer renderer = new CustomBoxAndWhiskerRenderer();

            
            Font legendFont = new Font("SansSerif", Font.PLAIN, 16);
            renderer.setLegendTextFont(0, legendFont);
            renderer.setLegendTextFont(1, legendFont);
            
            renderer.setFillBox(true);          
            renderer.setUseOutlinePaintForWhiskers(true);   
            renderer.setMedianVisible(true);
            renderer.setMeanVisible(false);
            
            if( !aType.equals(AnalysisType.BOTH) )
                renderer.setBaseSeriesVisibleInLegend(false);
            
            
            Color c = scn.getColor((i+1)*(60/255.f));
            renderer.setSeriesOutlinePaint(0, new Color(0, 0, 0));
            renderer.setSeriesOutlineStroke(0, new BasicStroke(1.0f)); 
            renderer.setSeriesPaint(0, new Color(c.getRed(), c.getGreen(), c.getBlue(), 200));
            renderer.setMaximumBarWidth(0.20);            
            renderer.setWhiskerWidth(1.0);
            
            ((CustomBoxAndWhiskerRenderer)renderer).setOutlierVisible(false);
            
            yAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 15));
            xAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD|Font.ITALIC, 15));
            
            CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
            plot.setRangeGridlinesVisible(true);
            
            JFreeChart chart = new JFreeChart(plot);
            
            ChartPanel panel = new ChartPanel(chart);  
            panel.setMouseWheelEnabled(true);
        
            smallMultiples.add(panel);
        }
                
        return smallMultiples;
    }
    
    
    @Override
    public Image getRepresentative(Matrix selected, Matrix projection) {
        return ((SmallMultiplesPanel)getSmallMultiples(selected, projection)).getImage();        
    }
    
    
    public class SmallMultiplesPanel extends JPanel {
        
        public SmallMultiplesPanel(int rows, int cols) {
            super(new GridLayout(rows, cols));
            setSize(320, 290);
        }
        
        public Image getImage() {
            layoutRecursively(this);
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            paint(image.getGraphics());
            
            return image;
        }
        
        public void saveImage() {
            layoutRecursively(this);
            BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            paint(bi.getGraphics());
            File file = new File("test.png");
            try {
                ImageIO.write(bi, "png", file);                
            } catch( IOException e ) {
                throw new RuntimeException(e);
            }
        }       
        
        private void layoutRecursively(Component component) {
            component.doLayout();
            if (component instanceof Container) {
                Container container = (Container) component;
                for (int i = 0; i < container.getComponentCount(); i++) {
                    layoutRecursively(container.getComponent(i));
                }
            }
        }
    }
    
}
