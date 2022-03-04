import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {

		File input = new File(
				"C:\\Users\\tiger\\Downloads\\소상공인시장진흥공단_상가(상권)정보_20211231\\소상공인시장진흥공단_상가(상권)정보_서울_202112_new.csv"); // 현재
																														// //
																														// 지정.
		BufferedReader br = null;

		File output = new File(
				"C:\\Users\\tiger\\Downloads\\소상공인시장진흥공단_상가(상권)정보_20211231\\소상공인시장진흥공단_상가(상권)정보_서울_202112_img.txt");
		BufferedWriter bw = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));

			br.readLine();
			String line = "";
			int cnt = 0;
			int progress = 0;
			while ((line = br.readLine()) != null) {
				if (cnt == 10) {
					cnt = 0;
					Thread.sleep(1100); // 1초 대기 - rate 제한 피하기 위함
				}
				if (progress % 100 == 0) {
					System.out.println("progress: " + progress);
				}
				String[] arr = line.split(",");
				String name = arr[0]; // 이름
				String apiURL = "https://openapi.naver.com/v1/search/image?sort=sim&query=" + URLEncoder(name + " 후기", "utf-8");

				Map<String, String> requestHeaders = new HashMap<>();
				requestHeaders.put("X-Naver-Client-Id", "-");
				requestHeaders.put("X-Naver-Client-Secret", "-");
				String responsebody = get(apiURL, requestHeaders);

//				System.out.println(responsebody);
				JSONObject jObject = new JSONObject(responsebody);
//				System.out.println(jObject.get("items"));
				if (!jObject.has("items")) {
					bw.write("N/A");
					bw.newLine();
					cnt++;
					progress++;
					continue;
				}
				JSONArray jArray = (JSONArray) jObject.get("items");
				if (jArray.length() == 0) {
					bw.write("N/A");
					bw.newLine();
					cnt++;
					progress++;
					continue;
				}

				JSONObject jObject2 = (JSONObject) jArray.get(0);
//				System.out.println(jObject2.get("thumbnail"));
				String imgsrc = (String) jObject2.get("link");
				bw.write(imgsrc);

				bw.newLine();
				cnt++;
				progress++;
			}
			System.out.println("파일생성완료");

		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.flush(); // 남아있는 데이터까지 보내 준다
					bw.close(); // 사용한 BufferedWriter를 닫아 준다

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);
		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();
			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

}
