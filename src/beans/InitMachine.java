package beans;

/**
 * 
 * @author Stubborn
 *
 */
public class InitMachine {
	private String Ip;   
	private String UserName; 
	private String Password;  
	private String LogPath; 
	private String Remarks;
	
	public InitMachine() {
			
	}
	public InitMachine(String Ip, String UserName, String Password, 
			String LogPath, String Remarks) {
		this.Ip = Ip;
		this.UserName = UserName;
		this.Password = Password;
		this.LogPath = LogPath;	
		this.Remarks = Remarks;
	}
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getLogPath() {
		return LogPath;
	}
	public void setLogPath(String LogPath) {
		this.LogPath = LogPath;
	}
	
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		this.Remarks = remarks;
	}
	@Override
	public String toString() {
		return "TargetMachineInfo [Ip=" + Ip + ", UserName=" + UserName + ", Password=" + Password
				+ ", LogPath=" + LogPath + ", Remarks=" + Remarks + "]";
	}
}
