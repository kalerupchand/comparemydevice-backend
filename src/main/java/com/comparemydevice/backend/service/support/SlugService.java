// src/main/java/com/comparemydevice/backend/service/support/SlugService.java
package com.comparemydevice.backend.service.support;

import java.util.function.Predicate;

import static com.comparemydevice.backend.util.SlugUtil.toSlug;

public class SlugService {

    /**
     * Ensure uniqueness by appending -2, -3... using the provided existsBySlug checker.
     */
    public String ensureUnique(String raw, Predicate<String> existsBySlug) {
        String base = toSlug(raw);
        String candidate = base;
        int i = 1;
        while (existsBySlug.test(candidate)) {
            candidate = base + "-" + (++i);
        }
        return candidate;
    }
}