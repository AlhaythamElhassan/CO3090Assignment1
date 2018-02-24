package uk.ac.le.cs.CO3090.cw1;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class Miner extends Thread {
	private URL url;
	private List<String> keyWords;
	private ConcurrentHashMap<String, Integer> result;
	private ConcurrentLinkedQueue<URL> urlQueue; 
	
	public Miner(URL _url, ConcurrentHashMap<String, Integer> _result, List<String> _keyWords, ConcurrentLinkedQueue<URL> _urlQueue) {
		super();
		setUrl(_url);
		setResult(_result);
		setKeyWords(_keyWords);
		setUrlQueue(_urlQueue);
		
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(List<String> _keyWords) {
		keyWords = _keyWords;
	}

	public ConcurrentHashMap<String, Integer> getResult() {
		return result;
	}

	public void setResult(ConcurrentHashMap<String, Integer> _result) {
		result = _result;
	}

	@Override
	public void run() {
		//get HTML 
		String content=Utils.getTextFromAddress(getUrl().toString());
				
		//get all links
		ArrayList<String> urls=Utils.extractHyperlinks(getUrl().toString(), content);
		
		// push all links to url queue
		urls.forEach(_url -> {
			try {
				getUrlQueue().add(new URL(_url));
			} catch (Exception e) {
				System.out.println("url : " + _url + " couldn't be added \n");
//				e.printStackTrace();
			}
		}); 
		
		//strips HTML tags
		String text=Utils.getPlainText(content);
	
		List<String> keywords = Arrays.asList("university", "sports", "holidays", "britain", "brexit");
				
		for (String _keyWord: getKeyWords()) {
			getResult().put(_keyWord, getResult().get(_keyWord) + Utils.calculate(getKeyWords(), text).get(_keyWord));
		}
				
		synchronized (MineManager.lock) {
			MineManager.lock.notifyAll();
		}
	}
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL _url) {
		url = _url;
	}

	public ConcurrentLinkedQueue<URL> getUrlQueue() {
		return urlQueue;
	}

	public void setUrlQueue(ConcurrentLinkedQueue<URL> _urlQueue) {
		urlQueue = _urlQueue;
	}

}
