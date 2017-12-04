package br.com.ufba.app;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.StmtIteratorImpl;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

public class AppOntology {
	private OntModel ontModel;
	private String stringOntologyFile;

	public AppOntology(String stringOntologyFile) {
		this.stringOntologyFile = stringOntologyFile;
	}

	public void exportOwlInference(InfModel infModel) {
		Writer writer;
		try {
			writer = new FileWriter("./owl-output-inference.owl");
			infModel.write(writer, null);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OntModel getOntologyModel() {
		OntModel ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, null);

		try {
			ontModel.read(AppStart.class.getResourceAsStream(this.stringOntologyFile), null);
		} catch (Exception e) {
			System.out.println("Could not find file");
		}

		this.ontModel = ontModel;

		return ontModel;
	}
	
	public void printInferenceFromModel(InfModel model, Individual indi) {
		StmtIterator stmts = model.listStatements(indi, (ObjectProperty) null, (RDFNode) null);

		while (stmts.hasNext()) {
			Statement stmt = stmts.nextStatement();
			Resource subject = stmt.getSubject();
			Property predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();

			System.out.print(subject.getLocalName());
			System.out.print(" " + predicate.getLocalName() + " ");

			if (object instanceof Resource) {
				System.out.print(((Resource) object).getLocalName());
			} else {
				System.out.print(" \"" + object.asLiteral().getValue() + "\"");
			}

			System.out.println();
		}
	}

	

	private void printInferenceFromModel(Model model) {
		StmtIterator stmts = model.listStatements(null, (ObjectProperty) null, (RDFNode) null);

		while (stmts.hasNext()) {
			Statement stmt = stmts.nextStatement();
			Resource subject = stmt.getSubject();
			Property predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();

			System.out.print(subject.getLocalName());
			System.out.print(" " + predicate.getLocalName() + " ");

			if (object instanceof Resource) {
				System.out.print(((Resource) object).getLocalName());
			} else {
				System.out.print(" \"" + object.asLiteral().getValue() + "\"");
			}

			System.out.println();
		}
	}

	public void printFactsFromIndividual(Individual individual) {
		StmtIterator stmts = this.ontModel.listStatements(individual, (ObjectProperty) null, (RDFNode) null);

		while (stmts.hasNext()) {
			Statement stmt = stmts.nextStatement();
			Resource subject = stmt.getSubject();
			Property predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();

			System.out.print(subject.getLocalName());
			System.out.print(" " + predicate.getLocalName() + " ");

			if (object instanceof Resource) {
				System.out.print(((Resource) object).getLocalName());
			} else {
				System.out.print(" \"" + object.asLiteral().getValue() + "\"");
			}

			System.out.println();
		}
	}

	public void printInference(InfModel inf) {
		ExtendedIterator<Statement> stmts = inf.listStatements().filterDrop(new Filter<Statement>() {
			@Override
			public boolean accept(Statement o) {
				return ontModel.contains(o);
			}
		});

		Model deductions = ModelFactory.createDefaultModel().add(new StmtIteratorImpl(stmts));
		printInferenceFromModel(deductions);
	}

	public void validateInference(InfModel infModel) {
		ValidityReport validity = infModel.validate();
		if (validity.isValid()) {
			System.out.println("OK");
		} else {
			System.out.println("Conflicts");
			for (Iterator i = validity.getReports(); i.hasNext();) {
				System.out.println(" - " + i.next());
			}
		}
	}
}
