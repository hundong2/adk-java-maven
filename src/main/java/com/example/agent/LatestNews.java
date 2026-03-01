// src/main/java/com/example/agent/LatestNews.java
package com.example.agent;

import java.time.LocalDate;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;
import com.google.adk.web.AdkWebServer;

public class LatestNews {
    public static void main(String[] args) {
        AdkWebServer.start(LlmAgent.builder()
            .name("news-search-agent")
            .description("A news search agent")
            .instruction("""
                You are a news search agent.
                Use the `google_search` tool
                when asked to search for recent events and information.
                Today is \
                """ + LocalDate.now())
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .build());
    }
}
