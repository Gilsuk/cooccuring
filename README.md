# simple-co-occuring

엑셀 파일에서 한 행씩 읽어 형태소분석기(KOMORAN)로 명사를 추출하여 같은 문장안에 들어있는 단어들의 빈도수를 세어 in-memory DB에 기록하고 엑셀파일로 출력

## 개발툴

JAVA, SQLite, KOMORAN

## erd

![db-scheme](http://drive.google.com/uc?export=view&id=1JbY85CyNTvr5DaXxdqMWmMvLzBJzJ1OU)

## 사용법

```bash
java -jar cooccurring.jar <input.xlsx> <output.xlsx>
```
