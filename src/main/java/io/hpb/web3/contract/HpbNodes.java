package io.hpb.web3.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.hpb.web3.abi.EventEncoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.DynamicArray;
import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.abi.datatypes.generated.Bytes32;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.protocol.core.methods.request.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.tuples.generated.Tuple4;
import io.hpb.web3.tx.Contract;
import io.hpb.web3.tx.RawTransactionManager;
import io.hpb.web3.tx.TransactionManager;
import io.hpb.web3.tx.gas.ContractGasProvider;
import io.hpb.web3.tx.gas.StaticGasProvider;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3.io/command_line.html">web3 command line tools</a>,
 * or the io.hpb.web3.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3/web3/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3 version none.
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class HpbNodes extends Contract {
    private static final String BINARY = "60c0604052601160808190527f485042204e6f646573205365727669636500000000000000000000000000000060a0908152620000409160009190620001b4565b5060006003553480156200005357600080fd5b5060018054600160a060020a031916331790556200007b600064010000000062000081810204565b6200031d565b600381905560028054906200009a906001830162000239565b5060006002600354815481101515620000af57fe5b600091825260208083203384526003928302016002908101909152604090922092909255905481548110620000e057fe5b600091825260208083206040805160808101825233815280840186815291810186815260608201878152600396870290940160019081018054808301825590895295909720915160049095029091018054600160a060020a031916600160a060020a0390951694909417845590519483019490945592516002808301919091559251908201555481544392919081106200017657fe5b6000918252602082206003909102019190915560405182917f9d49bd24aa746fc8f19004008bcfc0a0bebf6660e0bb478c3a1b83d27fa2759991a250565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620001f757805160ff191683800117855562000227565b8280016001018555821562000227579182015b82811115620002275782518255916020019190600101906200020a565b50620002359291506200026d565b5090565b81548183558181111562000268576003028160030283600052602060002091820191016200026891906200028d565b505050565b6200028a91905b8082111562000235576000815560010162000274565b90565b6200028a91905b8082111562000235576000808255620002b16001830182620002bb565b5060030162000294565b5080546000825560040290600052602060002090810190620002de9190620002e1565b50565b6200028a91905b8082111562000235578054600160a060020a0319168155600060018201819055600282018190556003820155600401620002e8565b611575806200032d6000396000f3006080604052600436106100da5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306ec967b81146100df57806306fdde03146101365780630eb82c13146101c0578063175a4ed2146101ea5780631de4745f146102115780632cf459051461022957806352baee20146103295780637e5a9ed9146104645780638655f0da146105645780638da5cb5b1461057c5780639508614b146105ad57806398791010146105c2578063dbe01790146105e3578063ebae7bf1146105f8578063f2fde38b14610622575b600080fd5b3480156100eb57600080fd5b5060408051602060048035808201358381028086018501909652808552610134953695939460249493850192918291850190849080828437509497506106439650505050505050565b005b34801561014257600080fd5b5061014b610696565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561018557818101518382015260200161016d565b50505050905090810190601f1680156101b25780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101cc57600080fd5b50610134600160a060020a0360043516602435604435606435610724565b3480156101f657600080fd5b506101ff610909565b60408051918252519081900360200190f35b34801561021d57600080fd5b506101ff60043561090f565b34801561023557600080fd5b506040805160206004803580820135838102808601850190965280855261013495369593946024949385019291829185019084908082843750506040805187358901803560208181028481018201909552818452989b9a998901989297509082019550935083925085019084908082843750506040805187358901803560208181028481018201909552818452989b9a998901989297509082019550935083925085019084908082843750506040805187358901803560208181028481018201909552818452989b9a9989019892975090820195509350839250850190849080828437509497506109339650505050505050565b34801561033557600080fd5b506103416004356109d1565b6040518080602001806020018060200180602001858103855289818151815260200191508051906020019060200280838360005b8381101561038d578181015183820152602001610375565b50505050905001858103845288818151815260200191508051906020019060200280838360005b838110156103cc5781810151838201526020016103b4565b50505050905001858103835287818151815260200191508051906020019060200280838360005b8381101561040b5781810151838201526020016103f3565b50505050905001858103825286818151815260200191508051906020019060200280838360005b8381101561044a578181015183820152602001610432565b505050509050019850505050505050505060405180910390f35b34801561047057600080fd5b506040805160206004803580820135838102808601850190965280855261013495369593946024949385019291829185019084908082843750506040805187358901803560208181028481018201909552818452989b9a998901989297509082019550935083925085019084908082843750506040805187358901803560208181028481018201909552818452989b9a998901989297509082019550935083925085019084908082843750506040805187358901803560208181028481018201909552818452989b9a998901989297509082019550935083925085019084908082843750949750610c999650505050505050565b34801561057057600080fd5b50610134600435610d30565b34801561058857600080fd5b50610591610ddf565b60408051600160a060020a039092168252519081900360200190f35b3480156105b957600080fd5b50610134610dee565b3480156105ce57600080fd5b50610134600160a060020a0360043516610e15565b3480156105ef57600080fd5b50610341611070565b34801561060457600080fd5b50610134600160a060020a036004351660243560443560643561108f565b34801561062e57600080fd5b50610134600160a060020a036004351661122e565b600154600090600160a060020a0316331461065d57600080fd5b5060005b81518110156106925761068a828281518110151561067b57fe5b90602001906020020151610e15565b600101610661565b5050565b6000805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561071c5780601f106106f15761010080835404028352916020019161071c565b820191906000526020600020905b8154815290600101906020018083116106ff57829003601f168201915b505050505081565b600154600090600160a060020a0316331461073e57600080fd5b600260035481548110151561074f57fe5b60009182526020808320600160a060020a038916845260026003909302019190910190526040902054905080151561078657600080fd5b84600260035481548110151561079857fe5b9060005260206000209060030201600101828154811015156107b657fe5b600091825260209091206004909102018054600160a060020a031916600160a060020a03929092169190911790556003546002805486929081106107f657fe5b90600052602060002090600302016001018281548110151561081457fe5b600091825260209091206001600490920201015560035460028054859290811061083a57fe5b90600052602060002090600302016001018281548110151561085857fe5b9060005260206000209060040201600201816000191690555081600260035481548110151561088357fe5b9060005260206000209060030201600101828154811015156108a157fe5b6000918252602091829020600360049092020181019290925590546040805187815292830186905280518593600160a060020a038a1693927f0f4c6e2a86640f4bcfd9af5da2cd576d60d8b0c1abe01dc40872e725d2b87ee692918290030190a45050505050565b60035481565b600280548290811061091d57fe5b6000918252602090912060039091020154905081565b600154600090600160a060020a0316331461094d57600080fd5b5060005b84518110156109ca576109c2858281518110151561096b57fe5b90602001906020020151858381518110151561098357fe5b90602001906020020151858481518110151561099b57fe5b9060200190602002015185858151811015156109b357fe5b9060200190602002015161108f565b600101610951565b5050505050565b6060806060806109df6113bf565b600060608060608060006003548c111515156109fa57600080fd5b600280548d908110610a0857fe5b90600052602060002090600302016040805190810160405290816000820154815260200160018201805480602002602001604051908101604052809291908181526020016000905b82821015610aad57600084815260209081902060408051608081018252600486029092018054600160a060020a03168352600180820154848601526002820154928401929092526003015460608301529083529092019101610a50565b50505050815250509650866020015151955085604051908082528060200260200182016040528015610ae9578160200160208202803883390190505b50945085604051908082528060200260200182016040528015610b16578160200160208202803883390190505b50935085604051908082528060200260200182016040528015610b43578160200160208202803883390190505b50925085604051908082528060200260200182016040528015610b70578160200160208202803883390190505b5091506001876020015151111515610b8757600080fd5b5060015b866020015151811015610c87576020870151805182908110610ba957fe5b60209081029091010151518551869083908110610bc257fe5b600160a060020a039092166020928302909101820152870151805182908110610be757fe5b90602001906020020151602001518482815181101515610c0357fe5b6020908102909101810191909152870151805182908110610c2057fe5b90602001906020020151604001518382815181101515610c3c57fe5b6020908102909101810191909152870151805182908110610c5957fe5b90602001906020020151606001518282815181101515610c7557fe5b60209081029091010152600101610b8b565b50929a91995097509095509350505050565b600154600090600160a060020a03163314610cb357600080fd5b5060005b84518110156109ca57610d288582815181101515610cd157fe5b906020019060200201518583815181101515610ce957fe5b906020019060200201518584815181101515610d0157fe5b906020019060200201518585815181101515610d1957fe5b90602001906020020151610724565b600101610cb7565b600154600160a060020a03163314610d4757600080fd5b801515610d5357600080fd5b6003548110610d6157600080fd5b6002805482908110610d6f57fe5b90600052602060002090600302016001016002600354815481101515610d9157fe5b9060005260206000209060030201600101908054610db09291906113d7565b5060405181907fa7a6ca9013c493f0ca553426096cd4fde53db0c3e6b49a427fb54b6578618d9390600090a250565b600154600160a060020a031681565b600154600160a060020a03163314610e0557600080fd5b610e13600354600101611291565b565b6001546000908190600160a060020a03163314610e3157600080fd5b6002600354815481101515610e4257fe5b60009182526020808320600160a060020a0387168452600260039093020191909101905260409020549150811515610e7957600080fd5b50805b60016002600354815481101515610e8f57fe5b90600052602060002090600302016001018054905003811015610f72576002600354815481101515610ebd57fe5b906000526020600020906003020160010181600101815481101515610ede57fe5b90600052602060002090600402016002600354815481101515610efd57fe5b906000526020600020906003020160010182815481101515610f1b57fe5b6000918252602090912082546004909202018054600160a060020a031916600160a060020a039092169190911781556001808301548183015560028084015490830155600392830154929091019190915501610e7c565b6002600354815481101515610f8357fe5b906000526020600020906003020160010182815481101515610fa157fe5b6000918252602082206004909102018054600160a060020a03191681556001810182905560028082018390556003918201929092555481548110610fe157fe5b90600052602060002090600302016001018054809190600190036110059190611463565b506000600260035481548110151561101957fe5b60009182526020808320600160a060020a03881680855260039390930201600201905260408083209390935591517f7e9922a9d0ca154569fb50d594599577dcbc2c274b19ea3646ec5f765aabb0049190a2505050565b6060806060806110816003546109d1565b935093509350935090919293565b600154600090600160a060020a031633146110a957600080fd5b60026003548154811015156110ba57fe5b60009182526020808320600160a060020a038916845260026003909302019190910190526040902054905080156110f057600080fd5b600260035481548110151561110157fe5b906000526020600020906003020160010180549050905080600260035481548110151561112a57fe5b60009182526020808320600160a060020a038a168452600392830201600290810190915260409092209290925590548154811061116357fe5b6000918252602080832060408051608081018252600160a060020a03808c168083528286018c81528385018c8152606085018c8152600160039a8b0290980188018054808a018255908c529a89902095516004909b0290950180549a909416600160a060020a0319909a169990991783555194820194909455955160028701555194840194909455915483518881529182018790528351869491927fa675bdfa314d9b6e636aae0b61a77597134927bbc7e168b00490f7379c73019c92908290030190a45050505050565b600154600160a060020a0316331461124557600080fd5b60018054600160a060020a031916600160a060020a03831690811790915560405133907f5c486528ec3e3f0ea91181cff8116f02bfa350e03b8b6f12e00765adbb5af85c90600090a350565b600381905560028054906112a89060018301611494565b50600060026003548154811015156112bc57fe5b6000918252602080832033845260039283020160029081019091526040909220929092559054815481106112ec57fe5b600091825260208083206040805160808101825233815280840186815291810186815260608201878152600396870290940160019081018054808301825590895295909720915160049095029091018054600160a060020a031916600160a060020a03909516949094178455905194830194909455925160028083019190915592519082015554815443929190811061138157fe5b6000918252602082206003909102019190915560405182917f9d49bd24aa746fc8f19004008bcfc0a0bebf6660e0bb478c3a1b83d27fa2759991a250565b60408051808201909152600081526060602082015290565b8280548282559060005260206000209060040281019282156114535760005260206000209160040282015b828111156114535782548254600160a060020a031916600160a060020a0390911617825560018084015490830155600280840154908301556003808401549083015560049283019290910190611402565b5061145f9291506114c0565b5090565b81548183558181111561148f5760040281600402836000526020600020918201910161148f91906114c0565b505050565b81548183558181111561148f5760030281600302836000526020600020918201910161148f91906114fc565b6114f991905b8082111561145f578054600160a060020a03191681556000600182018190556002820181905560038201556004016114c6565b90565b6114f991905b8082111561145f57600080825561151c6001830182611525565b50600301611502565b508054600082556004029060005260206000209081019061154691906114c0565b505600a165627a7a72305820efc13d97bfeb3011818b10e5c6c553f01fde6ae4bd2e932720887026d18248a70029";

    public static final String FUNC_ADDHPBNODE = "addHpbNode";

    public static final String FUNC_ADDHPBNODEBATCH = "addHpbNodeBatch";

    public static final String FUNC_ADDSTAGE = "addStage";

    public static final String FUNC_COPYALLHPBNODESBYSTAGENUM = "copyAllHpbNodesByStageNum";

    public static final String FUNC_DELETEHPBNODE = "deleteHpbNode";

    public static final String FUNC_DELETEHPBNODEBATCH = "deleteHpbNodeBatch";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UPDATEHPBNODE = "updateHpbNode";

    public static final String FUNC_UPDATEHPBNODEBATCH = "updateHpbNodeBatch";

    public static final String FUNC_CURRENTSTAGENUM = "currentStageNum";

    public static final String FUNC_GETALLHPBNODES = "getAllHpbNodes";

    public static final String FUNC_GETALLHPBNODESBYSTAGENUM = "getAllHpbNodesByStageNum";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_NODESTAGES = "nodeStages";

    public static final String FUNC_OWNER = "owner";

    public static final Event COPYALLHPBNODESBYSTAGENUM_EVENT = new Event("CopyAllHpbNodesByStageNum", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}));
    ;

    public static final Event CHANGESTAGE_EVENT = new Event("ChangeStage", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}));
    ;

    public static final Event TRANSFEROWNERSHIP_EVENT = new Event("TransferOwnership", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event DELETEHPBNODE_EVENT = new Event("DeleteHpbNode", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event UPDATEHPBNODE_EVENT = new Event("UpdateHpbNode", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event ADDHPBNODE_EVENT = new Event("AddHpbNode", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>(true) {}));
    ;

    protected HpbNodes(String contractAddress, Web3 web3, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, new RawTransactionManager(web3, credentials), new StaticGasProvider(gasPrice, gasLimit));
    }

    protected HpbNodes(String contractAddress, Web3 web3, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, transactionManager, new StaticGasProvider(gasPrice, gasLimit));
    }
    
    protected HpbNodes(String contractAddress,
            Web3 web3, TransactionManager transactionManager,
            ContractGasProvider gasProvider) {
    	super(BINARY, contractAddress, web3, transactionManager,gasProvider);
    }

    public List<CopyAllHpbNodesByStageNumEventResponse> getCopyAllHpbNodesByStageNumEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(COPYALLHPBNODESBYSTAGENUM_EVENT, transactionReceipt);
        ArrayList<CopyAllHpbNodesByStageNumEventResponse> responses = new ArrayList<CopyAllHpbNodesByStageNumEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CopyAllHpbNodesByStageNumEventResponse typedResponse = new CopyAllHpbNodesByStageNumEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.stageNum = (Uint256) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<CopyAllHpbNodesByStageNumEventResponse> copyAllHpbNodesByStageNumEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, CopyAllHpbNodesByStageNumEventResponse>() {
            @Override
            public CopyAllHpbNodesByStageNumEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(COPYALLHPBNODESBYSTAGENUM_EVENT, log);
                CopyAllHpbNodesByStageNumEventResponse typedResponse = new CopyAllHpbNodesByStageNumEventResponse();
                typedResponse.log = log;
                typedResponse.stageNum = (Uint256) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<CopyAllHpbNodesByStageNumEventResponse> copyAllHpbNodesByStageNumEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COPYALLHPBNODESBYSTAGENUM_EVENT));
        return copyAllHpbNodesByStageNumEventObservable(filter);
    }

    public RemoteCall<TransactionReceipt> addHpbNode(Address coinbase, Bytes32 cid1, Bytes32 cid2,
            Bytes32 hid) {
        final Function function = new Function(
                FUNC_ADDHPBNODE, 
                Arrays.<Type>asList(coinbase, cid1, cid2, hid), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addHpbNodeBatch(DynamicArray<Address> coinbases,
            DynamicArray<Bytes32> cid1s, DynamicArray<Bytes32> cid2s, DynamicArray<Bytes32> hids) {
        final Function function = new Function(
                FUNC_ADDHPBNODEBATCH, 
                Arrays.<Type>asList(coinbases, cid1s, cid2s, hids), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addStage() {
        final Function function = new Function(
                FUNC_ADDSTAGE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> copyAllHpbNodesByStageNum(Uint256 stageNum) {
        final Function function = new Function(
                FUNC_COPYALLHPBNODESBYSTAGENUM, 
                Arrays.<Type>asList(stageNum), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> deleteHpbNode(Address coinbase) {
        final Function function = new Function(
                FUNC_DELETEHPBNODE, 
                Arrays.<Type>asList(coinbase), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<ChangeStageEventResponse> getChangeStageEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CHANGESTAGE_EVENT, transactionReceipt);
        ArrayList<ChangeStageEventResponse> responses = new ArrayList<ChangeStageEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ChangeStageEventResponse typedResponse = new ChangeStageEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.stageNum = (Uint256) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ChangeStageEventResponse> changeStageEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, ChangeStageEventResponse>() {
            @Override
            public ChangeStageEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CHANGESTAGE_EVENT, log);
                ChangeStageEventResponse typedResponse = new ChangeStageEventResponse();
                typedResponse.log = log;
                typedResponse.stageNum = (Uint256) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<ChangeStageEventResponse> changeStageEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CHANGESTAGE_EVENT));
        return changeStageEventObservable(filter);
    }

    public List<TransferOwnershipEventResponse> getTransferOwnershipEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFEROWNERSHIP_EVENT, transactionReceipt);
        ArrayList<TransferOwnershipEventResponse> responses = new ArrayList<TransferOwnershipEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferOwnershipEventResponse typedResponse = new TransferOwnershipEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.to = (Address) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferOwnershipEventResponse> transferOwnershipEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, TransferOwnershipEventResponse>() {
            @Override
            public TransferOwnershipEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFEROWNERSHIP_EVENT, log);
                TransferOwnershipEventResponse typedResponse = new TransferOwnershipEventResponse();
                typedResponse.log = log;
                typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.to = (Address) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<TransferOwnershipEventResponse> transferOwnershipEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFEROWNERSHIP_EVENT));
        return transferOwnershipEventObservable(filter);
    }

    public List<DeleteHpbNodeEventResponse> getDeleteHpbNodeEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DELETEHPBNODE_EVENT, transactionReceipt);
        ArrayList<DeleteHpbNodeEventResponse> responses = new ArrayList<DeleteHpbNodeEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DeleteHpbNodeEventResponse typedResponse = new DeleteHpbNodeEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.coinbase = (Address) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DeleteHpbNodeEventResponse> deleteHpbNodeEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, DeleteHpbNodeEventResponse>() {
            @Override
            public DeleteHpbNodeEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DELETEHPBNODE_EVENT, log);
                DeleteHpbNodeEventResponse typedResponse = new DeleteHpbNodeEventResponse();
                typedResponse.log = log;
                typedResponse.coinbase = (Address) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<DeleteHpbNodeEventResponse> deleteHpbNodeEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DELETEHPBNODE_EVENT));
        return deleteHpbNodeEventObservable(filter);
    }

    public List<UpdateHpbNodeEventResponse> getUpdateHpbNodeEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPDATEHPBNODE_EVENT, transactionReceipt);
        ArrayList<UpdateHpbNodeEventResponse> responses = new ArrayList<UpdateHpbNodeEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UpdateHpbNodeEventResponse typedResponse = new UpdateHpbNodeEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.stageNum = (Uint256) eventValues.getIndexedValues().get(0);
            typedResponse.coinbase = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.hid = (Bytes32) eventValues.getIndexedValues().get(2);
            typedResponse.cid1 = (Bytes32) eventValues.getNonIndexedValues().get(0);
            typedResponse.cid2 = (Bytes32) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UpdateHpbNodeEventResponse> updateHpbNodeEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, UpdateHpbNodeEventResponse>() {
            @Override
            public UpdateHpbNodeEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(UPDATEHPBNODE_EVENT, log);
                UpdateHpbNodeEventResponse typedResponse = new UpdateHpbNodeEventResponse();
                typedResponse.log = log;
                typedResponse.stageNum = (Uint256) eventValues.getIndexedValues().get(0);
                typedResponse.coinbase = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.hid = (Bytes32) eventValues.getIndexedValues().get(2);
                typedResponse.cid1 = (Bytes32) eventValues.getNonIndexedValues().get(0);
                typedResponse.cid2 = (Bytes32) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<UpdateHpbNodeEventResponse> updateHpbNodeEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UPDATEHPBNODE_EVENT));
        return updateHpbNodeEventObservable(filter);
    }

    public List<AddHpbNodeEventResponse> getAddHpbNodeEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDHPBNODE_EVENT, transactionReceipt);
        ArrayList<AddHpbNodeEventResponse> responses = new ArrayList<AddHpbNodeEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddHpbNodeEventResponse typedResponse = new AddHpbNodeEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.stageNum = (Uint256) eventValues.getIndexedValues().get(0);
            typedResponse.coinbase = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.hid = (Bytes32) eventValues.getIndexedValues().get(2);
            typedResponse.cid1 = (Bytes32) eventValues.getNonIndexedValues().get(0);
            typedResponse.cid2 = (Bytes32) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddHpbNodeEventResponse> addHpbNodeEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, AddHpbNodeEventResponse>() {
            @Override
            public AddHpbNodeEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDHPBNODE_EVENT, log);
                AddHpbNodeEventResponse typedResponse = new AddHpbNodeEventResponse();
                typedResponse.log = log;
                typedResponse.stageNum = (Uint256) eventValues.getIndexedValues().get(0);
                typedResponse.coinbase = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.hid = (Bytes32) eventValues.getIndexedValues().get(2);
                typedResponse.cid1 = (Bytes32) eventValues.getNonIndexedValues().get(0);
                typedResponse.cid2 = (Bytes32) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<AddHpbNodeEventResponse> addHpbNodeEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDHPBNODE_EVENT));
        return addHpbNodeEventObservable(filter);
    }

    public RemoteCall<TransactionReceipt> deleteHpbNodeBatch(DynamicArray<Address> coinbases) {
        final Function function = new Function(
                FUNC_DELETEHPBNODEBATCH, 
                Arrays.<Type>asList(coinbases), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(Address newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(newOwner), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> updateHpbNode(Address coinbase, Bytes32 cid1,
            Bytes32 cid2, Bytes32 hid) {
        final Function function = new Function(
                FUNC_UPDATEHPBNODE, 
                Arrays.<Type>asList(coinbase, cid1, cid2, hid), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> updateHpbNodeBatch(DynamicArray<Address> coinbases,
            DynamicArray<Bytes32> cid1s, DynamicArray<Bytes32> cid2s, DynamicArray<Bytes32> hids) {
        final Function function = new Function(
                FUNC_UPDATEHPBNODEBATCH, 
                Arrays.<Type>asList(coinbases, cid1s, cid2s, hids), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<HpbNodes> deploy(Web3 web3, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(HpbNodes.class, web3, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<HpbNodes> deploy(Web3 web3, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(HpbNodes.class, web3, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public RemoteCall<Uint256> currentStageNum() {
        final Function function = new Function(FUNC_CURRENTSTAGENUM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>>> getAllHpbNodes() {
        final Function function = new Function(FUNC_GETALLHPBNODES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}, new TypeReference<DynamicArray<Bytes32>>() {}, new TypeReference<DynamicArray<Bytes32>>() {}, new TypeReference<DynamicArray<Bytes32>>() {}));
        return new RemoteCall<Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>>>(
                new Callable<Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>>>() {
                    @Override
                    public Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>> call()
                            throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>>(
                                (DynamicArray<Address>) results.get(0), 
                                (DynamicArray<Bytes32>) results.get(1), 
                                (DynamicArray<Bytes32>) results.get(2), 
                                (DynamicArray<Bytes32>) results.get(3));
                    }
                });
    }

    public RemoteCall<Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>>> getAllHpbNodesByStageNum(Uint256 _stageNum) {
        final Function function = new Function(FUNC_GETALLHPBNODESBYSTAGENUM, 
                Arrays.<Type>asList(_stageNum), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}, new TypeReference<DynamicArray<Bytes32>>() {}, new TypeReference<DynamicArray<Bytes32>>() {}, new TypeReference<DynamicArray<Bytes32>>() {}));
        return new RemoteCall<Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>>>(
                new Callable<Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>>>() {
                    @Override
                    public Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>> call()
                            throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>, DynamicArray<Bytes32>>(
                                (DynamicArray<Address>) results.get(0), 
                                (DynamicArray<Bytes32>) results.get(1), 
                                (DynamicArray<Bytes32>) results.get(2), 
                                (DynamicArray<Bytes32>) results.get(3));
                    }
                });
    }

    public RemoteCall<Utf8String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Uint256> nodeStages(Uint256 param0) {
        final Function function = new Function(FUNC_NODESTAGES, 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public static HpbNodes load(String contractAddress, Web3 web3, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new HpbNodes(contractAddress, web3, credentials, gasPrice, gasLimit);
    }

    public static HpbNodes load(String contractAddress, Web3 web3,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HpbNodes(contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }

    public static class CopyAllHpbNodesByStageNumEventResponse {
        public Log log;

        public Uint256 stageNum;
    }

    public static class ChangeStageEventResponse {
        public Log log;

        public Uint256 stageNum;
    }

    public static class TransferOwnershipEventResponse {
        public Log log;

        public Address from;

        public Address to;
    }

    public static class DeleteHpbNodeEventResponse {
        public Log log;

        public Address coinbase;
    }

    public static class UpdateHpbNodeEventResponse {
        public Log log;

        public Uint256 stageNum;

        public Address coinbase;

        public Bytes32 hid;

        public Bytes32 cid1;

        public Bytes32 cid2;
    }

    public static class AddHpbNodeEventResponse {
        public Log log;

        public Uint256 stageNum;

        public Address coinbase;

        public Bytes32 hid;

        public Bytes32 cid1;

        public Bytes32 cid2;
    }
}
