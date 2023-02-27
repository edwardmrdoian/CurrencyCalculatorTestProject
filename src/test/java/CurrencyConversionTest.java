import Data.Constants;
import Pages.CurrencyConversionPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class CurrencyConversionTest {
    WebDriver driver;
    CurrencyConversionPage conversionPage;
    double amount;

    @BeforeClass
    public void beforeClass() {
        driver = WebDriverManager.chromedriver().create();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(Constants.CurrencyConversionUrl);
        conversionPage = new CurrencyConversionPage(driver);
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    @Test(priority = 1,description = "When user fills BUY amount, SELL amount box is being emptied and vice versa")
    public void checkAmountBox() {
        amount = 50.55;
        conversionPage.inputBuyAmount(amount)
                .checkInputSellIsEmpty()
                .checkInputBuyIsNotEmpty()
                .inputSellAmount(amount)
                .checkInputBuyIsEmpty();
    }

    @Test(priority = 2,description = "When user selects country, rates must be updated")
    public void changeCountry() {
        double payseraRateBefore = conversionPage.getPayseraRate();
        conversionPage.changeCountry("Poland");
        double payseraRateAfter = conversionPage.getPayseraRate();
        conversionPage.checkPayseraRateAreDifferent(payseraRateBefore, payseraRateAfter)
                .checkSellCurrency("PLN");
    }

    @Test(priority = 3,description = "When user filters by USD, only USD should appear")
    public void checkCurrencyFilter() {
        amount = 100;
        conversionPage.inputBuyAmount(amount)
                .changeBuyCurrency("USD")
                .filterExchangeRates()
                .checkFilteredCurrencyCount(1);
    }

    @Test(priority = 4,description = "Text box is displayed, representing the loss")
    public void checkExchangeLoss() {
        conversionPage.checkExchangeAmountLoss();
    }

    @Test(priority = 5,description = "when user clears filter, all currencies should appear")
    public void clearCurrencyFilter() {
        conversionPage.clearExchangeRatesFilter()
                .checkFilteredCurrencyCount(31);
    }
}
