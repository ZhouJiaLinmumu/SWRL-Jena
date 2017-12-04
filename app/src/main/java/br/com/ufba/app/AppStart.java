package br.com.ufba.app;

import java.util.List;

import javax.swing.JOptionPane;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

public class AppStart {

	private final static String SOURCE = "http://www.semanticweb.org/mate85";
	private final static String NS = SOURCE + "#";
	private static List<Rule> rules;
	private static Reasoner reasoner;
	private static AppOntology appOntology;

	public static void main(String[] args) {

		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);

		showMenu();
		
		reasoner.setDerivationLogging(true);

		final OntModel ontModel = appOntology.getOntologyModel();

		Individual conta0002 = ontModel.getIndividual(NS + "Conta0002");
		Individual conta0003 = ontModel.getIndividual(NS + "Conta0003");

		InfModel inf = ModelFactory.createInfModel(reasoner, ontModel);

		appOntology.validateInference(inf);

		System.out.println(
				"---------------------------------Facts of Conta002-----------------------------------------------");
		appOntology.printFactsFromIndividual(conta0002);

		System.out.println(
				"---------------------------------Facts of Conta003-----------------------------------------------");
		appOntology.printFactsFromIndividual(conta0003);

		System.out.println("---------------------------------Inference-----------------------------------------------");
		appOntology.printInferenceFromModel(inf, conta0003);

		// appOntology.exportOwlInference(inf);
	}

	public static void showMenu() {

		String menu = "-------------------Opções-------------------\n" + "1: Carregar Regras do Arquivo .TXT\n"
				+ "2: Carregar Regras Inline\n" + "3: Carregar Ontologia Com as Regras";

		char option = JOptionPane.showInputDialog(null, menu, "MENU", JOptionPane.PLAIN_MESSAGE).charAt(0);

		appOntology = new AppOntology("/SWRLAulaWithRules.owl");
		
		switch (option) {
		case '1': {
			rules = readRulesFileTxt();
			reasoner = new GenericRuleReasoner(rules);
			break;
		}
		case '2': {
			rules = readRulesInline();
			reasoner = new GenericRuleReasoner(rules);
			break;
		}
		case '3': {
			appOntology = new AppOntology("/SWRLAulaWithRules.owl");
			reasoner = PelletReasonerFactory.theInstance().create();
			break;
		}
		}
	}

	public static List<Rule> readRulesFileTxt() {
		return Rule.rulesFromURL("file:rules");
	}

	public static List<Rule> readRulesInline() {
		String rules = "[rule1: (?c rdf:type " + NS + "Cliente) " + "(?c " + NS + "temContaBancaria ?x ) " + "(?x " + NS
				+ "saldoAtual ?saldo) " + "lessThan(?saldo, 0) " + "ge(?saldo, -2000) (EmprestimoLight valorContratado ?vc)" + "sum(?vc, ?saldo, ?total) "
				+ "now(?dataAtual) -> " + "(?x " + NS + "contratarEmprestimo 5000) " + "(?x " + NS
				+ "entrouChequeEspecial ?saldo) " + "(?x " + NS + "saldoAtual ?total) " + "(?x " + NS
				+ "dataContratoEmprestimo ?dataAtual)]";
		return Rule.parseRules(rules);
	}
}
