package jp.example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HelloServletTest {

	private static Server server;

	@BeforeAll
	static void startServer() throws Exception {
		server = new Server(8082);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		// Servletの登録
		context.addServlet(new ServletHolder(new HelloServlet()),"/hello");
		server.start();	
	}

	@AfterAll
	static void stopServer() throws Exception {
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}

	/**
	 * jsonデータをServletに渡し、jsonデータを取得できること
	 * @throws Exception
	 */
	@Test
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
}
