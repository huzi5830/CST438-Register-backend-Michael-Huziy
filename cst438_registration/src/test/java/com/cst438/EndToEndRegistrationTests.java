package com.cst438;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.*;
import com.cst438.controller.*;
import com.cst438.service.*;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;


public class EndToEndRegistrationTests {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:\\chromedriver-win64\\chromedriver.exe";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_EMAIL = "testSelenium@csumb.edu";
	
	public static final String TEST_USER_NAME = "testName";

	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	@Test
	public void addStudentTest() throws Exception{
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			WebElement adminLink = driver.findElement(By.linkText("Admin"));
			adminLink.click();
			System.out.println("CLICKED ADMIN");
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("custom-button")).click();
			Thread.sleep(SLEEP_DURATION);
			
			WebElement nameField = driver.findElement(By.name("name"));
			nameField.sendKeys(TEST_USER_NAME);
			Thread.sleep(SLEEP_DURATION);
			WebElement emailField = driver.findElement(By.name("email"));
			emailField.sendKeys(TEST_USER_EMAIL);
			Thread.sleep(SLEEP_DURATION);
			WebElement statusField = driver.findElement(By.name("status"));
			statusField.sendKeys("SeleniumTest");
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("addStudentBtn")).click();
			Thread.sleep(SLEEP_DURATION);
			System.out.println("ADDED STUDENT");
			WebElement student = driver.findElement(By.xpath("//tr[td='"+TEST_USER_EMAIL+"']"));
			Thread.sleep(SLEEP_DURATION);
			assertNotNull(student);
			  Thread.sleep(SLEEP_DURATION+1000);
			/*List<WebElement> rows = driver.findElements(By.xpath(xpathExpression));
			Thread.sleep(SLEEP_DURATION);
			for (WebElement row:rows) {
				  List<WebElement> cells = row.findElements(By.tagName("td"));
				  System.out.print(cells.get(1).getText() + "   ");
				  System.out.println(cells.get(2).getText());
			}*/
			
			 
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.toString());
			throw ex;
		} finally {
			driver.quit();
		}

	}
	
	@Test
	public void updateStudentTest() throws Exception{
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			WebElement adminLink = driver.findElement(By.linkText("Admin"));
			adminLink.click();
			System.out.println("CLICKED ADMIN");
			Thread.sleep(SLEEP_DURATION);
			
			driver.findElement(By.id("custom-button")).click();
			Thread.sleep(SLEEP_DURATION);
			
			WebElement nameField = driver.findElement(By.name("name"));
			nameField.sendKeys(TEST_USER_NAME);
			Thread.sleep(SLEEP_DURATION);
			WebElement emailField = driver.findElement(By.name("email"));
			emailField.sendKeys(TEST_USER_EMAIL);
			Thread.sleep(SLEEP_DURATION);
			WebElement statusField = driver.findElement(By.name("status"));
			statusField.sendKeys("UserAdded");
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("addStudentBtn")).click();
			Thread.sleep(SLEEP_DURATION);
			System.out.println("ADDED STUDENT");
			WebElement element = driver.findElement(By.xpath("//tr[td='"+TEST_USER_EMAIL + "']"));
			Thread.sleep(SLEEP_DURATION);
			assertNotNull(element);
			
			WebElement update = element.findElement(By.name("updateBtn") );//Updating student
			assertNotNull(update);
			update.click();
			Thread.sleep(SLEEP_DURATION);
			WebElement newnameField = driver.findElement(By.name("name"));
			newnameField.clear();
			String updatedName = TEST_USER_NAME + " Updated";
			newnameField.click(); 
			while (!newnameField.getAttribute("value").isEmpty()) {
			    newnameField.sendKeys(Keys.CONTROL + "a");
			    newnameField.sendKeys(Keys.DELETE);
			}
			newnameField.sendKeys(updatedName);
			Thread.sleep(SLEEP_DURATION);
			WebElement newstatusField = driver.findElement(By.name("status"));
			newstatusField.click();
			while (!newstatusField.getAttribute("value").isEmpty()) {
			    newstatusField.sendKeys(Keys.CONTROL + "a");
			    newstatusField.sendKeys(Keys.DELETE);
			}
			newstatusField.sendKeys("UserUpdated");
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("dialogBtnSubmit")).click();
			
		    Thread.sleep(SLEEP_DURATION);
		    
		    WebElement updatedRow = driver.findElement(By.xpath("//tr[td='"+ updatedName + "']"));
		    assertNotNull(updatedRow);
		    List<WebElement> cells = updatedRow.findElements(By.tagName("td"));
		    System.out.println(cells.get(1));
		    System.out.println("UPDATED STUDENT");
		    Thread.sleep(SLEEP_DURATION+1000);
		    
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.toString());
			throw ex;
		} finally {
			driver.quit();
		}
	
	}
	@Test
	public void dropStudentTest() throws Exception{
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			WebElement adminLink = driver.findElement(By.linkText("Admin"));
			adminLink.click();
			System.out.println("CLICKED ADMIN");
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("custom-button")).click();
			Thread.sleep(SLEEP_DURATION);
			
			WebElement nameField = driver.findElement(By.name("name"));
			nameField.sendKeys(TEST_USER_NAME);
			Thread.sleep(SLEEP_DURATION);
			WebElement emailField = driver.findElement(By.name("email"));
			emailField.sendKeys(TEST_USER_EMAIL);
			Thread.sleep(SLEEP_DURATION);
			WebElement statusField = driver.findElement(By.name("status"));
			statusField.sendKeys("SeleniumTest");
			
			driver.findElement(By.id("addStudentBtn")).click();
			Thread.sleep(SLEEP_DURATION);
			System.out.println("ADDED STUDENT");
			
			WebElement student = driver.findElement(By.xpath("//tr[td='"+TEST_USER_EMAIL+"']"));
			Thread.sleep(SLEEP_DURATION-200);
			assertNotNull(student);
			
			WebElement drop = student.findElement(By.id("dropbtn") );
			assertNotNull(drop);
			drop.click();Thread.sleep(SLEEP_DURATION );
			
			Alert simpleAlert = driver.switchTo().alert();
		    Thread.sleep(SLEEP_DURATION +500);
	        simpleAlert.accept();
	    
	        assertThrows(NoSuchElementException.class, () -> {
            	driver.findElement(By.xpath("//tr[td='"+TEST_USER_NAME+"']"));
            });	
	        System.out.println("STUDENT DELETED");
	        
			 
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.toString());
			throw ex;
		} finally {
			driver.quit();
		}

	}
	
}
