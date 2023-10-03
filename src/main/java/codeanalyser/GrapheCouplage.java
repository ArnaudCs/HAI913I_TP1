package codeanalyser;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheCouplage {
	
	private static Map<String, Map<String, List<String>>> graphCouplage;
	
//	public static void makeGraphCouplage(File folder) {
//		graphCouplage = new HashMap<String, Map<String, List<String>>>();
//		
//		for (File fileEntry : javaFiles) {
//			String content;
//			try {
//				// Récupération du contenu du fichier et parsing
//				content = FileUtils.readFileToString(fileEntry);
//				CompilationUnit parse = Parser.parse(content.toCharArray());
//				
//				callGraph.putAll(buildCallGraph(parse));
//				
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
