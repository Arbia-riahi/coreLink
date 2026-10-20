package com.utils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.pageObjects.BasePage;
/** driver. */

public class DateUtil extends BasePage {
	private static WebDriver driver;
	public DateUtil() {
		super(driver);
	
	}

	// Get The Current Day
	public static String getCurrentDay() {
		// Create a Calendar Object
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		// Get Current Day as a number
		int todayInt = calendar.get(Calendar.DAY_OF_MONTH);
		logger.info("Today Int: {}", todayInt);
		// Integer to String Conversion
		String todayStr = Integer.toString(todayInt);
		logger.info("Today Str: {}" + todayStr);
		return todayStr;
	}

	// Get The Current Day plus days. You can change this method based on your
	// needs.
	public static String getCurrentDayPlus(int days) {
		LocalDate currentDate = LocalDate.now();
		int dayOfWeekPlus = currentDate.getDayOfWeek().plus(days).getValue();
		return Integer.toString(dayOfWeekPlus);
	}

	/**
	 * Accesseur de l attribut date in java.
	 *
	 * @return date in java
	 */
	// Get The Date in Java
	public static String getDateInJava() {
		// Create object of SimpleDateFormat class and decide the format
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		// get current date time with Date()
		Date date = new Date();

		// Now format the date
		String date1 = dateFormat.format(date).trim();

		// Print the Date
		logger.info(date1);
		return date1;
	}

	/**
	 * methode Click given day
	 *
	 * @param elementList
	 * @param day
	 * @return
	 */
	// Click to given day
	public static String clickGivenDay(List<WebElement> elementList, String day) {
		// DatePicker is a table. Thus we can navigate to each cell
		// and if a cell matches with the current date then we will click it.
		/** Functional JAVA version of this method. */
		elementList.stream().filter(element -> element.getText().contains(day)).findFirst()
				.ifPresent(WebElement::click);
		return day;
	}
}