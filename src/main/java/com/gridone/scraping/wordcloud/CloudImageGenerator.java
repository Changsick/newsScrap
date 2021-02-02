package com.gridone.scraping.wordcloud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @author epicdevs
 */
public class CloudImageGenerator {
    private static final int REJECT_COUNT = 100;
    private static final int LARGEST_FONT_SIZE = 160;
    private static final int FONT_STEP_SIZE = 5;
    private static final int MINIMUM_FONT_SIZE = 20;
    private static final int MINIMUM_WORD_COUNT = 0;
//    public static final String FONT_FAMILY = "나눔명조";
    public static final String FONT_FAMILY = "Helvetica";
    public static final String[] THEME = ColorCombinations.THEME1;
    
    private String fontFamily;
    private final int width;
    private final int height;
    private final int padding;
    private BufferedImage bi;
    private ColorCombinations colorTheme;
    private ArrayList<Shape> occupied = new ArrayList<Shape>();
    
    public CloudImageGenerator(int width, int height, int padding) {
        this.width = width;
        this.height = height;
        this.fontFamily = FONT_FAMILY;
        this.padding = padding;
    }

    /**
     * This algorithm can be fancier than this sloppy random generation algorithm
     * For more information: http://static.mrfeinberg.com/bv_ch03.pdf
     */
    public BufferedImage generateImage(Iterable<WordCount> words, long seed) {
        Random rand = new Random(seed);
        bi = new BufferedImage(width + 2 * padding, height + 2 * padding, BufferedImage.TYPE_INT_ARGB);
        colorTheme = new ColorCombinations(THEME);
        Graphics2D g = bi.createGraphics();
        /**
         * Graphics2D
         * 
         * 2D 도형, 텍스트 및 이미지를 렌더링하기 위한 기본 클래스입니다.
         * Graphics2D 객체에게 건네지는 모든 좌표는 사용자 공간 (어플리케이션에 의해 사용된다)으로 불리는 디바이스에 존하지 않는 좌표계로 지정됩니다. 
         * Graphics2D 객체에는 디바이스 공간에서 사용자 공간의 좌표를 디바이스에 의존된 좌표로 변환하는 방법을 정의하는 AffineTransform 객체가 렌더링 상태의 일부로서 포함됩니다.
         * 
         * Graphics2D는 텍스트, 이미지를 랜더링하는 클래스고
         * 랜더링 조작에는 Shape 윤곽, 텍스트, 이미지 조작 등이 있음, 여기서는 크기지정 생성한 이미지에 텍스트 그리는 랜더링 조작으로 쓰인듯싶다.
         */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(colorTheme.background());
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.translate(padding, padding);
        
        Iterator<WordCount> iter = words.iterator();
        int k = LARGEST_FONT_SIZE;
        while (iter.hasNext()) {
            WordCount wc = iter.next();
//            System.out.println("wc : "+wc);
            if (wc.n < MINIMUM_WORD_COUNT) break; // 최소지정 빈도횟수보다 작으면 뺌
            int prevK = k; // 과거k 저장
            
            // iter는 우선순위 큐에의해 빈도가 높은 친구부터 나옴 즉 빈도수 높은 단어부터 폰트사이즈가 LARGEST_FONT_SIZE 부터 -FONT_STEP_SIZE 까지 작아짐
            if (k > MINIMUM_FONT_SIZE) k = k - FONT_STEP_SIZE;  
            Font font = new Font(fontFamily, Font.BOLD, k);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            Shape s = stringShape(font, fm, wc.word, rand); // 폰트 사이즈에 맞는 문자 영역
            boolean fitted = false; // 안겹치는 영역찾았다는 플래그
            for (int i = 0; i < REJECT_COUNT * wc.n; i++) { // 안겹치는 영역찾는 횟수만큼 반복
                s = stringShape(font, fm, wc.word, rand); // 해당 문자의 모양
                if (!collision(s.getBounds())) { // 문자모양에 따른 직사각형 영역이 occupied(기존 그려진 영역)과 교차하지 않는다면(겹치지 않는다면) 패스
                    fitted = true;
                    break;
                }
            }
            if (!fitted) { // 횟수만큼 안겹치는 영역 못찾았으면 다음꺼진행
                k = prevK; // k는 전걸로 초기화
                continue;
            }
            g.setColor(colorTheme.next());
            g.fill(s);
            occupied.add(s); // 해당 영역 배열에 저장
        }
        return bi;
    }
    
    private boolean collision(Rectangle area) { // 글자모양의 직사각형 영역이 occupied(이미 그려진 영역배열)과 교차영역 있으면 true
        for (Shape shape : occupied) {
            if (shape.intersects(area)) return true;
        }
        return false;
    }
    
    private Shape stringShape(Font font, FontMetrics fm, String word, Random rand) {
    	// 문자열을 보여주기 위해 필요한 총 너비를 계산하여 리턴 : Graphics2D에 적용된 폰트 크기만큼 영향을받아서 x좌표 계산하는데 음수가 나올 수 있어 요류가 발생할 수 있음 => 처리로직 추가필
        int strWidth = fm.stringWidth(word); 
        int strHeight = fm.getAscent(); // 기준선에서 글자 상단까지의 길이
        System.out.println("strWidth : "+strWidth);
        System.out.println("width : "+width);
        
        int x = rand.nextInt(width - strWidth); // 지정 width에서 지정표시 문자의 크기
        int y = rand.nextInt(height- strHeight) + strHeight;
        // GlyphVector 단일의 Glyph(글리프)의 정보를 나타냄 : 글리프(glyph)란, 1 kr 또는 복수의 캐릭터의 시각적인 표현, 
        // font에 의해 생성되며 해당 font의 특정 그래프를 적용가능
        // 유효폭, 시각 경계 및 좌측 상대 위치와 우측 상대 위치의 컴퍼넌트가 있음
        // 따라서 이건 그릴 문자(복수의 캐릭터)를 시각적 표현 즉 그림처럼 만들어 준다는거같음
        GlyphVector v = font.createGlyphVector(fm.getFontRenderContext(), word);
        
        // 좌표의 평행 이동, 회전, 스케일링(확대/축소), 반전(flip), 변형(shearing)의 좌표 변환을 구현한다. 이러한 좌표 변환은 3 × 3 행렬로 표현되고 이 행렬과 좌표 (x, y)를 곱하여 변형된 좌표 (x', y')를 구한다.
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        v.setGlyphTransform(0, at);
        return v.getOutline();
    }
    
    public void setColorTheme(String[] colorCodes, Color background) {
        colorTheme = new ColorCombinations(colorCodes, background);
    }
    
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
}
