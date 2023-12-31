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
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class CodeAnalyser {
	
	private List<String> projectClasses = new ArrayList<String>();
//	private ArrayList<File> javaFiles;
	private static int classNumber;
	private static int projectLinesOfCode;
	private static List<String> projectMethods;
	private static List<String> projectPackages;
	private static Map<String, List<String>> methodsByClassMap;
	private static Map<String, Integer> linesByMethodsMap;
	private static Map<String, List<String>> attributesByClassMap;
	private static List<String> topClassByMethods;
	private static List<String> topClassByAttributes;
	private static Map<String, Integer> topMethodsByLines;
	private static Map<String, List<String>> parametersByMethodsMap;
	private static Map<String, Map<String, Map<String, String>>> callGraph;
	private static String cmd;
	private static String graphCmd;
		
	public int getProjectClassesNumber() {
		return classNumber;
	}
	
	public List<String> getProjectClasses() {return projectClasses;}
	public double getAverageLinesPerMethod() {return averageSize(linesByMethodsMap);}
	public double getAveragMethodPerClass() {return averageSizeList(methodsByClassMap);}
	public double getAverageAttPerClass() {return averageSizeList(attributesByClassMap);}
	public double getTotalParametersPerMethod() {return averageSizeList(parametersByMethodsMap);}
	public int getProjectLinesOfCode() {return projectLinesOfCode;}
	public static List<String> getProjectMethods() {return projectMethods;}
	
	public int getProjectMethodsNumber() {
		if(projectMethods != null) {
			return projectMethods.size();
		}
		return -1;
	}
	
	public static List<String> getProjectPackages() {return projectPackages;}
	
	public int getProjectPackagesNumber() {
		if(projectPackages != null) {
			return projectPackages.size();
		}
		return -1;
	}
	
	public Map<String, Integer> getMethodAttributeClasses(){
		HashMap<String, Integer> commonClasses = new HashMap<String, Integer>();
        
        for (String className : topClassByMethods) {
            if (topClassByAttributes.contains(className)) {
                commonClasses.put(className, 1);
            }
        }
        return commonClasses;
	}
	
	public Map<String, Integer> getMethodsCountForTopClasses() {
	    Map<String, Integer> methodsCountMap = new HashMap<String, Integer>();

	    for (String topClassName : topClassByMethods) {
	        if (methodsByClassMap.containsKey(topClassName)) {
	            int methodCount = methodsByClassMap.get(topClassName).size();
	            methodsCountMap.put(topClassName, methodCount);
	            //Test methodsCountMap.put("Test", 4);
	        } else {
	            methodsCountMap.put(topClassName, 0);
	        }
	    }

	    return methodsCountMap;
	}
	
	public Map<String, Integer> getAttributesCountForTopClasses() {
	    Map<String, Integer> methodsCountMap = new HashMap<String, Integer>();

	    for (String topClassName : topClassByAttributes) {
	        if (attributesByClassMap.containsKey(topClassName)) {
	            int methodCount = attributesByClassMap.get(topClassName).size();
	            methodsCountMap.put(topClassName, methodCount);
	        } else {
	            methodsCountMap.put(topClassName, 0);
	        }
	    }
	    return methodsCountMap;
	}
	
	public Map<String, Integer> getLinesPerMethods(){return topMethodsByLines;}
	public static Map<String, List<String>> getMethodsByClassMap() {return methodsByClassMap;}
	public static Map<String, Integer> getLinesByMethodsMap() {return linesByMethodsMap;}
	public static Map<String, List<String>> getAttributesByClassMap() {return attributesByClassMap;}
	public static List<String> getTopClassByMethods() {return topClassByMethods;}
	public static List<String> getTopClassByAttributes() {return topClassByAttributes;}
	public static Map<String, Integer> getTopMethodsByLines() {return topMethodsByLines;}
	public static Map<String, List<String>> getParametersByMethodsMap() {return parametersByMethodsMap;}
	public String getCmd() {return cmd;}
	public String getGraphCmd() {return graphCmd;}

	public static void runAllStats(File folder) {
		// Récupération des fichiers du projet
		ArrayList<File> javaFiles = Parser.listJavaFilesForFolder(folder);
		List<String> projectClasses = new ArrayList<String>();
		projectLinesOfCode = 0;
		projectMethods = new ArrayList<String>();
		projectPackages = new ArrayList<String>();
		methodsByClassMap = new HashMap<String, List<String>>();
		linesByMethodsMap = new HashMap<String, Integer>();
		attributesByClassMap = new HashMap<String, List<String>>();
		topClassByMethods = new ArrayList<String>();
		topClassByAttributes = new ArrayList<String>();
		topMethodsByLines = new HashMap<String, Integer>();
		parametersByMethodsMap = new HashMap<String, List<String>>();
		callGraph = new HashMap<String, Map<String, Map<String, String>>>();
		
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
				callGraph.putAll(buildCallGraph(parse));
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
		displayNumber("ligne de codes du projet", projectLinesOfCode);
		displayListString("méthode du projet", projectMethods);
		Set<String> uniquePackagesSet = new HashSet<String>(projectPackages);
		List<String> uniquePackagesList = new ArrayList<String>(uniquePackagesSet);
		displayListString("package", uniquePackagesList);
		displayListStringWithin("méthodes", "classe", methodsByClassMap);
		displayNumberWithin("lignes de codes", "méthode", linesByMethodsMap);
		displayListStringWithin("attributs", "classe", attributesByClassMap);
		displayBestObjects("classes", "méthodes", topClassByMethods);
		displayBestObjects("classes", "attributs", topClassByAttributes);
		displayBestObjects("classes", "méthodes et d'attributs", commonTopClasses);
		displayBestObjects("classes", "2 méthodes", moreThanXMethods(methodsByClassMap, 2));
		displayBestObjects("méthodes", "lignes de codes", new ArrayList<String>(topMethodsByLines.keySet()));
		displayListStringWithin("paramètres", "méthodes", parametersByMethodsMap);
		System.out.println("\n\n");
		displayCallGraph(callGraph);
		classNumber = projectClasses.size();
	}

	public static void displayNumber(String nomObjet, float number) {
		System.out.println("Nombre de "+ nomObjet +": "+ number);
		cmd += "Nombre de "+ nomObjet +": "+ number + "\n";
	}
	
	public static void displayListString(String nomObjet, List<String> listObjet) {
		System.out.println("Nombre de "+ nomObjet +": "+ listObjet.size());
		for (int i = 0; i < listObjet.size(); i++) {
			System.out.println("-> Nom "+nomObjet+": "+listObjet.get(i));	
			cmd += "-> Nom "+nomObjet+": "+listObjet.get(i) + "\n";
		}
	}
	
	public static double averageSizeList(Map<String, List<String>> mapObjet) {
		int totalSize = 0;
	    for (Map.Entry<String, List<String>> entry : mapObjet.entrySet()) {
	        List<String> listObjet = entry.getValue();

	        int size = listObjet.size();
	        totalSize += size;
	    }

	    if (!mapObjet.isEmpty()) {
	        double averageSize = (double) totalSize / mapObjet.size();
	        return averageSize;
	    }
		return -1;
	}
	
	public static void displayListStringWithin(String nomObjet, String nomContainer, Map<String, List<String>> mapObjet) {
	    System.out.println("Liste des " + nomObjet + " par " + nomContainer + ": ");
	    int total = 0;
	    for (Map.Entry<String, List<String>> entry : mapObjet.entrySet()) {
	        String key = entry.getKey();
	        List<String> listObjet = entry.getValue();

	        int size = listObjet.size();
	        total += size;
	        System.out.println("-> "+key + ": " + size + " "+ nomObjet);
	        for (String string : listObjet) {
				System.out.println("\t -> "+string);
				cmd += "\t -> "+string + "\n";
			}
	    }

	    if (!mapObjet.isEmpty()) {
	        double averageSize = averageSizeList(mapObjet);
	        System.out.println("Nombre moyen de " + nomObjet + " par " + nomContainer + ": " + averageSize);
	        cmd += "Nombre moyen de " + nomObjet + " par " + nomContainer + ": " + averageSize + "\n";
	    }
	    System.out.println("Total: "+ total);
	}
	
	public static double averageSize(Map<String, Integer> mapObjet) {
		int totalSize = 0;
		for (Map.Entry<String, Integer> entry : mapObjet.entrySet()) {
	        int value = entry.getValue();

	        totalSize += value;
	    }

	    if (!mapObjet.isEmpty()) {
	        double averageSize = (double) totalSize / mapObjet.size();
	        return averageSize;
	    }
	    
	    return -1;
	}
	
	public static void displayNumberWithin(String nomObjet, String nomContainer, Map<String, Integer> mapObjet) {
	    System.out.println("Nombre de " + nomObjet + " par " + nomContainer + ": ");
	    
	    for(String key: mapObjet.keySet()) {
			System.out.println("-> "+key+": "+mapObjet.get(key) + " " + nomObjet);	
			cmd += "-> "+key+": "+mapObjet.get(key) + " " + nomObjet + "\n";
		}

	    double averageSize = averageSize(mapObjet);
        System.out.println("Nombre moyen de " + nomObjet + " par " + nomContainer + ": " + averageSize);
        cmd += "Nombre moyen de " + nomObjet + " par " + nomContainer + ": " + averageSize + "\n";
	}
	
	public static void displayBestObjects(String nomObjet, String comparateur, List<String> listeObjet) {
		System.out.println("Liste des " + nomObjet + " avec le plus de " + comparateur+ ": ");
		for (int i = 0; i < listeObjet.size(); i++) {
			System.out.println("-> "+ listeObjet.get(i));
			cmd += "-> "+ listeObjet.get(i) + "\n";
		}
	}

	public static void displayCallGraph(Map<String, Map<String, Map<String, String>>> callGraph) {
	    System.out.println("Graphe d'appels (sans méthodes provenants de Java:");
	    graphCmd += "==========================" + "\n";
	    graphCmd += "|      Graphe d'appel      |" + "\n";
	    graphCmd += "==========================" + "\n";

	    for (Map.Entry<String, Map<String, Map<String, String>>> classEntry : callGraph.entrySet()) {
	        String className = classEntry.getKey();
	        Map<String, Map<String, String>> methodCalls = classEntry.getValue();

	        System.out.println("Classe: " + className);
	        graphCmd += "Classe: " + className + "\n";

	        for (Map.Entry<String, Map<String, String>> methodEntry : methodCalls.entrySet()) {
	            String methodName = methodEntry.getKey();
	            Map<String, String> calledMethods = methodEntry.getValue();

	            System.out.println("-> Méthode: " + methodName);
	            graphCmd += "-> Méthode: " + methodName + "\n";

	            if (!calledMethods.isEmpty()) {
	                System.out.println("   Appelle:");
	                graphCmd += "   Appelle:" + "\n";
	                for (Map.Entry<String, String> calledMethodEntry : calledMethods.entrySet()) {
	                    String calledMethodName = calledMethodEntry.getKey();
	                    String declaringClass = calledMethodEntry.getValue();

	                    System.out.println("   -> " + calledMethodName + "  ->  " + declaringClass);
	                    graphCmd += "   -> " + calledMethodName + "  ->  " + declaringClass + "\n";
	                }
	            } else {
	                System.out.println("   Pas d'appel.");
	                graphCmd += "   Pas d'appel." + "\n";
	            }
	        }
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
		if(packageList.size() == 0) packageList.add("Default package");
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
		        List<String> parametersList = new ArrayList<String>();
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
	    Map<String, Integer> topMethods = new HashMap<String, Integer>();

	    List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<Map.Entry<String, Integer>>(methodsAndLinesMap.entrySet());
	    Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Integer>>() {
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

	public static Map<String, Map<String, Map<String, String>>> buildCallGraph(CompilationUnit parse) {
	    ClassDeclarationVisitor classVisitor = new ClassDeclarationVisitor();
	    parse.accept(classVisitor);

	    Map<String, Map<String, Map<String, String>>> callGraph = new HashMap<String, Map<String, Map<String, String>>>();

	    for (TypeDeclaration classDeclaration : classVisitor.getClasses()) {
	        String className = classDeclaration.getName().getIdentifier();
	        Map<String, Map<String, String>> methodCalls = new HashMap<String, Map<String, String>>();

	        MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();
	        classDeclaration.accept(methodVisitor);

	        for (MethodDeclaration methodDeclaration : methodVisitor.getMethods()) {
	            String methodName = methodDeclaration.getName().getIdentifier();
	            Map<String, String> calledMethods = new HashMap<String, String>();

	            MethodInvocationVisitor invocationVisitor = new MethodInvocationVisitor();
	            methodDeclaration.accept(invocationVisitor);

	            for (MethodInvocation methodInvocation : invocationVisitor.getMethods()) {
	                String invokedMethodName = methodInvocation.getName().getIdentifier();
	                IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
	                
	                if (methodBinding != null) {
	                    String declaringClassName = methodBinding.getDeclaringClass().getQualifiedName();
	                    calledMethods.put(invokedMethodName, declaringClassName);
	                }
	            }

	            methodCalls.put(methodName, calledMethods);
	        }

	        callGraph.put(className, methodCalls);
	    }

	    return callGraph;
	}



}
