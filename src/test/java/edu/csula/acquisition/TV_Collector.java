package edu.csula.acquisition;

import java.util.Collection;
import java.util.stream.Collectors;

public class TV_Collector implements Collector<SimpleData, TV_Model> {

	@Override
	public Collection<SimpleData> mungee(Collection<TV_Model> src) {
		  return src
		            .stream()
		            .map(SimpleData::build)
		            .collect(Collectors.toList());
	}

	@Override
	public void save(String path, String mongoCollection) {
		// TODO Auto-generated method stub
		
	}

	

}
