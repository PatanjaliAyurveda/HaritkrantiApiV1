package test;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;

public class ConsumeRestApi {

	public static void main(String[] args) {
		try {
			final String uri = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=0&limit=10";
		    RestTemplate restTemplate = new RestTemplate();
		    MandiRate result = restTemplate.getForObject(uri,MandiRate.class);
		   // List<MandiRate> mandiRates = JSONUtils.convertFromJsonToList(result, new TypeReference<List<MandiRate>>() {});
		    System.out.println(result.getDesc());
		    for(record r:result.getRecords()) {
		    	System.out.println(r.getState()+ " " + r.getDistrict());
		    }
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
