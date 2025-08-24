// src/main/java/com/comparemydevice/backend/util/SlugUtil.java
package com.comparemydevice.backend.util;

import java.text.Normalizer;
import java.util.Locale;

public final class SlugUtil {
    private SlugUtil() {}

    public static String toSlug(String input) {
        if (input == null) return "item";
        String n = Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        n = n.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
        return n.isBlank() ? "item" : n;
    }
}