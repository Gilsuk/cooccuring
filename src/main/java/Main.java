import java.util.Iterator;
import java.util.List;

import db.Creator;
import db.StoreKeeper;
import db.View;
import excel.Parser;
import excel.Writer;
import morph.Analyzer;

public class Main {

	public static void main(String[] args) {
		if (args.length != 2) return;
		
		Creator.createSchemes(); // 인메모리 DB 구조 생성
		
		Parser parser = new Parser(args[0]); // 인풋 .xlsx 파싱 객체 생성
		Iterator<String> rowIter = parser.iterator(); // 인풋 데이터를 한 줄씩 반환하는 반복자

		Analyzer analyzer = new Analyzer(); // 형태소 분석기
		StoreKeeper storeKeeper = StoreKeeper.getInstance(); // 분석결과의 insert를 도와주는 객체

		while(rowIter.hasNext()) { // 한 문장씩
			List<String> nouns = analyzer.getNouns(rowIter.next()); // 문장에서 명사를 뽑아내어
			storeKeeper.store(nouns); // DB에 저장
		}

		View view = new View(); // 분석 결과 조회 객체
		Writer writer = new Writer(args[1]); // 아웃풋 .xlsx 쓰기 객체
		
		view.forEachRemaining(x -> x.forEach((key, words) -> {
			writer.addRow(key, words); // 분석 결과를 한 건씩 row 데이터로 변환
		}));

		writer.out(); // 파일로 출력
	}
}
