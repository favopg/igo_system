package jp.example;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightSamplle {

	public static void main(String[] args) {
		
		String screenshotPath = "C:\\画面自動テスト\\screenshot.png";
		
		try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            
            Page page = context.newPage();
            page.navigate("http://localhost:8080/sample/index.html");
            
            // フォームの入力
            page.fill("#name", "John Doe");  // 名前を入力
            page.fill("#email", "john.doe@example.com");  // メールを入力
            
            // ボタンをクリック
            page.click("#submitButton");
                        
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
            
            System.out.println("スクリーンショットを保存しました: " + screenshotPath);
        }
	}

}
