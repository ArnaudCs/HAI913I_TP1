package codeanalyser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class CodeAnalyser {
	
	public static void runAllStats(File folder) {
		// Récupération des fichiers du projet
		ArrayList<File> javaFiles = Parser.listJavaFilesForFolder(folder);
		List<String> projectClasses = new ArrayList<String>();
		int projectLinesOfCode = 0;
		List<String> projectMethods = new ArrayList<String>();
		List<String> projectPackages = new ArrayList<String>();
		Map<String, List<String>> methodsByClassMap = new HashMap<String, List<String>>();
		Map<String, Integer> linesByMethodsMap = new HashMap<String, Integer>();
		Map<String, List<String>> attributesByClassMap = new HashMap<String, List<String>>();
		List<String> topClassByMethods = new ArrayList<String>();
		List<String> topClassByAttributes = new ArrayList<String>();
		Map<String, Integer> topMethodsByLines = new HashMap<String, Integer>();
		Map<String, List<String>> parametersByMethodsMap = new HashMap<String, List<String>>();
		
		// Loop sur chaque fichier
		for (File fileEntry : javaFiles) {
			String content;
			try {
				// Récupération du contenu du fichier et parsing
				content = FileUtils.readFileToString(fileEntry);
				CompilationUnit parse = Parser.parse(content.toCharArray());
				
				// Calcul nombre ligne de code
				projectLinesOfCode += content.split("\r\n|\r|\n").length; 
				
				projectClasses.addAll(printClassInfo(parse));
				projectMethods.addAll(printMethodsInfo(parse));
				projectPackages.addAll(printPackagesInfo(parse));
				methodsByClassMap.putAll(methodsByClass(parse));
				linesByMethodsMap.putAll(linesByMethods(parse));
				attributesByClassMap.putAll(attributesByClass(parse));
				parametersByMethodsMap.putAll(parametersByMethods(parse));
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		topClassByMethods = topClasses(methodsByClassMap);
		topClassByAttributes = topClasses(attributesByClassMap);
		topMethodsByLines = topMethodsByLines(linesByMethodsMap);
		List<String> commonTopClasses = new ArrayList<String>();
	    
	    for (String element : topClassByMethods) {
	        if (topClassByAttributes.contains(element)) {
	        	commonTopClasses.add(element);
	        }
	    }
		
		displayListString("classe", projectClasses);
		displayNumber("ligne de codes", projectLinesOfCode);
		displayListString("méthode", projectMethods);
		Set<String> uniquePackagesSet = new HashSet<String>(projectPackages);
		List<String> uniquePackagesList = new ArrayList<String>(uniquePackagesSet);
		displayListString("package", uniquePackagesList);
		displayListStringWithin("méthodes", "classes", methodsByClassMap);
		displayNumberWithin("lignes de codes", "méthodes", linesByMethodsMap);
		displayListStringWithin("attributs", "classe", attributesByClassMap);
		displayBestObjects("classes", "méthodes", topClassByMethods);
		displayBestObjects("classes", "attributs", topClassByAttributes);
		displayBestObjects("classes", "méthodes et d'attributs", commonTopClasses);
		displayBestObjects("classes", "2 méthodes", moreThanXMethods(methodsByClassMap, 2));
		displayBestObjects("méthodes", "lignes de codes", new ArrayList<>(topMethodsByLines.keySet()));
		displayListStringWithin("paramètres", "méthodes", parametersByMethodsMap);
	}
	
	public static void displayNumber(String nomObjet, float number) {
		System.out.println("Nombre de "+ nomObjet +": "+ number);
	}
	
	public static void displayListString(String nomObjet, List<String> listObjet) {
		System.out.println("Nombre de "+ nomObjet +": "+ listObjet.size());
		for (int i = 0; i < listObjet.size(); i++) {
			System.out.println("-> Nom "+nomObjet+": "+listObjet.get(i));			
		}
	}
	
	public static void displayListStringWithin(String nomObjet, String nomContainer, Map<String, List<String>> mapObjet) {
	    System.out.println("Nombre de " + nomObjet + " par " + nomContainer + ": ");

	    int totalSize = 0;
	    for (Map.Entry<String, List<String>> entry : mapObjet.entrySet()) {
	        String key = entry.getKey();
	        List<String> listObjet = entry.getValue();

	        int size = listObjet.size();
	        totalSize += size;

	        System.out.println("-> "+key + ": " + size + " "+ nomObjet);
	        for (String string : listObjet) {
				System.out.println("\t -> "+string);
			}
	    }

	    if (!mapObjet.isEmpty()) {
	        double averageSize = (double) totalSize / mapObjet.size();
	        System.out.println("Nombre moyen de " + nomObjet + " par " + nomContainer + ": " + averageSize);
	    }
	}
	
	public static void displayNumberWithin(String nomObjet, String nomContainer, Map<String, Integer> mapObjet) {
	    System.out.println("Nombre de " + nomObjet + " par " + nomContainer + ": ");

	    int totalSize = 0;
	    for (Map.Entry<String, Integer> entry : mapObjet.entrySet()) {
	        String key = entry.getKey();
	        int value = entry.getValue();

	        totalSize += value;

	        System.out.println("-> " + key + ": " + value + " " + nomObjet);
	    }

	    if (!mapObjet.isEmpty()) {
	        double averageSize = (double) totalSize / mapObjet.size();
	        System.out.println("Nombre moyen de " + nomObjet + " par " + nomContainer + ": " + averageSize);
	    }
	}
	
	public static void displayBestObjects(String nomObjet, String comparateur, List<String> listeObjet) {
		System.out.println("Liste des " + nomObjet + " avec le plus de " + comparateur+ ": ");
		for (int i = 0; i < listeObjet.size(); i++) {
			System.out.println("-> "+ listeObjet.get(i));
		}
	}


	
	// navigate classes declaration
	public static List<String> printClassInfo(CompilationUnit parse) {

		ClassDeclarationVisitor visitor1 = new ClassDeclarationVisitor();
		parse.accept(visitor1);
		List<String> classList = new ArrayList<String>();
		for (TypeDeclaration classe : visitor1.getClasses()) {
			classList.add(classe.getName().toString());
		}
		return classList; 
	}
	
	// navigate methods declaration
	public static List<String> printMethodsInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		List<String> methodsList = new ArrayList<String>();
		for (MethodDeclaration methode : visitor1.getMethods()) {
			methodsList.add(methode.getName().toString());
		}
		return methodsList; 
	}
	
	// navigate packages declaration
	public static List<String> printPackagesInfo(CompilationUnit parse) {

		PackageDeclarationVisitor visitor1 = new PackageDeclarationVisitor();
		parse.accept(visitor1);
		List<String> packageList = new ArrayList<String>();
		for (PackageDeclaration paquetage : visitor1.getPackages()) {
			packageList.add(paquetage.getName().toString());
		}
		return packageList; 
	}
	
	// Methods by classes
	public static Map<String, List<String>> methodsByClass(CompilationUnit parse) {
	    ClassDeclarationVisitor visitor1 = new ClassDeclarationVisitor();
	    parse.accept(visitor1);
	    Map<String, List<String>> methodsByClassMap = new HashMap<String, List<String>>();

	    for (TypeDeclaration classe : visitor1.getClasses()) {
	        List<String> methodNames = new ArrayList<String>();

	        for (MethodDeclaration methodDeclaration : classe.getMethods()) {
	            methodNames.add(methodDeclaration.getName().getIdentifier());
	        }

	        methodsByClassMap.put(classe.getName().toString(), methodNames);
	    }

	    return methodsByClassMap;
	}
	
	// Methods by classes
	public static Map<String, Integer> linesByMethods(CompilationUnit parse) {
	    MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
	    parse.accept(visitor1);
	    Map<String, Integer> linesByMethodsMap = new HashMap<String, Integer>();

	    for (MethodDeclaration method : visitor1.getMethods()) {
	        Block methodBody = method.getBody();
	        if (methodBody != null) {
	            int startLine = parse.getLineNumber(methodBody.getStartPosition());
	            int endLine = parse.getLineNumber(methodBody.getStartPosition() + methodBody.getLength());

	            int numberOfLines = endLine - startLine + 1;

	            linesByMethodsMap.put(method.getName().toString(), numberOfLines);
	        }
	    }

	    return linesByMethodsMap;
	}
	
	// Attributes by classes
	public static Map<String, List<String>> attributesByClass(CompilationUnit parse) {
	    ClassDeclarationVisitor visitor1 = new ClassDeclarationVisitor();
	    parse.accept(visitor1);
	    Map<String, List<String>> attributesByClassMap = new HashMap<String, List<String>>();

	    for (TypeDeclaration classe : visitor1.getClasses()) {
	        FieldDeclaration[] fields = classe.getFields();
	        List<String> attributeNames = new ArrayList<String>();

	        for (FieldDeclaration field : fields) {
	            for (Object obj : field.fragments()) {
	                if (obj instanceof VariableDeclarationFragment) {
	                    VariableDeclarationFragment var = (VariableDeclarationFragment) obj;
	                    attributeNames.add(var.getName().getIdentifier());
	                }
	            }
	        }

	        attributesByClassMap.put(classe.getName().toString(), attributeNames);
	    }

	    return attributesByClassMap;
	}
	
	// Parameters by methods
		public static Map<String, List<String>> parametersByMethods(CompilationUnit parse) {
		    MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		    parse.accept(visitor1);
		    Map<String, List<String>> parametersByMethodsMap = new HashMap<String, List<String>>();

		    for (MethodDeclaration method : visitor1.getMethods()) {
		        List<String> parametersList = new ArrayList<>();
		        @SuppressWarnings("unchecked")
				List<SingleVariableDeclaration> parameters = method.parameters();
		        for (SingleVariableDeclaration parameter : parameters) {
		            parametersList.add(parameter.getName().getIdentifier());
		        }

		        parametersByMethodsMap.put(method.getName().getIdentifier(), parametersList);
		    }

		    return parametersByMethodsMap;
		}
	
	
	public static List<String> topClasses(Map<String, List<String>> methodsByClassMap) {
	    // Calculate the threshold for the top 10%
	    int totalClasses = methodsByClassMap.size();
	    int threshold = (int) Math.ceil(totalClasses * 0.1);

	    List<String> topClasses = new ArrayList<String>();

	    List<Map.Entry<String, List<String>>> sortedClasses = new ArrayList<Entry<String, List<String>>>(methodsByClassMap.entrySet());
	    Collections.sort(sortedClasses, new Comparator<Map.Entry<String, List<String>>>() {
	        public int compare(Map.Entry<String, List<String>> a, Map.Entry<String, List<String>> b) {
	            return Integer.compare(b.getValue().size(), a.getValue().size());
	        }
	    });

	    for (int i = 0; i < threshold && i < sortedClasses.size(); i++) {
	        topClasses.add(sortedClasses.get(i).getKey());
	    }

	    return topClasses;
	}
	
	public static Map<String, Integer> topMethodsByLines(Map<String, Integer> methodsAndLinesMap) {
	    Map<String, Integer> topMethods = new HashMap<>();

	    List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(methodsAndLinesMap.entrySet());
	    Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Integer>>() {
	        @Override
	        public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
	            return Integer.compare(b.getValue(), a.getValue());
	        }
	    });

	    int totalMethods = sortedEntries.size();
	    int threshold = (int) Math.ceil(totalMethods * 0.1);

	    for (int i = 0; i < threshold && i < sortedEntries.size(); i++) {
	        Map.Entry<String, Integer> entry = sortedEntries.get(i);
	        topMethods.put(entry.getKey(), entry.getValue());
	    }

	    return topMethods;
	}
	

	public static List<String> moreThanXMethods(Map<String, List<String>> methodsByClassMap, int threshold) {
	    List<String> classesWithMoreThanXMethods = new ArrayList<String>();

	    for (Map.Entry<String, List<String>> entry : methodsByClassMap.entrySet()) {
	        String className = entry.getKey();
	        List<String> methods = entry.getValue();

	        if (methods.size() > threshold) {
	            classesWithMoreThanXMethods.add(className);
	        }
	    }

	    return classesWithMoreThanXMethods;
	}




}
