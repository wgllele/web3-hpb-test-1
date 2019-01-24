pragma solidity ^0.4.19;
/**
 * @title SafeMath
 * @dev Math operations with safety checks that throw on error
 */
library SafeMath {
  //基本的算术方法库
  //乘
  function mul(uint256 a, uint256 b) internal pure returns (uint256) {
    if (a == 0) {
      return 0;
    }
    uint256 c = a * b;
    assert(c / a == b);
    return c;
  }
  
  
  //除
  function div(uint256 a, uint256 b) internal pure returns (uint256) {
    // assert(b > 0); // Solidity automatically throws when dividing by 0
    uint256 c = a / b;
    // assert(a == b * c + a % b); // There is no case in which this doesn't hold
    return c;
  }
  //减
  function sub(uint256 a, uint256 b) internal pure returns (uint256) {
    assert(b <= a);
    return a - b;
  }
  //加
  function add(uint256 a, uint256 b) internal pure returns (uint256) {
    uint256 c = a + b;
    assert(c >= a);
    return c;
  }
}
/**
 * @title ERC20Basic
 * @dev Simpler version of ERC20 interface
 * @dev see https://github.com/ethereum/EIPs/issues/179
 */
contract ERC20Basic {
  //总供给量，也就是发币总量
  uint256 public totalSupply;
  //查询指定地址余额
  function balanceOf(address who) public view returns (uint256);
  //推荐的转账方法可以安全写入
  function transfer(address to, uint256 value) public returns (bool);
  //记录日志
  //address indexed fromaddress indexed to, uint256 value:
  event Transfer(address indexed from, address indexed to, uint256 value);
}
/**
 * @title ERC20 interface
 * @dev see https://github.com/ethereum/EIPs/issues/20
 */
contract ERC20 is ERC20Basic {
  //, _owner  _spender查询能够从地址 _owner 提出的代币数量 
  function allowance(address owner, address spender) public view returns (uint256);
  
  // A-B从地址 _from 转移代币到地址 _to，必须触发 Transfer 事件
  function transferFrom(address from, address to, uint256 value) public returns (bool);
  //  _spender  _value成功调用 approve 时触发 
  function approve(address spender, uint256 value) public returns (bool);
  
  //触发事件记录日志
  event Approval(address indexed owner, address indexed spender, uint256 value);
}
/**
 * @title Basic token
 * @dev Basic version of StandardToken, with no allowances.
 */
contract BasicToken is ERC20Basic {
  //SafeMathLibrary,
  using SafeMath for uint256;
  //
  mapping(address => uint256) balances;
  //_valuetoken_to
  /**
  * @dev transfer token for a specified address
  * @param _to The address to transfer to.
  * @param _value The amount to be transferred.
  */
  function transfer(address _to, uint256 _value) public returns (bool) {
    //require
    //address(0) 必须是有效地址
    require(_to != address(0));
    
    //balances:
    //msg.sender
    //发送者余额不为0
    require(_value <= balances[msg.sender]);
    // SafeMath.sub will throw if there is not enough balance.
    //发送者余额扣减
    balances[msg.sender] = balances[msg.sender].sub(_value);
    //接受者余额增加
    balances[_to] = balances[_to].add(_value);
    //进行交易
    Transfer(msg.sender, _to, _value);
    return true;
  }
  //// balanceOffunctionbalanceOf(address _owner)constantreturns(uint256 balance)
  /**
  * @dev Gets the balance of the specified address.
  * @param _owner The address to query the the balance of.
  * @return An uint256 representing the amount owned by the passed address.
  */
  function balanceOf(address _owner) public view returns (uint256 balance) {
    return balances[_owner];
  }
}
/**
 * @title Standard ERC20 token
 *
 * @dev Implementation of the basic standard token.
 * @dev https://github.com/ethereum/EIPs/issues/20
 */
contract StandardToken is ERC20, BasicToken {
  mapping (address => mapping (address => uint256)) internal allowed;
  /**
   * @dev Transfer tokens from one address to another
   * @param _from address The address which you want to send tokens from
   * @param _to address The address which you want to transfer to
   * @param _value uint256 the amount of tokens to be transferred
   */
  function transferFrom(address _from, address _to, uint256 _value) public returns (bool) {
    require(_to != address(0));
    require(_value <= balances[_from]);
    require(_value <= allowed[_from][msg.sender]);
    balances[_from] = balances[_from].sub(_value);
    balances[_to] = balances[_to].add(_value);
    allowed[_from][msg.sender] = allowed[_from][msg.sender].sub(_value);
    Transfer(_from, _to, _value);
    return true;
  }
  /**
   * @dev Approve the passed address to spend the specified amount of tokens on behalf of msg.sender.
   *
   * Beware that changing an allowance with this method brings the risk that someone may use both the old
   * and the new allowance by unfortunate transaction ordering. One possible solution to mitigate this
   * race condition is to first reduce the spender's allowance to 0 and set the desired value afterwards:
   * https://github.com/ethereum/EIPs/issues/20#issuecomment-263524729
   * @param _spender The address which will spend the funds.
   * @param _value The amount of tokens to be spent.
   */
  function approve(address _spender, uint256 _value) public returns (bool) {
    allowed[msg.sender][_spender] = _value;
    Approval(msg.sender, _spender, _value);
    return true;
  }
  /**
   * @dev Function to check the amount of tokens that an owner allowed to a spender.
   * @param _owner address The address which owns the funds.
   * @param _spender address The address which will spend the funds.
   * @return A uint256 specifying the amount of tokens still available for the spender.
   */
  function allowance(address _owner, address _spender) public view returns (uint256) {
    return allowed[_owner][_spender];
  }
 
}
/// @title UFO Protocol Token.
/// For more information about this token, please visit http://www.banyanbbt.org
contract UFOToken is StandardToken {
    string public name;
    string public symbol;
 
    uint public decimals;
    /**
     * CONSTRUCTOR 
     * 
     * @dev Initialize the UFO Token
     */
    function UFOToken() public {      
        totalSupply = 10 * 10 ** 26;
        balances[msg.sender] = totalSupply;
        name = "UFOToken";
        symbol = "UFO";
        decimals = 18;
    }
}