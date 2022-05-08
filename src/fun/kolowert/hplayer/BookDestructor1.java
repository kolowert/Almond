package fun.kolowert.hplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import fun.kolowert.serv.FileHand;

public class BookDestructor1 implements BookDestructor {

	private static final String PREPATH = "resources/book/";
	private final List<String> lines;

	public BookDestructor1(String bookName) {
		lines = new FileHand(PREPATH + bookName).read();
	}

	@Override
	public List<String> destructToLines() {
		return lines;
	}

	@Override
	public List<String> destructToWordSet(boolean letSort) {
		Set<String> wordSet = new HashSet<>();
		for (String line : lines) {
			String filtredLine = line.replaceAll("[^a-zA-Z]", " ");
			String[] words = filtredLine.split(" ");
			for (String word : words) {
				if (word.length() > 3) {
					wordSet.add(word);
				}
			}
		}
		List<String> result = new ArrayList<>();
		CollectionUtils.addAll(result, wordSet);
		if (letSort)
			Collections.sort(result);
		return result;
	}

	// debugging
	public static void main(String[] args) {
		BookDestructor bookDest = new BookDestructor1("book.txt");
		// List<String> destructed = bookDest.destructToLines();
		// System.out.println(destructed);
		// System.out.println(destructed.size());

		List<String> wordSet = bookDest.destructToWordSet(true);
		System.out.println(wordSet);
		System.out.println(wordSet.size());
	}

}
