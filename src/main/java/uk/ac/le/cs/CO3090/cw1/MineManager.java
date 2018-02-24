package uk.ac.le.cs.CO3090.cw1;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * This class manage the work of miners.
 * 	1) Class poll a url form a concurrent url queue (a share resource)
 * 	2) Instantiate a miner using the url in step 1), keywords, results
 * 	   & a unique minerId and then push it in the miners queue
 * 	3) start the miner.
 * 	4) check if urls queue and miners queue are empty yes finish reports
 * 	   results. No => 5)
 * 	5) miners queue is not empty check if timeout? Yes => 6) No => 7)
 * 	6) send signal to all miners to stop
 * 	7) check if maxPageNumbers has been visited or maxKeyWorkFrequency
 * 	   is reached, Yes => 8), No => 9)
 *	8) finish and reports results.
 *	9) Repeat doing 1) to 9) as long as urls queue is not empty.
 *	10) urls queue is empty this thread wait till timeout or to be notified
 *	    by a miner
 * 
 * @author alhaytham
 *
 */
public class MineManager implements Runnable{

	private ConcurrentLinkedQueue<URL> urlQueue;
	private List<Miner> miners;
	private ConcurrentHashMap<String, Integer> result;
	private List<String> keyWords;
	private long timeOut; 
	private int maxNumberOfUrls;
	private int maxKeyWordFrequency;
	private List<URL> visitedUrls;
	private long startTime;
	private int maxNumberOfMiners;
	private int mineCounter = 0;
	public static Object lock = new Object();

	public MineManager(List<String> _keyWords, ConcurrentLinkedQueue<URL> _urlQueue, List<Miner> _miners,
			ConcurrentHashMap<String, Integer> _results, long _timeOut, int _maxNumberOfUrls,
			int _maxKeyWordFrequency, int _maxNumberOfMiners, ArrayList<URL> _visitedUrls) {
		super();
		setKeyWords(_keyWords);
		setUrlQueue(_urlQueue);
		setMiners(_miners);
		setResult(_results);
		setTimeOut(_timeOut);
		setMaxNumberOfUrls(_maxNumberOfUrls);
		setMaxKeyWordFrequency(_maxKeyWordFrequency);
		setMaxNumberOfMiners(_maxNumberOfMiners);
		setVisitedUrls(_visitedUrls);
	}

	public ConcurrentLinkedQueue<URL> getUrlQueue() {
		return urlQueue;
	}

	public void setUrlQueue(ConcurrentLinkedQueue<URL> _urlQueue) {
		urlQueue = _urlQueue;
	}

	public List<Miner> getMiners() {
		return miners;
	}

