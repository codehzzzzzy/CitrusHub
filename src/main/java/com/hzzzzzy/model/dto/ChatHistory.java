package com.hzzzzzy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatHistory {
    private String role;
    private String content;
}
