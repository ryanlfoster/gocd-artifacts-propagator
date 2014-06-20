package com.lambdastack.go.executors;


import com.lambdastack.go.core.DependencyResolver;
import com.lambdastack.go.models.Dependencies;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class ArtifactsDownloaderExecutorTest {

    TaskExecutionContext mockTaskExecutionContext;
    DependencyResolver mockDependencyResolver;
    ArtifactsDownloaderExecutor mockArtifactsDownloaderExecutor;
    Dependencies mockDependencies;

    @Before
    public void setUp() {
        mockTaskExecutionContext = mock(TaskExecutionContext.class);;
        mockDependencyResolver = mock(DependencyResolver.class);
        mockArtifactsDownloaderExecutor = mock(ArtifactsDownloaderExecutor.class);
        mockDependencies = mock(Dependencies.class);
    }

    @Test
    public void shouldDownloadArtifactsFromAllUpstreams() throws Exception{
        when(mockArtifactsDownloaderExecutor.execute(null, mockTaskExecutionContext)).thenCallRealMethod();
        when(mockArtifactsDownloaderExecutor.getDependencyResolver(mockTaskExecutionContext)).thenReturn(mockDependencyResolver);
        when(mockDependencyResolver.resolveDependencies()).thenReturn(mockDependencies);

        assertTrue(mockArtifactsDownloaderExecutor.execute(null, mockTaskExecutionContext).isSuccessful());

        verify(mockArtifactsDownloaderExecutor).getDependencyResolver(mockTaskExecutionContext);
        verify(mockDependencies, times(1)).fetchArtifacts();
    }

    @Test
    public void shouldHandleExceptionsWhileDownloadingArtifactsFromAllUpstreams() throws Exception{
        when(mockArtifactsDownloaderExecutor.execute(null, mockTaskExecutionContext)).thenCallRealMethod();
        when(mockArtifactsDownloaderExecutor.getDependencyResolver(mockTaskExecutionContext)).thenReturn(mockDependencyResolver);
        when(mockDependencyResolver.resolveDependencies()).thenReturn(mockDependencies);
        when(mockDependencies.fetchArtifacts()).thenThrow(new Exception());

        assertFalse(mockArtifactsDownloaderExecutor.execute(null, mockTaskExecutionContext).isSuccessful());

        verify(mockArtifactsDownloaderExecutor).getDependencyResolver(mockTaskExecutionContext);
        verify(mockDependencies, times(1)).fetchArtifacts();
    }

}