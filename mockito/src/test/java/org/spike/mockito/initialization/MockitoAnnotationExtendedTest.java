package org.spike.mockito.initialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.spike.mockito.Dao;
import org.spike.mockito.MockitoAnnotationExtended;
import org.spike.mockito.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * https://www.baeldung.com/mockito-annotations
 */
public class MockitoAnnotationExtendedTest {

    private VerboseMockInvocationLogger VERBOSE_LISTENER;
    private ByteArrayOutputStream out;

    public static class VerboseAnswer<T> implements Answer<T> {

        @Override
        public T answer(InvocationOnMock invocationOnMock) throws Throwable {
            return null;
        }
    }

    public static final Answer VERBOSE = new VerboseAnswer<Object>();


    @BeforeEach
    public void init() {
        out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        VERBOSE_LISTENER = new VerboseMockInvocationLogger(ps);
    }


    @Mock
    private Dao dao;

    @InjectMocks
    private Service service;


    @Test
    public void fields_should_be_created_and_valued_after_initialization() {
        MockitoAnnotationExtended.initMocks(this);

        assertNotNull(service);
        assertNotNull(dao);
        assertNotNull(service.getDao());
    }

    @Test
    public void fields_should_be_injected_without_recreate_them_when_already_instantiate() throws IllegalAccessException {
        service = new Service();
        dao = Mockito.mock(Dao.class);
        Mockito.when(dao.getId()).thenReturn("Initial mock");

        MockitoAnnotationExtended.initMocks(this);

        assertEquals("Initial mock", service.getDao().getId());
    }


    @Test
    public void fields_should_be_valued_after_initialization_with_mock_to_create_with_settings() throws IllegalAccessException {
        service = new Service();

        MockitoAnnotationExtended.initMocks(this, Mockito.withSettings().invocationListeners(VERBOSE_LISTENER));

        service.getDao().findById(4);

        String log = out.toString();
        String expectedString = "findById(4L)";
        assertTrue(log.contains(expectedString), "Log not contains '"+expectedString+"' in :" + log);
    }

    @Test
    public void fields_should_be_valued_after_initialization_with_mock_without_override_settings() throws IllegalAccessException {
        service = new Service();
        dao = Mockito.mock(Dao.class);

        MockitoAnnotationExtended.initMocks(this, Mockito.withSettings().invocationListeners(VERBOSE_LISTENER));

        service.getDao().findById(4);

        assertTrue(out.toString().isBlank());
    }
}
