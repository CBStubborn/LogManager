package Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import SshClient.RemoteConnection;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.sftp.SftpFile;

/**
 * 
 * @author Stubborn
 *
 */
public class LogProcess {
	private static final Logger log = LoggerFactory.getLogger(RemoteConnection.class);

	/**
	 * This method is used to get all log file
	 * @param path the LogPath
	 * @param sftp
	 */
	@SuppressWarnings("unchecked")
	public static void getChildrenFiles(String path, SftpClient sftp) {
		String aPath = path;
		SftpClient aSftp = sftp;
		try {
			List<SftpFile> list = aSftp.ls(aPath);
			for (SftpFile file : list) {
				if (!(file.isDirectory())) {
					String fileName = file.getFilename();
					if (fileName.contains(".log") || fileName.contains(".out")) {
						// The file's last modified time
						long fileModifyTime = file.getAttributes().getModTimeString();
						// Current time
						long nowTime = System.currentTimeMillis();
						// if a Log file has remained exceeded 6 month,then it should be deleted
						if ((nowTime - fileModifyTime) > (6 * 30 * 24 * 60 * 60 * 1000L)) {
							removeFile(file);
						} else {
							String commandResult = execCommand("cd " + aPath + ";" + "du -sh " + fileName);
							if (commandResult != null) {
								String size = commandResult.replace(fileName, " ").trim();
								float convertSize = unitConvert(size);
								// if a Log file has exceeded 2G,then it should be deleted
								if (convertSize >= 2.0) {
									removeFile(file);
								}
							} 
						}
					}
				} else {
					getChildrenFiles(aPath + "/" + file.getFilename(), aSftp);
				}
			}
		} catch (IOException e) {
			log.error("IOException occurred to the method of getChildrenFiles",e);
		}
	}

	/**
	 * 
	 * @param file  that should be deleted
	 *           
	 * 
	 */
	public static void removeFile(SftpFile file) {
		try {
			file.delete();
			log.info(file.getFilename() + " has been removed !");
		} catch (IOException e) {
			log.error("IOException occurred to the method of removeFile", e);
		}
	}

	/**
	 * 
	 * @param command is used to get a file's size
	 *            
	 * @return the result of execute the command
	 */
	public static String execCommand(String command) {
		String line = null;
		try {
			SessionChannelClient Session = RemoteConnection.client.openSessionChannel();
			Session.executeCommand(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(Session.getInputStream()));
			line = in.readLine();
			Session.close();
		} catch (IOException e) {
			log.error(" IOException occurred to the method of execCommand ", e);
		}
		return line;
	}

	/**
	 * 
	 * @param size  a file size that is a type of String
	 *           
	 * @return a file size that is type of float
	 */
	public static float unitConvert(String size) {
		float result = 0;
		String str = size.substring(0, size.length() - 1);
		if (size.endsWith("K")) {
			result = Float.parseFloat(str) / (1024 * 1024);
		} else if (size.endsWith("M")) {
			result = Float.parseFloat(str) / (1024);
		} else if (size.endsWith("G")) {
			result = Float.parseFloat(str);
		} else if (size.endsWith("T")) {
			result = Float.parseFloat(str) * 1024;
		}
		return result;
	}
}
