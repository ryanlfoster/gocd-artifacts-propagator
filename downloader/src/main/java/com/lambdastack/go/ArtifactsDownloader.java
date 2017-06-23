package com.lambdastack.go;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.lambdastack.go.executors.ArtifactsDownloaderExecutor;
import com.lambdastack.go.views.ArtifactsDownloaderView;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Extension
public class ArtifactsDownloader implements GoPlugin {
    public static final String ADDITIONAL_OPTIONS = "AdditionalOptions";
    static Logger logger = Logger.getLoggerFor(ArtifactsDownloader.class);

//    @Override
//    public TaskConfig config() {
//        TaskConfig config = new TaskConfig();
//        return config;
//    }
//
//    @Override
//    public TaskExecutor executor() {
//        return new ArtifactsDownloaderExecutor();
//    }
//
//    @Override
//    public TaskView view() {
//        return new ArtifactsDownloaderView();
//    }
//
//    @Override
//    public ValidationResult validate(TaskConfig taskConfig) {
//        return new ValidationResult(); // No errors added to it.
//    }
//
//    @Override
//    public void setPluginDescriptor(PluginDescriptor pluginDescriptor) {
//        this.pluginDescriptor = pluginDescriptor;
//    }

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {

    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        try {
            if ("configuration".equals(request.requestName())) {
                return handleGetConfigRequest();
            } else if ("validate".equals(request.requestName())) {
                return handleValidation(request);
            } else if ("execute".equals(request.requestName())) {
                return handleTaskExecution(request);
            } else if ("view".equals(request.requestName())) {
                return handleTaskView();
            }
        } catch (IOException e) {
            log(e, "error when serializing");
            throw new UnhandledRequestTypeException(request.requestName());
        }
        throw new UnhandledRequestTypeException(request.requestName());
    }

    private GoPluginApiResponse handleTaskView() throws IOException {
        int responseCode = DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE;
        Map view = new HashMap();
        ArtifactsDownloaderView oldView = new ArtifactsDownloaderView();
        view.put("displayValue", oldView.displayValue());
        try {
            view.put("template", oldView.template());
        } catch (Exception e) {
            responseCode = DefaultGoPluginApiResponse.INTERNAL_ERROR;
            String errorMessage = "Failed to find template: " + e.getMessage();
            view.put("exception", errorMessage);
            log(e, errorMessage);
        }
        return createResponse(responseCode, view);
    }

    public static void log(Exception e, String errorMessage) {
        logger.error(errorMessage, e);
    }

    public static void log(String errorMessage) {
        logger.warn(errorMessage);
    }

    private GoPluginApiResponse handleTaskExecution(GoPluginApiRequest request) throws IOException {
        int responseCode = DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE;
        ArtifactsDownloaderExecutor executor = new ArtifactsDownloaderExecutor();
        Map executionRequest = (Map) new JacksonFactory().fromString(request.requestBody(), Object.class);
        Map context = (Map) executionRequest.get("context");

        try {
            executor.execute(context);
        } catch (Exception e) {
            log(e, "Error when executing");
        }
        HashMap body = new HashMap();
        body.put("success", true);
        body.put("message", "Artifacts downloaded successfully");
        return createResponse(responseCode, body);
    }

    private GoPluginApiResponse handleValidation(GoPluginApiRequest request) throws IOException {
        int responseCode = DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE;
        HashMap body = new HashMap();
        body.put("errors", new HashMap());
        return createResponse(responseCode, body);
    }

    private GoPluginApiResponse handleGetConfigRequest() throws IOException {
        HashMap config = new HashMap();

        HashMap additionalOptions = new HashMap();
        additionalOptions.put("display-order", "0");
        additionalOptions.put("display-name", "Additional Options");
        additionalOptions.put("required", false);
        config.put(ADDITIONAL_OPTIONS, additionalOptions);

        return createResponse(DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE, config);
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("task", Arrays.asList("1.0"));
    }

    private GoPluginApiResponse createResponse(int responseCode, Map body) throws IOException {
        final DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(responseCode);
        String bodyString = new JacksonFactory().toString(body);
        response.setResponseBody(bodyString);
        return response;
    }
}
