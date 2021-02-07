package com.gridone.scraping.wordcloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.core.io.ClassPathResource;

import com.gridone.scraping.morpheme.MorphemeAnalysis;

import java.util.PriorityQueue;
import java.util.Scanner;

public class StringProcessor implements Iterable<WordCount> {

	private String str;
    private final HashSet<String> filter;
    private ArrayList<WordCount> words;
    
    public ArrayList<WordCount> getWords() {
		return words;
	}

	public void setWords(ArrayList<WordCount> words) {
		this.words = words;
	}

	public StringProcessor(String str, HashSet<String> filter) {
        this.str = str;
        this.filter = filter;
        processMorphemeString();
    }
    
    public StringProcessor(String str) {
        this.str = str;
        this.filter = new HashSet<String>();
        ClassPathResource resource = new ClassPathResource("filter/korean_filtering.txt");
		
		try {
			File file = resource.getFile();
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF8"));
			String line = "";
			int i = 0;
			while ((line = br.readLine()) != null) {
				filter.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        processString();
    }
    
    private void processMorphemeString() {
    	MorphemeAnalysis ma = new MorphemeAnalysis(str);
    	ma.extractor();
    	words = (ArrayList<WordCount>) ma.getWordList();
    }
    
    private void processString() {
        Scanner scan = new Scanner(str);
        HashMap<String, Integer> count = new HashMap<String, Integer>();
        while (scan.hasNext()) {
            String word = removePunctuations(scan.next());
            if (filter.contains(word)) continue;
            if (word.equals("")) continue;
            Integer n = count.get(word);
            count.put(word, (n == null) ? 1 : n + 1);
        }
        
        // PriorityQueue<T> 우선순위 큐
        // T에 따른 우선순위로 호출됨, 기본생성자 : 우선순위 낮은 순, 생성자에 Collections.reverseOrder() 넣으면 우선순위 높은 순
        // T가 VO일 경우 VO에 compareTo(implements Comparable) 비교 정의의 높은 순
        PriorityQueue<WordCount> pq = new PriorityQueue<WordCount>(); 
        
        for (Entry<String, Integer> entry : count.entrySet()) {
            pq.add(new WordCount(entry.getKey(), entry.getValue()));
        }
        words = new ArrayList<WordCount>();
        while (!pq.isEmpty()) {
            WordCount wc = pq.poll(); // 첫번째 값을 반환하고 제거 비어있다면 null
            if (wc.word.length() > 1) words.add(wc);
        }
    }
    
    public void print() {
        Iterator<WordCount> iter = iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
	
	@Override
	public Iterator<WordCount> iterator() {
		// TODO Auto-generated method stub
		return words.iterator();
	}
	
	private static String removePunctuations(String str) {
        return str.replaceAll("\\p{Punct}|\\p{Digit}", "");
    }

	public ArrayList<WordCount> getTopFileWords() {
		ArrayList<WordCount> result = null;
		
		if(words != null && words.size() > 5) {
			result = new ArrayList<WordCount>(words.subList(0, 5));
		}else {
			result = (ArrayList<WordCount>)words.clone();
		}
		return result;
	}
	
	public ArrayList<WordCount> getLowFileWords() {
		ArrayList<WordCount> result = (ArrayList<WordCount>)words.clone();
		
		Collections.reverse(words);
		
		if(result != null && result.size() > 5) {
			result = new ArrayList<WordCount>(words.subList(0, 5));
		}
		return result;
	}
	
}
