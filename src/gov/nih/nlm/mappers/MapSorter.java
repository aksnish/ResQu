package gov.nih.nlm.mappers;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MapSorter {

	public Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap) {
		Map<String, Integer> sortedMap = new TreeMap<String, Integer>(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}

}

class ValueComparator implements Comparator<Object> {

	Map<String, Integer> map;

	public ValueComparator(Map<String, Integer> map) {
		this.map = map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueB.compareTo(valueA);
	}
}

