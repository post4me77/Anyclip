package com.anyclip.review;

import java.io.IOException;

/** Offline contract checks; no API key or HTTP requests. */
public final class ClientContractTest {
    public static void main(String[] args) throws Exception {
        check("1: a\n2: b\n3: \n".equals(CloudReviewClient.numbered("a\r\nb\n")), "Line numbering");
        check("Заключение".equals(CloudReviewClient.extractReview(
            "{\"choices\":[{\"message\":{\"content\":\"Заключение\"},\"finish_reason\":\"stop\"}]}")), "Valid response");
        check(CloudReviewClient.extractReview(
            "{\"choices\":[{\"message\":{\"content\":\"Partial\"},\"finish_reason\":\"length\"}]}")
            .contains("Ответ сокращен"), "Truncation notice");
        for (String invalid : new String[]{"bad json", "{}", "{\"choices\":[]}",
            "{\"choices\":[{\"message\":{\"content\":\" \"}}]}"}) {
            try { CloudReviewClient.extractReview(invalid); throw new AssertionError("Invalid response accepted"); }
            catch (IOException expected) { }
        }
        System.out.println("Offline client contract checks passed");
    }
    private static void check(boolean condition, String label) {
        if (!condition) throw new AssertionError(label);
    }
}
