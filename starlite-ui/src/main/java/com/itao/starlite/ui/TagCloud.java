package com.itao.starlite.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagCloud {
	private List<Object[]> tagFreqs;
	private double minSize = 65.0;
	private double maxSize = 130.0;
	private double medianSize = 100.0;
	private double lowerThreshold = 65.0;
	private int highest;
	private int lowest;
	private int median;
	
	public TagCloud(List<Object[]> tagFreqs) {
		this.tagFreqs = tagFreqs;
		if (!tagFreqs.isEmpty()) {
			List<Integer> numbers = new ArrayList<Integer>(tagFreqs.size());
			
			for (Object[] o: tagFreqs) {
				Integer freq = ((Number) o[1]).intValue();
				numbers.add(freq);
			}
			
			Collections.sort(numbers);
			lowest = numbers.get(0);
			highest = numbers.get(numbers.size()-1);
			median = numbers.get(numbers.size()/2);
		}
	}
	
	public String render() {
		StringBuilder buf = new StringBuilder();
		buf.append("<div class='tagCloud'>");
		
		for (Object[] tagFreq: tagFreqs) {
			String tag = (String) tagFreq[0];
			Integer count = ((Number) tagFreq[1]).intValue();
			double size = calculateSize(count);
			size = (double)Math.round(size);
			buf.append("<a href='bookmarkSearch.action?q=tag:" + tag + "' style='font-size:" +size+ "%;' class='tag'>" + tag + "</a> ");
		}
		
		buf.append("</div>");
		return buf.toString();
	}

	private double calculateSize(Integer count) {
		double size = 100.0;
		
		if (count > median) {
			int maxDiff = highest - median;
			int diff = count - median;
			double ratio = diff / (double)maxDiff;
			
			size = medianSize + ratio*(maxSize-medianSize);
		} else if (count < median) {
			int maxDiff = median - lowest;
			int diff = median - count;
			double ratio = diff/(double)maxDiff;
			size = medianSize - ratio*(medianSize-minSize);
		}
		
		//If lower than 30, raise to 30;
		size = Math.max(size, lowerThreshold);
		return size;
	}
}
