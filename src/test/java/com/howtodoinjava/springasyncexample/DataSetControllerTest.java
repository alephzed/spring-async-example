package com.howtodoinjava.springasyncexample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.howtodoinjava.springasyncexample.web.controller.DataSetController;
import com.howtodoinjava.springasyncexample.web.model.DataSet;
import com.howtodoinjava.springasyncexample.web.service.DataSetService;

@RunWith(SpringRunner.class)
@WebMvcTest(DataSetController.class)
public class DataSetControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private DataSetService dataSetService;

	@Test
	public void foo() throws Exception {
		Mockito.when(dataSetService.findAll()).thenReturn(Arrays.asList(new DataSet(BigInteger.valueOf(1), "data")));
		MvcResult mvcResult = mockMvc.perform(get("/data-sets")).andExpect(request().asyncStarted())
				.andDo(MockMvcResultHandlers.log()).andReturn();
		mockMvc.perform(asyncDispatch(mvcResult)).andDo(MockMvcResultHandlers.log()).andExpect(status().isOk())
				.andExpect(content().json("{\"id\":1,\"name\":\"data\"}"));
	}
}
