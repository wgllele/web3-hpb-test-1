# HPB web3-hpb-test Demo


This is the HPB Java API which implements the Generic JSON RPC spec.

You need to run a local HPB node to use this demo.




## Usage

open `package io.hpb.web3.controller;` 

```demo1 QueryByHash

@Autowired
	private Admin admin;
	@ApiOperation(value="Search the transaction receipt according to the transaction hash",
	notes = "Query the transaction receipt according to the transaction hash"
			+ " reqStrList [  parameter1：transaction hash]")
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

```

```demo1 QueryByHash
@ApiOperation(value="Get the current block number",notes = "Get the current block number")
	@PostMapping("/getCurrentBlock")
	public List<Object> getCurrentBlock()throws Exception{
		List<Object> list=new ArrayList<Object>();
		HpbBlockNumber blockNumber = admin.hpbBlockNumber().sendAsync().get(WEB3J_TIMEOUT, TimeUnit.MINUTES);
		list.add(blockNumber);
		return list;
	}

```

You can find more examples in the [`example`](https://github.com/loglos/web3-hpb-test/tree/master/src/main/java/io/hpb/web3/controller) directory.

# text
