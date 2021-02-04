package com.gridone.scraping.morpheme;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import org.snu.ids.ha.index.Keyword;
import org.snu.ids.ha.index.KeywordExtractor;
import org.snu.ids.ha.index.KeywordList;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;

import com.gridone.scraping.wordcloud.WordCount;

// http://kkma.snu.ac.kr/documents/index.jsp
public class MorphemeAnalysis {
	
	private List<WordCount> wordList;

	public void analysisString() {
		// string to analyze
		System.err.println("start analysis");
		String string = "[패션뷰티 디지털혁신] 1위 도약한 LG생활건강, 디지털  GS칼텍스 내재화 나선다";

		try {
			// init MorphemeAnalyzer
			MorphemeAnalyzer ma = new MorphemeAnalyzer();

			// create logger, null then System.out is set as a default logger
			ma.createLogger(null);

			// analyze morpheme without any post processing 
			List<MExpression> ret = ma.analyze(string);

			// refine spacing
			ret = ma.postProcess(ret);

			// leave the best analyzed result
			ret = ma.leaveJustBest(ret);

			// divide result to setences
			List<Sentence> stl = ma.divideToSentences(ret);

			// print the result
			for( int i = 0; i < stl.size(); i++ ) {
				Sentence st = stl.get(i);
				System.out.println("===>  " + st.getSentence());
				for( int j = 0; j < st.size(); j++ ) {
					System.out.println(st.get(j));
				}
			}

			ma.closeLogger();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void extractor() {
		// string to extract keywords
		String strToExtrtKwrd = "[패션뷰티 디지털혁신] 1위 도약한 LG생활건강, 디지털  GS칼텍스 내재화 나선다";

		// init KeywordExtractor
		KeywordExtractor ke = new KeywordExtractor();

		// extract keywords
		KeywordList kl = ke.extractKeyword(strToExtrtKwrd, true);

		// print result
		PriorityQueue<WordCount> pq = new PriorityQueue<WordCount>(); 
		for( int i = 0; i < kl.size(); i++ ) {
			Keyword kwrd = kl.get(i);
//			System.out.println(kwrd.getString() + "\t" + kwrd.getCnt());
			pq.add(new WordCount(kwrd.getString(), kwrd.getCnt()));
		}
		wordList = new ArrayList<>();
		while (!pq.isEmpty()) {
            WordCount wc = pq.poll(); // 첫번째 값을 반환하고 제거 비어있다면 null
            if (wc.word.length() > 1) wordList.add(wc);
        }
		System.out.println("wordList : "+wordList);
	}
	
}
