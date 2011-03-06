package com.obtiva.goose.acceptance.util;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;

public class WebSteps {
	private final WebDriver d;

	public WebSteps(WebDriverFacade facade) throws InvocationTargetException,
			InstantiationException, IllegalAccessException {
		d = facade.getWebDriver();
	}
	
	public WebDriver getWebDriver() {
		return d;
	}
	
	public JavascriptExecutor getJavascriptExecutor() {
		return (JavascriptExecutor) d;
	}

	public void visit(String url) {
		getWebDriver().get(url);
	}
	
	
	@When("^(?:|I )(press|click) \"([^\"]*)\"$")
	public void pressOrClick(String action, String field) {
		WebElement e = null;
		try {
			e = d.findElement(By.xpath("//input[@value='" + field + "']"));
		} catch (Exception ex) {
			e = find(field);
		}
		e.click();
	}

	@When("^(?:|I )follow \"([^\"]*)\"$")
	public void follow(String linkText) {
		d.findElement(By.linkText(linkText)).click();
	}

	@When("^(?:|I )fill in \"([^\"]*)\" with \"([^\"]*)\"$")
	public void fillInFieldWithValue(String field, String value) {
		find(field).sendKeys(value);
	}

	@When("^(?:|I )fill in the following$")
	public void fillInTheFollowingWithTable(cuke4duke.Table table) {
		Map<String, String> rows = table.rowsHash();
		for (String field : rows.keySet()) {
			fillInFieldWithValue(field, rows.get(field));
		}
	}
	
	@When("^(?:|I )check \"([^\"]*)\"$")
	public void check(String field) {
		find(field).click();
	}
	
	@When("^(?:|I )check the following$")
	public void checkTheFollowing(cuke4duke.Table table) {
		List<List<String>> rowsList = table.raw();
		for (List<String> rows : rowsList) {
			for (String field : rows) {
				check(field);
			}
		}
	}

	@When("^(?:|I )select \"([^\"]*)\"$")
	public void select(String field) {
		//find("//input[@value=\"" + value + "\"]").setSelected();
		find(field).setSelected();
	}
	
	@When("^(?:|I )select \"([^\"]*)\" from \"([^\"]*)\"$")
	public void selectFrom(String value, String field) {
		for (WebElement option: getOptions(field)) {
		    if (option.getText().equals(value)) {
		        option.setSelected();
		    }
		}
	}

	@When("^(?:|I )select first from \"([^\"]*)\"$")
	public void selectFirstFrom(String field) {
		List<WebElement> options = getOptions(field);
		if (options.size() > 0) {
			options.get(0).setSelected();
		} else {
			throw new NoSuchElementException("No options found for select '" + field + "'.");
		}
	}

	@When("^(?:|I )select last from \"([^\"]*)\"$")
	public void selectLastFrom(String field) {
		List<WebElement> options = getOptions(field);
		if (options.size() > 0) {
			options.get(options.size() - 1).setSelected();
		} else {
			throw new NoSuchElementException("No options found for select '" + field + "'.");
		}
	}
	
	private List<WebElement> getOptions(String field) {
		WebElement choices = find(field);
		return choices.findElements(By.tagName("option"));
	}
	
	@Then("^^(?:|I )should see \"([^\"]*)\"$")
	public void shouldSee(String text) throws InterruptedException {
		assertThat(d.getPageSource(), containsString(text));
	}

	@Then("^^(?:|I )should not see \"([^\"]*)\"$")
	public void shouldNotSee(String text) throws InterruptedException {
		assertThat(d.getPageSource(), not(containsString(text)));
	}
	
	@Then("^^(?:|I )title should be \"([^\"]*)\"$")
	public void titleIs(String value) {
		assertThat(find("//title").getText(), containsString(value));
	}
	
	@When("^(?:|I )wait \"([^\"]*)\" seconds$")
	public void wait(String seconds) {
		try {
			Thread.sleep(Long.parseLong(seconds) * 1000);
		} catch (InterruptedException e) {
			// okay to timeout
		}
	}

	protected WebElement find(String field) throws NoSuchElementException {
		WebElement element = find(By.id(field));
		if (element == null) {
			element = find(By.name(field));
			if (element == null) {
				element = find(By.xpath(field));
				if (element == null) {
					WebElement label = find(By
							.xpath("//label[contains(child::text(), \"" + field + "\")]"));
					if (null == label) {
						label = find(By
								.xpath("//*[contains(text(), \"" + field + "\")]/ancestor::label"));
					}
						
					element = find(By.id(label.getAttribute("for")));
					if (element == null) {
						throw new NoSuchElementException(
								"Unable to find element with id, name, or xpath of '"
										+ field + "'.");
					}
				}
			}
		}
		return element;
	}

	private WebElement find(By by) {
		try {
			return d.findElement(by);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

}