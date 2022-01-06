package oumayma_hamdi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKNotifier;

import gr.uom.java.xmi.diff.CodeRange;

public class Main extends Thread {

	private Repository repository;
	Git git;
	String projName;
	String appStore;
	LinkedList<RevCommit> gitCommits;

	public static String[] names = { "Extract Method", "Inline Method", "Move Method", "Move Attribute",
			"Pull Up Method", "Pull Up Attribute", "Push Down Method", "Push Down Attribute", "Extract Superclass",
			"Extract Interface", "Move Class", "Rename Class", "Extract And Move Method", "Change Package",
			"Move and Rename Class", "Extract Class", "Extract Subclass", "Extract Variable", "Inline Variable",
			"Parameterize Variable", "Rename Variable", "Rename Parameter", "Rename Attribute",
			"Move and Rename Attribute", "Replace Variable With Attribute", "Replace Attribute", "Merge Variable",
			"Merge Parameter", "Merge Attribute", "Split Variable", "Split Parameter", "Split Attribute",
			"Change Variable Type", "Change Parameter Type", "Change Return Type", "Change Attribute Type",
			"Extract Attribute", "Move and Inline Method", "Add Method Annotation", "Remove Method Annotation",
			"Modify Method Annotation", "Add Attribute Annotation", "Remove Attribute Annotation",
			"Modify Attribute Annotation", "Add Class Annotation", "Remove Class Annotation", "Modify Class Annotation",
			"Add Parameter Annotation", "Remove Parameter Annotation", "Modify Parameter Annotation",
			"Add Variable Annotation", "Remove Variable Annotation", "Modify Variable Annotation", "Add Parameter",
			"Remove Parameter", "Reorder Parameter", "Add Thrown Exception Type", "Add Thrown Exception Type",
			"Change Thrown Exception Type", "Change Method Access Modifier", "Change Attribute Access Modifier",
			"Encapsulate Attribute", "Parameterize Attribute", "Replace Attribute with Variable", "Rename Method",
			"Remove Thrown Exception Type", "Move And Rename Method", "Move Source Folder" };

	private Map<String, Map<String, Integer>> hm_ref;

	private GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

	public Main(String proj, String appStore) {
		this.projName = proj;
		this.appStore = appStore;
	}

