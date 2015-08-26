package org.molgenis;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder
{

	/**
	 * Let's configure the Camel routing rules using Java code...
	 */
	@Override
	public void configure()
	{
		from("file:src/data?noop=true").process(new DeconvolutionPlotProcessor())
				.setHeader("x-Molgenis-token", constant("e9efe6d4c130435898017ce5f68ba82a"))
				.setHeader(Exchange.HTTP_METHOD, constant("POST"))
				.to("https4://molgenis01.target.rug.nl/api/v1/DeconvolutionPlot").log("in.headers.Location");
	}
}
