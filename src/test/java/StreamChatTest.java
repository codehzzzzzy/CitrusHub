import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author hzzzzzy
 * @date 2025/1/2
 * @description
 */
public class StreamChatTest {
    public static void main(String[] args) {
        // 请求 URL
        String url = "http://localhost:3001/api/v1/workspace/hzy/stream-chat";

        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("message", "详细讲一下提前预防");
        requestBody.put("mode", "chat");

        HttpRequest request = HttpRequest.post(url)
                .header("accept", "text/event-stream")
                .header("Authorization", "Bearer VPZJW2Y-9E4453N-G0QW2WJ-TRT5QEG")
                .header("Content-Type", "application/json")
                .body(requestBody.toString());

        // 发送请求并逐行解析 JSON 响应
        try (HttpResponse response = request.executeAsync();
             BufferedReader reader = new BufferedReader(new InputStreamReader(response.bodyStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // 只处理带 "data: " 前缀的行
                if (line.startsWith("data: ")) {
                    // 去掉 "data: " 前缀
                    String jsonData = line.substring(6).trim();
                    if (!jsonData.isEmpty()) {
                        // 直接解析 JSON 数据并提取 textResponse
                        String textResponse = JSONUtil.parseObj(jsonData).getStr("textResponse");
                        if (textResponse != null) {
                            for (char ch : textResponse.toCharArray()) {
                                System.out.print(ch);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
