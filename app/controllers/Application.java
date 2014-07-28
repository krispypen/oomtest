package controllers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import play.libs.F;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static Map<String, Promise<Double>> map = new ConcurrentHashMap<>();

	public static Promise<Result> index() {
		Promise<Double> result = WS.url("http://finance.yahoo.com/d/quotes.csv").setQueryParameter("s", "msft").setQueryParameter("f", "price").get().map(new F.Function<WS.Response, Double>() {
			@Override
			public Double apply(Response arg0) throws Throwable {
				return Double.valueOf(arg0.getBody().split(",")[0]);
			}
		});
		map.put(System.currentTimeMillis()+"", result);
		return result.map(new F.Function<Double, Result>() {
			@Override
			public Result apply(Double arg0) throws Throwable {
				return ok("Current price: "+arg0);
			}
		});
	}

}
