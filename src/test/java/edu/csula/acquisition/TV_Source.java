package edu.csula.acquisition;

import java.util.Collection;

import com.google.common.collect.Lists;

public class TV_Source implements Source<TV_Model>{
	 int index = 0;
	public boolean hasNext() {
		 return index < 1;
	}

	public Collection<TV_Model> next() {
		return Lists.newArrayList(
	            new TV_Model("1", "FRIENDS",null),
	            new TV_Model("2", "GAME OF THRONES","content2"),
	            new TV_Model("3", "SUITS","content3")
	        );
		
	}

}