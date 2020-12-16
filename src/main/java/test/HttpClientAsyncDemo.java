package test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
//import com.httpclientdemo.business.bean.ProductBean;
//import com.httpclientdemo.utility.JSONUtils;
public class HttpClientAsyncDemo {
	
	private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
	private static final String serviceURL = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=0&limit=10";
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException{
		// TODO Auto-generated method stub
		getAllProducts();
	}
	
	public static void getAllProducts() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException{
	    HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"getDetails"))
	    .GET().build();
	    CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
	    response.thenAccept(res -> System.out.println(res));
	  //  List<ProductBean> products = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ProductBean>>() {});
	  //  products.forEach(System.out::println);
	    response.join();
	}

}
