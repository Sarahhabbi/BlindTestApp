package test.Service; 

import Service.GameService;
import junit.framework.Test;
import junit.framework.TestSuite; 
import junit.framework.TestCase;
import models.Audio;
import models.MyImage;

import java.util.ArrayList;

/** 
* GameService Tester. 
* 
* @author <Authors name> 
* @since <pre>01/07/2022</pre> 
* @version 1.0 
*/ 
public class GameServiceTest extends TestCase {
    GameService gameservice=new GameService();
public GameServiceTest(String name) { 
super(name); 
} 

public void setUp() throws Exception { 
super.setUp(); 
} 

public void tearDown() throws Exception { 
super.tearDown(); 
} 

/** 
* 
* Method: randomList() 
* 
*/ 
public void testRandomList() throws Exception { 
    ArrayList<MyImage> images1=gameservice.randomList(5);
    ArrayList<MyImage> images2=gameservice.randomList(5);
    ArrayList<MyImage> images3=gameservice.randomList(5);
    assert(!images1.equals(images2));
    assert(!images2.equals(images3));
    assert(!images1.equals(images3));
} 

/** 
* 
* Method: randomListaudio() 
* 
*/ 
public void testRandomListaudio() throws Exception {
    ArrayList<Audio> audios1=gameservice.randomListaudio(5);
    ArrayList<Audio> audios2=gameservice.randomListaudio(5);
    ArrayList<Audio> audios3=gameservice.randomListaudio(5);
    assert(!audios1.equals(audios2));
    assert(!audios2.equals(audios3));
    assert(!audios1.equals(audios3));
} 



public static Test suite() { 
return new TestSuite(GameServiceTest.class); 
} 
} 
