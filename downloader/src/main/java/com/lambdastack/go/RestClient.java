package com.lambdastack.go;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lambdastack.go.models.ValueStreamMap;
import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestClient {

    public ValueStreamMap getValueStreamMapFromGoServer(String url) throws Exception {
        ArtifactsDownloader.log("Trying URL: " + url);
        HttpRequestFactory httpRequestFactory = getHttpTransport().createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                httpRequest.setParser(new JsonObjectParser(getJsonFactory()));
            }
        });

        GenericUrl requestUrl = new GenericUrl(url);

        HttpRequest request = httpRequestFactory.buildGetRequest(requestUrl);
        request.getHeaders().setBasicAuthentication("artifacts-propagator", "Helpdesk");
        return request.execute().parseAs(ValueStreamMap.class);
    }

    public List<String> getStageFeed(String goServerUrl, String stageLocator) throws Exception {
        String xmlUrl = goServerUrl + stageLocator + ".xml";
        ArtifactsDownloader.log("Trying URL: " + xmlUrl);
        InputSource inputSource = getJobFeedInputSourceFromGoServer(xmlUrl);
        NodeList nodeList = parseXMLDocument(inputSource, "/stage/jobs/job/@href");
        List<String> jobFeedURL = new ArrayList<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            jobFeedURL.add(nodeList.item(i).getNodeValue());
        }
        return jobFeedURL;
    }

    public String getArtifactURLsFromJobFeed(String jobFeedURL) throws Exception {
        ArtifactsDownloader.log("Trying URL: " + jobFeedURL);
        InputSource inputSource = getJobFeedInputSourceFromGoServer(jobFeedURL);
        return parseXMLDocument(inputSource, "/job/artifacts/@baseUri").item(0).getNodeValue();
    }

    private NodeList parseXMLDocument(InputSource inputSource, String xpathExpression) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document stageFeedDocument = builderFactory.newDocumentBuilder().parse(inputSource);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.compile(xpathExpression).evaluate(stageFeedDocument, XPathConstants.NODESET);
    }

    protected HttpTransport getHttpTransport() {
        return new NetHttpTransport();
    }

    protected JsonFactory getJsonFactory() {
        return new JacksonFactory();
    }

    protected InputSource getJobFeedInputSourceFromGoServer(String jobFeedURL) throws Exception {
        URL feedURL = new URL(jobFeedURL);
        Content content = Executor.newInstance()
                .auth(new HttpHost(feedURL.getHost(), feedURL.getPort()), "artifacts-propagator", "Helpdesk")
                .authPreemptive(new HttpHost(feedURL.getHost(), feedURL.getPort()))
                .execute(Request.Get(jobFeedURL)).returnContent();
//        ArtifactsDownloader.log(content.asString());
        return new InputSource(new StringReader(content.asString()));
    }
}
