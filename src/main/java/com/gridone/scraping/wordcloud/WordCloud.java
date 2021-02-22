package com.gridone.scraping.wordcloud;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.gridone.scraping.morpheme.MorphemeAnalysis;

public class WordCloud {
	
    public static final int WIDTH = 2400;
    public static final int HEIGHT = 1600;
    public static final int PADDING = 30;

    public static String getImageWordCloud(MorphemeAnalysis strProcessor) {
    	CloudImageGenerator generator = new CloudImageGenerator(WIDTH, HEIGHT, PADDING);
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	try {			
    		BufferedImage bImage = generator.generateImage(strProcessor, System.currentTimeMillis());
    		ImageIO.write(bImage, "png", bos);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	Encoder encoder = Base64.getEncoder();
    	String data = DatatypeConverter.printBase64Binary(bos.toByteArray());
    	return data;
    }
    
    public static String getImageWordCloud(StringProcessor strProcessor) {
    	CloudImageGenerator generator = new CloudImageGenerator(WIDTH, HEIGHT, PADDING);
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	try {			
    		BufferedImage bImage = generator.generateImage(strProcessor, System.currentTimeMillis());
    		ImageIO.write(bImage, "png", bos);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	Encoder encoder = Base64.getEncoder();
    	String data = DatatypeConverter.printBase64Binary(bos.toByteArray());
//    	return encoder.encode(bos.toByteArray());
    	return data;
    }
    
    
//  public static void main(String[] args) throws IOException {
//  JFrame frame = new JFrame("Word Cloud");
//  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//  frame.setLocationByPlatform(true);
//  frame.pack();
//  Insets insets = frame.getInsets();
//  frame.setSize(calcScreenSize(insets));
//  StringProcessor strProcessor = new StringProcessor(readFile(TEXT), filteringList(FILTER));
//  CloudImageGenerator generator = new CloudImageGenerator(WIDTH, HEIGHT, PADDING);
//  BufferedImage a = generator.generateImage(strProcessor, System.currentTimeMillis());
//  ByteArrayOutputStream bos = new ByteArrayOutputStream();
//  ImageIO.write(a, "png", bos);
//  Base64Encoder
//  frame.setContentPane(new CloudViewer(generator.generateImage(strProcessor, System.currentTimeMillis())));
//  frame.setVisible(true);
//}
   
}
