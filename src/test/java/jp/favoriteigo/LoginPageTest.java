package jp.favoriteigo;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ログイン画面のテストクラス
 *
 * @author イッシー
 * @version 1.0
 * @see jp.favoriteigo.controller.LoginServlet
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
     * ログインページに遷移できること
     */
    @Test
    void testLoginInit() {

        String screenshotPath = "C:\\画面自動テスト\\testLoginInit.png";

        // ログインページにアクセス
        page.navigate("http://localhost/igo_app/login.html");
        // ページが完全に読み込まれたことを確認
        page.waitForLoadState(LoadState.NETWORKIDLE);
        String title = page.title();
        String loginId = page.textContent("#loginId");
        String password = page.textContent("#password");
        String message = page.textContent("h5");

        // 初期表示確認
        assertEquals("囲碁対戦成績管理", title, "ログインページのタイトル不一致");
        assertEquals("", loginId, "ログインIDは初期表示でブランクではありません");
        assertEquals("", password, "パスワードは初期表示でブランクではありません");
        assertEquals("", message, "メッセージは初期表示でブランクではありません");

        // スクリーンショット
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));

    }

    /**
     * ログインページでエラーになるログインIDを入力し、メッセージが表示できること
     */
    @Test
    void testLoginError() {

        String screenshotPath = "C:\\画面自動テスト\\testLoginError.png";

        // ログインページにアクセス
        page.navigate("http://localhost/igo_app/login.html");
        // ページが完全に読み込まれたことを確認
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // フォームの入力
        page.fill("#loginId", "エラーログイン");
        page.fill("#password", "1234567890");

        // ボタンをクリック
        page.click("button");
        // 5秒間待機
        page.waitForSelector("h5", new Page.WaitForSelectorOptions().setTimeout(5000));
        // メッセージ領域
        String message = page.textContent("h5");

        // スクリーンショット
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));

        // エラーメッセージ検証
        assertEquals("ログインIDかパスワードのいずれかが間違っています。", message, "ログイン認証エラー時にエラーメッセージが表示されていません。");

    }

    /**
     * ログインIDとパスワードが未入力の場合にhtml5仕様のエラーメッセージがログインIDに表示されること
     */
    @Test
    void testLoginErrorFromBlank() {

        String screenshotPath = "C:\\画面自動テスト\\testLoginErrorFromLoginIdBlank.png";

        // ログインページにアクセス
        page.navigate("http://localhost/igo_app/login.html");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // ボタンをクリック
        page.click("button");

        // ログインIDに表示されているエラーメッセージ内容を取得
        Locator inputField = page.locator("#loginId");
        String validationMessage = inputField.evaluate("input => input.validationMessage").toString();

        // スクリーンショット
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));

        // エラーメッセージ検証
        assertEquals("このフィールドを入力してください。", validationMessage, "html仕様エラーが出ていません");

    }

    /**
     * ログインIDとパスワードが未入力の場合にhtml5仕様のエラーメッセージがログインIDに表示できること
     */
    @Test
    void testLoginErrorFromPasswordBlank() {

        String screenshotPath = "C:\\画面自動テスト\\testLoginErrorFromPasswordBlank.png";

        // ログインページにアクセス
        page.navigate("http://localhost/igo_app/login.html");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // ログインIDを入力
        page.fill("#loginId", "ロボット");

        // ボタンをクリック
        page.click("button");

        // パスワードに表示されているエラーメッセージ内容を取得
        Locator inputField = page.locator("#password");
        String validationMessage = inputField.evaluate("input => input.validationMessage").toString();

        // スクリーンショット
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));

        // エラーメッセージ検証
        assertEquals("このフィールドを入力してください。", validationMessage, "html仕様エラーが出ていません");

    }

    /**
     * ログインIDとパスワードが入力済の場合にエラーメッセージが返却できること
     */
    @Test
    void testLoginErrorFromMock() {

        String screenshotPath = "C:\\画面自動テスト\\testLoginErrorFromMock.png";

        // ログインページにアクセス
        page.navigate("http://localhost/igo_app/login.html");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // ログインIDを入力
        page.fill("#loginId", "ロボット");
        // パスワードを入力
        page.fill("#password", "123");

        // モックレスポンス用のJSONObjectを作成
        JSONObject mockResponse = new JSONObject();
        mockResponse.put("status", "error");
        mockResponse.put("message", "モック用エラー");

        // モック化するエンドポイントとそのレスポンスを設定
        page.route("**/igo_app/login", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setContentType("application/json")
                    .setBody(mockResponse.toString())
                    .setStatus(200));
        });

        // ボタンをクリック
        page.click("button");

        // 5秒間待機
        page.waitForSelector("h5", new Page.WaitForSelectorOptions().setTimeout(5000));
        // メッセージ領域
        String message = page.textContent("h5");

        // スクリーンショット
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));

        // エラーメッセージ検証
        assertEquals("ログインIDかパスワードのいずれかが間違っています。", message, "モック結果がエラーで返ってきてないです");
    }

    /**
     * ログインページからログイン認証して対戦一覧画面に遷移できること
     */
    @Test
    void testLoginAble() {

        String screenshotPath = "C:\\画面自動テスト\\testLoginAble.png";

        // ログインページにアクセス
        page.navigate("http://localhost/igo_app/login.html");
        // ページが完全に読み込まれたことを確認
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // フォームの入力
        page.fill("#loginId", "ロボット");
        page.fill("#password", "123");

        // ボタンをクリック
        page.click("button");

        // ページ遷移確認
        page.waitForURL("http://localhost/igo_app/match_list.html");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // スクリーンショット
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));

        // ログイン認証OKであることを確認
        assertEquals("http://localhost/igo_app/match_list.html", page.url(), "表示ページが想定と違います");
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
