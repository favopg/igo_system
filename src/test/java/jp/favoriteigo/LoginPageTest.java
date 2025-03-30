package jp.favoriteigo;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ログイン画面のテストクラス
 *
 * @author イッシー
 * @version 1.0
 * @see
 * @since 1.0
 */
public class LoginPageTest {

    private static Playwright playwright;
    private static Browser browser;
    private Page page;

    @BeforeAll
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void setup() {
        BrowserContext context = browser.newContext();
        page = context.newPage();
    }

    /**
     * ログインぺーいに遷移できること
     */
    @Test
    void testLoginAble() {

        String screenshotPath = "C:\\画面自動テスト\\testLoginAble.png";

        // ログインページにアクセス
        page.navigate("http://localhost/igo_app/login.html");
        // ページが完全に読み込まれたことを確認
        page.waitForLoadState(LoadState.NETWORKIDLE);
        String title = page.title();
        String loginId = page.textContent("#loginId");
        String password = page.textContent("#password");

        // 初期表示確認
        assertEquals("囲碁対戦成績管理", title, "ログインページのタイトル不一致");
        assertEquals("", loginId, "ログインIDは初期表示でブランクではありません");
        assertEquals("", password, "パスワードは初期表示でブランクではありません");

        // スクリーンショット
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));

    }

    @AfterEach
    void teardown() {
        if (page != null) {
            page.close();
        }
    }

    @AfterAll
    static void teardownAll() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

}
