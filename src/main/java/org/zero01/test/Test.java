package org.zero01.test;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.zero01.core.BlockChain;

public class Test {

	public static void main(String[] args) throws Exception {

		BlockChain blockChain = BlockChain.getInstance();

		// һ�������п��Բ������κν��׼�¼
		Map<String, Object> block = blockChain.newBlock(300, null);
		System.out.println(new JSONObject(block));

		// һ�������п��԰���һ�ʽ��׼�¼
		blockChain.newTransactions("123", "222", 33);
		Map<String, Object> block1 = blockChain.newBlock(500, null);
		System.out.println(new JSONObject(block1));

		// һ�������п��԰�����ʽ��׼�¼
		blockChain.newTransactions("321", "555", 133);
		blockChain.newTransactions("000", "111", 10);
		blockChain.newTransactions("789", "369", 65);
		Map<String, Object> block2 = blockChain.newBlock(600, null);
		System.out.println(new JSONObject(block2));

		// �鿴����������
		Map<String, Object> chain = new HashMap<String, Object>();
		chain.put("chain", blockChain.getChain());
		chain.put("length", blockChain.getChain().size());
		System.out.println(new JSONObject(chain));
	}
}
