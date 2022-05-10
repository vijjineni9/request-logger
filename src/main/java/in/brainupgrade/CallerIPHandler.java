package in.brainupgrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallerIPHandler implements RequestStreamHandler {

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		JSONObject responseJson = new JSONObject();

		try {
			JSONObject event = (JSONObject) parser.parse(reader);
			log.info("Inputstream parsed:" + event.toJSONString());
			JSONObject responseBody = new JSONObject();
			if (event.get("requestContext") != null) {
				JSONObject requestContext = (JSONObject)event.get("requestContext");
				JSONObject http = (JSONObject)requestContext.get("http");
				String sourceIP = (String)http.get("sourceIp");
				responseBody.put("message", "Your IP: " + sourceIP);
			} else {
				responseBody.put("message", "Your request received");
			}

			JSONObject headerJson = new JSONObject();
			headerJson.put("x-custom-header", "AWS Tech custom header");

			responseJson.put("statusCode", 200);
			responseJson.put("headers", headerJson);
			responseJson.put("body", responseBody.toString());

		} catch (ParseException pex) {
			responseJson.put("statusCode", 400);
			responseJson.put("exception", pex);
		}

		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		writer.write(responseJson.toString());
		writer.close();
	}
}
