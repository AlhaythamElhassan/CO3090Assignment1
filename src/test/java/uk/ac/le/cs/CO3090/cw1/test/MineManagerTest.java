package uk.ac.le.cs.CO3090.cw1.test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import org.junit.jupiter.api.*;

import uk.ac.le.cs.CO3090.cw1.MineManager;
import uk.ac.le.cs.CO3090.cw1.Miner;

@DisplayName ("Testing MineManager Unit: ")
public class MineManagerTest {
	MineManager mineManager;
	@BeforeAll
//	public void setUp() {
//		mineManager = new MineManager();
//	}
	
	@DisplayName("MineManager should instantiate a Urlqueue")
	@Test
	public void testThatMineManagerHasAnotNullUrlQueue() {
		ConcurrentLinkedQueue<URL> urlQueue = new ConcurrentLinkedQueue<URL>();
		mineManager.setUrlQueue(urlQueue);
		
		assertNotNull(mineManager.getUrlQueue());
	}
	
//	@DisplayName ("Constructing a MineManager with url")
//	@Test
//	public void constructManagerWithUrlQueueThenManagergetUrlQueueShouldReturnSameUrlQueue() {
//		URL uniUrl = null;
//		try {
//			uniUrl= new URL("https://le.ac.uk/");
//			ConcurrentLinkedQueue<URL> testQueue = new ConcurrentLinkedQueue<URL>();
//			testQueue.add(uniUrl);
//			mineManager = new MineManager(testQueue);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		assertEquals(mineManager.getUrlQueue().poll(), uniUrl);
//	}
	
	@DisplayName ("MineManager should have a list of Runnable Miners")
	@Test
	public void mineManagerShouldHaveAnotNullRunnableMiners() {
		List<Miner> miners = new ArrayList<Miner>();
		mineManager.setMiners(miners);
		
		assertEquals(mineManager.getMiners(), miners);
		assertEquals(miners.getClass().getTypeParameters().getClass().getInterfaces()[0], "Runnable");
	}
	
	@DisplayName ("MineManager should have a list of key words")
	@Test
	public void mineManagerSetKeyWordsTest() {
		List<String> keyWords = new ArrayList<String>();
		keyWords.add("test1");
		keyWords.add("test2");
		mineManager.setKeyWords(keyWords);
		
		assertEquals(keyWords, mineManager.getKeyWords());
	}
	
//	@DisplayName ("MinerManager should have a not null result object")
//	@Test
//	public void mineManagerSetResultTest() {
//		ConcurrentHashMap<Integer, HashMap<String, Integer>> results = new ConcurrentHashMap<Integer, HashMap<String, Integer>>();
//		mineManager.setMinerResults(results);
//	
//		assertEquals(results, mineManager.getMinerResults());
//	}
	
	@DisplayName ("MineManger should keep track of visited Url")
	@Test 
	public void mineManagerShouldKeepTrackOfVisitedUrls() {
		
	}
}