	public void setMiners(List<Miner> _miners) {
		miners = _miners;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(List<String> _keyWords) {
		keyWords = _keyWords;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long _timeOut) {
		timeOut = _timeOut;
	}

	public int getMaxNumberOfUrls() {
		return maxNumberOfUrls;
	}

	public void setMaxNumberOfUrls(int _maxNumberOfPages) {
		maxNumberOfUrls = _maxNumberOfPages;
	}

	public int getMaxKeyWordFrequency() {
		return maxKeyWordFrequency;
	}

	public void setMaxKeyWordFrequency(int _maxKeyWordFrequency) {
		maxKeyWordFrequency = _maxKeyWordFrequency;
	}

	public List<URL> getVisitedUrls() {
		return visitedUrls;
	}

	public void setVisitedUrls(List<URL> _visitedUrls) {
		visitedUrls = _visitedUrls;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long _startTime) {
		startTime = _startTime;
	}
	
	@Override
	public void run() {
		
		while(true) {
			/** conditions that will let the mine manager to join all miners are
			 * 	1) MaxWordFrequency is reached
			 * 	2) TimeOut
			 * 	3) MaxVisitedUrl is reached
			 *  4) No more job to do all miner finish executing and no more url in the url queue
			 */
			if (isMaxKeyWordFrequencyReached() || isTimeOut() || isMaxVisitedUrl() || 
					(!isAnyActiveMiner() && getUrlQueue().isEmpty())) {
				joinMiners();
				printResuts();
				break;
			}
			/**
			 * Conditions to start a new miner: 
			 *  1) Url queue is not empty 
			 *  2) MxNumberOfMiners is not reached
			 */
			while (!getUrlQueue().isEmpty() && !isMaxNumberOfMiners()) {
				URL _url = getUrlQueue().poll();
				if(!isUrlVisited(_url)) {
					getVisitedUrls().add(_url);
					Miner _miner = new Miner(_url, getResult(), getKeyWords(), getUrlQueue());
					getMiners().add(_miner);
					_miner.start();
				}
			}
			/**
			 * Conditions that will put mine manager thread in wait state
			 *  1) maxNumberOfMiners limit is reached
			 *  2) url queue is empty cann't create more miners 
			 */
			if (getUrlQueue().isEmpty()) {
				synchronized (lock) {
					try {
						lock.wait(new Date().getTime() - getStartTime());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	
		
	}
	
	private boolean isUrlVisited(URL __url) {
		for(URL _url: getVisitedUrls()) {
			if(_url.equals(__url))
				return true;
		}
		return false;
	}

	private boolean isMaxNumberOfMiners() {
		int count = 0; 
		for (Miner _miner: getMiners()) {
			if (_miner.isAlive())
				count++;
		}
		
		if(count >= getMaxNumberOfMiners()) {
			return true;
		}
		return false;
	}

	private  boolean  isAnyActiveMiner() {
		for (Miner _miner: getMiners()) {
			if (!_miner.isAlive()) {
				return true;
			}
		}
		return false;
	}

	private boolean isMaxVisitedUrl() {
		if (getVisitedUrls().size() >= getMaxNumberOfUrls()) {
			return true;
		}
		return false;
	}

	private boolean isMaxKeyWordFrequencyReached() {
		for(String _keyWord: getKeyWords()) {
			if (getResult().get(_keyWord) >= getMaxKeyWordFrequency()) {
				return true;
			}
		}
		return false;
	}

	private void joinMiners() {
		getMiners().forEach(miner -> {
			try {
				miner.join();
			} catch (InterruptedException e) {
				System.out.println("couldn't join miner: " + miner.getId() + "\n");
				e.printStackTrace();
				System.exit(0);
			}
		});
		
	}

	private boolean isTimeOut() {
		long elapsedMillseconds = getStartTime() - new Date().getTime();
		return (elapsedMillseconds >= getTimeOut());
	}

	private void printResuts() {
		System.out.println("Results are ready ... ");
		getKeyWords().forEach(_keyWord -> {
			System.out.println(_keyWord + ": " + getResult().get(_keyWord));
		});
		System.out.println("Visited website count = " + getVisitedUrls().size());
	}
	

	public int getMaxNumberOfMiners() {
		return maxNumberOfMiners;
	}

	public void setMaxNumberOfMiners(int _maxNumberOfMiners) {
		maxNumberOfMiners = _maxNumberOfMiners;
	}
	

	public ConcurrentHashMap<String, Integer> getResult() {
		return result;
	}

	public void setResult(ConcurrentHashMap<String, Integer> _result) {
		result = _result;
	}

	public static void main(String[] _args) {
		// create new input output object
		InputOutput inout = new InputOutput();
		System.out.println("please enter a website and keywords saparated and ended with , e.g");
		System.out.println("https://www.bbc.co.uk, university, sports, holidays, britain, brexit,");
		// read webSite name 
		inout.read(System.in);
		URL _mainUrl = null; 
		// index 0 is the webSite to mine
		try {
			_mainUrl = new URL(inout.getTokens().get(0));
		} catch (Exception e) {
			System.out.println("couldn't create url object for following reason: ");
//			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Digging Website: " + _mainUrl + " for keywords: \n");
		// create a list of String to hold the _keyWords;
		List<String> _keyWords = new ArrayList<String>();
		
		for (int i = 1; i < inout.getTokens().size(); i++) {
			_keyWords.add(inout.getTokens().get(i));
		}
		
		//prints Keywords
		for (int i = 0; i < _keyWords.size(); i++) {
			System.out.println(i+ "] " + _keyWords.get(i));
		}
		
		// create a urls queue
		ConcurrentLinkedQueue<URL> _urlQueue = new ConcurrentLinkedQueue<URL>();
		// add the first url to the _urlQueue
		_urlQueue.add(_mainUrl);
		// create list of miners
		List<Miner> _miners = new ArrayList<Miner>();
		
	
		ConcurrentHashMap<String, Integer> _results = new ConcurrentHashMap<String, Integer>();
		// initialise _results for each keyWord
		_keyWords.forEach(keyWord -> {
			_results.put(keyWord, 0);
		});
		
		// define _timeOut in milliseconds, _maxNumberOfUrls, _maxKeyWordFrequency, _visitedUrls
		// and _maxNumberOfMiners
		final long _timeOut = 10;
		final int _maxNumberOfUrls = 20;
		final int _maxKeyWordFrequency = 10000;
		final int _maxNumberOfMiners = 8;
		// Instantiate a mine manager and run it
		Thread mineManagerThread = new Thread(new MineManager(_keyWords,_urlQueue,_miners,_results,_timeOut,
				_maxNumberOfUrls,_maxKeyWordFrequency, _maxNumberOfMiners, new ArrayList<URL>()));
		mineManagerThread.start();
	}
	
}
