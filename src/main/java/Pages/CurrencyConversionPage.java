package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.text.DecimalFormat;
import java.util.List;


public class CurrencyConversionPage {
    WebDriver driver;
    Actions actions;
    DecimalFormat decimalFormat;

    @FindBy(xpath = "//label[text()=\"Buy\"]/following-sibling::input")
    private WebElement inputBuy;

    @FindBy(xpath = "//label[text()=\"Sell\"]/following-sibling::input")
    private WebElement inputSell;

    @FindBy(xpath = "//table/tbody/tr[1]/td[@data-title=\"Paysera rate\"]")
    private WebElement payseraAmount;
    @FindBy(xpath = "//table/tbody/tr[1]/td[@class=\"ng-binding ng-scope commercial-rate\"]")
    private WebElement payseraRate;

    @FindBy(xpath = "//table/tbody/tr[1]/td[@data-title=\"mBank amount\"]")
    private WebElement mBankRate;
    @FindBy(css = "button[data-ng-click='currencyExchangeVM.filterExchangeRates()']")
    private WebElement buttonFilter;

    @FindBy(css = "button[data-ng-click='currencyExchangeVM.clearFilter()']")
    private WebElement buttonClearFilter;

    @FindBy(css = "span[class='dropup']")
    private WebElement countryDropUp;

    @FindBy(css = "button[id='countries-dropdown']")
    private WebElement countriesDropDown;

    @FindBy(xpath = "//label[text()=\"Sell\"]/following::span[@data-ng-bind=\"$select.selected\"][1]")
    private WebElement sellCurrency;

    @FindBy(xpath = "//label[text()=\"Buy\"]/following::span[@data-ng-bind=\"$select.selected\"]/following::i")
    private WebElement buyCurrency;

    @FindBy(xpath = "//label[text()=\"Buy\"]/following::input[@ng-model=\"$select.search\"]")
    private WebElement buyCurrencyInput;

    @FindBy(xpath = "//*[@id=\"currency-exchange-app\"]/div/div/div[2]/table/tbody/tr")
    private List<WebElement> exchangeRatesAll;

    public CurrencyConversionPage(WebDriver driver) {
        this.driver = driver;
        actions = new Actions(driver);
        decimalFormat = new DecimalFormat("0.00");
        PageFactory.initElements(driver, this);
    }

    public CurrencyConversionPage inputBuyAmount(double amount) {
        moveToElement(inputBuy);
        inputBuy.sendKeys(String.valueOf(amount));
        return this;
    }

    public CurrencyConversionPage inputSellAmount(double amount) {
        moveToElement(inputSell);
        inputSell.sendKeys(String.valueOf(amount));
        return this;
    }

    public CurrencyConversionPage checkInputSellIsEmpty() {
        Assert.assertTrue(elementHasClass(inputSell, "ng-empty"), "inputSell is not Empty");
        return this;
    }

    public CurrencyConversionPage checkInputBuyIsEmpty() {
        Assert.assertTrue(elementHasClass(inputBuy, "ng-empty"), "inputBuy is not Empty");
        return this;
    }

    public CurrencyConversionPage checkInputBuyIsNotEmpty() {
        Assert.assertTrue(elementHasClass(inputBuy, "ng-not-empty"), "inputBuy is Empty");
        return this;
    }

    public double getPayseraAmount() {
        moveToElement(payseraAmount);
        return Double.parseDouble(payseraAmount.getText());
    }

    public double getMBankAmount() {
        return Double.parseDouble(mBankRate.getText().split("\\(")[0]);
    }

    public double getExchangeLoss() {
        String[] ls = mBankRate.getText().split("\\(");
        String loss = ls[1].substring(0, ls[1].length() - 1);
        return Double.parseDouble(loss);
    }

    public double getPayseraRate() {
        moveToElement(payseraRate);
        return Double.parseDouble(payseraRate.getText());
    }

    public CurrencyConversionPage changeCountry(String Country) {
        moveToElement(countryDropUp);
        countryDropUp.click();
        countriesDropDown.click();
        WebElement countrySelect = driver.findElement(By.linkText(Country));
        moveToElement(countrySelect);
        countrySelect.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    public CurrencyConversionPage checkPayseraRateAreDifferent(double beforeRate, double afterRate) {
        Assert.assertNotEquals(beforeRate, afterRate);
        return this;
    }

    public CurrencyConversionPage checkSellCurrency(String currency) {
        Assert.assertEquals(sellCurrency.getText(), currency);
        return this;
    }

    public CurrencyConversionPage changeBuyCurrency(String currency) {
        buyCurrency.click();
        buyCurrencyInput.sendKeys(currency);
        buyCurrencyInput.sendKeys(Keys.ENTER);
        return this;
    }

    public CurrencyConversionPage filterExchangeRates() {
        buttonFilter.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    public CurrencyConversionPage clearExchangeRatesFilter() {
        buttonClearFilter.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    public CurrencyConversionPage checkFilteredCurrencyCount(int count) {
        Assert.assertEquals(exchangeRatesAll.size(), count);
        return this;
    }

    public CurrencyConversionPage checkExchangeAmountLoss() {
        double num = Double.parseDouble(decimalFormat.format(getMBankAmount() - getPayseraAmount()));
        if (getMBankAmount() < getPayseraAmount()) {
            Assert.assertEquals(num, getExchangeLoss());
        }
        return this;
    }

    private boolean elementHasClass(WebElement element, String active) {
        String classList = element.getAttribute("class");
        for (String s : classList.split(" ")) {
            if (s.equals(active)) {
                return true;
            }
        }
        return false;
    }

    private void moveToElement(WebElement el) {
        actions.moveToElement(el);
        actions.perform();
    }

}