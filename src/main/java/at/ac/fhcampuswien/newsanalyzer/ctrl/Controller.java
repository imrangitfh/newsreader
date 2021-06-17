package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "2a5f8954097d4d1b9ea4f6b2a98481cc";

	public void process(NewsApi newsApi) throws NewsApiException {
		System.out.println("Start process");

		List<Article> articlesList = (List<Article>) getData(newsApi);
		//articles.stream().forEach(article -> System.out.println(article.toString()));


		/**
		 * Start analyzing the articles
		 */

		System.out.println(System.lineSeparator() + "######################## Number of articles " +
				"##################################" + System.lineSeparator());

		analyzeNumberOfArticles(articlesList);

		System.out.println(System.lineSeparator() + "######################## Largest provider by number of articles " +
				"##################################" + System.lineSeparator());

		analyzeLargestNumberOfArticles(articlesList);

		System.out.println(System.lineSeparator() + "######################## Author with the shortest name " +
				"##################################" + System.lineSeparator());

		analyzeAuthorWithShortestName(articlesList);

		System.out.println(System.lineSeparator() + "######################## Sort titles by length and alphabet " +
				"and print them ##################################" + System.lineSeparator());

		sortTitlesByLengthAndAlphabet(articlesList);

		System.out.println(System.lineSeparator() + "#################################################" +
				"###############################" + System.lineSeparator());


		/**
		 * Save the files locally in a html document
		 * Please specify the path and name of the document
		 */
		writeToFile(newsApi,"D:\\FH Campus Wien\\2_Semester\\0_Programmieren 2\\2_Übungen" +
				"\\3_ExceptionsLambdaStreams\\test.html");


		System.out.println("End process");
	}




	/**
	 * Number of the articles
	 */
	public void analyzeNumberOfArticles(List<Article> articlesList) {
		System.out.println("The number of the articles is " + articlesList
				.stream()
				.count());
	}

	/**
	 * Searching and printing the name of the provider with the largest number of the articles
	 */
	public void analyzeLargestNumberOfArticles(List<Article> articlesList) {

		articlesList
				.stream()
				.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
				.entrySet().stream()
				.reduce((Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) -> entry1.getValue()
						< entry2.getValue() ? entry2 : entry1)
				.ifPresent(stringLongEntry ->
						System.out.println("Name of the provider which provides the largest number of the articles is "
								+ stringLongEntry.getKey() + ". Number of articles: " + stringLongEntry.getValue()));
	}


	/**
	 * Author with the shortest name
	 */
	public void analyzeAuthorWithShortestName(List<Article> articlesList) {
		try {
			articlesList
					.stream()
					.filter(article -> article.getAuthor() != null) //filter articles without authors
					//.peek(article -> System.out.println("Author: " + article.getAuthor()))
					.reduce((article1, article2) -> {
						return article1.getAuthor().length() < article2.getAuthor().length() ? article1 : article2;
					} )
					.ifPresent(article -> System.out.println("The author with the shortest name is " + article.getAuthor()));
		} catch (NullPointerException nullPointerException) {
			System.out.println("Some of the articles have no author");
		}
	}

	/**
	 * Sort the title by the longest title in alphabetical order
	 */
	public void sortTitlesByLengthAndAlphabet(List<Article> articlesList) {

		articlesList
				.stream()
				.sorted(Comparator.comparing(Article::getTitle).thenComparing(article -> article.getTitle().length()))
				.sorted((Article article1, Article article2) -> {

					if (article1.getTitle().startsWith(article2.getTitle().substring(0,1))) {
						return article1.getTitle().length() < article2.getTitle().length() ? 1 : -1;
					}
					return 0;
				})
				.forEach(article -> System.out.println(article.getTitle()));
	}


	/**
	 * Gets data from the newsApi
	 * @param newsApi - Takes newsApi as parameter and returns the articles
	 * @return - returns an object of articles, which have to be casted to an List of articles
	 * @throws NewsApiException
	 */
	public Object getData(NewsApi newsApi) throws NewsApiException {
		NewsResponse newsResponse = newsApi.getNews();
		if (newsResponse == null)
			throw new NewsApiException("It was not possible to load the news. Please modify your search.");
		return newsResponse.getArticles();
	}



	/**
	 *  Saves the articles to a specified file.
	 * @param newsApi - calls the method saveArticlesLocallyinHtml() in class NewsApi
	 * @param path - path and name of the file
	 * @throws NewsApiException
	 */
	public void writeToFile(NewsApi newsApi, String path) throws NewsApiException {
		newsApi.saveArticlesLocallyInHtml(path);
	}
}
