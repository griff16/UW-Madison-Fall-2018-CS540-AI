package HW6;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.FileNotFoundException;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;


public class Corpus {

	public static void main(String[] args) throws FileNotFoundException {
		File dir = new File("D:\\Computer science java files\\CS540\\news");
		ArrayList<String> tokens = new ArrayList<String>();
		File[] files = dir.listFiles();
		ArrayList<String> types = new ArrayList<String>();
		
		for (File file : files) {
			Scanner text = new Scanner(file);
				while (text.hasNext()) {
					String token = text.next();
					token = token.trim();
					tokens.add(token);
			}
		
			text.close();
		}
		System.out.println("tokens: " + tokens.size());
		
		for (String token : tokens) {
			if (!types.contains(token)) {
				types.add(token);
			}
		}
		
		System.out.println("types: " + types.size());
		
		
		
		Map<String, Integer> frequencymap = tokens.stream().collect(toMap (s -> s, s -> 1, Integer:: sum));
		
		List<String> top = tokens.stream().sorted(comparing(frequencymap::get).reversed()).distinct().limit(300).collect(toList());
		
		System.out.println(top);
		
		Set<String> unique = new HashSet<String>(top);
		for (String token : unique) {
			Collections.sort(top);
			System.out.println(token + ": " + Collections.frequency(tokens, token));
		}
	}
}
