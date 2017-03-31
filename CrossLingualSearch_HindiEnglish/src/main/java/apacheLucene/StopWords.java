package apacheLucene;

import java.util.ArrayList;
import java.util.List;

public class StopWords {
	public List<String> addWords() {
		List<String> wordList = new ArrayList<String>();
		wordList.add("i");
		wordList.add("me");
		wordList.add("my");
		wordList.add("myself");
		wordList.add("we");
		wordList.add("us");
		wordList.add("our");
		wordList.add("ours");
		wordList.add("ourselves");
		wordList.add("you");
		wordList.add("your");
		wordList.add("yours");
		wordList.add("yourself");
		wordList.add("yourselves");
		wordList.add("he");
		wordList.add("him");
		wordList.add("his");
		wordList.add("himself");
		wordList.add("it");
		wordList.add("its");
		wordList.add("itself");
		wordList.add("they");
		wordList.add("them");
		wordList.add("their");
		wordList.add("theirs");
		wordList.add("themselves");
		wordList.add("what");
		wordList.add("which");
		wordList.add("who");
		wordList.add("whom");
		wordList.add("this");
		wordList.add("that");
		wordList.add("these");
		wordList.add("am");
		wordList.add("is");
		wordList.add("are");
		wordList.add("was");
		wordList.add("were");
		wordList.add("be");
		wordList.add("been");
		wordList.add("being");
		wordList.add("have");
		wordList.add("has");
		wordList.add("had");
		wordList.add("having");
		wordList.add("do");
		wordList.add("does");
		wordList.add("did");
		wordList.add("doing");
		for (int i = 0; i < wordList.size(); i++) {
			wordList.set(i, wordList.get(i).toLowerCase());
		}
		return wordList;
	}
}
