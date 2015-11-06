package SshClient;

import Manager.ConFileProcess;
import Manager.LogProcess;
import beans.InitMachine;

import java.io.IOException;
import java.util.ArrayList;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Stubborn
 *
 */
public class RemoteConnection {
	private static final Logger log = LoggerFactory
			.getLogger(RemoteConnection.class);
	public static SshClient client = new SshClient();

	/**
	 * 
	 * @author Stubborn
	 */
	public static void Connection() {
		ConFileProcess aConFileProcess = new ConFileProcess();
		ArrayList<InitMachine> TargetMachineInfo = aConFileProcess
				.getMachineInfo();

		for (InitMachine init : TargetMachineInfo) {
			try {
				ConsoleKnownHostsKeyVerification console = new ConsoleKnownHostsKeyVerification();
				client.connect(init.getIp(), 22, console);
				PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
				pwd.setUsername(init.getUserName());
				pwd.setPassword(init.getPassword());
				int result = client.authenticate(pwd);
				if (result == AuthenticationProtocolState.COMPLETE) {
					SftpClient sftp = client.openSftpClient();
					LogProcess.getChildrenFiles(init.getLogPath(), sftp);
					client.disconnect();
				}
			} catch (IOException e) {
				log.error(" IOException occurred to the method of Connection ",
						e);
				log.warn("This Machine was failed to conntect: ", init.getIp());
			}
		}
		log.info("The process was completed");
	}
}
