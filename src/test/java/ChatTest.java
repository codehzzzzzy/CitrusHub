import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @author hzzzzzy
 * @date 2025/1/1
 * @description
 */
public class ChatTest {
    public static void main(String[] args) {
        // 请求 URL
        String url = "http://localhost:3001/api/v1/workspace/hzy/chat";

        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("message", "AnythingLLM是什么？（使用简体中文回答）");
        requestBody.put("mode", "chat");

        // 发送 POST 请求
        HttpResponse response = HttpRequest.post(url)
                .header("accept", "application/json")
                .header("Authorization", "Bearer VPZJW2Y-9E4453N-G0QW2WJ-TRT5QEG")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .execute();

        // 解析响应内容
        LLMResponse llmResponse = JSONUtil.toBean(response.body(), LLMResponse.class);
        System.out.println(llmResponse.getTextResponse());
    }
}
