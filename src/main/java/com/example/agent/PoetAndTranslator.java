// src/main/java/com/example/agent/PoetAndTranslator.java
package com.example.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.web.AdkWebServer;

public class PoetAndTranslator {
    public static void main(String[] args) {
        LlmAgent poet = LlmAgent.builder()
            .name("poet-agent")
            .description("Poet writing poems")
            .model("gemini-2.5-flash")
            .instruction("""
                You are a talented poet,
                who writes short and beautiful poems.
                """)
            .outputKey("poem")
            .build();

        LlmAgent translator = LlmAgent.builder()
            .name("translator-agent")
            .description("English to Korean translator")
            .model("gemini-2.5-flash")
            .instruction("""
                As an expert English-Korean translator,
                your role is to translate the following poem into Korean,
                ensuring the poem still rhymes even after translation:

                {poem}
                """)
            .outputKey("translated-poem")
            .build();

        AdkWebServer.start(SequentialAgent.builder()
            .name("poet-and-translator")
            .subAgents(poet, translator)
            .build());
    }
}
