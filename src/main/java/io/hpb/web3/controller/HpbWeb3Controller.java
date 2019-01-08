package io.hpb.web3.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.DynamicArray;
import io.hpb.web3.abi.datatypes.generated.Bytes32;
import io.hpb.web3.contract.HpbNodes;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.crypto.RawTransaction;
import io.hpb.web3.crypto.WalletUtils;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbBlockNumber;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.tuples.generated.Tuple4;
import io.hpb.web3.tx.ChainId;
import io.hpb.web3.tx.RawTransactionManager;
import io.hpb.web3.utils.Convert;
import io.hpb.web3.utils.Numeric;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class HpbWeb3Controller{
	private static Log log = LogFactory.getLog(HpbWeb3Controller.class);
	private final long WEB3J_TIMEOUT = 3;
	private final String contractAddr = "0x7be6aa25600feed355b79b6a4e14dcdb0bd529cb";
	private final BigInteger gasPrice = Convert.toWei("18", Convert.Unit.GWEI).toBigInteger();
	private final BigInteger gasLimit = new BigInteger("95000000");
	@Autowired
	private Admin admin;
	@ApiOperation(value="通过根据交易hash查询交易收据",notes = "过根据交易hash查询交易收据"
			+ " reqStrList [  参数1：交易hash]")
	@PostMapping("/QueryByHash")
	public List<Object> QueryByHash(@RequestBody List<String> reqStrList)throws Exception{
		List<Object> list=new ArrayList<Object>();
		if(reqStrList!=null&&reqStrList.size()>0) {
			String transactionHash = reqStrList.get(0);
			HpbGetTransactionReceipt receipt = admin.hpbGetTransactionReceipt(transactionHash).send();
			if(!receipt.hasError()) {
				TransactionReceipt transactionReceipt = receipt.getResult();
				if(transactionReceipt.isStatusOK()) {
					list.add(transactionReceipt);
				}
			}
		}
		return list;
	}
	@ApiOperation(value="获得当前区块号",notes = "获得当前区块号")
	@PostMapping("/getCurrentBlock")
	public List<Object> getCurrentBlock()throws Exception{
		List<Object> list=new ArrayList<Object>();
		HpbBlockNumber blockNumber = admin.hpbBlockNumber().sendAsync().get(WEB3J_TIMEOUT, TimeUnit.MINUTES);
		list.add(blockNumber);
		return list;
	}
	@ApiOperation(value="获得当前账户的Nonce",notes = "获得当前账户的Nonce"
			+ " reqStrList [ 参数1：账户地址;")
	@PostMapping("/getCurrentNonce")
	public List<Object> getCurrentNonce(@RequestBody List<String> reqStrList)throws Exception{
		List<Object> list=new ArrayList<Object>();
		if(reqStrList!=null&&reqStrList.size()>0) {
			String address =reqStrList.get(0);
			HpbGetTransactionCount transactionCount = admin.hpbGetTransactionCount(address, 
					DefaultBlockParameterName.PENDING).sendAsync().get(WEB3J_TIMEOUT, TimeUnit.MINUTES);
			BigInteger nonce = transactionCount.getTransactionCount();
			log.info(nonce);
			list.add(nonce);
		}
		return list;
	}
	@ApiOperation(value="获得当前账户的余额",notes = "获得当前账户的余额"
			+ " reqStrList [ 参数1：账户地址; ]")
	@PostMapping("/getBalance")
	public List<Object> getBalance(@RequestBody List<String> reqStrList)throws Exception{
		List<Object> list=new ArrayList<Object>();
		if(reqStrList!=null&&reqStrList.size()>0) {
			String address =reqStrList.get(0);
			HpbGetBalance balance = admin.hpbGetBalance(address, DefaultBlockParameterName.LATEST).send();
			log.info(balance);
			list.add(balance);
		}
		return list;
	}
	@ApiOperation(value="发送交易",notes = "发送交易"
			+ " reqStrList [ 参数1：账户keystore地址; 参数2：密码; 参数3：接收账户地址;参数4：转账金额;]")
	@PostMapping("/sendTransaction")
	public List<Object> sendTransaction(@RequestBody List<String> reqStrList)throws Exception{
		List<Object> list=new ArrayList<Object>();
		if(reqStrList!=null&&reqStrList.size()>3) {
			String keystore =reqStrList.get(0);
			String password =reqStrList.get(1);
			Credentials credentials = WalletUtils.loadCredentials(password, keystore);
			RawTransactionManager transactionManager=new RawTransactionManager(admin, credentials, ChainId.MAINNET);
			HpbGetTransactionCount transactionCount = admin.hpbGetTransactionCount(credentials.getAddress(), 
					DefaultBlockParameterName.PENDING).sendAsync().get(WEB3J_TIMEOUT, TimeUnit.MINUTES);
			BigInteger nonce = transactionCount.getTransactionCount();
			String to =reqStrList.get(2);
			String value =reqStrList.get(3);
			RawTransaction rawTransaction = RawTransaction.createTransaction(
					nonce,
					gasPrice,
					gasLimit,
					to,
					Convert.toWei(value, Convert.Unit.HPB).toBigInteger(),
					"");
			HpbSendTransaction transaction = transactionManager.signAndSend(rawTransaction);
			log.info(transaction.getTransactionHash());
			list.add(transaction);
		}
		return list;
	}
	@ApiOperation(value="调用HpbNodes智能合约",notes = "调用HpbNodes智能合约"
			+ " reqStrList [ 参数1：账户keystore地址; 参数2：密码]")
	@PostMapping("/invokeHpbNodes")
	public List<Object> invokeHpbNodes(@RequestBody List<String> reqStrList)throws Exception{
		List<Object> list=new ArrayList<Object>();
		if(reqStrList!=null&&reqStrList.size()>1) {
			String keystore =reqStrList.get(0);
			String password =reqStrList.get(1);
			Credentials credentials = WalletUtils.loadCredentials(password, keystore);
			RawTransactionManager transactionManager=new RawTransactionManager(admin, credentials, ChainId.MAINNET);
			HpbNodes hpbNodes = HpbNodes.load(contractAddr, admin, transactionManager, gasPrice, gasLimit);
			//调用智能合约
			Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>> send = 
					hpbNodes.getAllHpbNodes().send();
			Bytes32 bytes32 = send.getValue2().getValue().get(1);
			log.info(Numeric.toHexStringNoPrefix(bytes32.getValue()));
			Bytes32 bytes321 = send.getValue3().getValue().get(1);
			log.info(Numeric.toHexStringNoPrefix(bytes321.getValue()));
			Bytes32 bytes322 = send.getValue3().getValue().get(1);
			log.info(Numeric.toHexStringNoPrefix(bytes322.getValue()));
			list.add(send);
		}
		return list;
	}
	
}