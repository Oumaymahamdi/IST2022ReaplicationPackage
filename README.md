
# An Empirical Study on the Impact of Refactoring on Quality Metrics in Android Applications (IST2022)

This repository contains all the material required to replicate our analysis, including (1) the data (2) the statistical analysis scripts, and (3) the analysis 
results.


## Data collection :

The data used for this study can be obtained by executing the java proram available in the Script folder.

### To run this Java program, you need three tools: 

1.	RefactoringMiner: RefactoringMiner is a library/API written in Java that can detect refactorings applied in the history of a Java project ([Availaible here](https://github.com/mauricioaniche/repodriller)).
2.	Ck-metrics: CK calculates class-level and method-level code metrics in Java projects by means of static analysis ([Availaible here](https://github.com/mauricioaniche/ck)). 
3.	Understand: Calculates also the code metrics. Understand is not available in the Maven Central Repository ([Availaible here](https://www.scitools.com)).

### Instructions to run the program:
1.	To run this java project, you will need the Eclipse Mars EE ([Availaible here](https://www.eclipse.org/downloads/packages/release/mars/2)).
2.	You simply have to start a Java Project in Eclipse. This project is a maven project so you need import it as maven. 
3.	RefactoringMiner and CK-metrics are already integrated to the Java project as a maven dependency, you need just to check the pom.xml file and verify the version of the used tools; if they are updated you need just to change the version, else you keep it.   
4.	Understand is not available in the Maven Central Repository so it is executed from the command line just you should be sure that you have installed understand in your PC.
5.	Before running the project, you need also to change the path where you will clone the projects (“DIRECTORY_GIT”) in Utils.java. 
6.	Run the project. As a result you will have for each application two CSV files “ref_sb_ck.csv” and “ctrl_sb_ck.csv”. The first contain the list of applied refactorings and the quality metrics before and after each refactoring operation (Treatement Group). The second one contained the quality metrics before and after a commit that did not contain a refactoring (Control group).




## Analysis : 

The totality of the statistical analysis scripts utilized for the study are available in Analysis_scripts folder: 

* [Apriori.py](https://github.com/Oumaymahamdi/RepliactionPackage/blob/main/Analysis_scripts/Apriori.py): Performs analysis related to RQ2 by measuring the support, confidence, lift and leverge values.
* [Analysis_RQ2.R](https://github.com/Oumaymahamdi/RepliactionPackage/blob/main/Analysis_scripts/Analysis_RQ2.R): Performs analysis related to RQ2 by performing the Chi-square and Cramer's V tests.
* [Analysis_RQ13](https://github.com/Oumaymahamdi/RepliactionPackage/blob/main/Analysis_scripts/Analysis_RQ13.R):P Performs analysis related to RQ1 and RQ3 by performing the the Wilcoxon rank-sum test and Cliff's delta.

## Data :

The data used for the analysis is available in Data folder where we can found the list of refactoring and quality metrics for the treatement group as well as the control group.