package jp.favoriteigo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

import jakarta.servlet.MultipartConfigElement;

import jp.favoriteigo.controller.CsvUploadServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class CsvUploadServletTest {

	private static Server server;

	@BeforeAll
	static void startServer() throws Exception {
		server = new Server(8082);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		ServletHolder servletHolder = new ServletHolder(new CsvUploadServlet());
		// MultipartConfigを適用する
		servletHolder.getRegistration().setMultipartConfig(
			    new MultipartConfigElement("/tmp", 10 * 1024 * 1024, 50 * 1024 * 1024, 1024 * 1024)
		);
		// Servletの登録
		context.addServlet(servletHolder, "/sample/uploadCsv");
		
		server.start();
	}

	@AfterAll
	static void stopServer() throws Exception {
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}

	/**
	 * CSVファイルをServletに送信する
	 * @throws Exception
	 */
	@Test
	void testHelloServlet() throws Exception {

		File csvFile = new File(getClass().getClassLoader().getResource("csv/test.csv").toURI());

		// ファイルをアップロード
		HttpURLConnection connection = createFileUploadConnection(csvFile, "text/csv");

		// レスポンスを検証
		assertEquals(200, connection.getResponseCode());

		connection.disconnect();

	}

	private HttpURLConnection createFileUploadConnection(File file, String contentType) throws Exception {
		
		URI uri = new URI("http://localhost:8082/sample/uploadCsv");
		URL url = uri.toURL();
		
		// TODO HttpURLConnectionを使用しているが、HttpRequestを使用してもできそう
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---Boundary");

		// リクエストボディを構築
		String boundary = "---Boundary";
        String fileContent = Files.readString(file.toPath());
        String body = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"test.csv\"\r\n" +
                "Content-Type: text/csv\r\n\r\n" +
                fileContent + "\r\n" +
                "--" + boundary + "--";
        connection.getOutputStream().write(body.getBytes());

		return connection;

	}

}
