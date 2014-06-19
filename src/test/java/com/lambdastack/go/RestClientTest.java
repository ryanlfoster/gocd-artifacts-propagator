package com.lambdastack.go;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.lambdastack.go.models.ValueStreamMap;
import org.junit.Test;

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
}