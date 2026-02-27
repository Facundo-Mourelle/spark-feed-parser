package feed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import namedEntity.NamedEntity;
import namedEntity.heuristic.Heuristic;
import namedEntity.EntityFactory;
import java.io.Serializable;
/*Esta clase modela el contenido de un articulo (ie, un item en el caso del rss feed) */

public class Article implements Serializable {
	private String title;
	private String text;
	private Date publicationDate;
	private String link;

	private List<NamedEntity> namedEntityList = new ArrayList<NamedEntity>();

	public Article(String title, String text, Date publicationDate, String link) {
		super();
		this.title = title;
		this.text = text;
		this.publicationDate = publicationDate;
		this.link = link;
	}

	public List<NamedEntity> getNamedEntityList() {
		return this.namedEntityList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", text=" + text + ", publicationDate=" + publicationDate + ", link="
				+ link
				+ "]";
	}

	public NamedEntity getNamedEntity(String namedEntity) {
		for (NamedEntity n : namedEntityList) {
			if (n.getName().compareTo(namedEntity) == 0) {
				return n;
			}
		}
		return null;
	}

	// computa tokens chequeables hasta tamaño max size
	// Ej maxSize = 2 Hola como estas
	// token 1 = Hola
	// token 2 = Hola como
	// token 3 = como
	// token 4 = como estas
	// token 5 = estas
	// perdemos bastante complejidad pero ganamos flexibilidad
	// semantica a la hora de calcular, para mejorar todavía
	// mas el chequeo podríamos hacer una lista de alias para
	// una entidad nombrada asi cosas como Leo, Leo Messi,
	// Messi todas formen parte de la misma entidad nombrada
	// (tambien prevenir el doble conteo)
	public void computeNamedEntities(Heuristic h, int maxSize) {
		String text = this.getTitle() + " " + this.getText();

		String charsToRemove = ".,;:()'!?\n";
		for (char c : charsToRemove.toCharArray()) {
			text = text.replace(String.valueOf(c), "");
		}

		String[] tokens = text.split(" ");

		for (int i = 0; i < tokens.length; i++) {
			for (int size = 1; size <= maxSize && i + size <= tokens.length; size++) {
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < size; j++) {
					sb.append(tokens[i + j]);
					if (j < size - 1)
						sb.append(" ");
				}

				String ngram = sb.toString().trim();
				if (!ngram.isEmpty() && h.isEntity(ngram)) {
					NamedEntity ne = this.getNamedEntity(ngram);
					if (ne == null) {
						this.namedEntityList.add(EntityFactory.build(ngram, 1, null));
					} else {
						ne.incFrequency();
					}
				}
			}
		}
	}

	public void prettyPrint() {
		System.out.println(
				"**********************************************************************************************");
		System.out.println("Title: " + this.getTitle());
		System.out.println("Publication Date: " + this.getPublicationDate());
		System.out.println("Link: " + this.getLink());
		//System.out.println("Text: " + this.getText());

		if (!namedEntityList.isEmpty()) {
			System.out.println("\nNamed Entities: ");
			this.printNamedEntities();
		}
		System.out.println(
				"**********************************************************************************************");
	}

	public void printNamedEntities() {
		if (namedEntityList.isEmpty()) {
			System.out.println("No named entities found.");
			return;
		}
		System.out.println("Named Entities:");
		for (NamedEntity ne : namedEntityList) {
			ne.prettyPrint();
		}
	}

	public static void main(String[] args) {
		Article a = new Article("This Historically Black University Created Its Own Tech Intern Pipeline",
				"A new program at Bowie State connects computing students directly with companies, bypassing an often harsh Silicon Valley vetting process",
				new Date(),
				"https://www.nytimes.com/2023/04/05/technology/bowie-hbcu-tech-intern-pipeline.html");

		a.prettyPrint();
	}

}
