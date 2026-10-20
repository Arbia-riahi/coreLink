package com.factory;

public class DriverManagerFactory {

 // browserName accepté: "chrome", "firefox", "edge"
 public static DriverManager getDriverManager(String browserName) {
     if (browserName == null) browserName = "chrome";
     String b = browserName.trim().toLowerCase();

     switch (b) {
         case "firefox":
             return new FirefoxDriverManager();
         case "edge":
             return new EdgeDriverManager();
         case "chrome":
         default:
             return new ChromeDriverManager();
     }
 }
}


