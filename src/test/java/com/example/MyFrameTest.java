package com.example;

import org.junit.Before;
import org.junit.Test;
import java.awt.event.KeyEvent;
import static org.mockito.Mockito.*;

public class MyFrameTest {

    private MyFrame myFrame;
    private Mario mario;

    @Before
    public void setUp() {
        mario = mock(Mario.class);
        myFrame = new MyFrame(mario); // Pass the mock object to MyFrame
    }

    @Test
    public void testKeyPressed_LeftMove() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(37);

        myFrame.keyPressed(event);

        verify(mario).leftMove();
    }

    @Test
    public void testKeyPressed_RightMove() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(39);

        myFrame.keyPressed(event);

        verify(mario).rightMove();
    }

    @Test
    public void testKeyPressed_Jump() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(38);

        myFrame.keyPressed(event);

        verify(mario).jump();
    }
    
}