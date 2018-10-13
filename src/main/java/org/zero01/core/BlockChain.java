package org.zero01.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.zero01.util.Encrypt;

public class BlockChain {

	// �洢������
	private List<Map<String, Object>> chain;
	// ��ʵ���������ڵ�ǰ�Ľ�����Ϣ�б�
	private List<Map<String, Object>> currentTransactions;
	private static BlockChain blockChain = null;
	private Set<String> nodes;

	public Set<String> getNodes() {
		return nodes;
	}

	private BlockChain() {
		// ��ʼ���������Լ���ǰ�Ľ�����Ϣ�б�
		chain = new ArrayList<Map<String, Object>>();
		currentTransactions = new ArrayList<Map<String, Object>>();

		// ������������
		newBlock(100, "0");

		// ���ڴ洢�����������ڵ�ļ���
		nodes = new HashSet<String>();
	}

	// ������������
	public static BlockChain getInstance() {
		if (blockChain == null) {
			synchronized (BlockChain.class) {
				if (blockChain == null) {
					blockChain = new BlockChain();
				}
			}
		}
		return blockChain;
	}

	public List<Map<String, Object>> getChain() {
		return chain;
	}

	public void setChain(List<Map<String, Object>> chain) {
		this.chain = chain;
	}

	public List<Map<String, Object>> getCurrentTransactions() {
		return currentTransactions;
	}

	public void setCurrentTransactions(List<Map<String, Object>> currentTransactions) {
		this.currentTransactions = currentTransactions;
	}

	/**
	 * @return �õ��������е����һ������
	 */
	public Map<String, Object> lastBlock() {
		return getChain().get(getChain().size() - 1);
	}

	/**
	 * �����������½�һ������
	 * 
	 * @param proof
	 *            ������Ĺ�����֤��
	 * @param previous_hash
	 *            ��һ�������hashֵ
	 * @return �����½�������
	 */
	public Map<String, Object> newBlock(long proof, String previous_hash) {

		Map<String, Object> block = new HashMap<String, Object>();
		block.put("index", getChain().size() + 1);
		block.put("timestamp", System.currentTimeMillis());
		block.put("transactions", getCurrentTransactions());
		block.put("proof", proof);
		// ���û�д�����һ�������hash�ͼ���������������һ�������hash
		block.put("previous_hash", previous_hash != null ? previous_hash : hash(getChain().get(getChain().size() - 1)));

		// ���õ�ǰ�Ľ�����Ϣ�б�
		setCurrentTransactions(new ArrayList<Map<String, Object>>());

		getChain().add(block);

		return block;
	}

	/**
	 * �����½�����Ϣ����Ϣ�����뵽��һ�����ڵ�������
	 * 
	 * @param sender
	 *            ���ͷ��ĵ�ַ
	 * @param recipient
	 *            ���շ��ĵ�ַ
	 * @param amount
	 *            ��������
	 * @return ���ش洢�ý�������Ŀ������
	 */
	public int newTransactions(String sender, String recipient, long amount) {

		Map<String, Object> transaction = new HashMap<String, Object>();
		transaction.put("sender", sender);
		transaction.put("recipient", recipient);
		transaction.put("amount", amount);

		getCurrentTransactions().add(transaction);

		return (Integer) lastBlock().get("index") + 1;
	}

	/**
	 * ��������� SHA-256��ʽ�� hashֵ
	 * 
	 * @param block
	 *            ����
	 * @return ���ظ������hash
	 */
	public static Object hash(Map<String, Object> block) {
		return new Encrypt().getSHA256(new JSONObject(block).toString());
	}

	/**
	 * �򵥵Ĺ�����֤��: - ����һ�� p' ʹ�� hash(pp') ��4��0��ͷ - p ����һ�����֤��, p' �ǵ�ǰ��֤��
	 * 
	 * @param last_proof
	 *            ��һ�����֤��
	 * @return
	 */
	public long proofOfWork(long last_proof) {
		long proof = 0;
		while (!validProof(last_proof, proof)) {
			proof += 1;
		}
		return proof;
	}

	/**
	 * ��֤֤��: �Ƿ�hash(last_proof, proof)��4��0��ͷ?
	 * 
	 * @param last_proof
	 *            ��һ�����֤��
	 * @param proof
	 *            ��ǰ��֤��
	 * @return ��4��0��ͷ����true�����򷵻�false
	 */
	public boolean validProof(long last_proof, long proof) {
		String guess = last_proof + "" + proof;
		String guess_hash = new Encrypt().getSHA256(guess);
		return guess_hash.startsWith("0000");
	}

	/**
	 * ע��ڵ�
	 * 
	 * @param address
	 *            �ڵ��ַ
	 * @throws MalformedURLException
	 */
	public void registerNode(String address) throws MalformedURLException {
		URL url = new URL(address);
		String node = url.getHost() + ":" + (url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
		nodes.add(node);
	}

	/**
	 * ����Ƿ�����Ч��������ÿ��������֤hash��proof����ȷ��һ���������������Ƿ���Ч
	 * 
	 * @param chain
	 * @return
	 */
	public boolean validChain(List<Map<String, Object>> chain) {
		Map<String, Object> lastBlock = chain.get(0);
		int currentIndex = 1;
		while (currentIndex < chain.size()) {
			Map<String, Object> block = chain.get(currentIndex);
			System.out.println(lastBlock.toString());
			System.out.println(block.toString());
			System.out.println("\n-------------------------\n");

			// ���block��hash�Ƿ���ȷ
			if (!block.get("previous_hash").equals(hash(lastBlock))) {
				System.out.println("false");
				return false;
			}

			lastBlock = block;
			currentIndex++;
		}
		return true;
	}

	/**
	 * ��ʶ�㷨�����ͻ��ʹ�������������. �������е��ھӽڵ㣬������һ���������������Ч�ԣ� ���������Ч�����������滻���Լ�����
	 * 
	 * @return �������ȡ������true, ���򷵻�false
	 * @throws IOException
	 */
	public boolean resolveConflicts() throws IOException {
		Set<String> neighbours = this.nodes;
		List<Map<String, Object>> newChain = null;

		// Ѱ�����������
		long maxLength = this.chain.size();

		// ��ȡ����֤�����е����нڵ��������
		for (String node : neighbours) {

			URL url = new URL("http://" + node + "/BlockChain_Java/chain");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			if (connection.getResponseCode() == 200) {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(connection.getInputStream(), "utf-8"));
				StringBuffer responseData = new StringBuffer();
				String response = null;
				while ((response = bufferedReader.readLine()) != null) {
					responseData.append(response);
				}
				bufferedReader.close();

				JSONObject jsonData = new JSONObject(responseData.toString());
				long length = jsonData.getLong("length");
				List<Map<String, Object>> chain = (List) jsonData.getJSONArray("chain").toList();

				// ��鳤���Ƿ񳤣����Ƿ���Ч
				if (length > maxLength && validChain(chain)) {
					maxLength = length;
					newChain = chain;
				}
			}

		}
		// �������һ���µ���Ч�������ǵĳ������滻��ǰ����
		if (newChain != null) {
			this.chain = newChain;
			return true;
		}
		return false;
	}
}
