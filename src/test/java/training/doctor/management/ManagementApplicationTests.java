package training.doctor.management;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import training.doctor.management.controller.AppointmentController;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@SpringBootTest
@AutoConfigureMockMvc
class ManagementApplicationTests {
	@Autowired
	private MockMvc mvc;

//	@MockBean
//	private  service;


	@Test
	public void testGetAllClinics() throws Exception{
		mvc.perform(get("/doctors")
						.contentType(MediaType.APPLICATION_JSON).header("uuid","asndnsadas"))
				.andExpect(status().isOk());
//				.andExpect(jsonPath());

}

@Test
	public void checkGetDoctorAvailability() throws Exception{
    mvc.perform(
        get("/doctor-avaliablity")
            .header("uuid", "sdsds")
            .contentType(MediaType.APPLICATION_JSON)
            .param("doctorName", "ram")
            .param("clinicName", "avb").param("clinicCity","aman").param("date")).andExpect(status().isOk());

}

}
