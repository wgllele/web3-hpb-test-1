package io.hpb.web3.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import io.hpb.web3.codegen.SolidityFunctionWrapperGenerator;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.crypto.WalletUtils;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.http.HttpService;
import io.hpb.web3.tx.ChainId;
import io.hpb.web3.tx.RawTransactionManager;
import io.hpb.web3.utils.Convert;
import okhttp3.OkHttpClient;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.generated.Uint256;

public class UFOTokenRun {

	
	//发布智能合约账号的，keystore全路径,请根据实际地址来修改		
	private static String keyStoreAbsolutePath = "/path/UTC--2018-5cb988b9ce48fd3b5a328b582dd64f5c10d0e114"; 
	//发布智能合约的账号密码，请使用你自己的账号和密码
	private static String fromPassword = "demo111";
    //发布智能合约的地址：这是目前已发布到主网的UFOToken智能合约地址，用户可以进行查询，但是不能转账，转账需要有HPB余额才能转账
	private static String address = "0xfbbe0ba33812b531aced666d0bb2450216c11d11";
    //开放的HPB节点URL地址，也可以自行搭建节点；此节点是HPB正式网开放的节点
	private static String blockChainUrl = "http://pub.node.hpb.io/";
    
	//系统默认参数设置
	private static BigInteger GAS_PRICE = new BigInteger("18000000000");

	private static BigInteger GAS_LIMIT = new BigInteger("100000000");
    
    
	public static void main(String[] args) {
 
	  //指定生成智能合约java映射类的package路径
      String packageName = "io.hpb.web3.test";
	  //指定生成智能合约java映射类源码的本地存放地址
      String outDirPath = "//erc20//UFO//java";
      
      //指定智能合约源码的本地地址，这两个文件也放在本类的package下面，读者可以自行处理
      String binFilePath = "//erc20//UFO//bin//UFOToken.bin";
      String abiFilePath = "//erc20//UFO//bin//UFOToken.abi";


        
      //1、通过io.hpb.web3来生成智能合约sol源码的的映射类；然后把映射的类放到对应的package中
      GenContractJavaCode(packageName, binFilePath, abiFilePath, outDirPath) ;


  	  //2、发布智能合约，并好获取地址
      String address = depolyUFOTokenTest();
		
      //3、得到智能合约的余额
      getUFOTokenContractBalance();
		
	
      //查询指定地址的erc20余额
      String  queryAddress = "0xd8ACcED8A7A92b334007f9C6127A848fE51D3C3b";
      //4、校验智能合约并打印相关的信息	
      checkUFOTokenContract(queryAddress);
		
		
      //5、转账
      String  toAddress = "0x6cb988b9ce48Fd3b5a328B582dd64F5C10d0E114";
      transferUFOTokenContract(toAddress,"13333333000000000000000000");
 
		//5.2 查询余额
		//checkUFOTokenContract(contractAddress,toAddress);
		
	}
	

