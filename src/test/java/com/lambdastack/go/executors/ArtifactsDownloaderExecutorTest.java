package com.lambdastack.go.executors;


import com.lambdastack.go.core.DependencyResolver;
import com.lambdastack.go.models.Dependencies;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ArtifactsDownloaderExecutorTest {

    @Test
    public void shouldDownloadArtifactsFromAllUpstreams() {
        TaskExecutionContext mockTaskExecutionContext = mock(TaskExecutionContext.class);;
        DependencyResolver mockDependencyResolver = mock(DependencyResolver.class);
        ArtifactsDownloaderExecutor mockArtifactsDownloaderExecutor = mock(ArtifactsDownloaderExecutor.class);
        Dependencies mockDependencies = mock(Dependencies.class);

        when(mockArtifactsDownloaderExecutor.execute(null, mockTaskExecutionContext)).thenCallRealMethod();
        when(mockArtifactsDownloaderExecutor.getDependencyResolver(mockTaskExecutionContext)).thenReturn(mockDependencyResolver);
        when(mockDependencyResolver.resolveDependencies()).thenReturn(mockDependencies);

        mockArtifactsDownloaderExecutor.execute(null, mockTaskExecutionContext);

        verify(mockArtifactsDownloaderExecutor).getDependencyResolver(mockTaskExecutionContext);
        verify(mockDependencies, times(1)).fetchArtifacts();
    }

}