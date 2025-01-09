package com.hzzzzzy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatHistoryResponse {
    private List<ChatHistory> history;
}
