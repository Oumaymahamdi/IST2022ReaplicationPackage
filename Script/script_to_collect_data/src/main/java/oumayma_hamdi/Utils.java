package oumayma_hamdi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class Utils {
	public static final String ERROR_TREE = "worktree PROBLEM";
	public static String HEADER = "";
	public static String DIRECTORY_GIT = "C:/gitProj_/";
	public static final String UNDERSTAND = "C:\\Program Files\\SciTools\\bin\\pc-win64";

	public static synchronized void writeAtTheEnd(StringBuilder s, String filePath) {
		try (FileWriter fw = new FileWriter(filePath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.print(s.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> readData(String pathToData) throws IOException {
		List<String> res = new ArrayList<String>();
		String line = "";
		BufferedReader br = new BufferedReader(new FileReader(pathToData));
		HEADER = br.readLine();// header
		while ((line = br.readLine()) != null) {
			res.add(line);
		}
		return res;
	}

	public static LinkedList<RevCommit> getAllCommitMaster(Git git, Repository repository)
			throws NoHeadException, GitAPIException, IOException {
		LinkedList<RevCommit> res = new LinkedList<RevCommit>();
		Iterable<RevCommit> commits = git.log().all().call();
		for (RevCommit commit : commits) {
			res.add(commit);
		}
		return res;
	}
	public static void delete(String code_version) {
		// delete version
		try {
			FileUtils.deleteDirectory(new File(code_version));
			System.out.println(code_version + "deleted " );
		} catch (final IOException e) {
			System.err.println("Please delete the directory " +code_version + " manually");
		}
	}
	public static String extractCodeSourceAtCommit(String commit, String projName) throws IOException {
		String local_dir = DIRECTORY_GIT + projName;
		new File(local_dir + "/versions_builds/").mkdir();
		String current_version = local_dir + "/versions_builds/" + commit;
		return canParse(commit, current_version, local_dir);

	}

	public static String canParse(String commit, String current_version, String local_dir) throws IOException {

		if (!new File(current_version).exists()) {
			new File(current_version).mkdir();
			/*
			 * use git worktree to extract the code version at the time of a commit using
			 * the command line PS. you should install "git" on your machine to run the
			 * command.)
			 */
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
					"cd \"" + local_dir + "\" && " + "git worktree add \"" + current_version + "\" " + commit);

			builder.redirectErrorStream(true);
			Process p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line2;
			boolean canParse = true;
			while ((line2 = r.readLine()) != null) {
				if (line2.contains("fatal")) {
					System.err.println("\nworktree PROBLEM " + line2);
					// System.out.println(project+":
					// "+Arrays.toString(builder.command().toArray()));
					canParse = false;
				}
			}
			if (new File(current_version).exists() && canParse) {
				return current_version;
			} else {
				return (Utils.ERROR_TREE);
			}
		} else
			return current_version;

	}
}
