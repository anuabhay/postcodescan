package com.hes;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import com.hes.postcodescan.data.Business;

import java.util.List;

public class YPScraper {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private static  YPScraper yp = null;

  @Before
  public void setUp() throws Exception {
    // https://stackoverflow.com/questions/49789963/org-openqa-selenium-sessionnotcreatedexception-unable-to-find-a-matching-set-of
    DesiredCapabilities dc = new DesiredCapabilities();
    dc.setCapability("marionatte", false);
    FirefoxOptions opt = new FirefoxOptions();
    opt.addPreference("network.proxy.type", 0);
    opt.setHeadless(true);
    opt.merge(dc);
    driver = new FirefoxDriver(opt);
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public ArrayList<Business> runYPScan(String locations, String types) throws Exception {
    ArrayList<Business> list = new ArrayList<Business>();
    Params config = ParamReader.loadConfig(locations,types);
    int type_count = 0;
    while (config.types.size() > type_count){
      String type = (String)config.types.get(type_count);
      type_count++;
      int loc_count = 0;
      while (config.locations.size() > loc_count){
        String location = (String)config.locations.get(loc_count);
        ArrayList<Business> business_list = getData(type,location);
        list.addAll(business_list);
        loc_count++;
      }
    }
    return list;
  }

  ArrayList<Business> getData(String type, String location){
    ArrayList<Business> business_list = new ArrayList<Business>();
    driver.get("https://www.yellowpages.com.au/");
    driver.findElement(By.id("clue")).click();
    driver.findElement(By.id("clue")).clear();
    driver.findElement(By.id("clue")).sendKeys(type);
    driver.findElement(By.id("where")).click();
    driver.findElement(By.id("where")).sendKeys(location);
    driver.findElement(By.id("clue")).sendKeys(Keys.ENTER);
    List<WebElement>  listOfElements = driver.findElements(By.cssSelector("div.find-show-more-trial"));
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);

    //CVSWriter1.writeRecord("---------- " + location + " ----------");

    for (int i=1; i<listOfElements.size();i++){
      List<WebElement> elements_name  = listOfElements.get(i).findElements(By.cssSelector("div.listing-data[data-full-name]"));
      String name = "";
      String email = "";
      String phone = "";
      String full_address = "";
      String sububrb = "";
      String address = "";
      if (elements_name.size() > 0) {
        name = elements_name.get(0).getAttribute("data-full-name");
        full_address = elements_name.get(0).getAttribute("data-full-address");
        sububrb = elements_name.get(0).getAttribute("data-suburb");
        address = full_address + " " + sububrb;
      }

      // Email
      List<WebElement> elements_email  = listOfElements.get(i).findElements(By.cssSelector("a.contact[data-email]"));
      if (elements_email.size() > 0) {
        email = elements_email.get(0).getAttribute("data-email");
      }

      // Phone
      List<WebElement> elements_phone  = listOfElements.get(i).findElements(By.cssSelector("a.contact-phone[href]"));
      if (elements_phone.size() > 0) {
        phone = elements_phone.get(0).getAttribute("href").replace("tel:","");
      }

      //CVSWriter1.writeRecord(name , email , phone, address);
      business_list.add(new Business(name,email,phone,address));
    }

    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    return business_list;
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }

  static String[] parse_args(String[] args){
    ArgumentParser parser = ArgumentParsers.newArgumentParser("YPScraper")
            .defaultHelp(true)
            .description("Calculate checksum of given files.");
    parser.addArgument("-l", "--locations")
            .setDefault("locations.json")
            .help("Locations file to be used");
    parser.addArgument("-t", "--types")
            .setDefault("types.json")
            .help("Business Types file to be used");
    Namespace ns = null;
    try {
      ns = parser.parseArgs(args);
    } catch (ArgumentParserException e) {
      parser.handleError(e);
      System.exit(1);
    }
    return new String[]{(String)ns.get("locations"), (String)ns.get("types")};

  }

  public static void load() throws  Exception{
    if(YPScraper.yp == null) {
      YPScraper yp = new YPScraper();
      yp.setUp();
      YPScraper.yp = yp;
    }

  }
  public static ArrayList<Business> getBusinessData(String locations, String types) throws  Exception{
//    ArrayList<Business> business_list = new ArrayList<Business>();
//    business_list.add(new Business("name1","email1","phone1","address1"));
//    business_list.add(new Business("name2","email2","phone2","address2"));
//    business_list.add(new Business("name3","email3","phone1","address1"));
//    business_list.add(new Business("name4","email4","phone2","address2"));
//    business_list.add(new Business("name5","email5","phone1","address1"));
//    business_list.add(new Business("name6","email6","phone2","address2"));
//    business_list.add(new Business("name1","email1","phone1","address1"));
//    business_list.add(new Business("name2","email2","phone2","address2"));
//    business_list.add(new Business("name3","email3","phone1","address1"));
//    business_list.add(new Business("name4","email4","phone2","address2"));
//    business_list.add(new Business("name5","email5","phone1","address1"));
//    business_list.add(new Business("name6","email6","phone2","address2"));
//    business_list.add(new Business("name1","email1","phone1","address1"));
//    business_list.add(new Business("name2","email2","phone2","address2"));
//    business_list.add(new Business("name3","email3","phone1","address1"));
//    business_list.add(new Business("name4","email4","phone2","address2"));
//    business_list.add(new Business("name5","email5","phone1","address1"));
//    business_list.add(new Business("name6","email6","phone2","address2"));
//    business_list.add(new Business("name1","email1","phone1","address1"));
//    business_list.add(new Business("name2","email2","phone2","address2"));
//    business_list.add(new Business("name3","email3","phone1","address1"));
//    business_list.add(new Business("name4","email4","phone2","address2"));
//    business_list.add(new Business("name5","email5","phone1","address1"));
//    business_list.add(new Business("name6","email6","phone2","address2"));
//    try
//    {
//      Thread.sleep(1000);
//    }
//    catch(InterruptedException ex)
//    {
//      Thread.currentThread().interrupt();
//    }
//    return  business_list;

   YPScraper.load();
    return YPScraper.yp.runYPScan(locations,types);
  }
}