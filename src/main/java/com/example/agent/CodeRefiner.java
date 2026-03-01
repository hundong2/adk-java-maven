// src/main/java/com/example/agent/CodeRefiner.java
package com.example.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.ExitLoopTool;
import com.google.adk.web.AdkWebServer;

public class CodeRefiner {
    public static void main(String[] args) {
        var codeGenerator = LlmAgent.builder()
            .name("code-generator")
            .description("Writes and refines code based on a request and feedback.")
            .instruction("""
                Your role is to write a Python function based on the user's request.
                In the first turn, write the initial version of the code.
                In subsequent turns, you will receive feedback on your code.
                Your task is to refine the code based on this feedback.

                Previous feedback (if any):
                {feedback?}
                """)
            .model("gemini-2.5-flash")
            .outputKey("generated_code")
            .build();

        var codeReviewer = LlmAgent.builder()
            .name("code-reviewer")
            .description("Reviews code and decides if it's complete or needs more work.")
            .instruction("""
                Your role is to act as a senior code reviewer.
                Analyze the provided Python code for correctness, style, and potential bugs.

                Code to review:
                {generated_code}

                If the code is perfect and meets the user's request,
                you MUST call the `exit_loop` tool.

                Otherwise, provide constructive feedback for the `code-generator to improve the code.
                """)
            .model("gemini-2.5-flash")
            .outputKey("feedback")
            .tools(ExitLoopTool.INSTANCE)
            .build();

        var codeRefinerLoop = LoopAgent.builder()
            .name("code-refiner-loop")
            .description("Iteratively generates and reviews code until it is correct.")
            .subAgents(
                codeGenerator,
                codeReviewer
            )
            .maxIterations(3) // Safety net to prevent infinite loops
            .build();

        var finalPresenter = LlmAgent.builder()
            .name("final-presenter")
            .description("Presents the final, accepted code to the user.")
            .instruction("""
                The code has been successfully generated and reviewed.
                Present the final version of the code to the user in a clear format.

                Final Code:
                {generated_code}
                """)
            .model("gemini-2.5-flash")
            .build();

        AdkWebServer.start(SequentialAgent.builder()
            .name("code-refiner-assistant")
            .description("Manages the full code generation and refinement process.")
            .subAgents(
                codeRefinerLoop,
                finalPresenter)
            .build());
    }
}
