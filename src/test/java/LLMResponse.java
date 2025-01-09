import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/1
 * @description
 */
@AllArgsConstructor
@Data
public class LLMResponse {
    private String id;
    private String type;
    private boolean close;
    private String error;
    private int chatId;
    private String textResponse;
    private String[] sources;
    private Metrics metrics;

    @Data
    @AllArgsConstructor
    public static class Metrics {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;
        private double outputTps;
        private double duration;
    }
}
