package org.molgenis;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class DeconvolutionPlotProcessor implements Processor
{

	@Override
	public void process(Exchange exchange) throws Exception
	{
		String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
		Pattern pattern = Pattern.compile("^(([^-]+)\\-(.+)\\-([^-]+))\\-LLDeep.png$");
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.matches())
		{
			String name = matcher.group(1);
			String disease = matcher.group(2).replace("_", " ");
			String gene = matcher.group(3).replace("_", "-");
			String snp = matcher.group(4);
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.addTextBody("name", name);
			entityBuilder.addTextBody("gene", gene);
			entityBuilder.addTextBody("snp", snp);
			entityBuilder.addTextBody("disease", disease);
			entityBuilder.addBinaryBody("image",
					new File(exchange.getIn().getHeader("CamelFileAbsolutePath", String.class)));
			exchange.getOut().setHeaders(exchange.getIn().getHeaders());
			exchange.getOut().setBody(entityBuilder.build());
		}
		else
		{
			throw new IllegalArgumentException(
					"Failed to parse file name. It should be of the form <disease>-<gene>-<snp>-ggplot.png");
		}
	}
}
