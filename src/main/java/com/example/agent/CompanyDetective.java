// src/main/java/com/example/agent/CompanyDetective.java 
package com.example.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.GoogleSearchTool;
import com.google.adk.web.AdkWebServer;

public class CompanyDetective {
    public static void main(String[] args) {
        var companyProfiler = LlmAgent.builder()
            .name("company-profiler")
            .description("Provides a general overview of a company.")
            .instruction("""
                Your role is to provide a brief overview of the given company.
                Include its mission, headquarters, and current CEO.
                Use the Google Search Tool to find this information.
                """)
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .outputKey("profile")
            .build();

        var newsFinder = LlmAgent.builder()
            .name("news-finder")
            .description("Finds the latest news about a company.")
            .instruction("""
                Your role is to find the top 3-4 recent news headlines for the given company.
                Use the Google Search Tool.
                Present the results as a simple bulleted list.
                """)
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .outputKey("news")
            .build();

        var financialAnalyst = LlmAgent.builder()
            .name("financial-analyst")
            .description("Analyzes the financial performance of a company.")
            .instruction("""
                Your role is to provide a snapshot of the given company's recent financial performance.
                Focus on stock trends or recent earnings reports.
                Use the Google Search Tool.
                """)
            .model("gemini-2.5-flash")
            .tools(new GoogleSearchTool())
            .outputKey("financials")
            .build();

        var marketResearcher = ParallelAgent.builder()
            .name("market-researcher")
            .description("Performs comprehensive market research on a company.")
            .subAgents(
                companyProfiler,
                newsFinder,
                financialAnalyst
            )
            .build();

        var reportCompiler = LlmAgent.builder()
            .name("report-compiler")
            .description("Compiles a final market research report.")
            .instruction("""
                Your role is to synthesize the provided information into a coherent market research report.
                Combine the company profile, latest news, and financial analysis into a single, well-formatted report.

                ## Company Profile
                {profile}

                ## Latest News
                {news}

                ## Financial Snapshot
                {financials}
                """)
            .model("gemini-2.5-flash")
            .build();

        AdkWebServer.start(SequentialAgent.builder()
            .name("company-detective")
            .description("Collects various information about a company.")
            .subAgents(
                marketResearcher,
                reportCompiler
            ).build());
    }
}
