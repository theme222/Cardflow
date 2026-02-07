package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WordStorage {

	private static ArrayList<String> wordList;
	
	
	public static void init() {
		wordList = new ArrayList<String>();
		
		populateWordList();
		
	}
	
	public static void populateWordList() {
        try {
            InputStream inputStream = WordStorage.class.getClassLoader()
                    .getResourceAsStream("words.txt");

            if (inputStream == null) {
                throw new FileNotFoundException("words.txt not found in resources");
            }

            Scanner sc = new Scanner(inputStream);

            while (sc.hasNextLine()) {
                wordList.add(sc.nextLine());
            }
            sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isWordInList(String word) {
		if(word.length()!=5) {
			return false;
		}
		return wordList.contains(word);
	}
	
	public static String getRandomWord() {
		 int idx = new Random().nextInt(wordList.size());
		return wordList.get(idx);
	}
}
