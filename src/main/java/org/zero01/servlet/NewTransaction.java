package org.zero01.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.zero01.core.BlockChain;

//��Servlet���ڽ��ղ������µĽ�����Ϣ
@WebServlet("/transactions/new")
public class NewTransaction extends HttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		// ��ȡ�ͻ��˴��ݹ��������ݲ�ת����JSON��ʽ
		BufferedReader reader = req.getReader();
		String input = null;
		StringBuffer requestBody = new StringBuffer();
		while ((input = reader.readLine()) != null) {
			requestBody.append(input);
		}
		JSONObject jsonValues = new JSONObject(requestBody.toString());

		// �������Ҫ���ֶ��Ƿ�λ��POST��data��
		String[] required = { "sender", "recipient", "amount" };
		for (String string : required) {
			if (!jsonValues.has(string)) {
				// ���û����Ҫ���ֶξͷ��ش�����Ϣ
				resp.sendError(400, "Missing values");
			}
		}

		// �½�������Ϣ
		BlockChain blockChain = BlockChain.getInstance();
		int index = blockChain.newTransactions(jsonValues.getString("sender"), jsonValues.getString("recipient"),
				jsonValues.getLong("amount"));

		// ����json��ʽ�����ݸ��ͻ���
		resp.setContentType("application/json");
		PrintWriter printWriter = resp.getWriter();
		printWriter.println(new JSONObject().append("message", "Transaction will be added to Block " + index));
		printWriter.close();
	}
}