	private void setRepository(String projName) {
		try {
			System.out.println("cloning " + projName + " project..");
			File f = new File(Utils.DIRECTORY_GIT + projName);
			git = Git.cloneRepository().setDirectory(f).setURI("https://github.com/" + projName).call();
			repository = git.getRepository();
			gitCommits = Utils.getAllCommitMaster(git, repository);

		} catch (Exception e) {
			System.out.println("Problem in cloning " + projName);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		String output = projName.replaceAll("/", "-") + ".csv";
		if (!new File(output).exists()) {

			final StringBuilder global_sb_ck = new StringBuilder("app,commit,class");
			for (int i = 0; i < names.length; i++) {
				global_sb_ck.append("," + names[i]);
			}

			String under_str = "AvgCyclomatic_before,AvgCyclomaticModified_before,AvgCyclomaticStrict_before,AvgEssential_before,"
					+ "AvgLine_before,AvgLineBlank_before,AvgLineCode_before,AvgLineComment_before,CountClassBase_before,CountClassCoupled_before,"
					+ "CountClassCoupledModified_before,CountClassDerived_before,CountDeclClass_before,CountDeclClassMethod_before,"
					+ "CountDeclClassVariable_before,CountDeclExecutableUnit_before,CountDeclFile_before,CountDeclFunction_before,"
					+ "CountDeclInstanceMethod_before,CountDeclInstanceVariable_before,CountDeclMethod_before,CountDeclMethodAll_before,"
					+ "CountDeclMethodDefault_before,CountDeclMethodPrivate_before,CountDeclMethodProtected_before,CountDeclMethodPublic_before,"
					+ "CountInput_before,CountLine_before,CountLineBlank_before,CountLineCode_before,CountLineCodeDecl_before,CountLineCodeExe_before,"
					+ "CountLineComment_before,CountOutput_before,CountPath_before,CountPathLog_before,CountSemicolon_before,CountStmt_before,"
					+ "CountStmtDecl_before,CountStmtExe_before,Cyclomatic_before,CyclomaticModified_before,CyclomaticStrict_before,"
					+ "Essential_before,Knots_before,MaxCyclomatic_before,MaxCyclomaticModified_before,MaxCyclomaticStrict_before,"
					+ "MaxEssential_before,MaxEssentialKnots_before,MaxInheritanceTree_before,MaxNesting_before,MinEssentialKnots_before,"
					+ "PercentLackOfCohesion_before,PercentLackOfCohesionModified_before,RatioCommentToCode_before,SumCyclomatic_before,"
					+ "SumCyclomaticModified_before,SumCyclomaticStrict_before,SumEssential_before,AvgCyclomatic_after,"
					+ "AvgCyclomaticModified_after,AvgCyclomaticStrict_after,AvgEssential_after,AvgLine_after,AvgLineBlank_after,"
					+ "AvgLineCode_after,AvgLineComment_after,CountClassBase_after,CountClassCoupled_after,CountClassCoupledModified_after,"
					+ "CountClassDerived_after,CountDeclClass_after,CountDeclClassMethod_after,CountDeclClassVariable_after,"
					+ "CountDeclExecutableUnit_after,CountDeclFile_after,CountDeclFunction_after,CountDeclInstanceMethod_after,"
					+ "CountDeclInstanceVariable_after,CountDeclMethod_after,CountDeclMethodAll_after,CountDeclMethodDefault_after,"
					+ "CountDeclMethodPrivate_after,CountDeclMethodProtected_after,CountDeclMethodPublic_after,CountInput_after,"
					+ "CountLine_after,CountLineBlank_after,CountLineCode_after,CountLineCodeDecl_after,CountLineCodeExe_after,"
					+ "CountLineComment_after,CountOutput_after,CountPath_after,CountPathLog_after,CountSemicolon_after,CountStmt_after,"
					+ "CountStmtDecl_after,CountStmtExe_after,Cyclomatic_after,CyclomaticModified_after,CyclomaticStrict_after,Essential_after,"
					+ "Knots_after,MaxCyclomatic_after,MaxCyclomaticModified_after,MaxCyclomaticStrict_after,MaxEssential_after,MaxEssentialKnots_after,"
					+ "MaxInheritanceTree_after,MaxNesting_after,MinEssentialKnots_after,PercentLackOfCohesion_after,PercentLackOfCohesionModified_after,"
					+ "RatioCommentToCode_after,SumCyclomatic_after,SumCyclomaticModified_after,SumCyclomaticStrict_after,SumEssential_after";

			String colums = ",cbo_before,wmc_before,dit_before,rfc_before,lcom_before,tcc_before,lcc_before,totalMethodsQty_before,staticMethodsQty_before,"
					+ "publicMethodsQty_before,privateMethodsQty_before,protectedMethodsQty_before,defaultMethodsQty_before,visibleMethodsQty_before,abstractMethodsQty_before,"
					+ "finalMethodsQty_before,synchronizedMethodsQty_before,totalFieldsQty_before,staticFieldsQty_before,publicFieldsQty_before,privateFieldsQty_before,"
					+ "protectedFieldsQty_before,defaultFieldsQty_before,finalFieldsQty_before,synchronizedFieldsQty_before,nosi_before,loc_before,returnQty_before,loopQty_before,"
					+ "comparisonsQty_before,tryCatchQty_before,parenthesizedExpsQty_before,stringLiteralsQty_before,numbersQty_before,assignmentsQty_before,mathOperationsQty_before,"
					+ "variablesQty_before,maxNestedBlocksQty_before,anonymousClassesQty_before,innerClassesQty_before,lambdasQty_before,uniqueWordsQty_before,modifiers_before,"
					+ "logStatementsQty_before,cbo_after,wmc_after,dit_after,rfc_after,lcom_after,tcc_after,lcc_after,totalMethodsQty_after,staticMethodsQty_after,"
					+ "publicMethodsQty_after,privateMethodsQty_after,protectedMethodsQty_after,defaultMethodsQty_after,visibleMethodsQty_after,abstractMethodsQty_after,"
					+ "finalMethodsQty_after,synchronizedMethodsQty_after,totalFieldsQty_after,staticFieldsQty_after,publicFieldsQty_after,privateFieldsQty_after,"
					+ "protectedFieldsQty_after,defaultFieldsQty_after,finalFieldsQty_after,synchronizedFieldsQty_after,nosi_after,loc_after,returnQty_after,loopQty_after,"
					+ "comparisonsQty_after,tryCatchQty_after,parenthesizedExpsQty_after,stringLiteralsQty_after,numbersQty_after,assignmentsQty_after,mathOperationsQty_after,"
					+ "variablesQty_after,maxNestedBlocksQty_after,anonymousClassesQty_after,innerClassesQty_after,lambdasQty_after,uniqueWordsQty_after,modifiers_after,"
					+ "logStatementsQty_after";

			global_sb_ck.append(colums + "," + under_str + "\n");

			final StringBuilder ctrl_sb_ck = new StringBuilder("app,commit,class");
			ctrl_sb_ck.append(colums + "," + under_str + "\n");

			setRepository(projName);
			for (int i = gitCommits.size() - 1; i >= 0; i--) {
				final RevCommit currentCommit = gitCommits.get(i);

				hm_ref = new HashMap<String, Map<String, Integer>>();

				System.out.println("commit " + currentCommit.name() + ":" + currentCommit.getAuthorIdent().getWhen());

				if (currentCommit.getParents().length == 1) {
					// checkout
					final String current_version;
					final String prev_version;
					try {
						prev_version = Utils.extractCodeSourceAtCommit(currentCommit.getParent(0).name(), projName);
						current_version = Utils.extractCodeSourceAtCommit(currentCommit.name(), projName);
						if (!current_version.equals(Utils.ERROR_TREE) && !prev_version.equals(Utils.ERROR_TREE)) {

							final HashMap<String, String> before_ck = runCK(prev_version);
							final HashMap<String, String> after_ck = runCK(current_version);

							final HashMap<String, String> before_under = runUnderstand(prev_version);
							final HashMap<String, String> after_under = runUnderstand(current_version);

							if (before_under.isEmpty()) {
								for (String key : before_ck.keySet()) {
									before_under.put(key,
											",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
								}
							}
							if (after_under.isEmpty()) {
								for (String key : before_ck.keySet()) {
									after_under.put(key,
											",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
								}
							}
							miner.detectAtCommit(repository, currentCommit.name(), new RefactoringHandler() {
								@Override
								public void handle(String commitId, List<Refactoring> refactorings) {

									if (refactorings.size() > 0) {
										// refactoring commit
										// System.out.println(refactorings.size() + " Refactorings at " + commitId);
										System.err.println(commitId + " is a ref commit");

										for (Refactoring ref : refactorings) {
											CodeRange leftSide = ref.leftSide().get(0);
											CodeRange rightSide = ref.rightSide().get(0);
											hm_ref = getRefName(hm_ref, leftSide.getFilePath(), ref.getName());
											hm_ref = getRefName(hm_ref, rightSide.getFilePath(), ref.getName());
										}

										for (String key : before_ck.keySet()) {
											if (after_ck.get(key) != null) {
												String s = "";

												Map<String, Integer> h = getRefByPath(hm_ref,
														after_ck.get(key).substring(0, after_ck.get(key).indexOf(",")));

												for (int i = 0; i < names.length; i++) {
													s = s + "," + h.get(i + "");
												}

												global_sb_ck.append(
														appStore + "," + currentCommit.name() + "," + key + s + ","
																+ before_ck.get(key)
																		.substring(before_ck.get(key).indexOf(",") + 1)
																+ ","
																+ after_ck.get(key)
																		.substring(after_ck.get(key).indexOf(",") + 1)

																+ before_under.get(key) + after_under.get(key)

																+ "\n");
											}

										}
									} else {
										// ctrl group
										for (String key : before_ck.keySet()) {
											if (after_ck.get(key) != null) {
												ctrl_sb_ck
														.append(appStore + "," + currentCommit.name() + "," + key + ","
																+ before_ck.get(key)
																		.substring(before_ck.get(key).indexOf(",") + 1)
																+ ","
																+ after_ck.get(key)
																		.substring(after_ck.get(key).indexOf(",") + 1)

																+ before_under.get(key) + after_under.get(key)

																+ "\n");
											}

										}
									}
								}

								private Map<String, Integer> getRefByPath(Map<String, Map<String, Integer>> hm_ref,
										String key) {
									Map<String, Integer> res = getHM();
									;
									for (String kk : hm_ref.keySet()) {
										if (key.contains(kk.replaceAll("/", Matcher.quoteReplacement("\\")))) {
											res = hm_ref.get(kk);
											break;
										}
									}
									return res;
								}
							});

							// Utils.delete(current_version);
							// Utils.delete(prev_version);

						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			} // end while
			Utils.delete(Utils.DIRECTORY_GIT + projName);
			Utils.writeAtTheEnd(global_sb_ck, appStore + "_ref_sb_ck.csv");
			Utils.writeAtTheEnd(ctrl_sb_ck, appStore + "_ctrl_sb_ck.csv");
		} else {
			System.out.println(output + " already exists!!");
		}

	}

	private Map<String, Map<String, Integer>> getRefName(Map<String, Map<String, Integer>> hm, String path,
			String ref) {

		if (!hm.containsKey(path)) {
			Map<String, Integer> h = getHM();
			hm.put(path, h);
		}
		Map<String, Integer> h = hm.get(path);
		int index = getNumberElem(ref);
		if (index != -1) {
			h.put(index + "", h.get(index + "") + 1);
		} else {
			h.put(index + "", 1);
			System.err.println("**" + ref + "**mahouch mawjouuuud!");
		}
		hm.put(path, h);
		return hm;
	}

	String convert(String r) {
		return r.replaceAll(" ", "").toLowerCase();
	}

	int getNumberElem(String ref) {
		int i = 0;
		while (i < names.length && !convert(names[i]).equals(convert(ref)))
			i++;
		if (i < names.length)
			return i;
		else
			return -1;
	}

	public static Map<String, Integer> getHM() {
		Map<String, Integer> hm = new HashMap<String, Integer>();
		for (int i = 0; i < names.length; i++) {
			hm.put(i + "", 0);
		}
		return hm;
	}

	private HashMap<String, String> runUnderstand(String commitPath) throws IOException {

		HashMap<String, String> res = new HashMap<String, String>();
		String bdd = commitPath + "/project.udb";
		new File(bdd).createNewFile();

		String output = commitPath+"/understand_temp.csv";

		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd \"" + Utils.UNDERSTAND + "\" && " + "und -db \"" + bdd + "\" create -languages java add \""
						+ commitPath + "\" settings -metrics all -metricsOutputFile \"" + output
						+ "\" analyze report metrics");

		System.out.println(Arrays.toString(builder.command().toArray()));
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		  String line; while ((line = r.readLine()) != null) {
		  System.err.println(line); }
		 
		while (!new File(output).exists()) {
			// wait until file is created
		}
		System.out.println(output + " created");
		List<String> data = Utils.readData(output);
		for (String d : data) {
			if (d.split(",")[0].contains("Class")) {
				res.put(d.split(",")[1].replaceAll(Matcher.quoteReplacement("\""), ""),
						d.substring(d.lastIndexOf('"') + 1));
			}
		}

		return res;
	}

	private HashMap<String, String> runCK(String path) throws IOException {
		final HashMap<String, String> hm = new HashMap<String, String>();
		new CK(false, 0, false).calculate(path, new CKNotifier() {
			@Override
			public void notify(CKClassResult result) {
				String s = result.getFile() + "," + result.getCbo() + "," + result.getWmc() + "," + result.getDit()
						+ "," + result.getRfc() + "," + result.getLcom() + "," + result.getTightClassCohesion() + ","
						+ result.getLooseClassCohesion() + "," + result.getNumberOfMethods() + ","
						+ result.getNumberOfStaticMethods() + "," + result.getNumberOfPublicMethods() + ","
						+ result.getNumberOfPrivateMethods() + "," + result.getNumberOfProtectedMethods() + ","
						+ result.getNumberOfDefaultMethods() + "," + result.getVisibleMethods().size() + ","
						+ result.getNumberOfAbstractMethods() + "," + result.getNumberOfFinalMethods() + ","
						+ result.getNumberOfSynchronizedMethods() + "," +

						result.getNumberOfFields() + "," + result.getNumberOfStaticFields() + ","
						+ result.getNumberOfPublicFields() + "," + result.getNumberOfPrivateFields() + ","
						+ result.getNumberOfProtectedFields() + "," + result.getNumberOfDefaultFields() + ","
						+ result.getNumberOfFinalFields() + "," + result.getNumberOfSynchronizedFields() + "," +

						result.getNosi() + "," + result.getLoc() + "," + result.getReturnQty() + ","
						+ result.getLoopQty() + "," + result.getComparisonsQty() + "," + result.getTryCatchQty() + ","
						+ result.getParenthesizedExpsQty() + "," + result.getStringLiteralsQty() + ","
						+ result.getNumbersQty() + "," + result.getAssignmentsQty() + ","
						+ result.getMathOperationsQty() + "," + result.getVariablesQty() + ","
						+ result.getMaxNestedBlocks() + "," + result.getAnonymousClassesQty() + ","
						+ result.getInnerClassesQty() + "," + result.getLambdasQty() + "," + result.getUniqueWordsQty()
						+ "," + result.getModifiers() + "," + result.getNumberOfLogStatements();

				hm.put(result.getClassName(), s.replaceAll("NaN", "0"));
			}

			@Override
			public void notifyError(String sourceFilePath, Exception e) {
				System.err.println("Error in " + sourceFilePath);
				e.printStackTrace(System.err);
			}
		});
		return hm;
	}

	public static void main(String[] args) throws IOException {

		final List<String> projs_data = Utils.readData("Dataset-list-of-apps.csv");
		for (String line : projs_data) {
			String[] in = line.split(",");
			new Main(in[0], in[1]).start();
		}

	}
}