	/**
	 * 通过智能合约的源码.sol文件和编译后的.bin文件生成java的源码
	 * String packageName:java源码的packagename
	 * String binFileName：存放智能合约bin文件地址
	 * String abiFileName：存放智能合约的abi文件地址
	 * String outDirPath ：java源码输出地址 
	 * 
	 * **/
	public static void GenContractJavaCode(String packageName,String binFilePath,String abiFilePath,String outDirPath) {
		   try {
		        String SOLIDITY_TYPES_ARG = "--solidityTypes";
		        
		        SolidityFunctionWrapperGenerator.main(Arrays.asList(SOLIDITY_TYPES_ARG,
		        		binFilePath,abiFilePath,"-p",packageName, "-o", outDirPath).toArray(new String[0]));
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

	}

	


	/**
	 * 通过编译智能合约源码得到合约映射的java类
	 * 
	 * **/
	public static String depolyUFOTokenTest(){
	
	    Credentials credentials = null;
	    Admin admin = null;

        String contractAddress =  "";
	    try{
	        Web3Service web3Service = new HttpService(blockChainUrl, new OkHttpClient.Builder().build(), true);
	        admin = Admin.build(web3Service);
	        credentials = WalletUtils.loadCredentials(fromPassword, keyStoreAbsolutePath);
	        RawTransactionManager transactionManager=new RawTransactionManager(admin, credentials, ChainId.MAINNET);

	        // 1.发布 TOKEN
	        UFOToken contract = UFOToken.deploy(admin, transactionManager, GAS_PRICE, GAS_LIMIT).send();
	        System.out.println("合约地址：" + contract.getContractAddress());
	        contractAddress = contract.getContractAddress();
	  
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	    return contractAddress;
	}
	
	
	/**
	 * 查询余额
	 * 
	 * **/
	public static BigDecimal getUFOTokenContractBalance(){
		
	    Credentials credentials = null;
	    Admin admin = null;
	    BigDecimal balanceWeiAmt = null;

	    try{
	        
	        Web3Service web3Service = new HttpService(blockChainUrl, new OkHttpClient.Builder().build(), true);
	        admin = Admin.build(web3Service);
	  
	        BigInteger balance = admin.hpbGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
	        balanceWeiAmt = Convert.fromWei(balance.toString(), Convert.Unit.HPB);
	        System.out.println(address + "账户余额：" + balanceWeiAmt);
 
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	    
	    return balanceWeiAmt;
	}
	
	
	
	/**
	 * 校验智能合约并打印地址ERC20余额
	 * 
	 * **/
	public static void checkUFOTokenContract(String queryAddress){
		 
	    Credentials credentials = null;
	    Admin admin = null;
	    
	    try{
	        
	        Web3Service web3Service = new HttpService(blockChainUrl, new OkHttpClient.Builder().build(), true);
	        admin = Admin.build(web3Service);
	        credentials = WalletUtils.loadCredentials(fromPassword, keyStoreAbsolutePath);
	        RawTransactionManager transactionManager=new RawTransactionManager(admin, credentials, ChainId.MAINNET);
	        
	        //检查合约是否可用

	        UFOToken contract = UFOToken.load(address, admin, transactionManager, GAS_PRICE, GAS_LIMIT);
	        System.out.println("验证合约是否有效:" +contract.isValid() );
	        if(contract.isValid()) {
		        BigInteger totalSupply = contract.totalSupply().send().getValue().divide(new BigInteger("1000000000000000000"));	        
		        System.out.println("UFOtoken总供给量："+totalSupply);
	        	    System.out.println(address+" UFOToken余额："+contract.balanceOf(new Address(address)).sendAsync().get().getValue().divide(new BigInteger("1000000000000000000")));	  			        
		        System.out.println(queryAddress+" UFOToken余额："+contract.balanceOf(new Address(queryAddress)).sendAsync().get().getValue().divide(new BigInteger("1000000000000000000")));	  
	        }
	        


	    }catch (Exception e){
	        e.printStackTrace();
	    }
	}
	
	public String toDecimal(int decimal, BigInteger integer) {
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
        return balance;
    }



	/**
	 * 转账
	 * 
	 * **/
	public static void transferUFOTokenContract(String toAddress,String toValue){
		//keystore全路径
	    Credentials credentials = null;
	    Admin admin = null;
	    try{
	        
	        Web3Service web3Service = new HttpService(blockChainUrl, new OkHttpClient.Builder().build(), true);
	        admin = Admin.build(web3Service);
	        credentials = WalletUtils.loadCredentials(fromPassword, keyStoreAbsolutePath);
	        RawTransactionManager transactionManager=new RawTransactionManager(admin, credentials, ChainId.MAINNET);

	        // 转账交易
	        UFOToken contract = UFOToken.load(address, admin, transactionManager, GAS_PRICE, GAS_LIMIT);
	        TransactionReceipt receipt =  contract.transfer(new Address(toAddress), new Uint256(new BigInteger(toValue))).send();
	        System.out.println("交易Hash::::::"+receipt.getTransactionHash());
	        
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	}
 

}
