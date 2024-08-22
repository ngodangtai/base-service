package com.company.module.utils;

import com.company.module.common.Constants.Character;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.util.Objects;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VNAccentUtils {

    public static String removeVietnameseAccent(String src) {
        if (Objects.isNull(src)) {
            return null;
        }
        String temp = Normalizer.normalize(src, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll(Character.EMPTY)
                .replace("Đ", "D")
                .replace("đ", "d")
                .replaceAll("[^a-zA-Z0-9 ]+","");
    }
}
