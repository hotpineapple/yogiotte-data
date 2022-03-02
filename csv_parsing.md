# 공공데이터 파싱하기

## 1. CSV 파일 다운로드
[소상공인시장진흥공단_상가(상권)정보](https://www.data.go.kr/data/15083033/fileData.do)

## 2. 파싱을 통해 데이터 가공 및 필요한 정보만 가지고 새 파일 생성 
```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main {
	public static void main(String[] args) throws IOException {

		File input = new File(
				"C:\\Users\\tiger\\Downloads\\소상공인시장진흥공단_상가(상권)정보_20211231\\소상공인시장진흥공단_상가(상권)정보_경기_202112.csv"); // 현재																										// 지정.
		BufferedReader br = null;

		File output = new File(
				"C:\\Users\\tiger\\Downloads\\소상공인시장진흥공단_상가(상권)정보_20211231\\소상공인시장진흥공단_상가(상권)정보_경기_202112_new.csv");
		BufferedWriter bw = null;

		String line = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));

			bw.write("name,type,subtype,address,code,lat,lng");
			bw.newLine();
			
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("\",\"");
				String type1 = arr[4]; // 대분류
				
				if (type1.equals("음식")) {
					String name = arr[1]; // 이름
					String type2 = arr[6]; // 중분류
					String type3 = arr[8]; // 소분류
					String address = arr[14] + " " + arr[16]; // 주소
					String code = arr[17]; // 법정동코드
					String latlng = arr[arr.length-1]; // 위도,경도
					String lat = latlng.split(",")[2]; // 위도
					String lng = latlng.split(",")[1]; // 경도

					String res = "";
					if(type2.equals("커피점/카페")) { // 카페
						res = name + "," + "카페 "+ "," + " " + "," + address + "," + code + "," + lat + "," + lng; 
					}else if(type2.equals("유흥주점")) { // 바
						res = name + "," + "바 "+ "," + type3 + "," + address + "," + code + "," + lat + "," + lng; 
					}else { //일반 음식점
						res = name + "," + "식당 "+ "," + type3 + "," + address + "," + code + "," + lat + "," + lng; 
					}
					
					bw.write(res);
					bw.newLine();
				}
			}
			System.out.println("파일생성완료");
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
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

}

```
