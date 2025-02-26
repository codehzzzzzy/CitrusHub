package com.hzzzzzy.sensitive;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * AC自动机
 */
@Component
public class ACFilter implements SensitiveWordFilter{

    private ACTrie acProTrie;

    @Override
    public boolean hasSensitiveWord(String text) {
        if(StringUtils.isBlank(text)) return false;
        return !Objects.equals(filter(text),text);
    }

    @Override
    public String filter(String text) {
        return acProTrie.match(text);
    }

    @Override
    public void loadWord(List<String> words) {
        if (words == null) return;
        acProTrie = new ACTrie();
        acProTrie.createACTrie(words);
    }
}
