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

	
	//Keystore Full Path for Publishing Intelligent Contract Accounts	
	private static String keyStoreAbsolutePath = "/Users/hpb2017/path/hpbkey/UTC--2018-09-13T10-07-27.074Zd0e114"; 
	//Publish passwords for smart contracts
	private static String fromPassword = "demo111111";
        //Publish the address of the intelligent contract: This is the UFOToken intelligent contract address which has been published to the main network. Users can query, but can not transfer. Transfer needs HPB balance to transfer.
	private static String address = "0xfbbe0ba33812b531aced666d0bb2450216c11d11";
        //Public Node of HPB Main Network
	private static String blockChainUrl = "http://mainnet.hpb.io/";
    
	//System default parameter settings
	private static BigDecimal GAS_PRICE = Convert.toWei("18", Convert.Unit.GWEI);

	private static BigDecimal GAS_LIMIT = Convert.toWei("99000000", Convert.Unit.HPB);
 
	
	
    
	public static void main(String[] args) {
 
	//Specify the package path to generate smart contract Java mapping classes
        String packageName = "io.hpb.web3.test";
	//Specify the local storage address for generating smart contract Java mapping class source code
        String outDirPath = "//Users//hpb2017//test//erc20//UFO//java";
      
      //Specify the local address of the smart contract source code. These two files are also placed under the package of this class, and the reader can handle them by himself.
      String binFilePath = "//Users//hpb2017//test//erc20//UFO//bin//UFOToken.bin";
      String abiFilePath = "//Users//hpb2017//test//erc20//UFO//bin//UFOToken.abi";


        
      //1、Generate the mapping class of intelligent contract sol source code by io. hpb. web3; then put the mapping class into the corresponding package
      GenContractJavaCode(packageName, binFilePath, abiFilePath, outDirPath) ;


      //2、Publish Smart Contracts and Get Addresses
      String address = depolyUFOTokenTest();
		
      //3、Get the balance of the smart contract
      getUFOTokenContractBalance();
		
	
      //Query erc20 balance at specified address
      String  queryAddress = "0xd8ACcED8A7A92b334007f9C6127A848fE51D3C3b";
      
      //4、Check smart contracts and print relevant information	
      checkUFOTokenContract(queryAddress);
		
		
      //5、Transfer accounts
      String  toAddress = "0x6cb988b9ce48Fd3b5a328B582dd64F5C10d0E114";
      transferUFOTokenContract(toAddress,"13333333000000000000000000");
 
      //5.2 Check the balance
      //checkUFOTokenContract(contractAddress,toAddress);
		
	}
	

	/**
	 * Generate the source code of Java through the source code of intelligent contract. sol file and compiled. bin file
	 * String packageName: Packagename of Java source code
	 * String binFileName： Store the address of intelligent contract bin file
	 * String abiFileName： ABI file address for storing intelligent contracts
	 * String outDirPath ： Java source output address
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
	 * Compiling Intelligent Contract Source Code to Get the Java Class of Contract Mapping
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

	        // 1.Release TOKEN
	        UFOToken contract = UFOToken.deploy(admin, transactionManager, GAS_PRICE.toBigInteger(), GAS_LIMIT.toBigInteger()).send();
	        System.out.println("Contract address：" + contract.getContractAddress());
	        contractAddress = contract.getContractAddress();
	  
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	    return contractAddress;
	}
	
	
	/**
	 * Check the balance
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
	        System.out.println(address + "Account balance：" + balanceWeiAmt);
 
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	    
	    return balanceWeiAmt;
	}
	
	
	
	/**
	 * Check the smart contract and print the address ERC20 balance
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
	        
	        //Check whether the contract is available

	        UFOToken contract = UFOToken.load(address, admin, transactionManager, GAS_PRICE.toBigInteger(), GAS_LIMIT.toBigInteger());
	        System.out.println("Verify the validity of the contract:" +contract.isValid() );
	        if(contract.isValid()) {
		        BigInteger totalSupply = contract.totalSupply().send().getValue().divide(new BigInteger("1000000000000000000"));	        
		        System.out.println("UFOtoken Total Supply："+totalSupply);
	        	    System.out.println(address+" UFOToken balance："+contract.balanceOf(new Address(address)).sendAsync().get().getValue().divide(new BigInteger("1000000000000000000")));	  			        
		        System.out.println(queryAddress+" UFOToken balance："+contract.balanceOf(new Address(queryAddress)).sendAsync().get().getValue().divide(new BigInteger("1000000000000000000")));	  
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
	 * Transfer accounts
	 * 
	 * **/
	public static void transferUFOTokenContract(String toAddress,String toValue){
	    
	    Credentials credentials = null;
	    Admin admin = null;
	    try{
	        
	        Web3Service web3Service = new HttpService(blockChainUrl, new OkHttpClient.Builder().build(), true);
	        admin = Admin.build(web3Service);
	        credentials = WalletUtils.loadCredentials(fromPassword, keyStoreAbsolutePath);
	        RawTransactionManager transactionManager=new RawTransactionManager(admin, credentials, ChainId.MAINNET);

	        // Transfer transaction
	        UFOToken contract = UFOToken.load(address, admin, transactionManager, GAS_PRICE.toBigInteger(), GAS_LIMIT.toBigInteger());
	        TransactionReceipt receipt =  contract.transfer(new Address(toAddress), new Uint256(new BigInteger(toValue))).send();
	        System.out.println("transaction Hash::::::"+receipt.getTransactionHash());
	        
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	}
 

}
