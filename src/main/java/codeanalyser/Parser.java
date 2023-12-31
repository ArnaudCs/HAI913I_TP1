package codeanalyser;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Parser {
//	/home/e20190003865/Bureau/TP-M2/HAI916I Intelligence Artificielle pour Génie Logiciel/Projet 1/GLIA-Airlines/
//	/home/e20190003865/Bureau/TP-M2/HAI919I Ingénierie des modèles/TP1/
	//public static final String projectPath = "/home/e20190003865/Bureau/TP-M2/HAI916I Intelligence Artificielle pour Génie Logiciel/Projet 1/GLIA-Airlines/"; //Adam
	//public static final String projectPath = "/home/e20190000683/Bureau/TP-M2/HAI916I Intelligence Artificielle pour Génie Logiciel/Projet 1/GLIA-Airlines/"; //Arnaud

	public static String projectSourcePath;
	//public static final String jrePath = "/usr/lib/jvm/java-11-openjdk-amd64";
	public static final String jrePath = "";
	
	public void setProjectSourcePath(String filePath) {
		Parser.projectSourcePath = filePath;
	}

	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
//		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
//
//		for (File fileEntry : javaFiles) {
//			String content = FileUtils.readFileToString(fileEntry);
//
//			CompilationUnit parse = parse(content.toCharArray());
//
//			// print methods info
//			printMethodInfo(parse);
//
//			// print variables info
//			printVariableInfo(parse);
//			
//			//print method invocations
//			printMethodInvocationInfo(parse);
//		}
		
		System.out.println("\n----- Infos code source -----\n");
		CodeAnalyser.runAllStats(folder);
	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	public static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map<?, ?> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = { projectSourcePath }; 
		String[] classpath = {jrePath};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}

	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethods()) {
			System.out.println("Method name: " + method.getName()
					+ "| Return type: " + method.getReturnType2());
		}

	}

	// navigate variables inside method
	public static void printVariableInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
			method.accept(visitor2);

			for (VariableDeclarationFragment variableDeclarationFragment : visitor2
					.getVariables()) {
				System.out.println("variable name: "
						+ variableDeclarationFragment.getName()
						+ " variable Initializer: "
						+ variableDeclarationFragment.getInitializer());
			}

		}
	}
	
	// navigate method invocations inside method
		public static void printMethodInvocationInfo(CompilationUnit parse) {

			MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
			parse.accept(visitor1);
			for (MethodDeclaration method : visitor1.getMethods()) {

				MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
				method.accept(visitor2);

				for (MethodInvocation methodInvocation : visitor2.getMethods()) {
					System.out.println("method " + method.getName() + " invoc method "
							+ methodInvocation.getName());
				}

			}
		}
		
		

}