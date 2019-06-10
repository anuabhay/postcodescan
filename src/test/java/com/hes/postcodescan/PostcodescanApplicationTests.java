package com.hes.postcodescan;

import com.hes.YPScraper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostcodescanApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void  testInd(){
		String locations = "a,b,c,d";
		String types = "t1,t2";

		String[] s = locations.split(",");
		ArrayList suburbs1 = new ArrayList( Arrays.asList(s));
		System.out.println(s);
	}

}
