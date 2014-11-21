package com.lambdastack.go;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.lambdastack.go.models.ValueStreamMap;
import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestClientTest {
    @Test
    public void shouldConstructValueStreamMapFromUrl() throws Exception{

        // Sample response

        //{
        //   "current_pipeline":"B",
        //   "levels":[
        //      {
        //         "nodes":[
        //            {
        //               "locator":"",
        //               "name":"git@github.com:varadharajan/vector.git",
        //               "depth":2,
        //               "instances":[
        //                  {
        //                     "modified_time":"5 months ago",
        //                     "revision":"8a1dd70777d096bb5a87344109529d43346dd87b",
        //                     "comment":"add stuff to readme about how to read documentation",
        //                     "user":"Howard Mao <zhehao.mao@gmail.com>"
        //                  }
        //               ],
        //               "node_type":"GIT",
        //               "parents":[
        //
        //               ],
        //               "id":"6c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c33c09156028670284ff",
        //               "dependents":[
        //                  "A"
        //               ]
        //            }
        //         ]
        //      },
        //      {
        //         "nodes":[
        //            {
        //               "locator":"",
        //               "name":"git@github.com:varadharajan/Baboon.git",
        //               "depth":1,
        //               "instances":[
        //                  {
        //                     "modified_time":"over 2 years ago",
        //                     "revision":"099cb088068b1e5455ab7a71657fece2f66716ad",
        //                     "comment":"Added comments",
        //                     "user":"Varadharajan <srinathsmn@gmail.com>"
        //                  }
        //               ],
        //               "node_type":"GIT",
        //               "parents":[
        //
        //               ],
        //               "id":"996f4823cd4958097cf4d9a9252466aad9699e973150714956709b7b243d58a3",
        //               "dependents":[
        //                  "B"
        //               ]
        //            },
        //            {
        //               "locator":"/go/tab/pipeline/history/A",
        //               "name":"A",
        //               "depth":2,
        //               "instances":[
        //                  {
        //                     "label":"1",
        //                     "locator":"/go/pipelines/value_stream_map/A/1",
        //                     "stages":[
        //                        {
        //                           "locator":"/go/pipelines/A/1/defaultStage/1",
        //                           "name":"defaultStage",
        //                           "status":"Passed"
        //                        }
        //                     ],
        //                     "counter":1
        //                  }
        //               ],
        //               "node_type":"PIPELINE",
        //               "parents":[
        //                  "6c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c33c09156028670284ff"
        //               ],
        //               "id":"A",
        //               "dependents":[
        //                  "B"
        //               ]
        //            }
        //         ]
        //      },
        //      {
        //         "nodes":[
        //            {
        //               "locator":"/go/tab/pipeline/history/B",
        //               "name":"B",
        //               "depth":1,
        //               "instances":[
        //                  {
        //                     "label":"4",
        //                     "locator":"/go/pipelines/value_stream_map/B/4",
        //                     "stages":[
        //                        {
        //                           "locator":"/go/pipelines/B/4/defaultStage/2",
        //                           "name":"defaultStage",
        //                           "status":"Passed"
        //                        }
        //                     ],
        //                     "counter":4
        //                  }
        //               ],
        //               "node_type":"PIPELINE",
        //               "parents":[
        //                  "996f4823cd4958097cf4d9a9252466aad9699e973150714956709b7b243d58a3",
        //                  "A"
        //               ],
        //               "id":"B",
        //               "dependents":[
        //                  "C"
        //               ]
        //            }
        //         ]
        //      },
        //      {
        //         "nodes":[
        //            {
        //               "locator":"/go/tab/pipeline/history/C",
        //               "name":"C",
        //               "depth":1,
        //               "instances":[
        //                  {
        //                     "label":"3",
        //                     "locator":"/go/pipelines/value_stream_map/C/3",
        //                     "stages":[
        //                        {
        //                           "locator":"/go/pipelines/C/3/defaultStage/2",
        //                           "name":"defaultStage",
        //                           "status":"Passed"
        //                        }
        //                     ],
        //                     "counter":3
        //                  }
        //               ],
        //               "node_type":"PIPELINE",
        //               "parents":[
        //                  "B"
        //               ],
        //               "id":"C",
        //               "dependents":[
        //
        //               ]
        //            }
        //         ]
        //      }
        //   ]
        //}

        final String sampleContent = "{\"current_pipeline\":\"B\",\"levels\":[{\"nodes\":[{\"locator\":\"\",\"name\":\"git@github.com:varadharajan/vector.git\",\"depth\":2,\"instances\":[{\"modified_time\":\"5 months ago\",\"revision\":\"8a1dd70777d096bb5a87344109529d43346dd87b\",\"comment\":\"add stuff to readme about how to read documentation\",\"user\":\"Howard Mao <zhehao.mao@gmail.com>\"}],\"node_type\":\"GIT\",\"parents\":[],\"id\":\"6c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c33c09156028670284ff\",\"dependents\":[\"A\"]}]},{\"nodes\":[{\"locator\":\"\",\"name\":\"git@github.com:varadharajan/Baboon.git\",\"depth\":1,\"instances\":[{\"modified_time\":\"over 2 years ago\",\"revision\":\"099cb088068b1e5455ab7a71657fece2f66716ad\",\"comment\":\"Added comments\",\"user\":\"Varadharajan <srinathsmn@gmail.com>\"}],\"node_type\":\"GIT\",\"parents\":[],\"id\":\"996f4823cd4958097cf4d9a9252466aad9699e973150714956709b7b243d58a3\",\"dependents\":[\"B\"]},{\"locator\":\"/go/tab/pipeline/history/A\",\"name\":\"A\",\"depth\":2,\"instances\":[{\"label\":\"1\",\"locator\":\"/go/pipelines/value_stream_map/A/1\",\"stages\":[{\"locator\":\"/go/pipelines/A/1/defaultStage/1\",\"name\":\"defaultStage\",\"status\":\"Passed\"}],\"counter\":1}],\"node_type\":\"PIPELINE\",\"parents\":[\"6c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c33c09156028670284ff\"],\"id\":\"A\",\"dependents\":[\"B\"]}]},{\"nodes\":[{\"locator\":\"/go/tab/pipeline/history/B\",\"name\":\"B\",\"depth\":1,\"instances\":[{\"label\":\"4\",\"locator\":\"/go/pipelines/value_stream_map/B/4\",\"stages\":[{\"locator\":\"/go/pipelines/B/4/defaultStage/2\",\"name\":\"defaultStage\",\"status\":\"Passed\"}],\"counter\":4}],\"node_type\":\"PIPELINE\",\"parents\":[\"996f4823cd4958097cf4d9a9252466aad9699e973150714956709b7b243d58a3\",\"A\"],\"id\":\"B\",\"dependents\":[\"C\"]}]},{\"nodes\":[{\"locator\":\"/go/tab/pipeline/history/C\",\"name\":\"C\",\"depth\":1,\"instances\":[{\"label\":\"3\",\"locator\":\"/go/pipelines/value_stream_map/C/3\",\"stages\":[{\"locator\":\"/go/pipelines/C/3/defaultStage/2\",\"name\":\"defaultStage\",\"status\":\"Passed\"}],\"counter\":3}],\"node_type\":\"PIPELINE\",\"parents\":[\"B\"],\"id\":\"C\",\"dependents\":[]}]}]}";

        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() {
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.setStatusCode(200);
                        response.setContentType("application/json");
                        response.setContent(sampleContent);
                        return response;
                    }
                };
            }
        };

        RestClient restClient = mock(RestClient.class);
        when(restClient.getHttpTransport()).thenReturn(transport);
        String url = "https://10.0.0.1:8154/go/pipelines/value_stream_map/C/7.json";
        when(restClient.getValueStreamMapFromGoServer(url)).thenCallRealMethod();
        when(restClient.getJsonFactory()).thenCallRealMethod();

        ValueStreamMap valueStreamMap = restClient.getValueStreamMapFromGoServer(url);

        assertNotNull(valueStreamMap);
        assertEquals(valueStreamMap.getCurrentPipeline(), "B");
        assertEquals(valueStreamMap.getValueStreamLevels().length, 4);
    }

    @Test
    public void shouldGetStageFeedsWhichContainsJobInformation() throws Exception {

        // Sample content

        // <?xml version="1.0" encoding="UTF-8"?>
        //
        // <stage name="Build" counter="1">
        //   <link rel="self" href="http://y-ci-master.dnspam:8153/go/api/stages/29924.xml"/>
        //   <id><![CDATA[urn:x-go.studios.thoughtworks.com:stage-id:Merchandise_Service:883:Build:1]]></id>
        //   <pipeline name="Merchandise_Service" counter="883" label="883-master" href="http://y-ci-master.dnspam:8153/go/api/pipelines/// Merchandise_Service/18511.xml"/>
        //   <updated>2014-06-19T19:13:31+02:00</updated>
        //   <result>Passed</result>
        //   <state>Completed</state>
        //   <approvedBy><![CDATA[changes]]></approvedBy>
        //   <jobs>
        //     <job href="http://y-ci-master.dnspam:8153/go/api/jobs/39112.xml"/>
        //   </jobs>
        // </stage>



        final String sampleContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<stage name=\"Build\" counter=\"1\">\n" +
                "  <link rel=\"self\" href=\"http://y-ci-master.dnspam:8153/go/api/stages/29924.xml\"/>\n" +
                "  <id><![CDATA[urn:x-go.studios.thoughtworks.com:stage-id:Merchandise_Service:883:Build:1]]></id>\n" +
                "  <pipeline name=\"Merchandise_Service\" counter=\"883\" label=\"883-master\" href=\"http://y-ci-master.dnspam:8153/go/api/pipelines/Merchandise_Service/18511.xml\"/>\n" +
                "  <updated>2014-06-19T19:13:31+02:00</updated>\n" +
                "  <result>Passed</result>\n" +
                "  <state>Completed</state>\n" +
                "  <approvedBy><![CDATA[changes]]></approvedBy>\n" +
                "  <jobs>\n" +
                "    <job href=\"http://y-ci-master.dnspam:8153/go/api/jobs/39112.xml\"/>\n" +
                "    <job href=\"http://y-ci-master.dnspam:8153/go/api/jobs/39113.xml\"/>\n" +
                "  </jobs>\n" +
                "</stage>\n";

        String goServerUrl = "http://10.0.0.1:8154";
        String stageLocator = "/go/pipelines/A/4/defaultStage/2";

        RestClient restClient = mock(RestClient.class);
        InputSource inputSource = new InputSource(new StringReader(sampleContent));
        when(restClient.getJobFeedInputSourceFromGoServer(goServerUrl + stageLocator + ".xml")).thenReturn(inputSource);
        when(restClient.getStageFeed(goServerUrl, stageLocator)).thenCallRealMethod();
        List<String> jobsFromStageFeed = restClient.getStageFeed(goServerUrl, stageLocator);

        assertNotNull(jobsFromStageFeed);
        assertEquals(2, jobsFromStageFeed.size());
        List<String> expectedJobsFeeds = Arrays.asList("http://y-ci-master.dnspam:8153/go/api/jobs/39112.xml", "http://y-ci-master.dnspam:8153/go/api/jobs/39113.xml");
        assertEquals(expectedJobsFeeds, jobsFromStageFeed);
    }

    @Test
    public void shouldGetArtifactsURLFromJobFeeds() throws Exception {
        // Sample content

        // <?xml version="1.0" encoding="UTF-8"?>
        //
        // <job name="test">
        //   <link rel="self" href="http://y-ci-master.dnspam:8153/go/api/jobs/39112.xml"/>
        //   <id><![CDATA[urn:x-go.studios.thoughtworks.com:job-id:Merchandise_Service:883:Build:1:test]]></id>
        //   <pipeline name="Merchandise_Service" counter="883" label="883-master"/>
        //   <stage name="Build" counter="1" href="http://y-ci-master.dnspam:8153/go/api/stages/29924.xml"/>
        //   <result>Passed</result>
        //   <state>Completed</state>
        //   <properties>
        //     <property name="cruise_agent"><![CDATA[y-ci-slave1.dnspam]]></property>
        //     <property name="cruise_job_duration"><![CDATA[481]]></property>
        //     <property name="cruise_job_id"><![CDATA[39112]]></property>
        //     <property name="cruise_job_result"><![CDATA[Passed]]></property>
        //     <property name="cruise_pipeline_counter"><![CDATA[883]]></property>
        //     <property name="cruise_pipeline_label"><![CDATA[883-master]]></property>
        //     <property name="cruise_stage_counter"><![CDATA[1]]></property>
        //     <property name="cruise_timestamp_01_scheduled"><![CDATA[2014-06-19T19:05:01+02:00]]></property>
        //     <property name="cruise_timestamp_02_assigned"><![CDATA[2014-06-19T19:05:06+02:00]]></property>
        //     <property name="cruise_timestamp_03_preparing"><![CDATA[2014-06-19T19:05:16+02:00]]></property>
        //     <property name="cruise_timestamp_04_building"><![CDATA[2014-06-19T19:05:29+02:00]]></property>
        //     <property name="cruise_timestamp_05_completing"><![CDATA[2014-06-19T19:13:08+02:00]]></property>
        //     <property name="cruise_timestamp_06_completed"><![CDATA[2014-06-19T19:13:31+02:00]]></property>
        //   </properties>
        //   <agent uuid="7e6a2141-9946-48e8-9bf7-b114823ff4b0"/>
        //   <artifacts baseUri="http://y-ci-master.dnspam:8153/go/files/Merchandise_Service/883/Build/1/test" pathFromArtifactRoot="pipelines/// Merchandise_Service/883/Build/1/test">
        //     <artifact src="artifacts" dest="" type="file"/>
        //   </artifacts>
        //   <resources>
        //     <resource>service</resource>
        //   </resources>
        //   <environmentvariables>
        //     <variable name="CURRENT_BRANCH"><![CDATA[master]]></variable>
        //     <variable name="DEPENDENCY_LABEL_PIPE"><![CDATA[GO_DEPENDENCY_LABEL_PIPE]]></variable>
        //   </environmentvariables>
        // </job>

        String sampleContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<job name=\"test\">\n" +
                "  <link rel=\"self\" href=\"http://y-ci-master.dnspam:8153/go/api/jobs/39112.xml\"/>\n" +
                "  <id><![CDATA[urn:x-go.studios.thoughtworks.com:job-id:Merchandise_Service:883:Build:1:test]]></id>\n" +
                "  <pipeline name=\"Merchandise_Service\" counter=\"883\" label=\"883-master\"/>\n" +
                "  <stage name=\"Build\" counter=\"1\" href=\"http://y-ci-master.dnspam:8153/go/api/stages/29924.xml\"/>\n" +
                "  <result>Passed</result>\n" +
                "  <state>Completed</state>\n" +
                "  <properties>\n" +
                "    <property name=\"cruise_agent\"><![CDATA[y-ci-slave1.dnspam]]></property>\n" +
                "    <property name=\"cruise_job_duration\"><![CDATA[481]]></property>\n" +
                "    <property name=\"cruise_job_id\"><![CDATA[39112]]></property>\n" +
                "    <property name=\"cruise_job_result\"><![CDATA[Passed]]></property>\n" +
                "    <property name=\"cruise_pipeline_counter\"><![CDATA[883]]></property>\n" +
                "    <property name=\"cruise_pipeline_label\"><![CDATA[883-master]]></property>\n" +
                "    <property name=\"cruise_stage_counter\"><![CDATA[1]]></property>\n" +
                "    <property name=\"cruise_timestamp_01_scheduled\"><![CDATA[2014-06-19T19:05:01+02:00]]></property>\n" +
                "    <property name=\"cruise_timestamp_02_assigned\"><![CDATA[2014-06-19T19:05:06+02:00]]></property>\n" +
                "    <property name=\"cruise_timestamp_03_preparing\"><![CDATA[2014-06-19T19:05:16+02:00]]></property>\n" +
                "    <property name=\"cruise_timestamp_04_building\"><![CDATA[2014-06-19T19:05:29+02:00]]></property>\n" +
                "    <property name=\"cruise_timestamp_05_completing\"><![CDATA[2014-06-19T19:13:08+02:00]]></property>\n" +
                "    <property name=\"cruise_timestamp_06_completed\"><![CDATA[2014-06-19T19:13:31+02:00]]></property>\n" +
                "  </properties>\n" +
                "  <agent uuid=\"7e6a2141-9946-48e8-9bf7-b114823ff4b0\"/>\n" +
                "  <artifacts baseUri=\"http://y-ci-master.dnspam:8153/go/files/Merchandise_Service/883/Build/1/test\" pathFromArtifactRoot=\"pipelines/Merchandise_Service/883/Build/1/test\">\n" +
                "    <artifact src=\"artifacts\" dest=\"\" type=\"file\"/>\n" +
                "  </artifacts>\n" +
                "  <resources>\n" +
                "    <resource>service</resource>\n" +
                "  </resources>\n" +
                "  <environmentvariables>\n" +
                "    <variable name=\"CURRENT_BRANCH\"><![CDATA[master]]></variable>\n" +
                "    <variable name=\"DEPENDENCY_LABEL_PIPE\"><![CDATA[GO_DEPENDENCY_LABEL_PIPE]]></variable>\n" +
                "  </environmentvariables>\n" +
                "</job>";

        String jobFeedURL = "http://10.0.0.1:8154/some.xml";

        RestClient restClient = mock(RestClient.class);
        InputSource inputSource = new InputSource(new StringReader(sampleContent));
        when(restClient.getJobFeedInputSourceFromGoServer(jobFeedURL)).thenReturn(inputSource);
        when(restClient.getArtifactURLsFromJobFeed(jobFeedURL)).thenCallRealMethod();
        String expectedArtifactURLs = "http://y-ci-master.dnspam:8153/go/files/Merchandise_Service/883/Build/1/test";
        String actualArtifactURLs = restClient.getArtifactURLsFromJobFeed(jobFeedURL);

        assertNotNull(expectedArtifactURLs, actualArtifactURLs);
    }

    @Test
    public void shouldTest() throws  Exception{
        String jobFeedURL = "http://172.16.20.239:8153/go/pipelines/Chef_Repo/552/Build/3.xml";
        URL feedURL = new URL(jobFeedURL);
        DefaultHttpClient client = new DefaultHttpClient();
        client.setRedirectStrategy(new LaxRedirectStrategy());
        Content content = Executor.newInstance(client)
                .auth(new HttpHost(feedURL.getHost(), feedURL.getPort()), "admin", "Helpdesk")
                .authPreemptive(new HttpHost(feedURL.getHost(), feedURL.getPort()))
                .execute(Request.Get(jobFeedURL)).returnContent();
        System.out.println((content.asString()));
    }
}