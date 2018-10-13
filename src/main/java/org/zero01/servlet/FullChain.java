package org.zero01.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.zero01.core.BlockChain;

//��Servlet�����������������������
@WebServlet("/chain")
public class FullChain extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȡ�����������������ݲ���ʽ��ΪJSON��ʽ
		BlockChain blockChain = BlockChain.getInstance();
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("chain", blockChain.getChain());
		response.put("length", blockChain.getChain().size());
		JSONObject jsonResponse = new JSONObject(response);
		
		// �������������������ݸ��ͻ���
		resp.setContentType("application/json");
		PrintWriter printWriter = resp.getWriter();
		printWriter.println(jsonResponse);
		printWriter.close();
	}
}
