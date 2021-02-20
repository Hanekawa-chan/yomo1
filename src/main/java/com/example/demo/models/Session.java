package com.example.demo.models;

import com.example.demo.dao.DAO;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.concurrent.TimeUnit;

import static com.gargoylesoftware.htmlunit.BrowserVersion.BEST_SUPPORTED;

public class Session {
    String login;
    String pass;
    String auth;
    WebDriver driver;
    String urlLogin = "https://steamcommunity.com/login/home/";
    String url = "https://steamcommunity.com/id/sorryihavenoname/gcpd/730/?tab=matchhistorycompetitive";
    public boolean logout = false;
    private boolean rly = false;
    private boolean has = false;

    public Session() { start(); }

    public void setName(String login) {
        this.login = login;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void start() {
        driver = new HtmlUnitDriver(BEST_SUPPORTED, true);
        driver.get(urlLogin);
    }

    public void login(DAO dao){
        driver.get(urlLogin);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement log = driver.findElement(By.id("input_username"));
        WebElement pas = driver.findElement(By.id("input_password"));
        WebElement enterDiv = driver.findElement(By.id("login_btn_signin"));
        WebElement enterBut = enterDiv.findElement(By.tagName("button"));

        log.sendKeys(login);
        pas.sendKeys(pass);
        enterBut.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement twoAuth = driver.findElement(By.id("twofactorcode_entry"));
        WebElement authDiv = driver.findElement(By.id("login_twofactorauth_buttonset_entercode")).findElement(By.className("auth_button_h3"));
        twoAuth.sendKeys(auth);

        new Actions(driver).moveToElement(authDiv).click().perform();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.get(url);
        driver.navigate().refresh();
        driver.get(url);

        try {
            WebElement element = driver.findElement(By.id("account_pulldown"));
        }
        catch(NoSuchElementException e) {
            rly = true;
        }

        if(!rly) {
            logout = true;
            refresh(dao);
        }

        rly = false;
    }

    public void refresh(DAO dao) {
        driver.navigate().refresh();

        String date = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                + 2 + "]/td[1]/table/tbody/tr[2]/td")).getText();
        int n = 0;

        while((new Date(date)).year == 2021 || has) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 2 + n; i <= 9 + n; i++) {
                System.out.println(i);
                date = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                        + i + "]/td[1]/table/tbody/tr[2]/td")).getText();
                System.out.println(date);
                Stat stat = new Stat();
                if (!dao.has(date)) {
                    for (int j = 2; j <= 6; j++) {
                        String id = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                                + i + "]/td[2]/table/tbody/tr[" + j + "]/td[1]/div[2]/a")).getAttribute("href").split("/")[4];
                        int k,d,a;
                        k = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                                + i + "]/td[2]/table/tbody/tr[" + j + "]/td[3]")).getText());
                        d = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                                + i + "]/td[2]/table/tbody/tr[" + j + "]/td[5]")).getText());
                        a = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                                + i + "]/td[2]/table/tbody/tr[" + j + "]/td[4]")).getText());
                        double rate = Math.round((k + a * 0.41) / d * 100);
                        if (dao.id(id)) {
                            stat.add(id, rate / 100);
                        }
                    }
                    for (int j = 8; j <= 12; j++) {
                        String id = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                                + i + "]/td[2]/table/tbody/tr[" + j + "]/td[1]/div[2]/a")).getAttribute("href").split("/")[4];
                        int k,d,a;
                        k = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                                + i + "]/td[2]/table/tbody/tr[" + j + "]/td[3]")).getText());
                        d = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                                + i + "]/td[2]/table/tbody/tr[" + j + "]/td[5]")).getText());
                        a = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                                + i + "]/td[2]/table/tbody/tr[" + j + "]/td[4]")).getText());
                        double rate = Math.round((k + a * 0.41) / d * 100);
                        if (dao.id(id)) {
                            stat.add(id, rate / 100);
                        }
                    }
                    if(stat.check()) {
                        Date dat = new Date(date);
                        if(dat.year==2020) {
                            break;
                        }
                        stat.setDate(dat);
                        dao.add(stat);
                    }

                }
                else {
                    has = true;
                    break;
                }

            }

            WebElement we = driver.findElement(By.id("load_more_button"));
            System.out.println("НАШЛИ = " + we.getLocation());

            new Actions(driver).moveToElement(we).click().perform();
            System.out.println("КЛИКНУЛИ");
            n += 8;
        }
        if(has) {
            System.out.println("УЖЕ ЕСТЬ");
        }
        else {
            System.out.println("ДОСТИГНУТ 2020 ГОД");
        }
    }

    public void logout() {
        WebElement logoutThing = driver.findElement(By.id("account_pulldown"));
        new Actions(driver).moveToElement(logoutThing).click().perform();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement logoutButton = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/div/div[3]/div/a[3]"));
        new Actions(driver).moveToElement(logoutButton).click().perform();

        logout = false;
    }

}
