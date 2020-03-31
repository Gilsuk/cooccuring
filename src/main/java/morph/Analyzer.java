package morph;

import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

public class Analyzer {
	
	private final Komoran komoran;
	
	public Analyzer() {
		this.komoran = new Komoran(DEFAULT_MODEL.FULL);
	}
	
	public List<String> getNouns(String sentence) {
		KomoranResult analyzed = komoran.analyze(sentence);
		return analyzed.getNouns();
	}

}
