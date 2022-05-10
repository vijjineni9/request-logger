package in.brainupgrade;

import java.util.Collections;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloRequestHandler implements RequestHandler<Object, GatewayResponse> {

	@Override
	public GatewayResponse handleRequest(Object object, Context context) {

		String message = "Hello from Java Request Logger";
		log.info(message + object.toString());

		GatewayResponse response = new GatewayResponse(message, 200,
				Collections.singletonMap("X-Powered-By", "Cloud Tech"), false);
		log.info(response.toString());
		return response;
	}
}