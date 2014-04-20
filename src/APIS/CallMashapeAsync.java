package APIS;

import org.json.JSONException;

import android.os.AsyncTask;
import android.widget.TextView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

	protected HttpResponse<JsonNode> doInBackground(String... msg) {

		HttpResponse<JsonNode> request = null;
		try {
			int a=0;
			String artista="Coldplay";
			request = Unirest.get("https://mager-spotify-web.p.mashape.com/search/1/album.json?q="+artista)
					  .header("X-Mashape-Authorization", "eyYErFoYcZbfabUORW6w1It9UMhqFnGK")
					  .asJson();

		    try {
		    	while (a!=10){
				System.out.println("concha "+ request.getBody().getObject().getJSONArray("albums").getJSONObject(a).get("name").toString());
		    	a++;
		    	Search s=new Search();
	    		s.busqueda(request.getBody().getObject().getJSONArray("albums").getJSONObject(a).get("name").toString());
		    	} 
		    }catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("w");
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		} catch (UnirestException e) {
			// TODO Auto-generated catch block.z
			e.printStackTrace();
		}
		
		return request;
	}
	
	protected void onProgressUpdate(Integer...integers) {
	}

	protected void onPostExecute(HttpResponse<JsonNode> response) {
		String answer = response.getBody().toString();
    	//TextView txtView = (TextView) findViewById(R.id.textView1);
    	//txtView.setText(answer);
	}
}

	
