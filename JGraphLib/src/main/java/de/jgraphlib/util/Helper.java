package de.jgraphlib.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Helper {

	public static boolean isSet(Object object) {
		return object != null;
	}
	
	public static boolean isNull(Object object) {
		return object == null;
	}
	
	public static boolean isNotNull(Object object) {
		return !isNull(object);
	}
	
	public static <T> List<T> initialize(int size, T val) {			
        return Stream.generate(String::new)
                    .limit(size)
                    .map(s -> val)
                    .collect(Collectors.toList());
    }
	
	public static <T> Iterable<T> skipFirst(final Iterable<T> list) {
	    return new Iterable<T>() {
	        @Override public Iterator<T> iterator() {
	            Iterator<T> i = list.iterator();
	            i.next();
	            return i;
	        }
	    };
	}	 
}
