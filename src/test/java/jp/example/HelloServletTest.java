package jp.example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

class HelloServletTest {

	private static Server server;
	private static final Logger logger = LoggerFactory.getLogger(HelloServletTest.class);
	
	 private static Playwright playwright;
	 private static Browser browser;
	 private BrowserContext context;
	 private Page page;


	@BeforeAll
	static void startServer() throws Exception {
		server = new Server(8082);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		// 静的HTMLファイルのディレクトリを設定
		context.setResourceBase(Paths.get("src/main/webapp").toAbsolutePath().toString());
		//context.setContextPath("/");
		server.setHandler(context);

		// Servletの登録
		context.addServlet(new ServletHolder(new GetJsonSampleServlet()),"/sample/igo_system");
		context.addServlet(DefaultServlet.class,"/");

		server.start();	
		
		// ここからplaywrightの設定
		playwright = Playwright.create();
		// ヘッドレスモードを無効化
		browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)); 
	}

	@AfterAll
	static void stopServer() throws Exception {
		if (server != null && server.isRunning()) {
			server.stop();
		}
		
		 //browser.close();
	     playwright.close();
	}

	/**
	 * jsonデータをServletに渡し、jsonデータを取得できること
	 * @throws Exception
	 */
	@Disabled
	void testHelloServlet() throws Exception {

		// リクエストテストデータ
		JSONObject requestJson = new JSONObject();
        requestJson.put("name", "John");
        requestJson.put("age", 25);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8082/hello"))
				.POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println("ステータスコード" + response.statusCode()); 
		
		JSONObject result = new JSONObject(response.body());

		assertEquals(200, response.statusCode());
		assertEquals("Hello, Jetty!", result.optString("message"));
	}
	
	@ParameterizedTest
	@CsvSource({
	        "'黒', '白','黒勝ち','abc'",
	        "'黒2', '白2','白勝ち','abc2'",
	        "'黒3', '白3','白6目半勝ち','abc3'"
	})
	void testRegisterFromPlaywright(String blackPlayer, String whitePlayer, String result, String kifu) {
		
		System.out.println("テストデータ確認" + blackPlayer + whitePlayer + result + kifu);
		
		 // 新しいコンテキストとページを作成
        context = browser.newContext();
        page = context.newPage();

        // テスト対象のURLに移動
        page.navigate("http://localhost:8082/sample/register.html");

        // フォームフィールドに値を入力
        page.locator("#blackPlayer").fill(blackPlayer);
        page.locator("#whitePlayer").fill(whitePlayer);
        page.locator("#result").fill(result);
        page.locator("#kifu").fill(kifu);

        // ボタンをクリック
        page.locator("#submit").click();

        // コンテキストを閉じる
        context.close();
	}
	
}
