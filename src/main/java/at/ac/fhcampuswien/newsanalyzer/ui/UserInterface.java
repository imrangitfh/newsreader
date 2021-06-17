package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.enums.*;

public class UserInterface 
{
	public static final String APIKEY = "2a5f8954097d4d1b9ea4f6b2a98481cc";
	private Controller ctrl = new Controller();
	private Scanner scanner = new Scanner(System.in);

	public void getDataFromCtrl1(){
		System.out.println("50 Headlines in Austria sorted by publication date");

		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ("")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(Country.at)
				.setFrom("2021-04-10")
				.setExcludeDomains("Lifehacker.com")
				.setPageSize("50")
				.setSortBy(SortBy.PUBLISHED)
				.createNewsApi();

		try {
			ctrl.process(newsApi);
		} catch (NewsApiException newsApiException) {
			System.out.println("This is NewsApiException: " + newsApiException.getMessage());
		}
	}

	public void getDataFromCtrl2() {
		System.out.println("Articles about electric cars in English sorted by popularity");

		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ("")
				.setQInTitle("electric+car")
				.setEndPoint(Endpoint.EVERYTHING)
				.setLanguage(Language.en)
				.setSortBy(SortBy.POPULARITY)
				.setPageSize("100")
				.createNewsApi();
		try {
			ctrl.process(newsApi);
		} catch (NewsApiException newsApiException) {
			System.out.println("This is NewsApiException: " + newsApiException.getMessage());
		}
	}

	public void getDataFromCtrl3() {
		System.out.println("Headlines about Corona in category health");
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ("corona+OR+covid+OR+covid-19")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCategory(Category.health)
				.setExcludeDomains("Lifehacker.com")
				.setPageSize("100")
				.createNewsApi();
		try {
			ctrl.process(newsApi);
		} catch (NewsApiException newsApiException) {
			System.out.println("This is NewsApiException: " + newsApiException.getMessage());
		}
	}


	/**
	 * The user is able to choose a phrase or a keyword to search for. All other parameter are already predefined.
	 */
	public void getDataForCustomInput() {
		System.out.print("You have chosen User Input. Please insert a keyword or a phrase to search for in the articles: ");

		List<String> inputList = Arrays.asList(scanner.nextLine().split(" "));

		StringBuilder stringBuilder = new StringBuilder();
		inputList.forEach(element -> {
			stringBuilder.append(element).append("+");
		});
		stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("+"));
		System.out.println("Articles for " + stringBuilder.toString() + " sorted by relevancy");
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ(stringBuilder.toString())
				.setEndPoint(Endpoint.EVERYTHING)
				.setSortBy(SortBy.RELEVANCY)
				.setPageSize("100")
				.createNewsApi();
		try {
			ctrl.process(newsApi);
		} catch (NewsApiException newsApiException) {
			System.out.println("This is NewsApiException: " + newsApiException.getMessage());
		}
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitle("Wählen Sie aus:");
		menu.insert("a", "50 Headlines in Austria sorted by publication date", this::getDataFromCtrl1);
		menu.insert("b", "Articles about electric cars in English sorted by popularity", this::getDataFromCtrl2);
		menu.insert("c", "Headlines about Corona in category health", this::getDataFromCtrl3);
		menu.insert("d", "Enter a phrase or a word to search for:",this::getDataForCustomInput);
		menu.insert("e", "Configure your own search", this::getDataForCustomInput);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			choice.run();
		}
		System.out.println("Program finished");
	}


    protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
        } catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
        while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
            if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
                number = null;
			}
		}
		return number;
	}
}
