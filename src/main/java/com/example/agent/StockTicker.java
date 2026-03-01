// src/main/java/com/example/agent/StockTicker.java
package com.example.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.adk.web.AdkWebServer;
import java.util.Map;
import java.util.Random;

public class StockTicker {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        AdkWebServer.start(
            LlmAgent.builder()
                .name("stock_agent")
                .instruction("""
                    You are a stock exchange ticker expert.
                    When asked about the stock price of a company,
                    use the `lookup_stock_ticker` tool to find the information.
                    """)
                .model("gemini-2.5-flash")
                .tools(FunctionTool.create(StockTicker.class, "lookupStockTicker"))
                .build()
        );
    }

    @Schema(
        name = "lookup_stock_ticker",
        description = "Lookup stock price for a given company or ticker"
    )
    public static Map<String, String> lookupStockTicker(
        @Schema(name = "company_name_or_stock_ticker", description = "The company name or stock ticker")
        String ticker) {
    		if(ticker.equalsIgnoreCase("DEVOXX")){
			return Map.of(
				"error",
				"Impossible to look up the stock price for DEVOXX"
				);
			} else {
				return Map.of(
					"stock_price",
					String.valueOf(RANDOM.nextDouble() * 300)
					);
				}
	}
}
