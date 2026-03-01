// src/main/java/com/example/agent/SearchAgentAsTool.java
package com.example.agent;

import java.time.LocalDate;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.GoogleSearchTool;
import com.google.adk.web.AdkWebServer;

public class SearchAgentAsTool {
    public static void main(String[] args) {
        // 1. Define the specialized Search Agent
        LlmAgent searchAgent = LlmAgent.builder()
            .name("news-search-agent-tool")
            .description("Searches for recent events and provides a concise summary.")
            .instruction("""
                You are a concise information retrieval specialist.
                Use the `google_search` tool to find information.
                Always provide the answer as a short,
                direct summary, without commentary.
                Today is \
                """ + LocalDate.now())
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool()) // This agent uses the Google Search Tool
            .build();

        // 2. Wrap the Search Agent as a Tool
        AgentTool searchTool = AgentTool.create(searchAgent);

        // 3. Define the Main Agent that uses the Search Agent Tool
        AdkWebServer.start(LlmAgent.builder()
            .name("main-researcher")
            .description("Main agent for answering complex, up-to-date questions.")
            .instruction("""
                You are a sophisticated research assistant.
                When the user asks a question that requires up-to-date or external information,
                you MUST use the `news-search-agent-tool` to get the facts before answering.
                After the tool returns the result, synthesize the final answer for the user.
                """)
            .model("gemini-2.5-flash")
            .tools(searchTool) // This agent uses the Search Agent as a tool
            .build()
       );
    }
}
