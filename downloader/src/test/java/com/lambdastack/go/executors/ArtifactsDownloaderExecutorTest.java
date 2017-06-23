package com.lambdastack.go.executors;


import com.lambdastack.go.core.DependencyResolver;
import com.lambdastack.go.models.Dependencies;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;


public class ArtifactsDownloaderExecutorTest {

    TaskExecutionContext mockTaskExecutionContext;
    DependencyResolver mockDependencyResolver;
    ArtifactsDownloaderExecutor mockArtifactsDownloaderExecutor;
    Dependencies mockDependencies;

    @Before
    public void setUp() {
        mockTaskExecutionContext = mock(TaskExecutionContext.class);
        mockDependencyResolver = mock(DependencyResolver.class);
        mockArtifactsDownloaderExecutor = mock(ArtifactsDownloaderExecutor.class);
        mockDependencies = mock(Dependencies.class);
    }

    @Test
    public void test() {

    }
//    public void shouldDownloadArtifactsFromAllUpstreams() throws Exception{
//        when(mockArtifactsDownloaderExecutor.execute(mockTaskExecutionContext)).thenCallRealMethod();
//        when(new DependencyResolver((Map) mockTaskExecutionContext)).thenReturn(mockDependencyResolver);
//        when(mockDependencyResolver.resolveDependencies()).thenReturn(mockDependencies);
//
//        assertTrue(mockArtifactsDownloaderExecutor.execute(mockTaskExecutionContext).isSuccessful());
//
//        new DependencyResolver((Map) mockTaskExecutionContext);
//        verify(mockDependencies, times(1)).fetchArtifacts();
//    }
//
//    @Test
//    public void shouldHandleExceptionsWhileDownloadingArtifactsFromAllUpstreams() throws Exception{
//        when(mockArtifactsDownloaderExecutor.execute(mockTaskExecutionContext)).thenCallRealMethod();
//        when(new DependencyResolver((Map) mockTaskExecutionContext)).thenReturn(mockDependencyResolver);
//        when(mockDependencyResolver.resolveDependencies()).thenReturn(mockDependencies);
//        when(mockDependencies.fetchArtifacts()).thenThrow(new Exception());
//
//        assertFalse(mockArtifactsDownloaderExecutor.execute(mockTaskExecutionContext).isSuccessful());
//
//        new DependencyResolver((Map) mockTaskExecutionContext);
//        verify(mockDependencies, times(1)).fetchArtifacts();
//    }

}