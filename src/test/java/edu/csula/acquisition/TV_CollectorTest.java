package edu.csula.acquisition;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TV_CollectorTest {
	 private Collector<SimpleData, TV_Model> collector;
	    private Source<TV_Model> source;

	    @Before
	    public void setup() {
	        collector = new TV_Collector();
	        source = new TV_Source();
	    }

	    @Test
	    public void mungee() throws Exception {
	        List<SimpleData> list = (List<SimpleData>) collector.mungee(source.next());
	        List<SimpleData> expectedList = Lists.newArrayList(
	            new SimpleData("1", "'Allo 'Allo","8.3"),
	            new SimpleData("2","10 Things I Hate About You" ,"7.2"),
	            new SimpleData("3","100 Code" ,"7.3")
	        );

	        Assert.assertEquals(list.size(), 3);

	        for (int i = 0; i < 2; i ++) {
	            Assert.assertEquals(list.get(i).getId(), expectedList.get(i).getId());
	            Assert.assertEquals(list.get(i).getSeason(), expectedList.get(i).getSeason());
	        }
	    }
}
