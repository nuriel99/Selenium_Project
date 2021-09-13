package testPackage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
public class ProjectInSelenium {
	WebDriver driver;
	ArrayList<MarketData> list= new ArrayList<MarketData>();
	JavascriptExecutor jse;
	
	public void invokeBrowser(String url) {
		try {
			System.setProperty("webdriver.chrome.driver","c:\\Drivers\\chromedriver.exe");
			driver = new ChromeDriver();
			jse = (JavascriptExecutor)driver;
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
			driver.get(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void firstPart()  {
		try {
			invokeBrowser("https://www.tase.co.il/en");
			driver.findElement(By.cssSelector("body > app-tase > main-tase > privacy-terms > div > div.first-row > div.close-div > div > button")).click();
			driver.findElement(By.xpath("//a[normalize-space()='TA-125']")).click();
			driver.findElement(By.cssSelector("a[aria-label='Click here to go to Market Data']")).click();
			
			List<WebElement> cols = driver.findElements(By.xpath("//index-market-data/gridview-lib/div/div[2]/div/div/div[2]/table/thead/tr/th"));
			int sizeCols = cols.size();
			
			List<WebElement> pages=driver.findElements(By.xpath("//*[@id=\"pageS\"]/pagination-template/ul/li"));
			int pageSize = pages.size();
			
			//from first page to end
			for(int j=3;j<=pageSize-1;j++) {
				System.out.println("page size: "+j);
			    driver.findElement(By.xpath("//*[@id=\"pageS\"]/pagination-template/ul/li["+j+"]")).click();
				Thread.sleep(3000);
				jse.executeScript("scroll(0,1400)");
			
				//calculate how much rows in the table
				List<WebElement> rows = driver.findElements(By.xpath("//gridview-lib/div/div[2]/div/div/div[2]/table/tbody/tr"));
				int sizerows = rows.size();	
				System.out.println("rows: " + sizerows);
				
				// for each row 
				for (int i=1; i<=sizerows; i++) {
					String []name = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 1 +"]")).getText().split("\\r?\\n");
					String symbol = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 2 +"]")).getText();
					String isin = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 3 +"]")).getText();
					String lastRate = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 4 +"]")).getText();
					String change = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 5 +"]")).getText();
					double turnOver = Double.parseDouble(driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 6 +"]")).getText().replace(",",""));
					String lastTrade = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 7 +"]")).getText();
					String basePrice = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 8 +"]")).getText();
					String []openingPrice = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 9 +"]"))
							.getText().split("\\r?\\n");
					String href = driver.findElement(By.xpath("//div/div[2]/div/div/div[2]/table/tbody/tr["+ i +"]/td["+ 1 +"]/a")).getAttribute("href");
					System.out.println("href: "+href);
				    list.add(new MarketData(name[0],symbol, isin,lastRate, change,turnOver, lastTrade,basePrice,openingPrice[0],href));
	     		}
				
				//sort the list by Turnover, high to low
				Collections.sort(list);
				
				//printing to the console (for test )
				for (MarketData a: list) {
					System.out.println(a);
				}
			}
			
			    // printing to file text 
				File file = new File("out.txt");
		        FileWriter fw = new FileWriter(file);
		        PrintWriter pw = new PrintWriter(fw);
		        for (MarketData a: list) {
					pw.println(a);
				}
		        pw.close();
		        jse.executeScript("alert('First part completed');");
		        Thread.sleep(4000);
		        driver.switchTo().alert().accept();
			}
			catch(IOException | InterruptedException e) {
				e.printStackTrace();
			}
	}

	private void secondPart()  {
		try {
		
		// going to use with java-script
		JavascriptExecutor jse1 = (JavascriptExecutor)driver;
		System.out.println("Please enter company name, that is TA-125 or 'Q' to return to the main screen : ");
		Scanner scanner = new Scanner(System.in);
		boolean stay=true;
		MarketData saveInstance=null;
		String inputTemp = scanner.nextLine().toUpperCase();
		if (inputTemp.equals("Q"))
			return;
		
		while(stay) {
			// searching of all list of the table
			for(MarketData x:list) {
				if(x.name.equals(inputTemp)) {
					saveInstance= x;
					stay=false;
				}
			}
			
			if(stay) {
				System.out.println("Please enter company name again, that is TA-125 or 'Q' to return to the main screen : ");
			    inputTemp = scanner.nextLine().toUpperCase();
			}
			// exit from this case, if pressed Q
			if (inputTemp.equals("Q"))
				return;
	     }
		
		System.out.println("Thanks, you entered correct company :) ");
		Thread.sleep(4000);
		driver.navigate().to(saveInstance.href);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("body > app-tase > main-tase > privacy-terms > div > div.first-row > div.close-div > div > button")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("1Y")).click();
		// Scroll page to the graph
		jse.executeScript("scroll(0,40)");
		Thread.sleep(5000);
		
		// get the current date
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_HHmm");
	    String dateAndTime = ft.format(dNow);
	    String nameFile = saveInstance.name.replace(" ","_") + "_" + dateAndTime+".png" ; 
		//taking screenshot
		File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//saving to specific folder
		FileUtils.copyFile(src, new File(".\\screenshots\\"+nameFile));
		Thread.sleep(3000);
		jse.executeScript("alert('Second part completed');");
		Thread.sleep(4000);
		driver.switchTo().alert().accept();
		}
		catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void exit() {
		driver.close();	
	}
	
	public static void main(String []args) {
		ProjectInSelenium obj = new ProjectInSelenium();
		Scanner scanner= new Scanner(System.in);
		boolean startProgram = false;
		boolean flag = true;
		
		while(flag) {
            System.out.println(
                    "Welcome to the program:\n" +
                            "please enter 1-3.\n" +
                            "1. Create a list of all companies of TA-125, sort them by Turnover and save it to text file.\n" +
                            "2. Check if the company is in TA-125, make screenshoot and save it.\n" +
                            "3. Exit the program.\n\n"
            );
            // Input from the uswe
            String input = scanner.next();
            
            // switch case
            switch (input){
                case "1":
                	startProgram=true;
                	obj.firstPart();
                    break;
                case "2":
                	if(!startProgram) 
                		System.out.println("you need firstly press '1', to create a list!\n");
                	
                	if(startProgram) 
                		obj.secondPart();
                    break;
                case "3":
                	flag = false;
                	if(startProgram)
                		obj.exit();
                	break;
                default:
                    System.out.println("Please enter correct number!!!\n");
            }
        }	
	}
}


 
