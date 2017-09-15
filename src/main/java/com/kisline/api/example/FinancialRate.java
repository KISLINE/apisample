package com.kisline.api.example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FinancialRate extends Base {
	private static final Logger LOGGER = LogManager.getLogger("FinancialRate");

	private static final String APIURL = "/companyFinancialAnalIfo/companyFinancialAnal/financialRate";

	public static void main(String[] args) {

		try {

			Map<String, String> paramMap = new HashMap<>();
			StringBuilder sb = new StringBuilder();
			String key = "";
			String value = "";
			String json = "";

			Gson gson = new Gson();
			JsonParser parser = new JsonParser();

			paramMap.put("kiscode", "380725");
			paramMap.put("stac_date", "20161231");
			// ver : 비율분석구분(0:주요비율, 1:성장성, 2:수익성, 3:안정성, 4:활동성, 5:생산성, 기본 - 0)
			paramMap.put("ver", "2");

			for (Iterator<String> it = paramMap.keySet().iterator(); it.hasNext();) {
				key = String.valueOf(it.next());
				value = String.valueOf(paramMap.get(key));
				sb.append("&");
				sb.append(key);
				sb.append("=");
				sb.append(value);
			}

			OkHttpClient client;

			client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
					.readTimeout(30, TimeUnit.SECONDS).build();

			Request request = new Request.Builder().url(APIBASEURL + APIURL + "?uid=" + UID + sb).get()
					.addHeader("x-ibm-client-id", CLIENTID).addHeader("x-ibm-client-secret", CLIENTSECRET)
					.addHeader("accept", RESPONSETYPE).build();

			Response response = client.newCall(request).execute();

			json = response.body().string();

			LOGGER.info(response.headers().toString());
			LOGGER.info(json);

			JsonElement element = parser.parse(json).getAsJsonObject().get("items").getAsJsonObject().get("item");

			ResultObject[] example = gson.fromJson(element, ResultObject[].class);

			for (ResultObject temp : example) {
				LOGGER.info("farptcd : " + temp.getFarptcd() + ", farptitemcd : " + temp.getFarptitemcd()
						+ ", name_kor : " + temp.getName_kor() + ", biyul0 : " + temp.getBiyul0() + ", biyul1 : "
						+ temp.getBiyul1());
			}
		} catch (Exception ioe) {
			LOGGER.error(ioe);
		}
	}

}
